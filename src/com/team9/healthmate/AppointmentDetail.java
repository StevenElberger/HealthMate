package com.team9.healthmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AppointmentDetail extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_details);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		
		String doctorsName = intent.getStringExtra("name");
		String location = intent.getStringExtra("location");
		String phone = intent.getStringExtra("phone");
		String email = intent.getStringExtra("email");
		String comment = intent.getStringExtra("comment");
		String time = intent.getStringExtra("time");
		String date = intent.getStringExtra("date");
		
		TextView textView =(TextView) findViewById(R.id.AppointmentDetailName);
		textView.setText("Doctor's Name: " + doctorsName);
		
		textView =(TextView) findViewById(R.id.AppointmentDetailAddress);
		textView.setText("Location: " + location);
		
		textView =(TextView) findViewById(R.id.AppointmentDetailContactInformation);
		textView.setText("Contact Information: \n" + "\tPhone Number: " + phone + "\n\tEmail: " + email);
		
		textView =(TextView) findViewById(R.id.AppointmentDetailDate);
		textView.setText("Date: " + date);
		
		textView =(TextView) findViewById(R.id.AppointmentDetailTime);
		textView.setText("Time: " + time);
		
		textView =(TextView) findViewById(R.id.AppointmentDetailComment);
		textView.setText("Comment: \n" + comment);
		
	}

}
