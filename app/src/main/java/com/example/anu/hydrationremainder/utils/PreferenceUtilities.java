package com.example.anu.hydrationremainder.utils;

/**
 * Created by Design on 19-12-2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * class used to maintain water count and charging reminder count in shared prefrence
 */
public class PreferenceUtilities {

    private static final String TAG = PreferenceUtilities.class.getSimpleName();

    /**
     * preference key to store water count
     */
    public static final String KEY_WATER_COUNT = "water_count";

    /**
     * preference key to store charging remiander count
     */
    public static final String KEY_CHARGING_REMINDER_COUNT = "charging reminder_count";

    private static final int DEFAULT_COUNT = 0;

    /**
     * metod to get water count
     * @param context
     * @return the current water count
     */
    public static int getWaterCount(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(KEY_WATER_COUNT, DEFAULT_COUNT);
    }

 /**
     * metod to get charging remainder count
     * @param context
     * @return the current charging remainder count
     */
    public static int getChargingRemainderCount(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT);
    }

    /**
     * method to set water count
     * @param context
     * @param count water count to be updated
     */
    public static void setWaterCount(Context context, int count){
        Log.d(TAG, "setWaterCount");
        Log.d(TAG, "count : " + count);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_WATER_COUNT, count);
        editor.apply();
    }

    /**
     * method to set charging reminder count
     * @param context aclled context
     * @param count updated charging reminder count
     */
    public static void setChargingReminderCount(Context context, int count){
        Log.d(TAG, "setWaterCount");
        Log.d(TAG, "count : " + count);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_CHARGING_REMINDER_COUNT, count);
        editor.apply();
    }

    /**
     * method to increment water count
     * @param context called context
     */
    public static void incrementWaterCount(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int waterCount = preferences.getInt(KEY_WATER_COUNT, DEFAULT_COUNT);
        waterCount = waterCount + 1;
        setWaterCount(context, waterCount);

    }

    /**
     * method to increment water count
     * @param context called context
     */
    public static void incrementChargingRemindercount(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingRemindercount = preferences.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT);
        chargingRemindercount = chargingRemindercount + 1;
        setChargingReminderCount(context, chargingRemindercount);

    }
}
