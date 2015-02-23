package com.team9.healthmate.Medications;

import com.team9.healthmate.R;
import com.team9.healthmate.Medications.MedicationObject.DosageType;
import com.team9.healthmate.Medications.MedicationObject.FrequencyType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * The NewMedication class implements the functionalities for the creation of
 * a new medication form. 
 * 
 * @author Gustavo Arce
 */

public class NewMedication extends Activity {
	private boolean updatingMedication = false;	
	
	/**
	 * Android's onCreate method. Called on the start of the activity.
	 * Here is where all fields are filled method is called if the user 
	 * is editing an existing medication.
	 * 
	 * @param Bundle savedInstaceState as requested by Android
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_medication);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		populateFrequencySpinner();
		populateMedicationTypeSpinner();
		
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
            	if (!isValidForm())
            		return;
            	updateMedicationList();
            	Medication.adapter.notifyDataSetChanged();
            	Medication.saveMedication(NewMedication.this);
            	setResult(1);
              	finish();
              }
          });
	}
	
	/**
	 * fillFormWithMedication method populates all the fields on
	 * the form with the information of the given medication.
	 * 
	 * @param med is the existing medication object that contains
	 * all the necessary information to fill up the fields.
	 */
	private void fillFormWithMedication(MedicationObject med)
	{
		EditText name = (EditText) findViewById(R.id.new_medication_name);
		name.setEnabled(false);
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
		dosageStrength.setText(""+med.dosageValue);
					
		Button button = (Button) findViewById(R.id.new_medication_save);
		button.setText("Update");
	}
	
	/**
	 * Takes all the medication information from the fields and saves it to the
	 * medication list. It either updates the medication if it already exist or
	 * creates a new entry otherwise.
	 */
	private void updateMedicationList()	{
		String name;
		int frequencyValue, dosageValue;
		FrequencyType frequencyType;
		DosageType dosageType;
		TextView nameTextView, frequencyValueTextView, dosageValueTextView;
		Spinner frequencyTypeSppinner, dosageTypeSpinner;
		
		nameTextView           = (TextView) findViewById(R.id.new_medication_name);
		frequencyValueTextView = (TextView) findViewById(R.id.new_medication_dosage_frequency);
		dosageValueTextView    = (TextView) findViewById(R.id.new_medication_dosage_strength);
		dosageTypeSpinner      = (Spinner)  findViewById(R.id.new_medication_medication_type_spinner);
		frequencyTypeSppinner  = (Spinner)  findViewById(R.id.new_medication_frequency_type);
		
		name = nameTextView.getText().toString();
		dosageValue = Integer.parseInt(dosageValueTextView.getText().toString());
		dosageType = DosageType.valueOf(dosageTypeSpinner.getSelectedItem().toString());
		
		if (((RadioButton) findViewById(R.id.new_medication_frequency_radio_A)).isChecked()) {
			frequencyType = FrequencyType.Day;
			frequencyValue = 1;
		}
		else if (((RadioButton) findViewById(R.id.new_medication_frequency_radio_B)).isChecked()) {
			frequencyType = FrequencyType.Day;
			frequencyValue = 2;
		}
		else if (((RadioButton) findViewById(R.id.new_medication_frequency_radio_C)).isChecked()) {
			frequencyType = FrequencyType.Week;
			frequencyValue = 1;			
		}
		else if (((RadioButton) findViewById(R.id.new_medication_frequency_radio_D)).isChecked()) {
			frequencyType = FrequencyType.valueOf(frequencyTypeSppinner.getSelectedItem().toString());
			frequencyValue = Integer.parseInt(frequencyValueTextView.getText().toString());
		}
		else {
			throw new Error("No selected radio has been found.");		
		}
		
		MedicationObject medication = new MedicationObject(name, frequencyType, frequencyValue, 
				dosageType, dosageValue);
		
		if (updatingMedication) {
			boolean found = false;
			for (int i=0; i<Medication.medications.size(); i++) {
				if (Medication.medications.get(i).name.equals(medication.name)) {					
					Medication.medications.remove(i);
					Medication.medications.add(i,medication);
					found = true;
				}
			}
			if (!found) {
				throw new Error("Can't update an unexistent medication.");			
			}
		} else {
			Medication.medications.add(medication);				
		}
	}
		
