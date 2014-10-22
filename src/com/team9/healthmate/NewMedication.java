package com.team9.healthmate;

import com.team9.healthmate.MedicationObject.FrequencyType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class NewMedication extends Activity {
	private boolean updatingMedication = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_medication);
		
		populateFrequencySpinner();
		
		//Checks if it is edit or create new med
		Intent i = getIntent();
		MedicationObject med = (MedicationObject)i.getSerializableExtra("medInfo");
		
		//if it is edit populate form fields
		if (med != null) {
			updatingMedication = true;
			fillFormWithMedication(med);
		}
		
		//cancel button
		Button button = (Button) findViewById(R.id.new_medication_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Medication.Debug("Sending result = 0");
            	setResult(0);
            	finish();
            }
        });
        
      //save button
  		button = (Button) findViewById(R.id.new_medication_save);
          button.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
            	updateMedicationList();
            	setResult(1);
              	finish();
              }
          });
	}
	
	private void fillFormWithMedication(MedicationObject med)
	{
		EditText name = (EditText) findViewById(R.id.new_medication_name);
		name.setText(med.name);
		
		RadioButton radio;
		if (med.frequencyValue == 1 && med.frequencyType == FrequencyType.Day) {
			radio = (RadioButton) findViewById(R.id.new_medication_frequency_radio_A);
		}
		else if (med.frequencyValue == 2 && med.frequencyType == FrequencyType.Day) {
			radio = (RadioButton) findViewById(R.id.new_medication_frequency_radio_B);
		}
		else if (med.frequencyValue == 1 && med.frequencyType == FrequencyType.Week) {
			radio = (RadioButton) findViewById(R.id.new_medication_frequency_radio_C);
		}
		else {
			radio = (RadioButton) findViewById(R.id.new_medication_frequency_radio_D);
			radio.setChecked(true);
			enableFrequencyPicker(new View(this));
			EditText freq = (EditText) findViewById(R.id.new_medication_dosage_frequency);
			freq.setText(""+med.frequencyValue);
			Spinner spinner = (Spinner) findViewById(R.id.new_medication_frequency_type);
			spinner.setSelection(med.frequencyType.ordinal());
		}
		radio.setChecked(true);
		
		EditText dosageStrength = (EditText) findViewById(R.id.new_medication_dosage_strength);
		dosageStrength.setText(""+med.ammount);
					
		Button button = (Button) findViewById(R.id.new_medication_save);
		button.setText("Update");
	}
	
	private void updateMedicationList()	{
		String name = ((TextView)(findViewById(R.id.new_medication_name))).getText().toString();
		FrequencyType frequencyType = FrequencyType.valueOf(((Spinner)(findViewById(R.id.new_medication_frequency_type))).getSelectedItem().toString());
		int frequencyValue = Integer.parseInt(((TextView)findViewById(R.id.new_medication_dosage_frequency)).getText().toString());
		int dosage = Integer.parseInt(((TextView)findViewById(R.id.new_medication_dosage_strength)).getText().toString());
		
		MedicationObject medication = new MedicationObject(name, frequencyType, frequencyValue, dosage);
		if (updatingMedication) {
			boolean found = false;
			for (int i=0; i<Medication.medications.size(); i++) {
				if (Medication.medications.get(i).equals(medication.name))
					Medication.medications.remove(i);
					Medication.medications.add(i,medication);					
			}
			if (!found) {
				throw new Error("Can't update an unexistent medication.");			
			}
		} else {
			Medication.medications.add(medication);			
		}
	}
	
	public void increaseNumberPicker(View v) {
		EditText numberPicker = (EditText) findViewById(R.id.new_medication_dosage_frequency);
		int currentValue = Integer.parseInt(numberPicker.getText().toString());
		numberPicker.setText(""+(currentValue+1));
	}
	
	public void decreaseNumberPicker(View v) {
		EditText numberPicker = (EditText) findViewById(R.id.new_medication_dosage_frequency);
		int currentValue = Integer.parseInt(numberPicker.getText().toString());
		if(currentValue > 0) {
			numberPicker.setText(""+(currentValue-1));
		}
	}
	
	private void populateFrequencySpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.new_medication_frequency_type);
		String[] spinnerValues = {"Day", "Week", "Month"};
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerValues);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
	}
	
	public void enableFrequencyPicker(View v) {
		Button b = (Button) findViewById(R.id.new_medication_numer_picker_increase);
		b.setEnabled(true);
		b = (Button)findViewById(R.id.new_medication_numer_picker_decrease);
		b.setEnabled(true);
		Spinner spinner = (Spinner) findViewById(R.id.new_medication_frequency_type);
		spinner.setClickable(true);
	}
	
	public void disableFrequencyPicker(View v) {
		Button b = (Button) findViewById(R.id.new_medication_numer_picker_increase);
		b.setEnabled(false);
		b = (Button)findViewById(R.id.new_medication_numer_picker_decrease);
		b.setEnabled(false);
		Spinner spinner = (Spinner) findViewById(R.id.new_medication_frequency_type);
		spinner.setClickable(false);
	}
	
	public void onReminderToggleSwitch(View v) {		
		return;
	}
}