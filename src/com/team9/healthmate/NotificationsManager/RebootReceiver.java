package com.team9.healthmate.NotificationsManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RebootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Calendar calendar = Calendar.getInstance();

		try {
			ArrayList<Map<String, String>> singleAlarms = DataStorageManager
					.readJSONObject(context, "single alarms");
			ArrayList<Map<String, String>> repeatedAlarms = DataStorageManager
					.readJSONObject(context, "repeating alarms");

			for (Map<String, String> message : singleAlarms) {
				// Create an intent
				Intent newIntent = new Intent(context, AlarmReceiver.class);

				// Pass the information for the notification
				newIntent.putExtra("type", message.get("type"));
				newIntent.putExtra("title", message.get("title"));
				newIntent.putExtra("description", message.get("description"));
				calendar.setTimeInMillis(Long.parseLong(message
						.get("time of alarm")));

				if (newIntent.getStringExtra("type").equals("appointments")) {
					newIntent.putExtra("name", message.get("name"));
					newIntent.putExtra("location", message.get("location"));
					newIntent.putExtra("phone", message.get("phone"));
					newIntent.putExtra("email", message.get("email"));
					newIntent.putExtra("comment", message.get("comment"));
					newIntent.putExtra("start time", message.get("start time"));
					newIntent.putExtra("end time", message.get("end time"));
					newIntent.putExtra("date", message.get("date"));
				}
				
				newIntent.putExtra("alarm timestamp", message.get("timestamp"));

				// Create a pending intent that will be used when the alarm goes
				// off
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						context, 0, newIntent, PendingIntent.FLAG_ONE_SHOT);

				// Get the alarm manager of the system
				AlarmManager alarmManager = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);

				// Register the alarm
				alarmManager.set(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), pendingIntent);
			}

			for (Map<String, String> message : repeatedAlarms) {
				// Create an intent
				Intent newIntent = new Intent(context, AlarmReceiver.class);
				long intervalTimer = Long.parseLong(message.get("interval"));

				// Pass the information for the notification
				newIntent.putExtra("type", message.get("type"));
				newIntent.putExtra("title", message.get("title"));
				newIntent.putExtra("description", message.get("description"));

				if (newIntent.getStringExtra("type").equals("appointments")) {
					newIntent.putExtra("name", message.get("name"));
					newIntent.putExtra("location", message.get("location"));
					newIntent.putExtra("phone", message.get("phone"));
					newIntent.putExtra("email", message.get("email"));
					newIntent.putExtra("comment", message.get("comment"));
					newIntent.putExtra("start time", message.get("start time"));
					newIntent.putExtra("end time", message.get("end time"));
					newIntent.putExtra("date", message.get("date"));
				}

				calendar.setTimeInMillis(Integer.parseInt(message
						.get("time of alarm")));

				// Create a pending intent that will be used when the alarm goes
				// off
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						context, 0, newIntent, PendingIntent.FLAG_ONE_SHOT);

				// Get the alarm manager of the system
				AlarmManager alarmManager = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);

				// Register the alarm
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), intervalTimer,
						pendingIntent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

