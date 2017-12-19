package com.example.anu.hydrationremainder.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Design on 19-12-2017.
 */

/**
 * service to increment water count
 */
public class HydrationRemainderIntentService extends IntentService {

    private static final String TAG = HydrationRemainderIntentService.class.getSimpleName();

    public HydrationRemainderIntentService() {
        super("HydrationRemainderIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent");
        RemainderTasks.execute(this, intent.getAction());
    }
}
