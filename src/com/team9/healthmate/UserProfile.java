package com.team9.healthmate;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/** 
 * Displays user profile information to the
 * user. The user may edit his/her profile
 * and save their edited profile to local storage
 * if so desired. The user may also upload a photograph
 * for their user profile picture.
 * @author Steve
 */
public class UserProfile extends Activity {
	ImageView image;
	DatePicker eAge;
	Spinner eGender;
	Button uploadButton;
	TextView firstName, lastName, userName, gender, age;
	EditText eFirstName, eLastName, eUserName, ePassword;
	String sFirstName, sLastName, sUserName, sGender, sAge, sPassword, imagePath;
	boolean notPressedYet = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		firstName = (TextView) findViewById(R.id.f_name);
		lastName = (TextView) findViewById(R.id.l_name);
		userName = (TextView) findViewById(R.id.u_name);
		gender = (TextView) findViewById(R.id.gender);
		age = (TextView) findViewById(R.id.birthday);
		image = (ImageView) findViewById(R.id.profile_pic);
		
		// display user account information
		loadProfileInformation();
	}
	
	/**
	 * Returns the path to an image file selected
	 * from the photo gallery. This is used to save
	 * the image path of the user's profile picture
	 * to the user's account file.
	 * @param context		Application context
	 * @param contentUri	The URI being returned with the image data
	 * @return				A string of the image's direct file path
	 */
	public String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try { 
			String[] proj = { MediaStore.Images.Media.DATA };
		    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
		    }
		}
	}

	/**
	 * Grabs the data returned from the photo gallery.
	 * This will be used to load the profile picture in the
	 * user's profile.
	 */
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 super.onActivityResult(requestCode, resultCode, data);
		 
		 // grab the image's path to save it
		 // to the account file and load the image
		 if (resultCode == RESULT_OK) {
			 Uri targetUri = data.getData();
			 imagePath = getRealPathFromURI(getApplicationContext(), targetUri);
			 Bitmap bitmap;
			 try {
				 bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
				 image.setImageBitmap(bitmap);
			 } catch (FileNotFoundException e) {
				 e.printStackTrace();
			 }
		 }
	}
	
	/**
	 * Takes account information from local storage and
	 * displays them on the appropriate forms in the profile
	 * page.
	 */
	public void loadProfileInformation() {
		try {
			Context context = getApplicationContext();
			ArrayList<Map<String, String>> credentials = DataStorageManager.readJSONObject(context, "account");
			Iterator<Map<String, String>> iterator = credentials.iterator();

			Map<String, String> dataSet = new HashMap<String, String>();
			// attempt to authenticate the user
			while (iterator.hasNext()) {
				// go through all the keys
				dataSet = iterator.next();
				Iterator<String> it = dataSet.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					String value = dataSet.get(key);
					// make sure we only check the username & password keys
					switch (key) {
						case "username":
							userName.setText(value);
							sUserName = value;
							break;
						case "first_name":
							firstName.setText(value);
							sFirstName = value;
							break;
						case "last_name":
							lastName.setText(value);
							sLastName = value;
							break;
						case "sex":
							gender.setText(value);
							sGender = value;
							break;
						case "age":
							age.setText(value);
							sAge = value;
							break;
						case "picture":
							// load the image from the file path stored
							if (!value.equals("")) {
								File imgFile = new  File(value);
								Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
								image.setImageBitmap(bitmap);
							}
							imagePath = value;
						default:
							break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Takes account information from forms being displayed
	 * and writes them to the user account file.
	 */
	public void writeProfileInformation() {
		try {
			Context context = getApplicationContext();
			Map<String, String> dataSet = new HashMap<String, String>();
			dataSet.put("username", sUserName);
			dataSet.put("first_name", sFirstName);
			dataSet.put("last_name", sLastName);
			dataSet.put("sex", sGender);
			dataSet.put("age", sAge);
			dataSet.put("password", sPassword);
			if (imagePath == null) {
				imagePath = "";
			}
			dataSet.put("picture", imagePath);
			DataStorageManager.writeJSONObject(context, "account", dataSet, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set up the user profile button on the action bar.
	 * This is called every time the user clicks a menu
	 * button because of invalidateMenuOptions().
	 */
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
	    // Inflate the menu items for use in the action bar
		// depending on whether they need to save or edit.
		if (notPressedYet) {
			getMenuInflater().inflate(R.menu.profile_menu, menu);
		} else {
			getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
		}
		return true;
	}
	
	/**
	 * Called when the user hits the edit menu button.
	 * This will change the layout to the editing layout
	 * and load the forms with their appropriate information.
	 */
	public void setUpEditLayout() {
		// if the edit button was selected, load the
		// appropriate layout
		setContentView(R.layout.activity_edit_profile2);
		
		// grab forms on the new layout
		eFirstName = (EditText) findViewById(R.id.f_name);
		eLastName = (EditText) findViewById(R.id.l_name);
		eUserName = (EditText) findViewById(R.id.u_name);
		eGender = (Spinner) findViewById(R.id.gender);
		eAge = (DatePicker) findViewById(R.id.bday);
		ePassword = (EditText) findViewById(R.id.password);
		image = (ImageView) findViewById(R.id.edit_profile_pic);
		
		// set up the upload button
		uploadButton = (Button) findViewById(R.id.upload_profile_pic);
		
		uploadButton.setOnClickListener(new Button.OnClickListener() {
			
			// when clicked, launch the photo gallery
			// so the user may select a profile picture
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 0);
			}
		});
		
		// set up the gender spinner
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	            R.array.sex, R.layout.sex_spinner_textview);
	    adapter.setDropDownViewResource(R.layout.sex_spinner_textview);
		eGender.setAdapter(adapter);
		
		if (sGender.equals("Male")) {
			eGender.setSelection(0);
		} else if (sGender.equals("Female")) {
			eGender.setSelection(1);
		} else {
			eGender.setSelection(2);
		}
		
		// set form values
		eFirstName.setText(sFirstName);
		eLastName.setText(sLastName);
		eUserName.setText(sUserName);
		
		// load image
		if (imagePath != null) {
			File imgFile = new  File(imagePath);
			Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			image.setImageBitmap(bitmap);
		}
		
		// set the datepicker
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		try {
			Date age = sdf.parse(sAge);
			Calendar c = Calendar.getInstance();
			c.setTime(age);
			eAge.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// Set flag and call onCreateOptionsMenu
		// to inflate the correct menu.
		notPressedYet = false;
		invalidateOptionsMenu();
	}
	
	/**
	 * Called when the user hits the save menu button.
	 * It checks to make sure the user entered his/her
	 * password before attempting to write the profile
	 * information to the account file (including the
	 * path to the user's profile picture) and then
	 * brings the user back to the normal profile layout
	 * with the new information and profile picture, if any.
	 */
	public void setUpNormalLayout() {
		// grab form values
		sFirstName = eFirstName.getText().toString();
		sLastName = eLastName.getText().toString();
		sUserName = eUserName.getText().toString();
		sGender = eGender.getSelectedItem().toString();
		
		// grab from the datepicker
		int day = eAge.getDayOfMonth();
		int month = eAge.getMonth();
		int year = eAge.getYear();
		Calendar c = Calendar.getInstance();
		c.set(year,  month, day);
		Date userAge = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String dateString = sdf.format(userAge);
		sAge = dateString;
		
		// check to make sure user entered his/her password
		sPassword = ePassword.getText().toString();
		if (sPassword.equals("")) {
			TextView warningLabel = (TextView) findViewById(R.id.no_password_label);
			warningLabel.setTextColor(Color.RED);
			warningLabel.setVisibility(0);
		} else {
			// bring the old layout back to present
			// the updated account information and new
			// profile picture, if any
			setContentView(R.layout.activity_user_profile);
			// grab new forms
			firstName = (TextView) findViewById(R.id.f_name);
			lastName = (TextView) findViewById(R.id.l_name);
			userName = (TextView) findViewById(R.id.u_name);
			gender = (TextView) findViewById(R.id.gender);
			age = (TextView) findViewById(R.id.birthday);
			image = (ImageView) findViewById(R.id.profile_pic);
			
			// set new forms' values
			firstName.setText(sFirstName);
			lastName.setText(sLastName);
			userName.setText(sUserName);
			gender.setText(sGender);
			age.setText(sAge);
			
			// load new image
			if (imagePath != null) {
				File imgFile = new  File(imagePath);
				Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				image.setImageBitmap(bitmap);
			}
			
			// write information to user account file
			// and bring user back to the original layout
			writeProfileInformation();
			notPressedYet = true;
			invalidateOptionsMenu();
		}
	}

	/**
	 * If the user clicked the edit button, show all of their
	 * profile information in the edit_profile_menu menu so
	 * they may edit their profile. Otherwise, write all their
	 * new information to file and bring them back to the original
	 * profile layout.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Check to see which option was selected by the user.
		if (item.getItemId() == R.id.action_edit_profile) {
			setUpEditLayout();
		} else if (item.getItemId() == R.id.action_save_profile) {
			setUpNormalLayout();
		}
		return super.onOptionsItemSelected(item);
	}
}