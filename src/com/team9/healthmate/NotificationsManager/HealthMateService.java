package com.team9.healthmate.NotificationsManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HealthMateService extends Service {
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
