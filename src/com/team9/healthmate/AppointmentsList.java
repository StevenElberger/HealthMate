package com.team9.healthmate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
		
		// The information that will be displayed to the user.
		String appointmentInformation = "";
		
		// Object used to process each appointment
		Map<String, String> appointment;
		
		// Go through all the appointments and generate a list of Strings.
		// This list will be used to generate a list with information about
		// each appointment.
		for (int i = 0; i < appointments.size(); i++) {
			appointment = appointments.get(i);
			appointmentInformation = appointment.get("title") + "\n" + "Doctor: " + 
					appointment.get("name") + " Date: " + appointment.get("date");
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appointments_list, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.action_create_new_appointment) {
			Intent intent = new Intent(this, AppointmentForm.class);
			startActivity(intent);
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		String key;
		Set<String> setOfKeys;
		Iterator<String> stringIterator;
		Map<String, String> appointment = appointments.get(position);
		Intent intent = new Intent(this, AppointmentDetail.class);
		
		setOfKeys = appointment.keySet();
		stringIterator = setOfKeys.iterator();
		while (stringIterator.hasNext()){
			key = stringIterator.next();
			intent.putExtra(key, appointment.get(key));
			}
		
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

}
