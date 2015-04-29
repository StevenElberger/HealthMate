package com.team9.healthmate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.team9.healthmate.R;

import com.team9.healthmate.DataManager.DataStorageManager;

public class Emergency extends Activity {
	
	protected static final int PICK_REQUEST = 0;
	protected static final int OPTION_REQUEST = 1;
	public static ArrayList<Contact> eRcontacts;
	public static ArrayAdapter<Contact> adapter;
	public ListView eRList;
	private int itemSelected = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Reads contacts stored on the file and populate ER-list
				populateErList();
	
				//Select a contact from contact-list intent
				Button buttonPickContact = 
						(Button)findViewById(R.id.ERnumber_add);
				
				buttonPickContact.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View arg0) {
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType(ContactsContract.
										CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
						startActivityForResult(intent, PICK_REQUEST);             
			    }});
	
	}
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	   
	   
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
//		case R.id.mAdd:
//			
//			return true;
			
		default:
			return super.onOptionsItemSelected(item);
	
		}
		
	}
	
	public void populateErList() {
		eRList = (ListView) findViewById(R.id.Emergency_List);	
		eRcontacts = new ArrayList<Contact>(readContactList()); 
		adapter = new TwoLinesAdapter();
		eRList.setAdapter(adapter);
	}
	
	private ArrayList<Contact> readContactList() {			
		try {
			ArrayList<Map<String, String>> contactMap;
			ArrayList<Contact> contactList;			
			
			contactMap = DataStorageManager.readJSONObject(Emergency.this, 
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
	            
	            eRcontacts.add(new Contact(contactUri, name, number, email));
	            adapter.notifyDataSetChanged();
	           
	            //Save to file 
	            saveContactList(Emergency.this);
	        }
	    }
	}
	
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
	
	static void saveContactList(Context context) {
		try {
			boolean firstElement = true;	
			if (eRcontacts.size() == 0)
				DataStorageManager.deleteFile(context, "contacts");
			for(Contact c : eRcontacts)
			{		
				Map<String,String> contactMap = new HashMap<String,String>();		
				contactMap.put("uri", c.uri.toString());
				contactMap.put("name", c.name);				
				contactMap.put("phone", c.phone);	
				 
				DataStorageManager.writeJSONObject(context, "contacts", 
												   contactMap, true);
				firstElement = false;
			}
		} catch (Exception e) {
		     new Error(e);
		} 
	}
	
	private class TwoLinesAdapter extends ArrayAdapter<Contact> {
		public TwoLinesAdapter() {
			super(Emergency.this, R.layout.list_item, eRcontacts);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.list_item, 
						parent, false);
				
				Contact currentContact = eRcontacts.get(position);			
				TextView title = (TextView) itemView.findViewById(R.id.list_title);
				TextView subtitle = (TextView) itemView.findViewById(R.id.list_subtitle);
				title.setText(currentContact.name);
				subtitle.setText(currentContact.phone); 
			}
			return itemView;
		}
	}
	
}
