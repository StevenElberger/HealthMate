package com.team9.healthmate.Notes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.team9.healthmate.R;
import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Activity Class that Displays a List of Notes that have been
 * created by the user through the application. The user can select
 * a saved note and view its information. The user also has the
 * option of creating new notes.
 * @author Michael Sandoval
 *
 */
public class ListOfNotes extends ListActivity {
	
		// Container for the list of notes that will
		// be read from the notes file.
		ArrayList<Map<String, String>> notes; 
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_appointment_list);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			
			// Attempt to read from the Notes file
			try {
				
			notes =  DataStorageManager.readJSONObject(this, "notes");
			
			// A list of strings that will be displayed to the user
			ArrayList<String> noteList = new ArrayList<String>();
			
			// Reverse the order of the list, this will
			// allow the information of the list be in the
			// order of the latest notes to be on the top of the list
			Collections.reverse(notes);
			
			// The information that will be displayed to the user.
			String noteInformation = "";
			String[] dateInfo;
			String date = "";
			
			// Go through all the notes and generate a list of Strings.
			// This list will be used to generate a list with information about
			// each note.
			for (Map<String, String> note : notes) {
				noteInformation = note.get("title");
				date = note.get("timestamp").substring(0, 10);
				dateInfo = date.split("-");
				noteInformation = note.get("title") + "\nDate Created: " + 
						dateInfo[1] + "-" + dateInfo[2] + "-" + dateInfo[0];
				noteList.add(noteInformation);
			}
			
			// Pass in the list of information, the adapter will generate
			// a list.
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_list_item_1, noteList);
			
			// set the adapter as active.
			setListAdapter(adapter);
			
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Method sets up the options available to the user.
		 * An icon of an addition sign is displayed to the user as an option. 
		 */
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.note_list, menu);
			return true;
		}
		
		/**
		 * Method to activate the user options in the Action Bar.
		 * The only option is to create a new note, this leads
		 * to the note editor class.
		 */
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			
			// Check to see which option was selected by the user.
			if (item.getItemId() == R.id.action_create_new_note) {
				Intent intent = new Intent(this, NoteEditor.class);
				startActivity(intent);
			}
			
			return super.onOptionsItemSelected(item);
		}
		
		/**
		 * Method to handle the event the user selects a note
		 * from the list of notes. The note's information
		 * is sent to the note details class.
		 */
		@Override
		protected void onListItemClick(ListView l, View v, int position, long id) {
			super.onListItemClick(l, v, position, id);
			
			String key;
			Set<String> setOfKeys;
			Iterator<String> stringIterator;
			
			// Get the note that was selected from the list of notes.
			Map<String, String> note = notes.get(position);
			
			// Create a new intent that will start the Note Detail Activity
			Intent intent = new Intent(this, NoteDetail.class);
			
			// Go through all the information stored in the note
			// selected using the objects keys. 
			setOfKeys = note.keySet();
			stringIterator = setOfKeys.iterator();
			
			while (stringIterator.hasNext()){
				key = stringIterator.next();
				
				// Add them to the intent, this will allow
				// the activity started by the intent to access the information.
				intent.putExtra(key, note.get(key));
				}
			
			// Remove the current activity from the top of the stack when finished.
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}
