package com.team9.healthmate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View.OnClickListener;

/** 
 * Presents the registration screen to users. Allows
 * users to create a new account.
 * @author Steve
 * 
 */
public class Registration extends Activity implements OnClickListener {
	public Button createAccount;
	public EditText firstName;
	public EditText lastName;
	public EditText username;
	public EditText password;
	public EditText cpassword;
	public DatePicker datePicker;
	public Spinner sexSpinner;
	
	/**
	 * Sets up the picker and spinner as well as
	 * references to text box values. Also adds
	 * a listener to the registration button.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		// User's birthday
	    datePicker = (DatePicker) findViewById(R.id.bday);
	    
	    // User's sex
	    sexSpinner = (Spinner) findViewById(R.id.sex);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	            R.array.sex, R.layout.sex_spinner_textview);
	    adapter.setDropDownViewResource(R.layout.sex_spinner_textview);
		sexSpinner.setAdapter(adapter);
	    
		createAccount = (Button) findViewById(R.id.create_account_button);
		createAccount.setOnClickListener(this);
		
		firstName = (EditText) findViewById(R.id.first_name);
		lastName = (EditText) findViewById(R.id.last_name);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
	}
	
	/**
	 * Launch the login activity.
	 */
	public void startAct()	{
		Intent intent = new Intent(Registration.this, Login.class);
		startActivity(intent);
	}
	
	/**
	 * Calculates user's age and writes all
	 * information provided by the user to the
	 * account file. It will overwrite any existing
	 * data so there can only be one account at a time.
	 */
	public void generateAccount() {
		// Calculate age -- sorry for the mess
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		Date age = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String dateString = sdf.format(age);
		// Write contents to file
		Context context = getApplicationContext();
		Map<String, String> accountInfo = new HashMap<String, String>();
		accountInfo.put("username", username.getText().toString());
		accountInfo.put("password", password.getText().toString());
		accountInfo.put("first_name", firstName.getText().toString());
		accountInfo.put("last_name", lastName.getText().toString());
		accountInfo.put("sex", sexSpinner.getSelectedItem().toString());
		accountInfo.put("age", "" + dateString);
		
		try {
			DataStorageManager.writeJSONObject(context, "account", accountInfo, true);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onClick(View v) {
		generateAccount();
		startAct();
	}
}