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
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
public class Profile extends Activity {
	String sFirstName, sLastName, sUserName, sPassword, sGender, sAge, sImagePath;
	boolean notPressedYet = true;
	private static final String IMAGE_PATH = "Image_Path";
	private static final String FIRST_NAME = "First_Name";
	private static final String LAST_NAME = "Last_Name";
	private static final String USERNAME = "Username";
	private static final String GENDER = "Gender";
	private static final String AGE = "Age";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (savedInstanceState == null) {
			loadProfileInformation();
			// Bundle the information and send it to the fragment
			DisplayFragment df = new DisplayFragment();
			Bundle profileInformation = new Bundle();
			profileInformation.putString(IMAGE_PATH, sImagePath);
			profileInformation.putString(FIRST_NAME, sFirstName);
			profileInformation.putString(LAST_NAME, sLastName);
			profileInformation.putString(USERNAME, sUserName);
			profileInformation.putString(GENDER, sGender);
			profileInformation.putString(AGE, sAge);
			df.setArguments(profileInformation);
			getFragmentManager().beginTransaction().add(R.id.profile_container, df).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (notPressedYet) {
			getMenuInflater().inflate(R.menu.profile_menu, menu);
		} else {
			getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
		}
		return true;
	}
	
	/**
	 * Responsible for swapping the fragments out and transferring data between them.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		FragmentManager fm = getFragmentManager();
		
		if (item.getItemId() == R.id.action_edit_profile) {
			// Going from the DisplayFragment to the EditFragment
			DisplayFragment df = (DisplayFragment) fm.findFragmentById(R.id.profile_container);
			EditFragment ef = new EditFragment();
			
			// Bundle the info and send it to the new fragment
			Bundle profileInformation = new Bundle();
			profileInformation.putString(IMAGE_PATH, df.sImagePath);
			profileInformation.putString(FIRST_NAME, df.sFirstName);
			profileInformation.putString(LAST_NAME, df.sLastName);
			profileInformation.putString(USERNAME, df.sUserName);
			profileInformation.putString(GENDER, df.sGender);
			profileInformation.putString(AGE, df.sAge);
			ef.setArguments(profileInformation);
			
			// Really should just be hidden instead of replaced, but #YOLO
			fm.beginTransaction().replace(R.id.profile_container, ef).commit();
			notPressedYet = false;
			invalidateOptionsMenu();
			
		} else if (item.getItemId() == R.id.action_save_profile) {
			// Going from the EditFragment to the DisplayFragment
			EditFragment ef = (EditFragment) fm.findFragmentById(R.id.profile_container);
			DisplayFragment df = new DisplayFragment();
			
			// Grab from the DatePicker
			int day = ef.age.getDayOfMonth();
			int month = ef.age.getMonth();
			int year = ef.age.getYear();
			Calendar c = Calendar.getInstance();
			c.set(year,  month, day);
			Date userAge = c.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String dateString = sdf.format(userAge);
			String sUserAge = dateString;
			
			// Check to make sure user entered his/her password
			sPassword = ef.ePassword.getText().toString();
			if (sPassword.equals("")) {
				TextView warningLabel = (TextView) findViewById(R.id.edit_profile_fragment_no_password_label);
				warningLabel.setTextColor(Color.RED);
				warningLabel.setVisibility(0);
			} else {
				String imagePath = ef.sImagePath;
				String firstName = ef.eFirstName.getText().toString();
				String lastName = ef.eLastName.getText().toString();
				String userName = ef.eUserName.getText().toString();
				String password = ef.ePassword.getText().toString();
				String gender = ef.gender.getSelectedItem().toString();
				String age = sUserAge;
				
				// Write information to user account file
				// and bring user back to the display fragment
				writeProfileInformation(userName, firstName, lastName, gender, age, password, imagePath);
				
				// Show the DisplayFragment with new information
				Bundle profileInformation = new Bundle();
				profileInformation.putString(IMAGE_PATH, ef.sImagePath);
				profileInformation.putString(FIRST_NAME, ef.eFirstName.getText().toString());
				profileInformation.putString(LAST_NAME, ef.eLastName.getText().toString());
				profileInformation.putString(USERNAME, ef.eUserName.getText().toString());
				profileInformation.putString(GENDER, ef.gender.getSelectedItem().toString());
				profileInformation.putString(AGE, sUserAge);
				df.setArguments(profileInformation);
				
				fm.beginTransaction().replace(R.id.profile_container, df).commit();
				notPressedYet = true;
				invalidateOptionsMenu();
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Takes account information from local storage.
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
							sUserName = value;
							break;
						case "first_name":
							sFirstName = value;
							break;
						case "last_name":
							sLastName = value;
							break;
						case "sex":
							sGender = value;
							break;
						case "age":
							sAge = value;
							break;
						case "picture":
							sImagePath = value;
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
	public void writeProfileInformation(String userName, String firstName, String lastName, String gender, String age, String password, String imagePath) {
		try {
			Context context = getApplicationContext();
			Map<String, String> dataSet = new HashMap<String, String>();
			dataSet.put("username", userName);
			dataSet.put("first_name", firstName);
			dataSet.put("last_name", lastName);
			dataSet.put("sex", gender);
			dataSet.put("age", age);
			dataSet.put("password", password);
			if (imagePath == null) {
				imagePath = "";
			}
			dataSet.put("picture", imagePath);
			DataStorageManager.writeJSONObject(context, "account", dataSet, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
    public void onBackPressed() {
            super.onBackPressed();
            this.finish();
    }
	
	/**
	 * This fragment is responsible for displaying the profile
	 * data. 
	 */
	public static class DisplayFragment extends Fragment {
		ImageView profileImage;
		String sFirstName, sLastName, sUserName, sGender, sAge, sImagePath;
		TextView firstName, lastName, userName, gender, age;
		
