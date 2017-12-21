package com.example.anu.hydrationremainder.utils;

import android.content.Context;
import android.provider.Telephony;

import com.example.anu.hydrationremainder.services.HydrationReminderIntentService;
import com.example.anu.hydrationremainder.services.WaterReminderFirebaseJobService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by Design on 21-12-2017.
 */

public class ReminderUtilities {

    private static final int CHARGING_REMINDER_IN_MINUTES = 60;

    public static boolean isInitialized = false;

    /**
     * convert {@value CHARGING_REMINDER_IN_MINUTES} to seconds
     */
    private static final int CHARGING_REMINDER_IN_SECONDS = (int) TimeUnit.MINUTES.toSeconds(CHARGING_REMINDER_IN_MINUTES);

    private static final int SYNC_FLEXTIME_SECONDS = CHARGING_REMINDER_IN_SECONDS;

    private static final String REMINDER_TAG = "charging_reminder_tag";

    /**
     * synchronized method which will use {@link FirebaseJobDispatcher}to schedule a job that will run
     * roughly {@value CHARGING_REMINDER_IN_MINUTES} minutes when the phone is charging
     * it will trigger {@link com.example.anu.hydrationremainder.services.WaterReminderFirebaseJobService}
     * @param context called context
     * Checkout https://github.com/firebase/firebase-jobdispatcher-android for an example
     */
    synchronized public static void scheduleChargingReminder(Context context){
        if (isInitialized)
            return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);

        /**
         * use newJobBuilder to build new job
         */
        Job job = firebaseJobDispatcher.newJobBuilder()
                /**
                 *the servuce which will be used to write to preference
                 */
                .setService(WaterReminderFirebaseJobService.class)

                /**
                 * tag used to uniquely identifies the job
                 */
                .setTag(REMINDER_TAG)
                /**
                 * the network constraints which indicated when the job should be executed
                 * best practice is to write the constraint to a prefernce cause different user's will be having
                 * different constrainton when to execute the job
                 *
                 * here it indicates that the job should be excuted whenever the device is on charge
                 */
                .setConstraints(Constraint.DEVICE_CHARGING)
                /**
                 * indicated howlong the job should be alive
                 * it can be forever or it should be die next time the device boots
                 */
                .setLifetime(Lifetime.FOREVER)
                /**
                 * recur=true indicatees we need this reminder to happen continuously
                 */
                .setRecurring(true)
                /*
                 * We want the reminders to happen every 15 minutes or so. The first argument for
                 * Trigger class's static executionWindow method is the start of the time frame
                 * when the
                 * job should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
                .setTrigger(Trigger.executionWindow(
                        CHARGING_REMINDER_IN_SECONDS,
                        CHARGING_REMINDER_IN_SECONDS + SYNC_FLEXTIME_SECONDS))
                /**
                 * if a job with the same provided tag already exists, replace
                 */
                .setReplaceCurrent(true)
                /**
                 * once the job is ready, call build() to return the job
                 */
                .build();

        /**
         * schdule the job with the dispatcher
         */
        firebaseJobDispatcher.schedule(job);
        isInitialized = true;
    }
}
