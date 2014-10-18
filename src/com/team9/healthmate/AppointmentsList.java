package com.team9.healthmate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AppointmentsList extends ListActivity {
	
	private static final int REQUEST_CODE = 100;
	ArrayList<Map<String, String>> appointments; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_list);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		try {
		appointments =  DataStorageManager.readJSONObject(this, "appointments");
		ArrayAdapter<Map<String, String>> adapter = new ArrayAdapter<Map<String, String>>(
				this, android.R.layout.simple_list_item_1, appointments);
		setListAdapter(adapter);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
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
			intent.putExtra(key, stringIterator.next());
			}
		startActivityForResult(intent, REQUEST_CODE);
	}

}
