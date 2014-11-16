package com.team9.healthmate.NotificationsManager;

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

public class NotifyService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		String type = intent.getStringExtra("type");
		String title = intent.getStringExtra("title");
		String description = intent.getStringExtra("description");
		
		Intent resultIntent;
		Context context = this.getApplicationContext();
		
		Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
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
		default:
			mBuilder.setSmallIcon(R.drawable.ic_action_content_new);	
			resultIntent = new Intent(context, Login.class);
		}
		
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(description);
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
		
		mBuilder.setContentIntent(pendingIntent);
		mBuilder.setSound(sound);
		
		mBuilder.addAction(0, "Details", pendingIntent);
		mBuilder.setAutoCancel(true);
		
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0, mBuilder.build());
		
		return super.onStartCommand(intent, flags, startId);
	}

}
