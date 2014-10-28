package com.team9.healthmate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.team9.healthmate.R;
import com.team9.healthmate.DataManager.DataStorageManager;

/** 
 * Presents the login screen to users. Also allows
 * new users to register for an account.
 * @author Steve
 */
public class Login extends Activity implements OnClickListener{
	public Button login;
	public Button register;
	EditText name;
	EditText pass;
	TextView debugText;
	boolean debugMode = false;

	
	/**
	 * Attaches listeners to both buttons and sets up references
	 * for the text boxes.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		login = (Button) findViewById(R.id.login_button);
		login.setOnClickListener(this);
		
		register = (Button) findViewById(R.id.register_button);
		register.setOnClickListener(this);
		
		name = (EditText)findViewById(R.id.username);
		pass = (EditText)findViewById(R.id.password);
		
		if (debugMode) {
			debugText = (TextView) findViewById(R.id.debug_text);
			debugText.setVisibility(1);
			debugText.setText("Debug values");
		}

	}
	
	/**
	 * Launch the menu activity.
	 */
	public void startMenu()	{
		Intent intent = new Intent(Login.this, Menu.class);
		startActivity(intent);
	}
	
	/**
	 * Launch the registration activity.
	 */
	public void startRegistration()	{
		Intent intent = new Intent(Login.this, Registration.class);
		startActivity(intent);
	}
	
	/**
	 * If the user clicks on the register button, take
	 * them to the registration activity.
	 * If the user clicks on the login button, try to
	 * authenticate them. If authentication is successful,
	 * send the user to the menu activity.
	 */
	@Override
	public void onClick(View v) {
		if (v.equals(register)) {
			startRegistration();
		} else if (v.equals(login)) {
			try {
				// check the contents of the account file against
				// the credentials provided by the user
				Context context = getApplicationContext();
				ArrayList<Map<String, String>> credentials = DataStorageManager.readJSONObject(context, "account");
				Iterator<Map<String, String>> iterator = credentials.iterator();

				Map<String, String> dataSet = new HashMap<String, String>();
				// flags for correct credentials
				boolean userCorrect = false;
				boolean passCorrect = false;
				// credentials provided
				String providedName = name.getText().toString();
				String providedPass = pass.getText().toString();
				// check for both a username and a password
				if (providedName.equals("") || providedPass.equals("")) {
					// need both!
					name.setText("");
					pass.setText("");
					
					TextView incorrectLabel = (TextView) findViewById(R.id.login_title);
					incorrectLabel.setText("Please enter both a username and a password");
					incorrectLabel.setTextColor(Color.RED);
					return;
				}
				// attempt to authenticate the user
				while (iterator.hasNext()) {
					// go through all the keys
					dataSet = iterator.next();
					Iterator<String> it = dataSet.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						String value = dataSet.get(key);
						// make sure we only check the username & password keys
						if (key.equals("username") && providedName.equals(value)) {
								userCorrect = true;
						} else if (key.equals("password") && providedPass.equals(value)) {
								passCorrect = true;
						}
					}
				}
				if (userCorrect && passCorrect) {
					startMenu();
				} else {
					// authentication failed
					name.setText("");
					pass.setText("");
					
					TextView incorrectLabel = (TextView) findViewById(R.id.login_title);
					incorrectLabel.setText("Incorrect username or password");
					incorrectLabel.setTextColor(Color.RED);

				}
				// debug values for username and password
				//DataStorageManager.displayText(this, R.id.textView1, "U: " + userCorrect + " / " + "P: " + passCorrect);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}	
	
	
}