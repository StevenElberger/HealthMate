package com.team9.healthmate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/** 
 * Displays user profile information to the
 * user. The user may edit his/her profile
 * and save their edited profile to local storage
 * if so desired. The user may also upload a photograph
 * for their user profile picture.
 * @author Steve
 *
 */
public class UserProfile extends Activity {
	ImageView image;
	TextView firstName, lastName, userName, gender, age;
	EditText eFirstName, eLastName, eUserName, ePassword;
	DatePicker eAge;
	Spinner eGender;
	String sFirstName, sLastName, sUserName, sGender, sAge, sPassword;
	boolean notPressedYet = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		
		firstName = (TextView) findViewById(R.id.f_name);
		lastName = (TextView) findViewById(R.id.l_name);
		userName = (TextView) findViewById(R.id.u_name);
		gender = (TextView) findViewById(R.id.gender);
		age = (TextView) findViewById(R.id.birthday);
		image = (ImageView) findViewById(R.id.profile_pic);
		
		// display user account information
		loadProfileInformation();
	}
	
	/**
	 * Takes account information from local storage and
	 * displays them on the appropriate forms in the profile
	 * page.
	 */
	public void loadProfileInformation() {
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
							sUserName = value;
							break;
						case "first_name":
							firstName.setText(value);
							sFirstName = value;
							break;
						case "last_name":
							lastName.setText(value);
							sLastName = value;
							break;
						case "sex":
							gender.setText(value);
							sGender = value;
							break;
						case "age":
							age.setText(value);
							sAge = value;
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
	 * Takes account information from forms being displayed
	 * and writes them to the user account file.
	 */
	public void writeProfileInformation() {
		try {
			Context context = getApplicationContext();
			Map<String, String> dataSet = new HashMap<String, String>();
			dataSet.put("username", sUserName);
			dataSet.put("first_name", sFirstName);
			dataSet.put("last_name", sLastName);
			dataSet.put("sex", sGender);
			dataSet.put("age", sAge);
			dataSet.put("password", sPassword);
			DataStorageManager.writeJSONObject(context, "account", dataSet, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set up the user profile button on the action bar.
	 * This is called every time the user clicks a menu
	 * button because of invalidateMenuOptions().
	 */
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
	    // Inflate the menu items for use in the action bar
		// depending on whether they need to save or edit.
		if (notPressedYet) {
			getMenuInflater().inflate(R.menu.profile_menu, menu);
		} else {
			getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
		}
		return true;
	}

	/**
	 * If the user clicked the edit button, show all of their
	 * profile information in the edit_profile_menu menu so
	 * they may edit their profile. Otherwise, write all their
	 * new information to file.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Check to see which option was selected by the user.
		if (item.getItemId() == R.id.action_edit_profile) {
			// if the edit button was selected, load the
			// appropriate layout
			setContentView(R.layout.activity_edit_profile2);
			
			// grab forms on the new layout
			eFirstName = (EditText) findViewById(R.id.f_name);
			eLastName = (EditText) findViewById(R.id.l_name);
			eUserName = (EditText) findViewById(R.id.u_name);
			eGender = (Spinner) findViewById(R.id.gender);
			eAge = (DatePicker) findViewById(R.id.bday);
			ePassword = (EditText) findViewById(R.id.password);
			
			// set up the gender spinner
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		            R.array.sex, R.layout.sex_spinner_textview);
		    adapter.setDropDownViewResource(R.layout.sex_spinner_textview);
			eGender.setAdapter(adapter);
			
			if (sGender.equals("Male")) {
				eGender.setSelection(0);
			} else if (sGender.equals("Female")) {
				eGender.setSelection(1);
			} else {
				eGender.setSelection(2);
			}
			
			// set form values
			eFirstName.setText(sFirstName);
			eLastName.setText(sLastName);
			eUserName.setText(sUserName);
			
			// set the datepicker
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			try {
				Date age = sdf.parse(sAge);
				Calendar c = Calendar.getInstance();
				c.setTime(age);
				eAge.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			// Set flag and call onCreateOptionsMenu
			// to inflate the correct menu.
			notPressedYet = false;
			invalidateOptionsMenu();
		} else if (item.getItemId() == R.id.action_save_profile) {
			// if the save button was selected, load
			// the previous layout with the new values
			// after writing the new values to the account file
			
			// grab form values
			sFirstName = eFirstName.getText().toString();
			sLastName = eLastName.getText().toString();
			sUserName = eUserName.getText().toString();
			sGender = eGender.getSelectedItem().toString();
			
			// grab from the datepicker
			int day = eAge.getDayOfMonth();
			int month = eAge.getMonth();
			int year = eAge.getYear();
			Calendar c = Calendar.getInstance();
			c.set(year,  month, day);
			Date userAge = c.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String dateString = sdf.format(userAge);
			sAge = dateString;
			
			// check to make sure user entered his/her password
			sPassword = ePassword.getText().toString();
			if (sPassword.equals("")) {
				TextView warningLabel = (TextView) findViewById(R.id.no_password_label);
				warningLabel.setTextColor(Color.RED);
				warningLabel.setVisibility(0);
			} else {
				setContentView(R.layout.activity_user_profile);
				// grab new forms
				firstName = (TextView) findViewById(R.id.f_name);
				lastName = (TextView) findViewById(R.id.l_name);
				userName = (TextView) findViewById(R.id.u_name);
				gender = (TextView) findViewById(R.id.gender);
				age = (TextView) findViewById(R.id.birthday);
				// set new forms' values
				firstName.setText(sFirstName);
				lastName.setText(sLastName);
				userName.setText(sUserName);
				gender.setText(sGender);
				age.setText(sAge);
				writeProfileInformation();
				notPressedYet = true;
				invalidateOptionsMenu();
			}
		}
		return super.onOptionsItemSelected(item);
	}
}