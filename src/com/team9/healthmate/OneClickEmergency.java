/* @author Davit Avetikyan 10/26/2014
 * OneClickEmergency class will provide with 
 * set of functionalities to send emails and texts. 
 * 
 */

package com.team9.healthmate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;


public class OneClickEmergency {
	
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	
	/** Sends text to specified users
	 *  from the contact list. Specifies in
	 *  text body the purpose of the message	
	 */
	protected void sendSMSMessage(Context context) {
	      Log.v("Send SMS", "");      
	      
	      String phoneNo = "805-320-8737";
	      String message = "An emergency call was made by Dave at this time: " + dateFormat.format(date);

	      try {
	         SmsManager smsManager = SmsManager.getDefault();
	         smsManager.sendTextMessage(phoneNo, null, message, null, null);
	         Log.v("debugme", "Send Sms 2 try");
	         Toast.makeText(context.getApplicationContext(), "SMS sent.",
	         Toast.LENGTH_LONG).show();
	      } catch (Exception e) {
	    	  
	         Toast.makeText(context.getApplicationContext(),
	         "SMS failed, please try again.",
	         Toast.LENGTH_LONG).show();
	         e.printStackTrace();
	      }
	   }
	
	/**	Sends email out to specified Users
	 * 	from the contact list. It specifies 
	 * 	in the message body the purpose of the 
	 * 	email, stamping the date and time the 
	 * 	call was made.
	 */
	protected void sendEmail(Context context) {
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
			 context.startActivity(Intent.createChooser(email, "Choose an email client from..."));
		 } catch (android.content.ActivityNotFoundException ex) {
			 Toast.makeText(context.getApplicationContext(), "No email client installed.",
					 Toast.LENGTH_LONG).show();
		 }
	}

	/**	Makes a call to a specified phone number
	 */
	protected void makeACall(Context context){
		Intent callIntent = new Intent(Intent.ACTION_CALL);
	    callIntent.setData(Uri.parse("tel:8053208737"));
	    context.startActivity(callIntent);	
	}

}
