package com.team9.healthmate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class EditProfile extends Activity implements OnClickListener {
	EditText firstName;
	EditText lastName;
	EditText userName;
	Spinner gender;
	EditText password;
	EditText cPassword;
	DatePicker age;
	Button editProfile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		
		firstName = (EditText) findViewById(R.id.first_name);
		lastName = (EditText) findViewById(R.id.last_name);
		userName = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		cPassword = (EditText) findViewById(R.id.confirm_password);
		age = (DatePicker) findViewById(R.id.bday);
		
		gender = (Spinner) findViewById(R.id.sex);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	            R.array.sex, R.layout.sex_spinner_textview);
	    adapter.setDropDownViewResource(R.layout.sex_spinner_textview);
		gender.setAdapter(adapter);
		
		editProfile = (Button) findViewById(R.id.create_account_button);
		editProfile.setOnClickListener(this);
		
		try {
			Context context = getApplicationContext();
			ArrayList<Map<String, String>> credentials = DataStorageManager.readJSONObject(context, "account");
			Iterator<Map<String, String>> iterator = credentials.iterator();

			Map<String, String> dataSet = new HashMap<String, String>();
			// attempt to authenticate the user
			while (iterator.hasNext()) {
				// go through all the keys
				dataSet = iterator.next();
				Iterator<String> it = dataSet.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					String value = dataSet.get(key);
					// make sure we only check the username & password keys
					switch (key) {
						case "username":
							userName.setText(value);
							break;
						case "first_name":
							firstName.setText(value);
							break;
						case "last_name":
							lastName.setText(value);
							break;
						case "sex":
							if (value == "male") {
								gender.setSelection(0);
							} else if (value == "female") {
								gender.setSelection(1);
							} else {
								gender.setSelection(2);
							}
							break;
						default:
							break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Does basic error checking on the information
	 * provided by the user. If any field is left blank
	 * or the confirmation passwords do not match, it will
	 * return false.
	 * @return		Returns true if there are errors.
	 */
	public boolean errorCheck() {
		boolean errors = false;
		TextView topLabel = (TextView) findViewById(R.id.edit_profile_label);
		if (firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || 
				userName.getText().toString().equals("") || password.getText().toString().equals("") 
				|| cPassword.getText().toString().equals("")) {
			topLabel.setText("Fill out all fields");
			topLabel.setTextColor(Color.RED);
			errors = true;
		}
		if (!password.getText().toString().equals(cPassword.getText().toString())) {
			topLabel.setText("Passwords do not match");
			topLabel.setTextColor(Color.RED);
			errors = true;
		}
		return errors;
	}
	
	/**
	 * Calculates user's age and writes all
	 * information provided by the user to the
	 * account file. It will overwrite any existing
	 * data so there can only be one account at a time.
	 */
	public void generateAccount() {
		if (!errorCheck()) {
			// Calculate age -- sorry for the mess
			int day = age.getDayOfMonth();
			int month = age.getMonth();
			int year = age.getYear();
			Calendar c = Calendar.getInstance();
			int currentYear = c.get(Calendar.YEAR);
			int currentMonth = c.get(Calendar.MONTH);
			int currentDay = c.get(Calendar.DAY_OF_MONTH);
			int age;
			if (currentMonth >= month && currentDay >= day) {
				age = currentYear - year;
			} else {
				age = currentYear - year - 1;
			}
			// Write contents to file
			Context context = getApplicationContext();
			Map<String, String> accountInfo = new HashMap<String, String>();
			accountInfo.put("username", userName.getText().toString());
			accountInfo.put("password", password.getText().toString());
			accountInfo.put("first_name", firstName.getText().toString());
			accountInfo.put("last_name", lastName.getText().toString());
			accountInfo.put("sex", gender.getSelectedItem().toString());
			accountInfo.put("age", "" + age);
			
			try {
				DataStorageManager.writeJSONObject(context, "account", accountInfo, true);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			startAct();
		}
	}
	
	/**
	 * Launch the login activity.
	 */
	public void startAct()	{
		Intent intent = new Intent(EditProfile.this, UserProfile.class);
		startActivity(intent);
	}
	
	/**
	 * Starts the account generation process.
	 */
	public void onClick(View v) {
		generateAccount();
	}
}