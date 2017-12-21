package com.example.anu.hydrationremainder.services;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;


/**
 * Created by Design on 21-12-2017.
 */

/**
 * this class should extend from {@link JobService}
 */
public class WaterReminderFirebaseJobService extends JobService {

    private AsyncTask mBackgroundTask;
    /**
     * this is the starting point for our job
     * this is called by Job dispatcher to tell us that we should start our job
     * NB : this method run in application's main thread. so we should offload th work to som background thread
     * @param job job to be done
     * @return indicates weather more job is pending
     */
    @Override
    public boolean onStartJob(final JobParameters job) {
        /**
         * by default the work is being executed in the main thread
         * so we will create an anonymous class called {@link mBackgroundTask}
         * ti offload the work to the background thread
         */
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                /**
                 * eeute the new charging reminder task
                 */
                ReminderTasks.execute(WaterReminderFirebaseJobService.this, ReminderTasks.ACTION_CHARGING_REMAINDER_NOTIFICATION);

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                /**
                 * call jobFinished by passing {@link job} and false as parameters
                 * passing false indicate the job manager that our job has been finished and we don't want to do anything
                 * we will pass true to continue running our job incase of some failures
                 */
                jobFinished(job, false);
            }
        };

        //execute the background task
        mBackgroundTask.execute();

        //return true
        return true;
    }

    /**
     * this method is called when the scheduling engine has decide to interrupt the execution of the job
     * incase if the running constraints of the job are not satisfied
     * @param job
     * @return indicated wether we need to restat the job or not
     * @see Job.Builder#setRetryStrategy(RetryStrategy)
     * @see RetryStrategy
     */
    @Override
    public boolean onStopJob(JobParameters job) {

        /**
         * if the background task is vli, cancel it
         */
        if (null != mBackgroundTask)
            mBackgroundTask.cancel(true);

        //return true to indicate that the job should be retried
        return true;
    }
}
