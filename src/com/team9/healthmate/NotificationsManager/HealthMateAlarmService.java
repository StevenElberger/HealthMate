package com.team9.healthmate.NotificationsManager;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HealthMateAlarmService extends Service {
	
	private PendingIntent pendingIntent;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		try {
			int day;
			int month;
			int year;
			String [] appointmentDate;
			String description = "";
			
			List<Map<String, String>> appointments = DataStorageManager.readJSONObject(this, "appointments");
			Calendar date = Calendar.getInstance(Locale.US);
			Intent intent;
			
			
			for (Map<String, String> appointment : appointments) {
				appointmentDate = appointment.get("date").split("-");

				// Set the day, month, year for the previous date entered.
				year = Integer.parseInt(appointmentDate[2]);
				month = Integer.parseInt(appointmentDate[0]) - 1;
				day = Integer.parseInt(appointmentDate[1]);
				
				date.set(year, month, day);
				date.set(Calendar.HOUR_OF_DAY, 0);
				date.set(Calendar.MINUTE, 0);
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
