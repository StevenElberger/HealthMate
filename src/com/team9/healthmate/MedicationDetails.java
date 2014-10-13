package com.team9.healthmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MedicationDetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medication_details);
		
		//Set field values		
		Intent i = getIntent();
		final MedicationObject med = (MedicationObject)i.getSerializableExtra("medInfo");
		
		TextView name = (TextView) findViewById(R.id.medication_details_name);
		name.setText(med.name);
		
		TextView freq = (TextView) findViewById(R.id.medication_details_frequency);
		freq.setText("Take "+med.frequencyValue+" every "+med.frequencyType);
		
		TextView dosage = (TextView) findViewById(R.id.medication_details_dosage);
		dosage.setText(med.ammount+"mg");
		
		TextView reminder = (TextView) findViewById(R.id.medication_details_reminder);				
		if(med.reminderStatus)
			reminder.setText("ON");
		else
			reminder.setText("OFF");
		
		//back button
		Button back = (Button) findViewById(R.id.medication_details_button_back);
		back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
        
        //edit button
		Button edit = (Button) findViewById(R.id.medication_details_button_edit);
	      edit.setOnClickListener(new View.OnClickListener() {
	          public void onClick(View v) {
	        	Intent intent = new Intent(MedicationDetails.this, NewMedication.class);  
	        	intent.putExtra("medInfo", med);
	        	startActivity(intent);
	        	finish();
	          }
	      });
	}
	
}
