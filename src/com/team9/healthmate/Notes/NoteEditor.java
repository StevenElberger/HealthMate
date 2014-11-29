package com.team9.healthmate.Notes;

import java.util.HashMap;
import java.util.Map;

import com.team9.healthmate.R;
import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class NoteEditor extends Activity implements OnClickListener {

	// Button to save the Note
	ImageButton save;

	// The container of the Note that will be saved
	Map<String, String> note;
	
	// The container of the Note that will be deleted
	Map<String, String> noteToDelete;

	/**
	 * Method that sets the listener for the save button and initializes the
	 * container for the note to be deleted.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_editor);

		save = (ImageButton) findViewById(R.id.SaveNote);
		save.setOnClickListener((OnClickListener) this);

		noteToDelete = new HashMap<String, String>();

		// Check if the user is editing a previous note.
		checkIfInEditorMode();

	}

	/**
	 * Method that creates an intent to start the next activity. The next
	 * activity is the ListOfNotes Activity.
	 */
	private void noteList() {
		Intent intent = new Intent(NoteEditor.this, ListOfNotes.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	/**
	 * Method that handles the event when the user clicks the save button. Input
	 * from the user is read and stored in the internal storage of the android
	 * device in a file named "notes".
	 */
	@Override
	public void onClick(View v) {
		try {

			// Create a new object to hold the information entered
			// by the user.
			note = new HashMap<String, String>();

			// Go through all the input boxes, and store the information
			// as key/value pairs in the Map object.
			EditText userInput;

			userInput = (EditText) findViewById(R.id.NoteEditorTitle);
			note.put("title", userInput.getText().toString());

			userInput = (EditText) findViewById(R.id.NoteEditorDescription);
			note.put("description", userInput.getText().toString());

			// Check to see if the Appointment was being edited.
			if (noteToDelete.get("timestamp") != null) {

				// Delete the old Note from the file.
				DataStorageManager.deleteJSONObject(this, "notes", noteToDelete);
			}

			// Save the new Note to the "notes" file
			DataStorageManager.writeJSONObject(this, "notes", note, false);

			// Call method to go to the List of Notes Activity.
			noteList();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to check if the user is editing a previous note. If the
	 * user is editing an note, load the information into the form.
	 * @author Michael Sandoval
	 */
	private void checkIfInEditorMode() {

		// Get the intent that activated this Activity
		Intent intent = getIntent();

		// Check if information has been sent.
		if (intent.getStringExtra("timestamp") != null) {
			EditText editInput;

			// Load the time stamp into a Map Object that will
			// be used to delete the old appointment settings.
			noteToDelete.put("timestamp", intent.getStringExtra("timestamp"));

			// Fill the Form Input boxes with the previous user inputs
			// Get the id of each text box, and set their values.
			editInput = (EditText) findViewById(R.id.NoteEditorTitle);
			editInput.setText(intent.getStringExtra("title"));

			editInput = (EditText) findViewById(R.id.NoteEditorDescription);
			editInput.setText(intent.getStringExtra("description"));
		}
	}
}
