package com.team9.healthmate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
	public Button emergency;
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	/*
	// These contain the text value of the ids for username and password 
	EditText name = null;
	EditText pass = null;
	//public Intent intent;
	*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		login = (Button) findViewById(R.id.login_button);
		login.setOnClickListener(this);
		
		register = (Button) findViewById(R.id.register_button);
		register.setOnClickListener(this);
		
		emergency = (Button)findViewById(R.id.emerg);
		emergency.setOnClickListener(this);
		/*
		// Get the reference for the two text fields in the activity
		name = (EditText)findViewById(R.id.username);
		pass = (EditText)findViewById(R.id.password);
		*/
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
			/*
			// Data Structure that will contain the key value pairs that need to be stored
			Map<String, String> information = new HashMap<String, String>();
			
			// Add the information that will be stored.
			information.put("username", name.getText().toString());
			information.put("password", pass.getText().toString());
			
			try {
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
			}
			catch (Exception e) {
				e.printStackTrace();
			}*/
			startMenu();
		}
		else if(v.equals(emergency)){
			Intent callIntent = new Intent(Intent.ACTION_CALL);
		    callIntent.setData(Uri.parse("tel:123456789"));
		    startActivity(callIntent);
		    
			sendSMSMessage();
			sendEmail();
		}
	}
	
	
	//Sends a text 
	protected void sendSMSMessage() {
	      Log.i("Send SMS", "");
	      
	      
	      String phoneNo = "818-795-6013";
	      String message = "An emergency call was made by Dave at this time: " + dateFormat.format(date);

	      try {
	         SmsManager smsManager = SmsManager.getDefault();
	         smsManager.sendTextMessage(phoneNo, null, message, null, null);
	         Toast.makeText(getApplicationContext(), "SMS sent.",
	         Toast.LENGTH_LONG).show();
	      } catch (Exception e) {
	         Toast.makeText(getApplicationContext(),
	         "SMS faild, please try again.",
	         Toast.LENGTH_LONG).show();
	         e.printStackTrace();
	      }
	   }
	
	//Sends an email out
	protected void sendEmail() {
		String recipient = "avetikyan.davit@gmail.com";
		String subject = "An emergency call was made by Dave at this time: " + dateFormat.format(date);
		String body = "Emergency Call";
				
		String[] recipients = {recipient};
		 Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
		// prompts email clients only
		 email.setType("message/rfc822");
		 
		 email.putExtra(Intent.EXTRA_EMAIL, recipients);
		 email.putExtra(Intent.EXTRA_SUBJECT, subject);
		 email.putExtra(Intent.EXTRA_TEXT, body);

		 try {
			 // the user can choose the email client
			 startActivity(Intent.createChooser(email, "Choose an email client from..."));
		 } catch (android.content.ActivityNotFoundException ex) {
			 Toast.makeText(Login.this, "No email client installed.",
					 Toast.LENGTH_LONG).show();
		 }
	}

	
	 public boolean onCreateOptionsMenu(Menu registration) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      getMenuInflater().inflate(R.menu.registration, (android.view.Menu) registration);
	      return true;
	   }
}