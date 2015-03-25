package com.team9.healthmate.StepCounter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.team9.healthmate.ImageListViewArrayAdapter;
import com.team9.healthmate.R;
import com.team9.healthmate.DataManager.DataStorageManager;
import com.team9.healthmate.GraphManager.ColumnGraph;
import com.team9.healthmate.GraphManager.GraphManager;
import com.team9.healthmate.GraphManager.LineGraph;

/**
 * Health Mate - Step Counter
 * 
 * @author Hoxsey
 * 
 * This is the StepCounter Activity that helps
 * the user track and set their steps, height
 * weight and BMI 
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
	private static int REQUEST_STEP = 100;
	private static int REQUEST_HEIGHT = 102;
	private static int REQUEST_WEIGHT = 103;
	public LayoutInflater in;
	ColumnGraph[] columnGraph;
	View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step_counter);
		
		
		
		// calls the method to initializes some variables
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
	 * Initializes the listview, values that go into the
	 * ImageListViewArrayAdapter adapter and sets the adapter
	 * of the listview
	 */
	public void init()	{
		// initializes the listview
		list = (ListView) findViewById(R.id.list);
		
		// initializes the values string
		String[] values = new String[]{
				fetchStepData(),"0",fetchHeightData(),fetchWeightData(),fetchBMI() };
		
		// initializes the ImageListViewArrayAdapter
		final ImageListViewArrayAdapter adapter = new ImageListViewArrayAdapter(this,values);
		
		
		
		// sets listview adapter
		list.setAdapter(adapter);
		
		
	}
	
	
	
	/**
	 * creates a broadcaster receiver
	 */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	// updates the steps
        	updateSteps(intent);       
        }
    };
    
    /**
     * This is a helper method to change between activities
     * 
     * @param pos An int variable that chooses the activity
     */
	public void changeActivity(int pos)	{
		// creates an intent to change activities
		Intent intent = new Intent(getApplicationContext(), getMenuItem(pos));
		
		// Checker block of code, to choose an activity
		if(pos == 0)	{
			// puts data in the variables and starts activity
			intent.putExtra("mainCount", steps);
			startActivity(intent);
		}	else	{
			// puts data in the intent and starts activity
			intent.putExtra("BMI", BMI);
			startActivity(intent);
		}
	}
	
	private String fetchWeightData()	{
		String weightString = "0";
		try {
			ArrayList<Map<String,String>> weightData = DataStorageManager.readJSONObject(this, "weight_data");
			if(!(weightData.size() == 0))	{
				weightString = weightData.get(weightData.size()-1).get("weight_value");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("WEIGHT_VALUE", weightString);
		weight =Integer.parseInt(weightString);
		return weightString;
	}
	
	private String fetchHeightData()	{
		String heightString = "0";
		try {
			ArrayList<Map<String,String>> heightData = DataStorageManager.readJSONObject(this, "height_data");
			if(!(heightData.size() == 0))	{
				heightString = heightData.get(heightData.size()-1).get("height_value");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("HEIGHT_VALUE", heightString);
		height = Integer.parseInt(heightString);
		heightString = ""+(height/12)+"' "+(height%12)+"\"";
		
		return heightString;
	}
	
	private String fetchStepData()	{
		String stepString = "0";
		try {
			ArrayList<Map<String,String>> stepData = DataStorageManager.readJSONObject(this, "step_data");
			if(!(stepData.size() == 0))	{
				stepString = stepData.get(stepData.size()-1).get("step_value");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		steps = Integer.parseInt(stepString);
		return stepString;
	}
	
	/**
	 * Updates the step view on the listview
	 * 
	 * @param intent An intent that has data from the steps
	 */
	protected void updateSteps(Intent intent) {
		// grabs the step view
		View v = list.getChildAt(0);
		
		// changes the textview
		TextView change = (TextView) v.findViewById(R.id.counter);
		
		// parses the string into an integer from the intent
		// data and then changes the textview 
		steps = Integer.parseInt(intent.getStringExtra("counter"));
		change.setText(""+steps);
	}
	
	/**
	 * Chooses the activity to return bases on the pos 
	 * using a switch statement
	 * 
	 * @param pos Selects the activity
	 * 
	 * @return the activity class
	 */
	public Class<?> getMenuItem(int pos)	{
		switch(pos)	{
			case 0: return StepCounterActivity.class;
			case 2: return HeightActivity.class;
			case 3: return WeightActivity.class;
			//case 4: return BmiActivity.class;
		}
		return null;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		init();
		registerReceiver(broadcastReceiver, new IntentFilter(StepService.BROADCAST_ACTION));
	}
	
	/**
	 * Updates the BMI view
	 */
	public String fetchBMI()	{
		String BMIStr = "";
		// creates a number format for only two digits
		NumberFormat f = new DecimalFormat("#0.00");
		
		// updates the BMI variable using a formal
		BMI = ((double)weight/(height*height))*703.0;
		BMIStr = ""+f.format(BMI);
		return BMIStr;
	}
	
}
