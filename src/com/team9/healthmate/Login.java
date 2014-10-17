package com.team9.healthmate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.team9.healthmate.R;
import com.team9.healthmate.DataManager.DataStorageManager;

/** 
 * Presents the login screen to users. Also allows
 * new users to register for an account.
 * @author Steve
 * 
 */
public class Login extends Activity implements OnClickListener{
	/*Please don't not delete delete this button until full
	system integration */
	public Button login;
	public Button register;
	// These contain the text value of the ids for username and password 
	EditText name;
	EditText pass;
	//public Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		login = (Button) findViewById(R.id.login_button);
		login.setOnClickListener(this);
		
		register = (Button) findViewById(R.id.register_button);
		register.setOnClickListener(this);
		
		// Get the reference for the two text fields in the activity
		name = (EditText)findViewById(R.id.username);
		pass = (EditText)findViewById(R.id.password);
	}
	
	public void startMenu()	{
		Intent intent = new Intent(Login.this, Menu.class);
		startActivity(intent);
	}
	
	public void startRegistration()	{
		Intent intent = new Intent(Login.this, Registration.class);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		if (v.equals(register)) {
			startRegistration();
		} else if (v.equals(login)) {
			// Data Structure that will contain the key value pairs that need to be stored
			Map<String, String> information = new HashMap<String, String>();
			
			// Add the information that will be stored.
			information.put("username", name.getText().toString());
			information.put("password", pass.getText().toString());
			
			try {
				
				// Check the contents of the account file against
				// the credentials provided by the user
				Context context = getApplicationContext();
				ArrayList<Map<String, String>> credentials = DataStorageManager.readJSONObject(context, "account");
				Iterator<Map<String, String>> iterator = credentials.iterator();
				Map<String, String> dataSet = new HashMap<String, String>();
				
				while (iterator.hasNext()) {
					// go through all key value pairs
					dataSet = iterator.next();
					Iterator<String> it = dataSet.keySet().iterator();
					while (it.hasNext()) {
						// check the key, value pair against the edit texts' values
						String key = it.next();
						String value = dataSet.get(key);
						// make sure we only check the username key
						if (key.equals("username")) {
							if (name.getText().toString().equals(value)) {
								DataStorageManager.displayText(this, R.id.textView1, "Successful authentication");
							}
						}
					}
				}
				//DataStorageManager.displayText(this, R.id.textView1, "Weiner");
				
				/*
				// Get the context of the Application, send the information that needs to be written,
				// read the information from a file, and display the information read onto the current screen.
				Context context = getApplicationContext();
				DataStorageManager.writeJSONObject(context, "account", information, true);
				ArrayList<Map<String, String>> info = 
						DataStorageManager.readJSONObject(context, "account");
				Iterator<Map<String, String>> iterator = info.iterator();
				Map<String, String> dataSet = new HashMap<String, String>();
				String collectionOfData = "";
				String key;
				Set<String> setOfKeys;
				Iterator<String> stringIterator;
				while (iterator.hasNext())
				{
					dataSet = iterator.next();
					setOfKeys = dataSet.keySet();
					stringIterator = setOfKeys.iterator();
					while (stringIterator.hasNext())
					{
						key = stringIterator.next();
						collectionOfData = collectionOfData + key + ": " + dataSet.get(key) + "\n";
					}
				}
				DataStorageManager.displayText(this, R.id.textView1, collectionOfData);
				*/
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			//startMenu();
		}
	}
}