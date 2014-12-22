package com.team9.healthmate.NotificationsManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Class to receives the incoming alarm events of the application. A service
 * is started when an alarm is received to send a notification. 
 * @author Michael Sandoval
 */
public class AlarmReceiver extends BroadcastReceiver {

	/**
	 * Method to handle the event that an alarm is received. This
	 * in general handles intents when the application may not be running.
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		
		// Create an intent to pass to the Service that will handle the
		// creation of the notification.
		Intent service = new Intent(context, NotifyService.class);
		
		// Information that will be used to set the content of the 
		// notification
		service.putExtras(intent.getExtras());
		
		// Start the service
		context.startService(service);

	}

}
