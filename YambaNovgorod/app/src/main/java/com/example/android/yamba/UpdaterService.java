package com.example.android.yamba;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientInterface;
import com.thenewcircle.yamba.client.YambaStatus;

import java.util.List;

public class UpdaterService extends Service {
	
	static final int DELAY = 60000; // 1 minute delay bewteen the get timeline operation
	private boolean runFlag  = false; // state of the running service operation
	private Updater updater;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("Yamba", "Service onCreate");
		
		//at the beginning we create a thread for the update
		updater = new Updater();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d("Yamba", "Service onStartCommand");
		
		//check if we already have a running thread
		if (!this.runFlag)
		{
			this.runFlag = true;
			try
			{
			this.updater.start();
			}
			catch(Throwable e)
			{			this.runFlag = false;}
		}		
		
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("Yamba", "Service onDestroy");
		
		//try to force-closed the updater if is still running
		this.runFlag = false;
		this.updater.interrupt();
		this.updater = null;
	}
	
	private class Updater extends Thread
	{

		public Updater ()
		{
			super("UpdaterService-Updater");
		}
		
		@Override
		public void run() {
			// information if service is running
			UpdaterService updaterService = UpdaterService.this;
			while (updaterService.runFlag)
			{
				try {

					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(UpdaterService.this);
					final String username = prefs.getString(getString(R.string.username_key), "");
					final String password = prefs.getString(getString(R.string.password_key), "");

					// Check that username and password are not empty
					if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
						Log.w("Yamba", "Please update your username and password");
						return;
					}

					YambaClientInterface cloud = YambaClient.getClient(username, password);
					List<YambaStatus> timeline = cloud.getTimeline(20);

					 //parse the values
					for (YambaStatus status : timeline)
						Log.d("Yamba", status.getUser() + ": " + status.getMessage() +
								"-" + status.getCreatedAt());
					
					//delay sleep between data fetch
					Thread.sleep(DELAY);
				} catch (Throwable e) {
					updaterService.runFlag = false;
					UpdaterService.this.runFlag = false;
				}
			}

		}//end updater
		
	}

}
