package com.team9.healthmate.Appointments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.team9.healthmate.R;
import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Activity Class that Displays a List of Appointments that
 * have been created by the user of the application. The user
 * can select an appointment and view its information through
 * another activity. The user may also create new appointments
 * through another activity by clicking the New Activity Button.
 * @author Michael Sandoval
 *
 */
public class AppointmentsList extends ListActivity {
	
	// Container for the list of appointments that will
	// be read from the appointments file.
	ArrayList<Map<String, String>> appointments; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_list);
		
		// Attempt to read from the Appointments file
		try {
			
		appointments =  DataStorageManager.readJSONObject(this, "appointments");
		
		// A list of strings that will be displayed to the user
		ArrayList<String> appointmentList = new ArrayList<String>();
		
		// reverse the list of appointments in order to list them in
		// newest appointment on the top of the list
		Collections.reverse(appointments);
		
		// The information that will be displayed to the user.
		String appointmentInformation = "";
		
		// Object used to process each appointment
		//Map<String, String> appointment;
		
		// Go through all the appointments and generate a list of Strings.
		// This list will be used to generate a list with information about
		// each appointment.
		
		for (Map<String, String> appointment : appointments) {
			appointmentInformation = appointment.get("title") + "\n" + "Doctor: " + 
					appointment.get("name") + "\nDate: " + appointment.get("date");
			appointmentList.add(appointmentInformation);
		}
		
		// Pass in the list of information, the adapter will generate
		// a list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1, appointmentList);
		
		// set the adapter as active.
		setListAdapter(adapter);
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method sets up the options available to the user.
	 * An icon of an addition sign is displayed to the user as an option. 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appointments_list, menu);
		return true;
	}
	
	/**
	 * Method to activate the user options in the Action Bar.
	 * The only option is to create a new appointment, this leads
	 * to the appointment form class.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Check to see which option was selected by the user.
		if (item.getItemId() == R.id.action_create_new_appointment) {
			Intent intent = new Intent(this, AppointmentForm.class);
			startActivity(intent);
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Method to handle the event the user selects an appointment
	 * from the list of appointments. The appointment's information
	 * is sent to the appointment details class.
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		
		String key;
		Set<String> setOfKeys;
		Iterator<String> stringIterator;
		
		// Get the appointment that was selected from the list of appointments.
		Map<String, String> appointment = appointments.get(position);
		
		// Create a new intent that will start the Appointment Detail Activity
		Intent intent = new Intent(this, AppointmentDetail.class);
		
		// Go through all the information stored in the appointment
		// selected using the objects keys. 
		setOfKeys = appointment.keySet();
		stringIterator = setOfKeys.iterator();
		
		while (stringIterator.hasNext()){
			key = stringIterator.next();
			
			// Add them to the intent, this will allow
			// the activity started by the intent to access the information.
			intent.putExtra(key, appointment.get(key));
			}
		
		// Remove the current activity from the top of the stack when finished.
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

}
