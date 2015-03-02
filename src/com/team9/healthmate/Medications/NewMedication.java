package com.team9.healthmate.Medications;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.team9.healthmate.R;
import com.team9.healthmate.Appointments.AppointmentsList;
import com.team9.healthmate.DataManager.DataStorageManager;
import com.team9.healthmate.Medications.MedicationObject.DosageType;
import com.team9.healthmate.Medications.MedicationObject.FrequencyType;
import com.team9.healthmate.NotificationsManager.NotificationsManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

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
	 * Create medicationDetils from MedicationObject
	 */
	private Map<String, String> medicationToMap(MedicationObject med) {		
		Map<String, String> medicationDetails = new HashMap<String,String>();			
		medicationDetails.put("name"		 	, med.name);
		medicationDetails.put("frequencyType"	, med.frequencyType.name());
		medicationDetails.put("frequencyValue"	, med.frequencyValue + "");		     
		medicationDetails.put("dosageType"		, med.dosageType.name());
		medicationDetails.put("dosageValue"		, med.dosageValue + "");
		medicationDetails.put("reminder"		, (med.reminderStatus) ? "ON" : "OFF");			
		medicationDetails.put("time", med.medicationCalendar.get(Calendar.HOUR_OF_DAY) + ":" + 
									  med.medicationCalendar.get(Calendar.MINUTE));
		medicationDetails.put("date", med.medicationCalendar.get(Calendar.MONTH) + "/" +
									  med.medicationCalendar.get(Calendar.DATE)  + "/" +
									  med.medicationCalendar.get(Calendar.YEAR));			
		return medicationDetails;
	}
	
	private long getMedicationIntervalTimer(MedicationObject med) {
		long intervalTimer;		
		switch(med.frequencyType)
		{
			case Hour: intervalTimer = 3600 * med.frequencyValue;
				break;
			case Day: intervalTimer  = 86400;
				break;
			case Week: intervalTimer = 604800;
				break;
			default: intervalTimer = -1;
				break;			
		}		
		
		return 1000;//intervalTimer;
	}
	
	private void registerMedicationNotification(MedicationObject med) {
		
		Map<String, String> medicationDetails = medicationToMap(med);				
				
		// Unregister previous notifiation 
		unregisterMedicationNotification(med);
		
		// Create Notification
		Map<String, String> message = new HashMap<String, String>();
		String description = "Take medication: " + medicationDetails.get("name");
	
		message.putAll(medicationDetails);
		message.put("type", "medications");
		message.put("description", description);
		
		//	Register notification
		try {
			NotificationsManager.registerRepeatNotification(this, message, med.medicationCalendar, getMedicationIntervalTimer(med));
		} catch (Exception e) { throw new Error("Couldn't register a notification for this medication."); }
	}
	
	//unregister when editing an existing medication
	private void unregisterMedicationNotification(MedicationObject med) {
		try { 			
			// Container for registered notification information
			ArrayList<Map<String, String>> registeredNotifications; 
			
			//Create medicationDetils from MedicationObject
			Map<String, String> medicationDetails = medicationToMap(med);
			
			// Read all the registered single alarms for notifications
			registeredNotifications = DataStorageManager.readJSONObject(this, "repeated alarms");		
				
			// Look for the registered notification for the given appointment based on the
			// appointments time stamp. If found, delete the alarm from the file 
			for (Map<String, String> map : registeredNotifications) {
				
				if (medicationDetails.get("name").equals(map.get("name"))) {
					DataStorageManager.deleteJSONObject(this, "repeated alarms", map);
				}
			}
			
			// Create a new message that will be used to cancel any existing alarms
			Map<String, String>  message = new HashMap<String, String>();
			
			// fill the message with the medication information
			message.putAll(medicationDetails);
						
			// insert into message the information of the notification that
			// would have be shown to the user
			String description = "Take medication: " + medicationDetails.get("name") + 
					"\n take: "+medicationDetails.get("freqValue")+" every: " + medicationDetails.get("freqType") + 
					"\n reminder set at: " + medicationDetails.get("time");
			message.put("type", "medications");
			message.put("title", medicationDetails.get("name"));
			message.put("description", description);
			
			// Unregister the alarm for the notification
			NotificationsManager.unregisterNotification(this, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		
		TimePicker timePicker = (TimePicker) findViewById(R.id.new_medication_time);
		timePicker.setCurrentHour(med.medicationCalendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(med.medicationCalendar.get(Calendar.MINUTE));
	
		DatePicker datePicker = (DatePicker) findViewById(R.id.new_medication_start_date);
		datePicker.init(med.medicationCalendar.get(Calendar.YEAR), med.medicationCalendar.get(Calendar.MONTH), 
				med.medicationCalendar.get(Calendar.DATE), null);
		
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
		Calendar medCalendar;
		TextView nameTextView, frequencyValueTextView, dosageValueTextView;
		Spinner frequencyTypeSppinner, dosageTypeSpinner;
		TimePicker timePicker;
		DatePicker datePicker;
		
		nameTextView           = (TextView) findViewById(R.id.new_medication_name);
		frequencyValueTextView = (TextView) findViewById(R.id.new_medication_dosage_frequency);
		dosageValueTextView    = (TextView) findViewById(R.id.new_medication_dosage_strength);
		dosageTypeSpinner      = (Spinner)  findViewById(R.id.new_medication_medication_type_spinner);
		frequencyTypeSppinner  = (Spinner)  findViewById(R.id.new_medication_frequency_type);
		timePicker 			   = (TimePicker) findViewById(R.id.new_medication_time);
		datePicker 			   = (DatePicker) findViewById(R.id.new_medication_start_date);
		
		name = nameTextView.getText().toString();
		dosageValue = Integer.parseInt(dosageValueTextView.getText().toString());
		dosageType = DosageType.valueOf(dosageTypeSpinner.getSelectedItem().toString());
		
		medCalendar = getCalendarFromForm();
		
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
		
		Switch reminder = (Switch) findViewById(R.id.new_medication_reminder_switch);
		
		boolean reminderStatus = reminder.isChecked();
		medCalendar = Calendar.getInstance();
		medCalendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), 
				timePicker.getCurrentHour(), timePicker.getCurrentMinute());
		
		MedicationObject medication = new MedicationObject(name, frequencyType, frequencyValue, 
				dosageType, dosageValue, reminderStatus, medCalendar);
		
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
		
		if (reminderStatus)
			registerMedicationNotification(medication);
		else
			unregisterMedicationNotification(medication);
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
	
	/**
	 * Returns a calendar with the user specified date and time from forms
	 */
	private Calendar getCalendarFromForm() {		
		// Get an instance of the Calendar to set the time of the appointment
		Calendar medicationCalendar = Calendar.getInstance();
		
		try {	
			// Get the start time hour and minutes
			TimePicker timePicker = (TimePicker) findViewById(R.id.new_medication_time);
			timePicker.clearFocus();
			int startHour = timePicker.getCurrentHour();
			int startMinutes = timePicker.getCurrentMinute();
						
			//change the times to a specified format, and save start and end time.			
			// Set the time of appointment
			medicationCalendar.set(Calendar.HOUR_OF_DAY, startHour);
			medicationCalendar.set(Calendar.MINUTE, startMinutes);
			medicationCalendar.set(Calendar.SECOND, 0);
			
			// Go through the time and convert from 24 hours to 12 hour time
			if (startHour > 12) {
				startHour = startHour - 12;
			}
			else if (startHour == 0) {
				startHour = startHour + 12;
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}		

		// Get the reference to Date.
		DatePicker datePicker = (DatePicker) findViewById(R.id.new_medication_start_date);
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth() + 1;
		int year = datePicker.getYear();
		
		try {					
			// Set the date of appointment
			medicationCalendar.set(Calendar.YEAR, year);
			medicationCalendar.set(Calendar.MONTH, month - 1);
			medicationCalendar.set(Calendar.DAY_OF_MONTH, day);				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return medicationCalendar;
	}
}