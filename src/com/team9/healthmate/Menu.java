package com.team9.healthmate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.team9.healthmate.Appointments.AppointmentsList;
import com.team9.healthmate.DataManager.DataStorageManager;
import com.team9.healthmate.Medications.Medication;
import com.team9.healthmate.Notes.ListOfNotes;

public class Menu extends Activity {
	public ListView menu;
	public Intent intent;
	Drawable pic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		String [] menuString = {
				"Moods","Medication","Steps","Health Locations","Notes", "Appointments", "Graphs", "Animated Graphs", "More Graphs", "Contact My Doctor"
		};
		menu = (ListView) findViewById(R.id.menu);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,menuString);
		menu.setAdapter(adapter);
		/*This is a listener to find the users option selected */
		menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Toast.makeText(Menu.this, ""+position, Toast.LENGTH_SHORT).show();
				changeActivity(position);

			}
		});
	}
	
	public void changeActivity(int pos)	{
		intent = new Intent(this, getMenuItem(pos));
		startActivity(intent);
	}
	
	/* switch statement to find the correct option to go to */
	public Class<?> getMenuItem(int pos)	{
		switch(pos)	{
			case 0: return Moods.class; 
			case 1: return Medication.class;
			case 2: return StepCounter.class;
			case 3:	return HealthLocation.class;
			case 4:	return ListOfNotes.class;
			case 5: return AppointmentsList.class;
			case 6: return ChartDemo.class;
			case 7: return AnimatedChartDemo.class;
			case 8: return PreviewChartDemo.class;
			case 9: return ContactMyDoctor.class;
		}
		return null;
	}
	
	/**
	 * Set up the user profile button on the action bar.
	 */
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
	    // Inflate the menu items for use in the action bar
		getMenuInflater().inflate(R.menu.main_menu, menu);
		loadProfileInformation();
		if (pic != null) {
			menu.getItem(0).setIcon(pic);
		}
		return true;
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
					if (key.equals("picture")) {
						if (!value.equals("")) {
							File imgFile = new  File(value);
							Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
							pic = new BitmapDrawable(getResources(), bitmap);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * User clicked on edit profile button so send them to their
	 * profile page.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Check to see which option was selected by the user.
		if (item.getItemId() == R.id.action_edit_profile) {
			Intent intent = new Intent(this, Profile.class); // used to be UserProfile.class
			startActivity(intent);
		}
		
		return super.onOptionsItemSelected(item);
	}
}
