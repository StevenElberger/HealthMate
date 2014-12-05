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

/**
 * Class to receives the incoming event of the android system being rebooted.
 * New alarms are created to replace the alarms lost when the system was turned off.
 * The generates the alarms from a registration alarm file that keeps track of current
 * alarms that are active.
 * @author Michael Sandoval
 */
public class RebootReceiver extends BroadcastReceiver {

	/**
	 * Method to handle the event that the system is rebooted. This
	 * in general handles intents when the application may not be running.
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		// Create a calendar object
		Calendar calendar = Calendar.getInstance();
		
		// intent used to register single alarms
		Intent singleAlarmIntent;
		
		// intent used to register single alarms
		Intent repeatedAlarmIntent;

		// Attempt to read from the two registered alarm files 
		try {
			
			// Read information from the single alarm files
			ArrayList<Map<String, String>> singleAlarms = DataStorageManager.readJSONObject(context, "single alarms");
			
			// Read information from the single alarm files
			ArrayList<Map<String, String>> repeatedAlarms = DataStorageManager.readJSONObject(context, "repeating alarms");

			// Go through the list of single alarms and register new alarms for each one
			for (Map<String, String> message : singleAlarms) {
				
				// Create an intent
				singleAlarmIntent = new Intent(context, AlarmReceiver.class);
				
				// Pass the information for the notification
				singleAlarmIntent.putExtra("type", message.get("type"));
				singleAlarmIntent.putExtra("title", message.get("title"));
				singleAlarmIntent.putExtra("description", message.get("description"));
				
				// Set the calendar to the time the alarm should go off
				calendar.setTimeInMillis(Long.parseLong(message.get("time of alarm")));

				// Set the information that will be used to generate the notification
				// If the notification is for an appointment set the appropriate information
				if (singleAlarmIntent.getStringExtra("type").equals("appointments")) {
					singleAlarmIntent.putExtra("name", message.get("name"));
					singleAlarmIntent.putExtra("location", message.get("location"));
					singleAlarmIntent.putExtra("phone", message.get("phone"));
					singleAlarmIntent.putExtra("email", message.get("email"));
					singleAlarmIntent.putExtra("comment", message.get("comment"));
					singleAlarmIntent.putExtra("start time", message.get("start time"));
					singleAlarmIntent.putExtra("end time", message.get("end time"));
					singleAlarmIntent.putExtra("date", message.get("date"));
				}
				
				// Set the alarm's time stamp and the appointments time stamp
				singleAlarmIntent.putExtra("alarm timestamp", message.get("timestamp"));
				singleAlarmIntent.putExtra("application timestamp", message.get("application timestamp"));

				// Create a pending intent that will be used when the alarm goes off
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						context, (int)Calendar.getInstance().getTimeInMillis(), singleAlarmIntent, PendingIntent.FLAG_ONE_SHOT);

				// Get the alarm manager of the system
				AlarmManager alarmManager = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);

				// Register the alarm
				alarmManager.set(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), pendingIntent);
			}

			// Go through the list of repeated alarms and register new alarms for each one
			for (Map<String, String> message : repeatedAlarms) {
				// Create an intent
				repeatedAlarmIntent = new Intent(context, AlarmReceiver.class);
				long intervalTimer = Long.parseLong(message.get("interval"));

				// Pass the information for the notification
				repeatedAlarmIntent.putExtra("type", message.get("type"));
				repeatedAlarmIntent.putExtra("title", message.get("title"));
				repeatedAlarmIntent.putExtra("description", message.get("description"));
				
				// Set the calendar to the time the alarm should go off
				calendar.setTimeInMillis(Integer.parseInt(message.get("time of alarm")));

				// Set the information that will be used to generate the notification
				// If the notification is for an appointment set the appropriate information
				if (repeatedAlarmIntent.getStringExtra("type").equals("appointments")) {
					repeatedAlarmIntent.putExtra("name", message.get("name"));
					repeatedAlarmIntent.putExtra("location", message.get("location"));
					repeatedAlarmIntent.putExtra("phone", message.get("phone"));
					repeatedAlarmIntent.putExtra("email", message.get("email"));
					repeatedAlarmIntent.putExtra("comment", message.get("comment"));
					repeatedAlarmIntent.putExtra("start time", message.get("start time"));
					repeatedAlarmIntent.putExtra("end time", message.get("end time"));
					repeatedAlarmIntent.putExtra("date", message.get("date"));
				}
				
				// Set the alarm's time stamp and the appointments time stamp
				repeatedAlarmIntent.putExtra("alarm timestamp", message.get("timestamp"));
				repeatedAlarmIntent.putExtra("application timestamp", message.get("application timestamp"));

				// Create a pending intent that will be used when the alarm goes off
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						context, (int)Calendar.getInstance().getTimeInMillis(), repeatedAlarmIntent, PendingIntent.FLAG_ONE_SHOT);

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

