package com.team9.healthmate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AppointmentDetail extends Activity {

	private Map<String, String> appointmentDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_details);

		appointmentDetails = new HashMap<String, String>();

		Intent intent = getIntent();

		appointmentDetails.put("timestamp", intent.getStringExtra("timestamp"));
		appointmentDetails.put("title", intent.getStringExtra("title"));
		appointmentDetails.put("name", intent.getStringExtra("name"));
		appointmentDetails.put("location", intent.getStringExtra("location"));
		appointmentDetails.put("phone", intent.getStringExtra("phone"));
		appointmentDetails.put("email", intent.getStringExtra("email"));
		appointmentDetails.put("comment", intent.getStringExtra("comment"));
		appointmentDetails.put("start time", intent.getStringExtra("start time"));
		appointmentDetails.put("end time", intent.getStringExtra("end time"));
		appointmentDetails.put("date", intent.getStringExtra("date"));
		
		String key;
		Set<String> setOfKeys;
		Iterator<String> stringIterator;
		
		setOfKeys = appointmentDetails.keySet();
		stringIterator = setOfKeys.iterator();
		while (stringIterator.hasNext()){
			key = stringIterator.next();
			Log.w("Deletion", key + ":" + appointmentDetails.get(key));
			}
		

		TextView textView = (TextView) findViewById(R.id.AppointmentDetailTitle);
		textView.setText(appointmentDetails.get("title"));

		textView = (TextView) findViewById(R.id.AppointmentDetailName);
		textView.setText("Doctor's Name: " + appointmentDetails.get("name"));

		textView = (TextView) findViewById(R.id.AppointmentDetailAddress);
		textView.setText("Location: " + appointmentDetails.get("location"));

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appointment_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_delete_appointment) {
			
			try {
				if (DataStorageManager.deleteJSONObject(this, "appointments", appointmentDetails)) {
					Log.w("Deletion", "Successful Deletion");
				} else {
					Log.w("Deletion", "Failure");
				}
				DataStorageManager.deleteJSONObject(this, "appointments", appointmentDetails);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Intent intent = new Intent(this, AppointmentsList.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			//finish();
		}

		if (item.getItemId() == R.id.action_edit_appointment) {
			String key;
			Set<String> setOfKeys;
			Iterator<String> stringIterator;
			Intent intent = new Intent(this, AppointmentForm.class);

			setOfKeys = appointmentDetails.keySet();
			stringIterator = setOfKeys.iterator();
			while (stringIterator.hasNext()) {
				key = stringIterator.next();
				intent.putExtra(key, appointmentDetails.get(key));
			}

			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			//finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		Intent newIntent = new Intent(AppointmentDetail.this,
				AppointmentsList.class);
		newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(newIntent);
		finish();
	}

}
