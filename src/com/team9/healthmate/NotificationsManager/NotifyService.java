package com.team9.healthmate.NotificationsManager;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


import com.team9.healthmate.Login;
import com.team9.healthmate.Menu;
import com.team9.healthmate.R;
import com.team9.healthmate.Appointments.AppointmentDetail;
import com.team9.healthmate.DataManager.DataStorageManager;
import com.team9.healthmate.Medications.Medication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * Service Class that creates notifications when the service
 * is started. An intent is required with data passed using the
 * intent in order for the application not to crash. The notifications
 * created are from the intent data given. The service expects to receive
 * data with keys, "type", "title", and "description".
 * @author Michael Sandoval
 *
 */
public class NotifyService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	/**
	 * Method to that is called when the service is started. The method
	 * creates notifications and determines the content that will be set
	 * in the notification using an intent passed in when the method is called.
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		// Container for alarm information
		Map<String, String> alarm = new HashMap<String, String>();
		
		// Get the time stamp of the alarm saved in the alarm file
		alarm.put("timestamp", intent.getStringExtra("alarm timestamp"));
		
		// Retrieve the data from the intent that is necessary
		// to create the notification.
		String type = intent.getStringExtra("type");
		String title = intent.getStringExtra("title");
		String description = intent.getStringExtra("description");
		
		// Intent that will be set with the notification
		Intent resultIntent;
		
		// The current context of the application
		Context context = this.getApplicationContext();
		
		// Get the default notification sound of the device
		Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
		// Container used to build a notification
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		
		// Determine what kind of notification this will be.
		// Set the icon and event that will occur for the notification.
		switch(type) {
		
		case "appointments":
			// Appointment Notification
			mBuilder.setSmallIcon(R.drawable.ic_appointment_notification);
			resultIntent = new Intent(context, AppointmentDetail.class);
			
			// Try to delete the alarm from the single alarm file, this ensures that
			// the notification is not sent on reboot
			try {
				if (alarm.get("timestamp") != null) {
					DataStorageManager.deleteJSONObject(context, "single alarms", alarm);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case "medications":
			// Medication Notification
			mBuilder.setSmallIcon(R.drawable.ic_medication_notification);
			resultIntent = new Intent(context, Medication.class);
			break;
			
		case "change":
			// Change Notification
			mBuilder.setSmallIcon(R.drawable.ic_change_notification);
			resultIntent = new Intent(context, Menu.class);
			break;
			
		default:
			mBuilder.setSmallIcon(R.drawable.ic_action_content_new);	
			resultIntent = new Intent(context, Login.class);
		}
		
		// Transfer the information of the given intent to the new intent
		resultIntent.putExtras(intent.getExtras());
		
		// Give the information that this is from notification.
		resultIntent.putExtra("notification", "true");
		
		// Clear the top of the Application Stack
		resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		// Set the title of the notification
		mBuilder.setContentTitle(title);
		
		// Set the information for the body of the notification
		mBuilder.setContentText(description);
		
		// Create a pending intent, this will be called when the user selects
		// the notification action
		// The request code of this pending intent is the current system time
		// This allows for unique pending intents
		PendingIntent pendingIntent = PendingIntent.getActivity(context, (int)System.currentTimeMillis(), resultIntent, PendingIntent.FLAG_ONE_SHOT);
		
		// Set the intent for the notification
		mBuilder.setContentIntent(pendingIntent);
		
		// Set the sound of notification, this sound will play
		// when the notification goes off
		mBuilder.setSound(sound);
		
		// The notification goes away when the user selects it
		mBuilder.setAutoCancel(true);
		
		// Set the device to vibrate when notification received
		mBuilder.setVibrate(new long[]{100, 200, 100, 500});
		
		// Get the notification manager system of the device
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		// Register the notification
		mNotificationManager.notify((int)Calendar.getInstance().getTimeInMillis(), mBuilder.build());
		
		// After sending the notification, stop the service
		this.stopSelf();
		
		return super.onStartCommand(intent, flags, startId);
	}

}
