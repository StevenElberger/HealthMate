package com.team9.healthmate.StepCounter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.team9.healthmate.AnimatedChartDemo;
import com.team9.healthmate.AppointmentsList;
import com.team9.healthmate.ChartDemo;
import com.team9.healthmate.Emergency;
import com.team9.healthmate.HealthLocation;
import com.team9.healthmate.ImageListViewArrayAdapter;
import com.team9.healthmate.Moods;
import com.team9.healthmate.Note;
import com.team9.healthmate.PreviewChartDemo;
import com.team9.healthmate.R;
import com.team9.healthmate.Medications.Medication;
import com.team9.healthmate.R.id;
import com.team9.healthmate.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
	public ListView list;
	public int steps = 0;
	public double miles = 0.0;
	public int feet = 0;
	public int inches = 0;
	public int height = 0;
	public int weight= 0;
	public double BMI = 0.0;
	private static int REQUEST_HEIGHT = 102;
	private static int REQUEST_WEIGHT = 103;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step_counter);
		init();
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position,
					long id) {
				changeActivity(position);
		    }
		});
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
		
		
	}
	public void changeActivity(int pos)	{
		Intent intent = new Intent(getApplicationContext(), getMenuItem(pos));
		startActivityForResult(intent, getActivityResultCode(pos));
	}
	
	/* switch statement to find the correct option to goto*/
	public Class<?> getMenuItem(int pos)	{
		switch(pos)	{
			case 2: return HeightActivity.class;
			case 3: return WeightActivity.class;
		}
		return null;
	}
	
	public int getActivityResultCode(int pos)	{
		switch(pos)	{
			case 2: return REQUEST_HEIGHT = 102;
			case 3: return REQUEST_WEIGHT = 103;
		}
		return -1;
	}
	public void updateBMI()	{
		NumberFormat f = new DecimalFormat("#0.00");
		BMI = ((double)weight/(height*height))*703.0;
		View v = list.getChildAt(4);
		TextView change = (TextView) v.findViewById(R.id.counter);
		change.setText(""+f.format(BMI));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode)	{
		case 102:
			if(resultCode == RESULT_OK)	{
				View v = list.getChildAt(2);
				TextView change = (TextView) v.findViewById(R.id.counter);
				height = data.getIntExtra("Height", -1);
				change.setText((height/12)+"' "+(height%12)+"\"");
				
				Toast.makeText(this, ""+height, Toast.LENGTH_LONG).show();
				updateBMI();
			}break;
		case 103:
			if(resultCode == RESULT_OK)	{
				View v = list.getChildAt(3);
				TextView change = (TextView) v.findViewById(R.id.counter);
				change.setText(data.getStringExtra("Weight"));
				Toast.makeText(this, data.getStringExtra("Weight"), Toast.LENGTH_LONG).show();
				weight = Integer.parseInt(data.getStringExtra("Weight"));
				updateBMI();
			}break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