	/**
	 * Custom number picker that increases the displayed number on the form.
	 * @param v is the View requested by Android for all event listeners called
	 * from the XML file.
	 */
	public void increaseNumberPicker(View v) {
		EditText numberPicker = (EditText) findViewById(R.id.new_medication_dosage_frequency);
		int currentValue = Integer.parseInt(numberPicker.getText().toString());
		numberPicker.setText(""+(currentValue+1));
	}
	
	/**
	 * Custom number picker that decreases the displayed number on the form.
	 * @param v is the View requested by Android for all event listeners called
	 * from the XML file.
	 */
	public void decreaseNumberPicker(View v) {
		EditText numberPicker = (EditText) findViewById(R.id.new_medication_dosage_frequency);
		int currentValue = Integer.parseInt(numberPicker.getText().toString());
		if(currentValue > 0) {
			numberPicker.setText(""+(currentValue-1));
		}
	}
	
	/**
	 * Sets the values for the frequency spinner.
	 */
	private void populateFrequencySpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.new_medication_frequency_type);
		String[] spinnerValues = {"Hour", "Day", "Week", "Month"};
		
		ArrayAdapter<String> adapter = 
				new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerValues);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
	
	/**
	 * Sets the values for the medication type spinner.
	 */
	private void populateMedicationTypeSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.new_medication_medication_type_spinner);
		String[] spinnerValues = {"mg", "mL", "cc"};
		
		ArrayAdapter<String> adapter = 
				new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerValues);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
	
	/**
	 * Enables the frequency picker.
	 * @param v is the View requested by Android for all event listeners called
	 * from the XML file.
	 */
	public void enableFrequencyPicker(View v) {
		Button b = (Button) findViewById(R.id.new_medication_numer_picker_increase);
		b.setEnabled(true);
		b = (Button)findViewById(R.id.new_medication_numer_picker_decrease);
		b.setEnabled(true);
		Spinner spinner = (Spinner) findViewById(R.id.new_medication_frequency_type);
		spinner.setClickable(true);
	}
	
	/**
	 * Disables the frequency picker.
	 * @param v is the View requested by Android for all event listeners called
	 * from the XML file.
	 */
	public void disableFrequencyPicker(View v) {
		Button b = (Button) findViewById(R.id.new_medication_numer_picker_increase);
		b.setEnabled(false);
		b = (Button)findViewById(R.id.new_medication_numer_picker_decrease);
		b.setEnabled(false);
		Spinner spinner = (Spinner) findViewById(R.id.new_medication_frequency_type);
		spinner.setClickable(false);
	}
	
	/**
	 * isValidForm method validates all the form fields. Returns true if everything is
	 * valid, false if one of the fields are wrong
	 * 
	 * @return boolean value representing if the form is valid or not
	 */
	public boolean isValidForm() {
		boolean isValid = false;
		
		String name;
		int frequencyValue, dosageValue;
		TextView nameTextView, frequencyValueTextView, dosageValueTextView;
				
		nameTextView           = (TextView) findViewById(R.id.new_medication_name);
		frequencyValueTextView = (TextView) findViewById(R.id.new_medication_dosage_frequency);
		dosageValueTextView    = (TextView) findViewById(R.id.new_medication_dosage_strength);
		
		name = nameTextView.getText().toString();
		String strDosageValue = dosageValueTextView.getText().toString();
		if (!strDosageValue.equals("")) {
			dosageValue = Integer.parseInt(strDosageValue);
		}
		else  {
			dosageValue = 0;
		}
		
		if (((RadioButton) findViewById(R.id.new_medication_frequency_radio_A)).isChecked()) {
			frequencyValue = 1;
		}
		else if (((RadioButton) findViewById(R.id.new_medication_frequency_radio_B)).isChecked()) {
			frequencyValue = 2;
		}
		else if (((RadioButton) findViewById(R.id.new_medication_frequency_radio_C)).isChecked()) {
			frequencyValue = 1;			
		}
		else if (((RadioButton) findViewById(R.id.new_medication_frequency_radio_D)).isChecked()) {
			frequencyValue = Integer.parseInt(frequencyValueTextView.getText().toString());
		}
		else {
			return false;
		}
		
		if (name != null && dosageValue > 0 && frequencyValue > 0)
			isValid = true;

		return isValid;
	}
	
	public void onReminderToggleSwitch(View v) {		
		return;
	}
}