package com.team9.healthmate.NotificationsManager;

import com.team9.healthmate.AppointmentDetail;
import com.team9.healthmate.AppointmentsList;
import com.team9.healthmate.Login;
import com.team9.healthmate.R;
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
import android.util.Log;

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
		
		Log.v("Notif Type: ", intent.getStringExtra("type"));
		
		// Determine what kind of notification this will be.
		// Set the icon and event that will occur for the notification.
		switch(type) {
		
		case "appointments":
			// Appointment Notification
			mBuilder.setSmallIcon(R.drawable.ic_appointment_notification);
			resultIntent = new Intent(context, AppointmentDetail.class);
			break;
			
		case "medications":
			// Medication Notification
			mBuilder.setSmallIcon(R.drawable.ic_medication_notification);
			resultIntent = new Intent(context, Medication.class);
			break;
			
		case "change":
			// Change Notification
			mBuilder.setSmallIcon(R.drawable.ic_change_notification);
			resultIntent = new Intent(context, AppointmentsList.class);
			break;
			
		default:
			mBuilder.setSmallIcon(R.drawable.ic_action_content_new);	
			resultIntent = new Intent(context, Login.class);
		}
		
		resultIntent.putExtras(intent.getExtras());
		
		// Set the title of the notification
		mBuilder.setContentTitle(title);
		
		// Set the information for the body of the notification
		mBuilder.setContentText(description);
		
		// Create a pending intent, this will be called when the user selects
		// the notification action
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
		
		// Set the intent for the notification
		mBuilder.setContentIntent(pendingIntent);
		
		// Set the sound of notification, this sound will play
		// when the notification goes off
		mBuilder.setSound(sound);
		
		// The notification goes away when the user selects it
		mBuilder.setAutoCancel(true);
		
		// Get the notification manager system of the device
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		// Register the notification
		mNotificationManager.notify(0, mBuilder.build());
		
		// After sending the notification, stop the service
		this.stopSelf();
		
		return super.onStartCommand(intent, flags, startId);
	}

}
