package com.team9.healthmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AppointmentDetail extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_details);
		
		Intent intent = getIntent();
		
		String appointmentTitle = intent.getStringExtra("title");
		String doctorsName = intent.getStringExtra("name");
		String location = intent.getStringExtra("location");
		String phone = intent.getStringExtra("phone");
		String email = intent.getStringExtra("email");
		String comment = intent.getStringExtra("comment");
		String startTime = intent.getStringExtra("start time");
		String endTime = intent.getStringExtra("end time");
		String date = intent.getStringExtra("date");
		
		TextView textView =(TextView) findViewById(R.id.AppointmentDetailTitle);
		textView.setText(appointmentTitle);
		
		textView =(TextView) findViewById(R.id.AppointmentDetailName);
		textView.setText("Doctor's Name: " + doctorsName);
		
		textView =(TextView) findViewById(R.id.AppointmentDetailAddress);
		textView.setText("Location: " + location);
		
		textView =(TextView) findViewById(R.id.AppointmentDetailContactInformation);
		textView.setText("Contact Information: \n" + "\tPhone Number: " + phone + "\n\tEmail: " + email);
		
		textView =(TextView) findViewById(R.id.AppointmentDetailDate);
		textView.setText("Date: " + date);
		
		textView =(TextView) findViewById(R.id.AppointmentDetailStartTime);
		textView.setText("Start Time: " + startTime);
		
		textView =(TextView) findViewById(R.id.AppointmentDetailEndTime);
		textView.setText("End Time: " + endTime);
		
		textView =(TextView) findViewById(R.id.AppointmentDetailComment);
		textView.setText("Comment: \n" + comment);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.menu.appointment_details, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		Intent newIntent = new Intent(AppointmentDetail.this, AppointmentsList.class);
		newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(newIntent);
		finish();
	}

}
