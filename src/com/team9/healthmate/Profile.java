package com.team9.healthmate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
	String sFirstName, sLastName, sUserName, sGender, sAge, sImagePath;
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
			EditFragment ef = new EditFragment();
			// Bundle the info and send it to the new fragment
			Bundle profileInformation = new Bundle();
			profileInformation.putString(IMAGE_PATH, sImagePath);
			profileInformation.putString(FIRST_NAME, sFirstName);
			profileInformation.putString(LAST_NAME, sLastName);
			profileInformation.putString(USERNAME, sUserName);
			profileInformation.putString(GENDER, sGender);
			profileInformation.putString(AGE, sAge);
			ef.setArguments(profileInformation);
			fm.beginTransaction().replace(R.id.profile_container, ef).commit(); // really should just be hidden instead of replaced
		} else if (item.getItemId() == R.id.action_save_profile) {
			// Show the DisplayFragment with new information
			//getFragmentManager().beginTransaction().replace(R.id.profile_container, new DisplayFragment()).commit();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putString(FIRST_NAME, sFirstName);
		savedInstanceState.putString(LAST_NAME, sLastName);
		savedInstanceState.putString(USERNAME, sUserName);
		savedInstanceState.putString(GENDER, sGender);
		savedInstanceState.putString(AGE, sAge);
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
			
			if (!sImagePath.equals("")) {
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
		Spinner gender;
		DatePicker age;
		String sFirstName, sLastName, sUserName, sGender, sAge, sImagePath;
		EditText eFirstName, eLastName, eUserName, ePassword;
		
		public EditFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
			
			// Wire the widgets
			profileImage = (ImageView) rootView.findViewById(R.id.display_profile_fragment_profile_pic);
			eFirstName = (EditText) rootView.findViewById(R.id.edit_profile_fragment_f_name);
			eLastName = (EditText) rootView.findViewById(R.id.edit_profile_fragment_l_name);
			eUserName = (EditText) rootView.findViewById(R.id.edit_profile_fragment_u_name);
			gender = (Spinner) rootView.findViewById(R.id.edit_profile_fragment_gender);
			age = (DatePicker) rootView.findViewById(R.id.edit_profile_fragment_bday);
			
			return rootView;
		}
	}
}