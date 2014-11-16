package com.team9.healthmate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class UserProfile extends Activity {
	TextView firstName;
	TextView lastName;
	TextView userName;
	TextView gender;
	TextView age;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		
		firstName = (TextView) findViewById(R.id.f_name);
		lastName = (TextView) findViewById(R.id.l_name);
		userName = (TextView) findViewById(R.id.u_name);
		gender = (TextView) findViewById(R.id.gender);
		age = (TextView) findViewById(R.id.age);
		
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
							gender.setText(value);
							break;
						case "age":
							age.setText(value);
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
	 * Set up the user profile button on the action bar.
	 */
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
	    // Inflate the menu items for use in the action bar
		getMenuInflater().inflate(R.menu.profile_menu, menu);
		return true;
	}
	
	/**
	 * User clicked on edit profile button so send them to their
	 * profile page.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Check to see which option was selected by the user.
		if (item.getItemId() == R.id.action_edit_profile) {
			Intent intent = new Intent(this, EditProfile.class);
			startActivity(intent);
		}
		
		return super.onOptionsItemSelected(item);
	}
}