package com.example.anu.hydrationremainder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        populateWaterCount();
        populateChargingRemainderCount();

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
     * @param key changed key
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
}
