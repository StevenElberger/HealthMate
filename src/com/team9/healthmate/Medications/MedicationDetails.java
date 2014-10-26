package com.team9.healthmate.Medications;

import com.team9.healthmate.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MedicationDetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medication_details);
		
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
