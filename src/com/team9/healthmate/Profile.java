package com.team9.healthmate;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/** 
 * Displays user profile information to the
 * user. The user may edit his/her profile
 * and save their edited profile to local storage
 * if so desired. The user may also upload a photograph
 * for their user profile picture.
 * @author Steve
 */
public class Profile extends Activity {
	boolean notPressedYet = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.profile_container, new DisplayFragment()).commit();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (item.getItemId() == R.id.action_edit_profile) {
			//setUpEditLayout();
		} else if (item.getItemId() == R.id.action_save_profile) {
			//setUpNormalLayout();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This fragment is responsible for displaying the profile
	 * data. 
	 */
	public static class DisplayFragment extends Fragment {

		public DisplayFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_display_profile,
					container, false);
			return rootView;
		}
	}
	
	/**
	 * This fragment is responsible for editing the profile
	 * data. 
	 */
	public static class EditFragment extends Fragment {

		public EditFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_edit_profile,
					container, false);
			return rootView;
		}
	}
}