		public DisplayFragment() {}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_display_profile, container, false);
			
			// Wire the widgets
			profileImage = (ImageView) rootView.findViewById(R.id.display_profile_fragment_profile_pic);
			firstName = (TextView) rootView.findViewById(R.id.display_profile_fragment_first_name_textview);
			lastName = (TextView) rootView.findViewById(R.id.display_profile_fragment_last_name_textview);
			userName = (TextView) rootView.findViewById(R.id.display_profile_fragment_username_textview);
			gender = (TextView) rootView.findViewById(R.id.display_profile_fragment_gender_textview);
			age = (TextView) rootView.findViewById(R.id.display_profile_fragment_age_textview);
			
			// Grab the profile info
			sImagePath = getArguments().getString(IMAGE_PATH);
			sFirstName = getArguments().getString(FIRST_NAME);
			sLastName = getArguments().getString(LAST_NAME);
			sUserName = getArguments().getString(USERNAME);
			sGender = getArguments().getString(GENDER);
			sAge = getArguments().getString(AGE);
			
			// Display the information
			firstName.setText(sFirstName);
			lastName.setText(sLastName);
			userName.setText(sUserName);
			gender.setText(sGender);
			age.setText(sAge);
			
			if (sImagePath != null) {
				File imgFile = new  File(sImagePath);
				Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				profileImage.setImageBitmap(bitmap);
			}
			
			return rootView;
		}
	}
	
	/**
	 * This fragment is responsible for editing the profile
	 * data. 
	 */
	public static class EditFragment extends Fragment {
		ImageView profileImage;
		Button uploadButton;
		Spinner gender;
		DatePicker age;
		String sFirstName, sLastName, sUserName, sGender, sAge, sImagePath;
		EditText eFirstName, eLastName, eUserName, ePassword;
		
		public EditFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
			
			// set up the upload button
			uploadButton = (Button) rootView.findViewById(R.id.edit_profile_fragment_upload_profile_pic);
			
			uploadButton.setOnClickListener(new Button.OnClickListener() {
				
				// When clicked, launch the photo gallery
				// so the user may select a profile picture
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, 0);
				}
			});
			
			// Wire the widgets
			profileImage = (ImageView) rootView.findViewById(R.id.edit_profile_fragment_edit_profile_pic);
			eFirstName = (EditText) rootView.findViewById(R.id.edit_profile_fragment_f_name);
			eLastName = (EditText) rootView.findViewById(R.id.edit_profile_fragment_l_name);
			eUserName = (EditText) rootView.findViewById(R.id.edit_profile_fragment_u_name);
			ePassword = (EditText) rootView.findViewById(R.id.edit_profile_fragment_password);
			gender = (Spinner) rootView.findViewById(R.id.edit_profile_fragment_gender);
			age = (DatePicker) rootView.findViewById(R.id.edit_profile_fragment_bday);
			
			// Set up the gender spinner -- grab the activity's context
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
		            R.array.sex, R.layout.sex_spinner_textview);
		    adapter.setDropDownViewResource(R.layout.sex_spinner_textview);
			gender.setAdapter(adapter);
			
			// Grab the profile info
			sImagePath = getArguments().getString(IMAGE_PATH);
			sFirstName = getArguments().getString(FIRST_NAME);
			sLastName = getArguments().getString(LAST_NAME);
			sUserName = getArguments().getString(USERNAME);
			sGender = getArguments().getString(GENDER);
			sAge = getArguments().getString(AGE);
			
			// Display the information
			eFirstName.setText(sFirstName);
			eLastName.setText(sLastName);
			eUserName.setText(sUserName);
			
			// Show the gender
			if (sGender.equals("Male")) {
				gender.setSelection(0);
			} else if (sGender.equals("Female")) {
				gender.setSelection(1);
			} else {
				gender.setSelection(2);
			}
			
			// Show the birthday
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			try {
				Date userAge = sdf.parse(sAge);
				Calendar c = Calendar.getInstance();
				c.setTime(userAge);
				age.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			// Show the profile picture
			if (sImagePath != null) {
				File imgFile = new  File(sImagePath);
				Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				profileImage.setImageBitmap(bitmap);
			}
			
			return rootView;
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
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			 super.onActivityResult(requestCode, resultCode, data);
			 
			 // grab the image's path to save it
			 // to the account file and load the image
			 if (resultCode == RESULT_OK) {
				 Uri targetUri = data.getData();
				 sImagePath = getRealPathFromURI(getActivity().getApplicationContext(), targetUri);
				 Bitmap bitmap;
				 try {
					 bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
					 profileImage.setImageBitmap(bitmap);
				 } catch (FileNotFoundException e) {
					 e.printStackTrace();
				 }
			 }
		}
	}
}