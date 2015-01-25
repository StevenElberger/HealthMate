package com.team9.healthmate.NotificationsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONException;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/** Class that registers notifications to be created at a specified time.
 * 	There are two types of notifications that can be registered, single time
 * 	notifications and repeated notifications. In order to create a notification
 * 	properly, a Map<String, String> object must be provided with keys, "type",
 *  "title", and "description". These will be used to set the notification content.
 * 
 * @author Michael Sandoval
 */
public class NotificationsManager {
	
	/**
	 * Method to register a notification using an alarm manager. The notification 
	 * will be set to activate at a specified time. This is a one time notification
	 * register, after the notification goes off, it will no longer activate or exist.
	 * There are three key values that must be passed in the Map object for the method
	 * to work properly, "type", "title", and "description". The values of these keys
	 * will be used to set the notification content. 
	 * @param context the current context of the application
	 * @param message The information that will be used to create the notification
	 * @param calendar The time the notification will be activated
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static void registerNotification(Context context, Map<String, String> message, Calendar calendar) throws JSONException, IOException {
		
		// Create an intent 
		Intent intent = new Intent(context, AlarmReceiver.class);
		
		// Container for the alarms registered
		ArrayList<Map<String, String>> alarms;
		
		// Pass the information for the notification
		intent.putExtra("type", message.get("type"));
		intent.putExtra("title", message.get("title"));
		intent.putExtra("description", message.get("description"));
		
		// If the notification is meant for appointments, send the appropriate
		// references and data to the alarm.
		if (intent.getStringExtra("type").equals("appointments")) {
			intent.putExtra("name", message.get("name"));
			intent.putExtra("location", message.get("location"));
			intent.putExtra("phone", message.get("phone"));
			intent.putExtra("email", message.get("email"));
			intent.putExtra("comment", message.get("comment"));
			intent.putExtra("start time", message.get("start time"));
			intent.putExtra("end time", message.get("end time"));
			intent.putExtra("date", message.get("date"));
		}
		
		// If the notification is meant for medications, send the appropriate
		// references and data to the alarm.
		if (intent.getStringExtra("type").equals("medications")) {
			
			// This needs to be filled with medication information
			
		}
		
		// set the time stamp that will indicate which notification belongs to which
		intent.putExtra("application timestamp", message.get("timestamp"));
		message.put("application timestamp", message.get("timestamp"));
		message.put("time of alarm", "" + calendar.getTimeInMillis());
		
		
		// save notification information
		DataStorageManager.writeJSONObject(context, "single alarms", message, false);
		alarms = DataStorageManager.readJSONObject(context, "single alarms");
		intent.putExtra("alarm timestamp", alarms.get(alarms.size() - 1).get("timestamp"));
		
		// Create a pending intent that will be used when the alarm goes off
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)Calendar.getInstance().getTimeInMillis(), intent, PendingIntent.FLAG_ONE_SHOT);
		
		// Get the alarm manager of the system
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		// Register the alarm
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	}
	
	/**
	 * Method to register a repeating notification using an alarm manager. The notification 
	 * will be set to activate at a specified time. This notification will then be
	 * activated after a specified amount of time has passed. There are three key values 
	 * that must be passed in the Map object for the method to work properly, 
	 * "type", "title", and "description". The values of these keys will be used to 
	 * set the notification content. 
	 * @param context the current context of the application
	 * @param message The information that will be used to create the notification
	 * @param calendar The time the notification will be activated
	 * @param intervalTimer The time that will pass before the notification to be activated again.
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static void registerRepeatNotification(Context context, Map<String, String> message, Calendar calendar, long intervalTimer) throws JSONException, IOException {
		
		// Create an intent
		Intent intent = new Intent(context, AlarmReceiver.class);
		ArrayList<Map<String, String>> alarms;
		
		// Pass the information for the notification
		intent.putExtra("type", message.get("type"));
		intent.putExtra("title", message.get("title"));
		intent.putExtra("description", message.get("description"));
		
		// If the notification is meant for appointments, send the appropriate
		// references and data to the alarm.
		if (intent.getStringExtra("type").equals("appointments")) {
			intent.putExtra("name", message.get("name"));
			intent.putExtra("location", message.get("location"));
			intent.putExtra("phone", message.get("phone"));
			intent.putExtra("email", message.get("email"));
			intent.putExtra("comment", message.get("comment"));
			intent.putExtra("start time", message.get("start time"));
			intent.putExtra("end time", message.get("end time"));
			intent.putExtra("date", message.get("date"));
		}
		
		// If the notification is meant for medications, send the appropriate
		// references and data to the alarm.
		if (intent.getStringExtra("type").equals("medications")) {
					
			// This needs to be filled with medication information
					
		}

		// used to remove the alarm saved in the alarm registration file
		intent.putExtra("application timestamp", message.get("timestamp"));
		
		// add relevant information for the alarm registration
		message.put("time of alarm", "" + calendar.getTimeInMillis());
		message.put("interval", "" + intervalTimer);
		message.put("application timestamp", message.get("timestamp"));
		
		
		// save notification information
		DataStorageManager.writeJSONObject(context, "repeated alarms", message, false);
		alarms = DataStorageManager.readJSONObject(context, "repeated alarms");
		intent.putExtra("alarm timestamp", alarms.get(alarms.size() - 1).get("timestamp"));
		
		// Create a pending intent that will be used when the alarm goes off
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)Calendar.getInstance().getTimeInMillis(), intent, PendingIntent.FLAG_ONE_SHOT);
		
		// Get the alarm manager of the system
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		// Register the alarm
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalTimer ,pendingIntent);
	}
	
	/**
	 * Method to unregister notifications. The alarm that was set to go off at a specified time
	 * will be canceled. The alarm that is canceled is determined by the information sent
	 * in the Map<String, String> message and the context. 
	 * @param context the current context of the Application
	 * @param message The information of the intent that started the alarm
	 */
	public static void unregisterNotification(Context context, Map<String, String> message) {
		
		// Create an intent
		Intent intent = new Intent(context, AlarmReceiver.class);
		
		// Pass the information for the notification
		intent.putExtra("type", message.get("type"));
		intent.putExtra("title", message.get("title"));
		intent.putExtra("description", message.get("description"));
		
		// Create a pending intent that will be used to cancel the alarm 
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		
		// Get the alarm manager of the system
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		// Cancel the alarm
		alarmManager.cancel(pendingIntent);
		
	}
}