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

public class AppointmentsList extends ListActivity {
	
	ArrayList<Map<String, String>> appointments; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_list);
		
		try {
		appointments =  DataStorageManager.readJSONObject(this, "appointments");
		ArrayList<String> appointmentList = new ArrayList<String>();
		for (int i = 0; i < appointments.size(); i++) {
			appointmentList.add("Appointment " + (i + 1));
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1, appointmentList);
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
