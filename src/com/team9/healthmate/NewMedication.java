package com.team9.healthmate;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class NewMedication extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_medication);
		
		populateFrequencySpinner();
		
		EditText dosage = (EditText) findViewById(R.id.new_medication_dosage_strength);
		dosage.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		//cancel button
		Button button = (Button) findViewById(R.id.new_medication_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
	}
	
	public void increaseNumberPicker(View v)
	{
		EditText numberPicker = (EditText) findViewById(R.id.new_medication_dosage_frequency);
		int currentValue = Integer.parseInt(numberPicker.getText().toString());
		numberPicker.setText(""+(currentValue+1));
	}
	
	public void decreaseNumberPicker(View v)
	{
		EditText numberPicker = (EditText) findViewById(R.id.new_medication_dosage_frequency);
		int currentValue = Integer.parseInt(numberPicker.getText().toString());
		if(currentValue > 0)
			numberPicker.setText(""+(currentValue-1));
	}
	
	private void populateFrequencySpinner(){
		Spinner spinner = (Spinner) findViewById(R.id.new_medication_frequency_lapse);
		String[] spinnerValues = {"Day", "Week", "Month"};
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerValues);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
	}
	
	public void enableFrequencyPicker(View v)
	{
		Button b = (Button) findViewById(R.id.new_medication_numer_picker_increase);
		b.setEnabled(true);
		b = (Button)findViewById(R.id.new_medication_numer_picker_decrease);
		b.setEnabled(true);
		Spinner spinner = (Spinner) findViewById(R.id.new_medication_frequency_lapse);
		spinner.setClickable(true);
	}
	
	public void disableFrequencyPicker(View v)
	{
		Button b = (Button) findViewById(R.id.new_medication_numer_picker_increase);
		b.setEnabled(false);
		b = (Button)findViewById(R.id.new_medication_numer_picker_decrease);
		b.setEnabled(false);
		Spinner spinner = (Spinner) findViewById(R.id.new_medication_frequency_lapse);
		spinner.setClickable(false);
	}
	
	public void onReminderToggleSwitch(View v)
	{		
		/**/
		return;
	}
}