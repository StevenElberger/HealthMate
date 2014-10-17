package com.team9.healthmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.view.View.OnClickListener;

/** 
 * Presents the registration screen to users. Allows
 * users to create a new account.
 * @author Steve
 * 
 */
public class Registration extends Activity {
	public Button createAccount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		//DialogFragment newFragment = new DatePickerFragment();
	    //newFragment.show(getFragmentManager(), "datePicker");
		
		
		// User's birthday
	    DatePicker datePicker = (DatePicker) findViewById(R.id.bday);
	    int year = datePicker.getYear();
	    int month = datePicker.getMonth();
	    int day = datePicker.getDayOfMonth();
	    
	    // User's sex
	    Spinner sexSpinner = (Spinner) findViewById(R.id.sex);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	            R.array.sex, R.layout.sex_spinner_textview);
	    adapter.setDropDownViewResource(R.layout.sex_spinner_textview);
		sexSpinner.setAdapter(adapter);
	    
		//createAccount = (Button) findViewById(R.id.register_button);
		//createAccount.setOnClickListener((OnClickListener) this);
	}

	public void startAct()	{
		Intent intent = new Intent(Registration.this, Menu.class);
		startActivity(intent);
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//startAct();
	}
}