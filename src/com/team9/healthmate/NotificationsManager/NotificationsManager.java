package com.team9.healthmate.NotificationsManager;

import java.util.Map;

import com.team9.healthmate.AppointmentsList;
import com.team9.healthmate.R;
import com.team9.healthmate.Medications.Medication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationsManager {
	
	public static Intent resultIntent;
	
	public static void generateNotification(Context context, Map<String, String> message) {
		
		String type = message.get("type");
		String title = message.get("title");
		String description = message.get("description");
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		
		switch(type) {
		
		case "appointments":
			mBuilder.setSmallIcon(R.drawable.ic_appointment_notification);
			resultIntent = new Intent(context, AppointmentsList.class);
			break;
		case "medications":
			mBuilder.setSmallIcon(R.drawable.ic_medication_notification);
			resultIntent = new Intent(context, Medication.class);
			break;
		case "change":
			mBuilder.setSmallIcon(R.drawable.ic_change_notification);
			resultIntent = new Intent(context, AppointmentsList.class);
			break;
			
		}
		
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(description);
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
		
		mBuilder.setContentIntent(pendingIntent);
		
		mBuilder.addAction(R.drawable.ic_action_content_save, "Confirm", pendingIntent);
		
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0, mBuilder.build());
		
	}
}
