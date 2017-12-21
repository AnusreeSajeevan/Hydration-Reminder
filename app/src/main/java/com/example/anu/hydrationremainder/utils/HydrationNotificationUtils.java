package com.example.anu.hydrationremainder.utils;

/**
 * Created by Design on 19-12-2017.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.anu.hydrationremainder.MainActivity;
import com.example.anu.hydrationremainder.R;
import com.example.anu.hydrationremainder.services.HydrationReminderIntentService;
import com.example.anu.hydrationremainder.services.ReminderTasks;

/**
 * utility class helps to create and maintain hydration reminder notifications
 */
public class HydrationNotificationUtils {

    /**
     * id used to uniquely identifies the notification
     * it is useful if we want to cancel the notification after it has been displayed
     */
    private static final int NOTIFICATION_ID = 10;

    /**
     * id which uniquely identifis the notification channel which categorises notifications
     */
    private static final int NOTIFICATION_PENDING_INTENT_ID = 20;

    /**
     * id which uniquely identifis the drink water pending intent
     */
    private static final int NOTIFICATION_DRINK_WATER_PENDING_INTENT_ID = 30;

    /**
     * id which uniquely identifis cancel notifications pending intent
     */
    private static final int NOTIFICATION_CANCEL_PENDING_INTENT_ID = 40;

    /**
     * uniwue name for the notification channel
     */
    private static final String NOTIFICATION_CHANNEL_ID = "notification_reminder_channel";

    /**
     * method to create hydration remainder notification
     * @param context called context
     */
    public static void createHydrationReminderNotification(Context context){

        /**
         * get access to {@link android.app.NotificationManager}
         */
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        /**
         * creat a NotificationChannel for Android O devices
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    context.getResources().getString(R.string.notification_channel_name), NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_glass_water)
                .setLargeIcon(getLargeIcon(context))
                .setContentTitle(context.getResources().getString(R.string.reminder_notification_title))
                .setContentText(context.getResources().getString(R.string.reminder_notification_text))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getResources().getString(R.string.reminder_notification_text)))
                .setContentIntent(getPendingIntent(context))    //should redirect to {@link MainActivity}
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .addAction(drinWaterAction(context))
                .addAction(cancelAction(context))
                .setAutoCancel(true);   //notification will be cancelled automatically when we click on it

        /**
         * if the build version is greater than JELLY BEAN and less than OREO,
         * set the priority to HIGH
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }

        /**
         * trigger the notifcation by calling notify on NotificationManager
         */
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * method to create action for drink water
     * @param context called context
     * @return created acton
     */
    private static NotificationCompat.Action drinWaterAction(Context context) {

        Intent intentDrinkWater = new Intent(context, HydrationReminderIntentService.class);
        intentDrinkWater.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);

        PendingIntent pendingIntentDrinkWater = PendingIntent.getService(context,
                NOTIFICATION_DRINK_WATER_PENDING_INTENT_ID, intentDrinkWater, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action drinkWaterAction = new NotificationCompat.Action(R.drawable.ic_glass_water_small,
                context.getResources().getString(R.string.action_i_did_it), pendingIntentDrinkWater);
        return drinkWaterAction;
    }

/**
     * method to create action for drink water
     * @param context called context
     * @return created acton
     */
    private static NotificationCompat.Action cancelAction(Context context) {

        Intent intentCancel = new Intent(context, HydrationReminderIntentService.class);
        intentCancel.setAction(ReminderTasks.ACTION_CANCEL_NOTIFICATION);

        PendingIntent pendingIntentCancel = PendingIntent.getService(context,
                NOTIFICATION_CANCEL_PENDING_INTENT_ID, intentCancel, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action cancelAction = new NotificationCompat.Action(R.drawable.ic_cancel,
                context.getResources().getString(R.string.action_no_thanks), pendingIntentCancel);
        return cancelAction;
    }

    /**
     * method to create PendingIntent to redirect to {@link MainActivity}
     * on clicking notification
     * @param context called context
     * @return created PendingIntent
     */
    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, NOTIFICATION_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap getLargeIcon(Context context){
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_glass_water);
    }

    /**
     * method to cancel all the notifications
     * @param context
     */
    public static void clearNotifications(Context context){
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
