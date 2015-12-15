package com.intel.nizhny.yamba;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientInterface;

/**
 * Created by mailat on 15.12.15.
 */
public class StatusUpdateService extends IntentService {

    public static final String EXTRA_MESSAGE = "message";

    private static final String TAG =
            StatusUpdateService.class.getSimpleName();

    public StatusUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Gather received parameters
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        try {
            String username = "student";
            String password = "password";

            YambaClientInterface cloud = YambaClient.getClient(username, password);
            cloud.postStatus(message);

            Log.d(TAG, "Successfully posted to the cloud: " + message);
        } catch (Exception e) {
            Log.e(TAG, "Failed to post to the cloud", e);
        }
    }
}
