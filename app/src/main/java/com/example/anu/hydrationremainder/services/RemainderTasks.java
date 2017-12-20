package com.example.anu.hydrationremainder.services;

import android.content.Context;
import android.util.Log;

import com.example.anu.hydrationremainder.utils.HydrationNotificationUtils;
import com.example.anu.hydrationremainder.utils.PreferenceUtilities;

/**
 * Created by Design on 19-12-2017.
 */

public class RemainderTasks {

    private static final String TAG = RemainderTasks.class.getSimpleName();

    /**
     * action to increment the water count
     */
    public static final String ACTION_INCREMENT_WATER_COUNT = "increment_water_count";

    /**
     * action to cancel notifications
     */
    public static final String ACTION_CANCEL_NOTIFICATION = "cancel_notification";

    /*method to increment the water count
     *
     * @param context called context
     * @param action acction to be done
     */
    public static void execute(Context context, String action){
        Log.d(TAG, "execute");
        if (action.equalsIgnoreCase(ACTION_INCREMENT_WATER_COUNT)) {
            PreferenceUtilities.incrementWaterCount(context);
            HydrationNotificationUtils.clearNotifications(context);
        }
        else if (action.equalsIgnoreCase(ACTION_CANCEL_NOTIFICATION))
            HydrationNotificationUtils.clearNotifications(context);
    }
}
