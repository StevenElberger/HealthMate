package com.team9.healthmate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Serves as a menu for the contact selected on ContactMyDoctor's contact list.
 * The options are: make a phone call, send text message, remove contact. Upon
 * selection it returns an integer id value as the action to be performed. 
 * 
 * @author Guss
 */
public class ContactMyDoctorItemOptions extends Activity {
	ListView optionList;
	String[] values;
	ArrayAdapter<String> adapter;
	
	/**
	 * Initializes class objects
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_my_doctor_item_options);
		buildMenuOption();	
	}
	
	/**
	 * Sets the list of options and their event-listeners implementations
	 */
	private void buildMenuOption() {		
		optionList = (ListView) findViewById(R.id.ContactMyDoctorDetailsOptionList);
		values = new String[] { 
				"Make a phone call", "Send a text message", "Send e-mail", "Remove"
		};
		
		adapter = new ArrayAdapter<String>(this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, values);	
		
		optionList.setAdapter(adapter); 			
		optionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, 
									final View view, int position, long id) {
				Intent intent = new Intent();
				
				//if removing was selected, then display confirmation dialog
				if(position == 3) 
					confirmRemove();						
				else
				{
					setResult(position+1, intent);
					finish();
				}
			}
		 });
	}
	
	/**
	 * Displays a dialog to confirm the deletion of a contact
	 * On 'Yes' button press: return deletion intent
	 * On 'No' button press: return -1 code (do nothing) intent
	 */
	public void confirmRemove() {
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Removing contact")
	        .setMessage("Do you want to remove this contact from the list? (contact information will remain on phone's contact list)")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	        {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	Intent intent = new Intent();
		        	setResult(4, intent);
		        	finish();
	        }})
	    .setNegativeButton("No",new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	Intent intent = new Intent();
	        	setResult(-1, intent);
	        	finish();
        }})
	    .show();
	}	
	
	/**
	 * Auto-generated code. Not used.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_my_doctor_item_options, menu);
		return true;
	}
	
	/**
	 * Auto-generated code. Not used.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
