package com.team9.healthmate.Medications;

import com.team9.healthmate.R;
import com.team9.healthmate.Medications.Medication;
import com.team9.healthmate.Medications.MedicationObject;
import com.team9.healthmate.Medications.NewMedication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * MedicationDetails class main purpose is to display a detail information
 * of a given medication. This class also contain the button listeners to
 * edit or remove the displayed medication.
 * 
 * @author Guss
 *
 */
public class MedicationDetails extends Activity {
	
	/**
	 * Android's onCreate method. Called on the start of the activity.
	 * Here is where all fields are filled method is called if the user 
	 * is editing an existing medication
	 * 
	 * @param Bundle savedInstaceState as requested by Android
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medication_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent i = getIntent();
		final MedicationObject med = (MedicationObject)i.getSerializableExtra("medInfo");
		TextView name, freq, dosage, reminder;
				
		name     = (TextView) findViewById(R.id.medication_details_name);
		freq     = (TextView) findViewById(R.id.medication_details_frequency);
		dosage   = (TextView) findViewById(R.id.medication_details_dosage);
		reminder = (TextView) findViewById(R.id.medication_details_reminder);
		
		name.setText(med.name);		
		freq.setText("Take "+med.frequencyValue+" every "+med.frequencyType);		
		dosage.setText(med.dosageValue+"mg");		
		if(med.reminderStatus) {
			reminder.setText("ON");
		}
		else {
			reminder.setText("OFF");
		}
		
		//back button event-listener, returns to previous activity
		Button back = (Button) findViewById(R.id.medication_details_button_back);
		back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
        
		//edit button event-listener, calls NewMedication activity passaing the current med
		//to be edited
		Button edit = (Button) findViewById(R.id.medication_details_button_edit);
		edit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		    	Intent intent = new Intent(MedicationDetails.this, NewMedication.class);  
		    	intent.putExtra("medInfo", med);
		    	startActivity(intent);
		    	finish();
			}
		});
	      
		//remove button
		Button remove = (Button) findViewById(R.id.medication_details_button_remove);
		remove.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
	        	  removeMedication(med);
	        	  Medication.adapter.notifyDataSetChanged();
	        	  Medication.saveMedication(MedicationDetails.this);
	        	finish();
			}
		});
	}
	
	/**
	 * Removes medication from the list on the Medication class
	 * @param med is the medication to be removed
	 */
	private void removeMedication(MedicationObject med)
	{
		boolean found = false;
		for (int i=0; i<Medication.medications.size(); i++) {
			if (Medication.medications.get(i).name.equals(med.name)) {
				found = true;
				Medication.medications.remove(i);
			}
		}
		if (!found) {
			throw new Error("Can't update an unexistent medication.");			
		}
	}
}
