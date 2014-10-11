package com.team9.healthmate;

import java.util.HashMap;
import java.util.Map;

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
	EditText name = null;
	EditText pass = null;
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
			information.put("filename", "account");
			information.put("username", name.getText().toString());
			information.put("password", pass.getText().toString());
			
			try {
				// Get the context of the Application, send the information that needs to be written,
				// read the information from a file, and display the information read onto the current screen.
				Context context = getApplicationContext();
				DataStorageManager.writeJSONObject(context, information);
				String info = DataStorageManager.readJSONObject(getApplicationContext(), "account");
				DataStorageManager.displayText(this, R.id.textView1, info);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			}
			//startMenu();
	}	
}