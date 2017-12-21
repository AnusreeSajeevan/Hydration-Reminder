package com.example.anu.hydrationremainder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anu.hydrationremainder.services.HydrationReminderIntentService;
import com.example.anu.hydrationremainder.services.ReminderTasks;
import com.example.anu.hydrationremainder.utils.PreferenceUtilities;
import com.example.anu.hydrationremainder.utils.ReminderUtilities;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.txt_label)
    TextView txtLabel;
    @BindView(R.id.imgbtn)
    ImageButton imageButton;
    @BindView(R.id.txt_water_count)
    TextView txtWaterCount;
    @BindView(R.id.txt_charging_remainder_count)
    TextView txtChargingRemainderCount;

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.img_charging)
    ImageView imgCharging;

    private IntentFilter mIntentFilter;
    private ChargingBroadcastReceiver mChargingBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        populateWaterCount();
        populateChargingRemainderCount();

        /**
         * initialize {@link mIntentFilter} and {@link mChargingBroadcastReceiver}
         */
        mIntentFilter = new IntentFilter();
        mChargingBroadcastReceiver = new ChargingBroadcastReceiver();

        /**
         * add power connected and disconnected actions to the IIntentFilter
         */
        mIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        mIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);


        /**
         * schedule the job to remind
         */
        ReminderUtilities.scheduleChargingReminder(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * method to update current water count
     */
    private void populateWaterCount() {
        Log.d(TAG, "populateWaterCount");
        Log.d(TAG, "count : " + PreferenceUtilities.getWaterCount(this));
        txtWaterCount.setText(String.valueOf(PreferenceUtilities.getWaterCount(this)));
    }

    /**
     * method to update current charging reminder count
     */
    private void populateChargingRemainderCount() {
        int chargingRemainderCount = PreferenceUtilities.getChargingRemainderCount(this);
        String chargingRemainder = getResources().getQuantityString(R.plurals.charging_remainder_count, chargingRemainderCount, chargingRemainderCount);
        txtChargingRemainderCount.setText(chargingRemainder);
    }

    /**
     * method called on clicking glass image
     */
    @OnClick(R.id.imgbtn)
    public void onViewClicked() {
        /**
         * call the intent service by passing{@link ReminderTasks.ACTION_INCREMENT_WATER_COUNT}
         * as the action
         */
        Intent intent = new Intent(this, HydrationReminderIntentService.class);
        intent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
        startService(intent);
    }

    /**
     * update either the water count or the reminder count based on the change in preference
     *
     * @param sharedPreferences changed preference
     * @param key               changed key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase(PreferenceUtilities.KEY_WATER_COUNT))
            populateWaterCount();
        else if (key.equalsIgnoreCase(PreferenceUtilities.KEY_CHARGING_REMINDER_COUNT))
            populateChargingRemainderCount();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /**
         * unregister onSharedPrefrenceChangeListener in onDestroy
         */
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * method responsible for changing the charging icon
     * depending on the value of isCharging
     * will show black color charging is charging is disconnected and showsgreen color charging when device is in charging
     *
     * @param isCharging true if device i charging, false otherwise
     */
    private void chageChrgingImage(boolean isCharging) {
        if (isCharging) {
            imgCharging.setImageResource(R.drawable.ic_charger_green);
        } else {
            imgCharging.setImageResource(R.drawable.ic_charger);
        }
    }

    /**
     * inner class extends from {@link BroadcastReceiver}
     */
    private class ChargingBroadcastReceiver extends BroadcastReceiver {

        /**
         * override this method to receive action from the intent to match with ACTION_POWER_CONNECTED
         * if it matchs, power is connected
         *
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean isCharging = action.equals(Intent.ACTION_POWER_CONNECTED);
            chageChrgingImage(isCharging);
        }
    }

    /**
     * register {@link ChargingBroadcastReceiver} to receive action when power is connected or disconnected
     */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mChargingBroadcastReceiver, mIntentFilter);
    }

    /**
     * clean up the broadcast receiver
     */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mChargingBroadcastReceiver);
    }
}
