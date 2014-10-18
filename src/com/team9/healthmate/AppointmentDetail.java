package com.team9.healthmate;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class AppointmentDetail extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_details);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		
	}

}
