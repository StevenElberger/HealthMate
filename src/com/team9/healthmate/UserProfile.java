package com.team9.healthmate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
		age = (TextView) findViewById(R.id.birthday);
		
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
}