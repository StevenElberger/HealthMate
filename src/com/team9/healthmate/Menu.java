package com.team9.healthmate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;

import com.team9.healthmate.Appointments.AppointmentsList;
import com.team9.healthmate.DataManager.DataStorageManager;
import com.team9.healthmate.Medications.Medication;
import com.team9.healthmate.Notes.ListOfNotes;

public class Menu extends Activity {
	GridView menu_grid;
	Drawable pic;
	
	private Context context;
	
	public Intent intent;
	
	String[] menuString = {
			"Moods","Medication","Steps","Health Locations","Notes", "Appointments", "Contact My Doctor", "Call ER", "About Us"
	}; // "Graphs", "Animated Graphs", "More Graphs",
	
	int[] imageId = {
		      R.drawable.moods,
		      R.drawable.medication,
		      R.drawable.steps,
		      R.drawable.healthlocations,
		      R.drawable.notes,
		      R.drawable.appointments,
		      R.drawable.contactmydoctor,
		      R.drawable.emergency,
		      R.drawable.aboutus
		  };
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_menu);
	    CustomGridView adapter = new CustomGridView(Menu.this, menuString, imageId);
	    menu_grid = (GridView)findViewById(R.id.menu);
	    menu_grid.setAdapter(adapter);
	    
	   context = getApplicationContext();
	    
	    menu_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Toast.makeText(Menu.this, ""+position, Toast.LENGTH_SHORT).show();
				changeActivity(position);

			}
		});
	    
	   
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

	public void changeActivity(int pos)	{
		intent = new Intent(this, getMenuItem(pos));
		startActivity(intent);
	}

	/* switch statement to find the correct option to go to */
	public Class<?> getMenuItem(int pos)	{
		switch(pos)	{
			case 0: return Moods.class; 
			case 1: return Medication.class;
			
			case 3:	return HealthLocation.class;
			case 4:	return ListOfNotes.class;
			case 5: return AppointmentsList.class;
//			case 5: return ChartDemo.class;
//			case 7: return AnimatedChartDemo.class;
//			case 8: return PreviewChartDemo.class;
			case 6: return ContactMyDoctor.class;
			case 7: return Emergency.class;
			case 8: return AboutUs.class;
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
			//menu.getItem(0).setIcon(pic);
		}
		
		
		
		return true;
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
			startActivity(intent);		}
		
		if (item.getItemId() == R.id.action_emergency){
		 View vAction = (View)findViewById(R.id.action_emergency);
			
			vAction.isClickable();	
			
			vAction.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v)
				{
					OneClickEmergency ER = new OneClickEmergency();
		   			ER.makeACall(context);			
		   			ER.sendEmail(context);
		   			ER.sendSMSMessage(context);
					Toast.makeText(getBaseContext(), "Long Clicked", Toast.LENGTH_SHORT).show();
					return false;
				}
			});
		}
		return super.onOptionsItemSelected(item);
	}
	
	
		
	
}
