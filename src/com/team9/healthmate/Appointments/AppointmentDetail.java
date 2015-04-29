package com.team9.healthmate.Appointments;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.team9.healthmate.JsonManager.JSONParser;
import com.team9.healthmate.R;
import com.team9.healthmate.DataManager.DataStorageManager;
import com.team9.healthmate.NotificationsManager.NotificationsManager;
import com.team9.healthmate.ServerManager.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity Class that displays the information for a given
 * Appointment. Buttons are initialized that give the user 
 * will have the option of editing or deleting the Appointment
 * being displayed. 
 * @author Michael Sandoval
 *
 */
public class AppointmentDetail extends Activity {
	
	// Map Object to contain the appointment details
	private Map<String, String> appointmentDetails;
	
	// Intent object passed in
	Intent intent;
	
	// Create an instance for JSONParser
	 JSONParser jsonParser = new JSONParser();
	 
	// Create an instance for JSONObject class
	 JSONObject json;
	 
	 //server timestamp 
	 String timestamp;
	 
	// JSON Node names
	    private static final String TAG_SUCCESS = "success";
	 
	 // url to delete product
	    private static final String url_delete_appointment = "http://108.77.76.162/HealthMateIntegration/delete_appointment.php";

	/**
	 * Method retrieves information sent by the previous activity
	 * and displays the information on the screen.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_details);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Create a new Map Object
		appointmentDetails = new HashMap<String, String>();
		
		// Get the intent sent by the previous activity
		intent = getIntent();

		// Insert information from the intent into the Map Object
		appointmentDetails.put("timestamp", intent.getStringExtra("timestamp"));
		appointmentDetails.put("title", intent.getStringExtra("title"));
		appointmentDetails.put("name", intent.getStringExtra("name"));
		appointmentDetails.put("lastname", intent.getStringExtra("lastname"));
		appointmentDetails.put("doctorid", intent.getStringExtra("doctorid"));
		appointmentDetails.put("location", intent.getStringExtra("location"));
		appointmentDetails.put("city", intent.getStringExtra("city"));
		appointmentDetails.put("zip", intent.getStringExtra("zip"));
		appointmentDetails.put("state", intent.getStringExtra("state"));
		appointmentDetails.put("phone", intent.getStringExtra("phone"));
		appointmentDetails.put("email", intent.getStringExtra("email"));
		appointmentDetails.put("comment", intent.getStringExtra("comment"));
		appointmentDetails.put("start time", intent.getStringExtra("start time"));
		appointmentDetails.put("end time", intent.getStringExtra("end time"));
		appointmentDetails.put("date", intent.getStringExtra("date"));		

		// Go Through the information and display it on the screen.
		TextView textView = (TextView) findViewById(R.id.AppointmentDetailTitle);
		textView.setText(appointmentDetails.get("title"));

		textView = (TextView) findViewById(R.id.AppointmentDetailName);
		textView.setText("Doctor's Name: " + appointmentDetails.get("name") + " " +  appointmentDetails.get("lastname"));
		
		textView = (TextView) findViewById(R.id.AppointmentDetailId);
		textView.setText("Doctor's ID#: " + appointmentDetails.get("doctorid"));

		textView = (TextView) findViewById(R.id.AppointmentDetailAddress);
		textView.setText("Location: " + appointmentDetails.get("location") + ", " + appointmentDetails.get("city") + ", " + 
		appointmentDetails.get("state") + " " + appointmentDetails.get("zip"));

		textView = (TextView) findViewById(R.id.AppointmentDetailContactInformation);
		textView.setText("Contact Information: \n" + "\tPhone Number: " + 
				appointmentDetails.get("phone") + "\n\tEmail: " + appointmentDetails.get("email"));

		textView = (TextView) findViewById(R.id.AppointmentDetailDate);
		textView.setText("Date: " + appointmentDetails.get("date"));

		textView = (TextView) findViewById(R.id.AppointmentDetailStartTime);
		textView.setText("Start Time: " + appointmentDetails.get("start time"));

		textView = (TextView) findViewById(R.id.AppointmentDetailEndTime);
		textView.setText("End Time: " + appointmentDetails.get("end time"));

		textView = (TextView) findViewById(R.id.AppointmentDetailComment);
		textView.setText("Comment: \n" + appointmentDetails.get("comment"));
	}
	
	/**
	 * Method to set the options to delete or edit the 
	 * information of the appointment.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (intent.getStringExtra("notification") == null) {
			getMenuInflater().inflate(R.menu.appointment_details, menu);
		}
		return true;
	}

	/**
	 * Method to handle the events of when the user selects
	 * an option.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Check if the User is deleting an appointment
		if (item.getItemId() == R.id.action_delete_appointment) {
			
			try { 
				
				// Container for registered notification information
				ArrayList<Map<String, String>> registeredNotifications; 
				
				// Read all the registered single alarms for notifications
				registeredNotifications = DataStorageManager.readJSONObject(this, "single alarms");
				
				// Save the timestamp for server side deletion
				timestamp = appointmentDetails.get("timestamp");			
				
				// Delete the appointment from the file of appointments
				DataStorageManager.deleteJSONObject(this, "appointments", appointmentDetails);
				
				
				
				// Look for the registered notification for the given appointment based on the
				// appointments time stamp. If found, delete the alarm from the file 
				for (Map<String, String> map : registeredNotifications) {
					
					if (appointmentDetails.get("timestamp").equals(map.get("application timestamp"))) {
						DataStorageManager.deleteJSONObject(this, "single alarms", map);
					}
				}
				
				// Create a new message that will be used to cancel any existing alarms
				Map<String, String>  message = new HashMap<String, String>();
				
				// fill the message with the appointment information
				message.putAll(appointmentDetails);
				
				// remove the time stamp, this was not in the original message used
				// to create the alarm
				message.remove("timestamp");
				
				// insert into message the information of the notification that
				// would have be shown to the user
				String description = "Appointment with: " + appointmentDetails.get("name") + 
						"\n You have an appointment on " + appointmentDetails.get("date") + 
						"\n Staring at: " + appointmentDetails.get("start time");
				message.put("type", "appointments");
				message.put("title", appointmentDetails.get("title"));
				message.put("description", description);
				
				// Unregister the alarm for the notification
				NotificationsManager.unregisterNotification(this, message);
				
				// Execute the deletion from server as well
				deleteDataonServer();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// Go back to the appointment list after deletion.
			Intent intent = new Intent(this, AppointmentsList.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}

		// Check to see if the user wants to edit the appointment
		if (item.getItemId() == R.id.action_edit_appointment) {
			
			String key;
			Set<String> setOfKeys;
			Iterator<String> stringIterator;
			
			// Create a new intent to go to the Appointment Form
			Intent intent = new Intent(this, AppointmentForm.class);

			// Go through the information of the appointment details,
			// and add them to the intent that will be sent to the
			// Appointment Form activity.
			setOfKeys = appointmentDetails.keySet();
			stringIterator = setOfKeys.iterator();
			while (stringIterator.hasNext()) {
				key = stringIterator.next();
				intent.putExtra(key, appointmentDetails.get(key));
			}
			// Go to the next activity
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    startActivity(intent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Method to handle the event that the user wants
	 * to go back to the Appointment List.
	 */
	@Override
	public void onBackPressed() {
		Intent newIntent = new Intent(AppointmentDetail.this,
				AppointmentsList.class);
		newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(newIntent);
		finish();
	}
	/**
	 * Background Async Task to Delete Appointment
	 * data on the server side
	 */
	public void deleteDataonServer(){
		// map object to pass in async thread
		Map<String, String> paramToDelete = new HashMap<String, String>();
		// will delete data row by timestamp
		paramToDelete.put("timestamp", timestamp);
		// creat the thread with deletion procedure		
		new ServerStorageManager("Delete",url_delete_appointment, paramToDelete, this);
		
				
	}
	  
    
    
}
