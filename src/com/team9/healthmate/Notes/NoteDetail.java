package com.team9.healthmate.Notes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.team9.healthmate.R;
import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Activity Class that Displays the information
 * about a certain note. The title, and description
 * of the note are displayed to the user.
 * @author Michael Sandoval
 *
 */
public class NoteDetail extends Activity {

	// Map Object to contain the note details
	private Map<String, String> noteDetails;

	/**
	 * Method retrieves information sent by the previous activity and displays
	 * the information on the screen.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Create a new Map Object
		noteDetails = new HashMap<String, String>();

		// Get the intent sent by the previous activity
		Intent intent = getIntent();

		// Insert information from the intent into the Map Object
		noteDetails.put("timestamp", intent.getStringExtra("timestamp"));
		noteDetails.put("title", intent.getStringExtra("title"));
		noteDetails.put("description", intent.getStringExtra("description"));

		// Go Through the information and display it on the screen.
		TextView textView = (TextView) findViewById(R.id.NoteDetailName);
		textView.setText(noteDetails.get("title"));

		textView = (TextView) findViewById(R.id.NoteDetailDescription);
		textView.setText(noteDetails.get("description"));
	}

	/**
	 * Method to set the options to delete or edit the information of the
	 * note.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.note_details, menu);
		return true;
	}

	/**
	 * Method to handle the events of when the user selects an option.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Check if the User is deleting a note
		if (item.getItemId() == R.id.action_delete_note) {

			try {
				DataStorageManager.deleteJSONObject(this, "notes", noteDetails);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Go back to the note list after deletion.
			Intent intent = new Intent(this, ListOfNotes.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}

		// Check to see if the user wants to edit the note
		if (item.getItemId() == R.id.action_edit_note) {

			String key;
			Set<String> setOfKeys;
			Iterator<String> stringIterator;

			// Create a new intent to go to the Note Editor
			Intent intent = new Intent(this, NoteEditor.class);

			// Go through the information of the note details,
			// and add them to the intent that will be sent to the
			// Note Editor activity.
			setOfKeys = noteDetails.keySet();
			stringIterator = setOfKeys.iterator();
			while (stringIterator.hasNext()) {
				key = stringIterator.next();
				intent.putExtra(key, noteDetails.get(key));
			}
			// Go to the next activity
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Method to handle the event that the user wants to go back to the
	 * Note List.
	 */
	@Override
	public void onBackPressed() {
		Intent newIntent = new Intent(NoteDetail.this, ListOfNotes.class);
		newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(newIntent);
		finish();
	}
}
