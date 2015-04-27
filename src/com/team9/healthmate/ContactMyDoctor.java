package com.team9.healthmate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ContactMyDoctor class displays preferred contacts on a list. Containing methods
 * to perform phone calls, send text messages, but also add and remove contact on 
 * the list.
 * 
 * @author Gustavo Arce
 */
public class ContactMyDoctor extends Activity {
	protected static final int PICK_REQUEST = 0;
	protected static final int OPTION_REQUEST = 1;
	public static ArrayList<Contact> contacts;
	public static ArrayAdapter<Contact> adapter;
	public ListView contactList;
	private int itemSelected = -1;
	
	/**
	 * Initializes activity objects
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_my_doctor);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//Reads contacts stored on the file and populate contact-list
		populateContactList();
		
		//event-listener for the list
		onItemListClick();
		
		//Select a contact from contact-list intent
		Button buttonPickContact = 
				(Button)findViewById(R.id.ContactMyDoctor_AddNewButton);
		
		buttonPickContact.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType(ContactsContract.
								CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
				startActivityForResult(intent, PICK_REQUEST);             
	    }});
	}
	
	/**
	 * Add event-listener to the items of the list
	 */
	public void onItemListClick() {
		ListView list = (ListView) findViewById(R.id.ContactMyDoctor_ContactList);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, 
									View viewClicked, 
									int position, 
									long id) {				
				itemSelected = position;
				Intent intent = new Intent(ContactMyDoctor.this, 
										   ContactMyDoctorItemOptions.class);
				startActivityForResult(intent, OPTION_REQUEST);			
			}
		});
	}
	
	/**
	 * Autogenerated code. Not used.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_my_doctor, menu);
		return true;
	}
	
	/**
	 * Autogenerated code. Not used.
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
	
	/**
	 * Method called upon returning from some other activity. 
	 * Options are: save contact, phone call, sms, email, remove contact 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    Log.v("debugme", "requestCode ="+requestCode+" - resultCode: "+resultCode);
		// Check which request it is that we're responding to		
	    if (requestCode == PICK_REQUEST) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	            // Get the URI that points to the selected contact
	            Uri contactUri = data.getData();
	            String name = retrieveContactName(contactUri);
	            String number = retrieveContactNumber(contactUri);
	            String email = retriveContactEmail(contactUri);
	            if(name.isEmpty() || number.isEmpty())
	            	return;
	            
	            contacts.add(new Contact(contactUri, name, number, email));
	            adapter.notifyDataSetChanged();
	           
	            //Save to file 
	            saveContactList(ContactMyDoctor.this);
	        }
	    }	    
	    else if (requestCode == OPTION_REQUEST && resultCode == 1)
	    {
	    	Contact c = contacts.get(itemSelected);
	    	Log.v("debugme", "Making a phone call to "+c.name);
	    	makeACall(ContactMyDoctor.this, c);
	    }
	    else if (requestCode == OPTION_REQUEST && resultCode == 2)
	    {
	    	Contact c = contacts.get(itemSelected);
	    	Log.v("debugme", "Sending a text messageto "+c.name);
	    	sendTextMessage(ContactMyDoctor.this, c);
	    }
	    else if (requestCode == OPTION_REQUEST && resultCode == 3)
	    {
	    	Contact c = contacts.get(itemSelected);
	    	sendEmail(ContactMyDoctor.this, c);
	    }
	    else if (requestCode == OPTION_REQUEST && resultCode == 4)
	    {
	    	Contact c = contacts.get(itemSelected);
	    	Log.v("debugme", "Removing "+c.name);
	    	removeContact(c);
	    }
	}	
	
	private void removeContact(Contact c) {		
		contacts.remove(c);
		adapter.notifyDataSetChanged();
		saveContactList(ContactMyDoctor.this);		
	}
	
	/**
	 * Reads contact list from local storage file "contacts"
	 * @return Arraylist of contacts which were stored on local storage file "contacts"
	 */
	private ArrayList<Contact> readContactList() {			
		try {
			ArrayList<Map<String, String>> contactMap;
			ArrayList<Contact> contactList;			
			
			contactMap = DataStorageManager.readJSONObject(ContactMyDoctor.this, 
														   "contacts");			
			contactList = new ArrayList<Contact>();
			for (Map<String,String> c : contactMap)
			{
				Uri uri = Uri.parse(c.get("uri"));
				String name = c.get("name");
				String phone = c.get("phone");				
				String email = c.get("email");
				
				Contact contact = new Contact(uri, name, phone, email);				
				contactList.add(contact);
			}			
			return contactList;	
		} catch (Exception e) {
		     throw new Error(e);
		}
	}
	
	/**
	 * Saves contact list in local storage
	 * @param context is the activity context
	 */
	static void saveContactList(Context context) {
		try {
			boolean firstElement = true;	
			if (contacts.size() == 0)
				DataStorageManager.deleteFile(context, "contacts");
			for(Contact c : contacts)
			{		
				Map<String,String> contactMap = new HashMap<String,String>();		
				contactMap.put("uri", c.uri.toString());
				contactMap.put("name", c.name);				
				contactMap.put("phone", c.phone);	
				 
				DataStorageManager.writeJSONObject(context, "contacts", 
												   contactMap, firstElement);
				firstElement = false;
			}
		} catch (Exception e) {
		     new Error(e);
		} 
	}
	
	/**
	 * Fill the activity contact with data read from the contact file "contacts"
	 */
	public void populateContactList() {
		contactList = (ListView) findViewById(R.id.ContactMyDoctor_ContactList);	
		contacts = new ArrayList<Contact>(readContactList()); 
		adapter = new TwoLinesAdapter();
		contactList.setAdapter(adapter);
	}
	
	/**
	 * Used as custom adapter to display contact information on the activity list 
	 */
	private class TwoLinesAdapter extends ArrayAdapter<Contact> {
		public TwoLinesAdapter() {
			super(ContactMyDoctor.this, R.layout.list_item, contacts);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.list_item, 
						parent, false);
				
				Contact currentContact = contacts.get(position);			
				TextView title = (TextView) itemView.findViewById(R.id.list_title);
				TextView subtitle = (TextView) itemView.findViewById(R.id.list_subtitle);
				title.setText(currentContact.name);
				subtitle.setText(currentContact.phone); 
			}
			return itemView;
		}
	}
	
	/**
	 * Gets the contact Phone number from the phones contactlist given the contact uri.
	 * On success the method returns the contact phone number. On failure it returns
	 * an empty string.
	 * 
	 * @param uriContact is the uri address pointing to a specific contact
	 * @return contact's main number
	 */
	private String retrieveContactNumber(Uri uriContact) {
		 
        String contactNumber = "";
        
        String[] projection = {Phone.NUMBER};
        Cursor cursor = getContentResolver().query(uriContact, projection, 
        		null, null, null);
        cursor.moveToFirst();

        int column = cursor.getColumnIndex(Phone.NUMBER);
        contactNumber = cursor.getString(column);
        
        Log.v("debugme", "Contact Phone Number: " + contactNumber);
        return contactNumber;
    }
	
	/** To-do: Fix me!
	 * Gets the contact e-mail address from the phone's contact-list given the contact URI.
	 * On success the method returns the contact phone number. On failure it returns
	 * an empty string.
	 * 
	 * @param uriContact
	 * @return
	 */
	private String retriveContactEmail(Uri uriContact) {
		//Log.v("debugme", uriContact.toString());
		
		String contactEmail = null;
		String contactID = "";
		String projection[] = new String[]{ContactsContract.Contacts._ID};
        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                projection, null, null, null);
 
        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursorID.close();
        
     // Using the contact ID now we will get contact email address
        String emailProjection[] = new String[] {ContactsContract.CommonDataKinds.Email.DATA};
        Cursor emailCur = getContentResolver().query(
        						uriContact,
        						emailProjection,
        						null,
        						null,
        						null);
        emailCur.moveToFirst();
        contactEmail = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
        
        Log.d("debugme", "Contact ID: " + contactID);
        Log.d("debugme", "Email: "+contactEmail);
		return "";
	}
	
	/**
	 * Gets the contact name from the phones contactlist given the contact uri.
	 * On success the method returns the contact name. On failure it returns an
	 * empty string.
	 *   
	 * @param uriContact is the uri address pointing to a specific contact
	 * @return contact's name displayed on the user's contactlist
	 */
    private String retrieveContactName(Uri uriContact) {
 
        String contactName = "";
        
        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, 
        		null, null, null, null);
        
        if (cursor.moveToFirst()) { 
            contactName = cursor.getString(cursor.getColumnIndex(
            		ContactsContract.Contacts.DISPLAY_NAME));
        } 
        cursor.close(); 
        Log.v("debugme", "Contact Name: " + contactName); 
        return contactName;
    }
    
    /**
     * Opens SMS application filling in contact information
     */
    protected void sendTextMessage(Context context, Contact c) {
    	Intent sendIntent = new Intent(Intent.ACTION_VIEW);         
    	sendIntent.setData(Uri.parse("sms:"+c.phone));
    	context.startActivity(sendIntent);
	}	

	/**
	 * 	Makes a call to a specified contact
	 */
	protected void makeACall(Context context, Contact c){
		Intent callIntent = new Intent(Intent.ACTION_CALL);
	    callIntent.setData(Uri.parse("tel:"+c.phone));
	    context.startActivity(callIntent);	
	}

	protected void sendEmail(Context context, Contact c) {
		String recipient = c.email;
		Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
		// prompts email clients only
		 email.setType("message/rfc822"); 
		 email.putExtra(Intent.EXTRA_EMAIL, recipient);
	
		 try {
			 // the user can choose the email client
			 context.startActivity(Intent.createChooser(email, "Choose an email client from..."));
		 } catch (android.content.ActivityNotFoundException ex) {
			 Toast.makeText(context.getApplicationContext(), "No email client installed.",
					 Toast.LENGTH_LONG).show();
		 }
	}
}
/**
 * Contact class holds basic information about a contact. 
 * This is used to save the information to the database. 
 */
class Contact implements Serializable {
	private static final long serialVersionUID = 1L;
	Uri uri;
	String name, phone, email;
	
	/**
	 * Class constructor. Populates contact information.
	 * @param uri that points to the user contact information
	 * @param name of the contact
	 * @param phone of the contact
	 */
	public Contact(Uri uri, String name, String phone, String email) {
		this.uri = uri;
		this.name = name;
		this.phone = phone;
		this.email = email;
	}
}