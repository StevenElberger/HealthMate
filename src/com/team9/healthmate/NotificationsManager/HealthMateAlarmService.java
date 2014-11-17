package com.team9.healthmate.NotificationsManager;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.team9.healthmate.AppointmentsList;
import com.team9.healthmate.R;
import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class HealthMateAlarmService extends Service {
	
	private PendingIntent pendingIntent;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		
		/*Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
		NotificationManager n = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		Intent intent = new Intent(this.getApplicationContext(), AppointmentsList.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		
		NotificationCompat.Builder m = new NotificationCompat.Builder(this);
		m
		.setContentTitle("Hello World")
		.setContentText("This is a test")
		.setSmallIcon(R.drawable.ic_action_editor_mode_edit)
		.setContentIntent(pIntent)
		.setSound(sound);
		
		m.addAction(0, "Load Service", pIntent);
		
		n.notify(1, m.build());*/
		
		try {
			int day;
			int month;
			int year;
			int hour;
			int minute;
			String [] appointmentDate;
			String [] time;
			String description = "";
			
			List<Map<String, String>> appointments = DataStorageManager.readJSONObject(this, "appointments");
			Calendar date = Calendar.getInstance();
			Intent intent;
			
			
			for (Map<String, String> appointment : appointments) {
				appointmentDate = appointment.get("date").split("-");

				// Set the day, month, year for the previous date entered.
				year = Integer.parseInt(appointmentDate[2]);
				month = Integer.parseInt(appointmentDate[0]) - 1;
				day = Integer.parseInt(appointmentDate[1]);
				
				// Parse the text Time information sent by the intent.
				time = appointment.get("start time").split(":");
				hour = Integer.parseInt(time[0]);
				minute = Integer.parseInt(time[1].substring(0, 2));

				// Check to see if the time of day
				if (time[1].substring(2, 4).equals("pm")) {
					hour = hour + 12;
				}
				
				
				date.set(year, month, day);
				date.set(Calendar.HOUR_OF_DAY, hour);
				date.set(Calendar.MINUTE, minute);
				date.set(Calendar.SECOND, 0);
				
				description = appointment.get("title") + "\n";
				description = description + "Name: " + appointment.get("name") + "\n";
				description = description + "Date: " + appointment.get("date") + "\n";
				description = description + "Time: " + appointment.get("start time") + " - " + appointment.get("end time");
				
				intent = new Intent(this, NotifyService.class);
				intent.putExtra("type", "appointment");
				intent.putExtra("title", "Appointment Reminder");
				intent.putExtra("description", description);
				
				pendingIntent = PendingIntent.getService(this, 0, intent, 0);
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
				
				Log.w("Service: ", "HealthMateAlarmService");
				
				this.startService(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
