package com.team9.healthmate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Health Mate - Step Counter
 * 
 * @author Hoxsey
 * 
 * This is the StepCounter Activity that helps the user track their steps using
 * the Sensor STEP_DETECTOR. 
 *
 */

public class StepCounter extends Activity {
	ListView list;
	int steps = 0;
	double miles = 0.0;
	int feet = 0;
	int inches = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step_counter);
		init();
	}
	/**
	 * 
	 */
	public void init()	{
		list = (ListView) findViewById(R.id.list);
		
		String[] values = new String[]{
				"100000","2.9","6' 0\"","229","30.49"};
		
		
		final ImageListViewArrayAdapter adapter = new ImageListViewArrayAdapter(this,values);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position,
					long id) {
				Toast.makeText(parent.getContext(), "CLICKED", Toast.LENGTH_SHORT).show();
		    }
		});
	}
}
