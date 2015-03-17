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
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.team9.healthmate.ImageListViewArrayAdapter;
import com.team9.healthmate.R;
import com.team9.healthmate.DataManager.DataStorageManager;

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
				"0","0","0' 0\"","0","0"};
		
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
			startActivityForResult(intent, getActivityResultCode(pos));
		}
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
		
		try {
			ArrayList<Map<String,String>> weightData = DataStorageManager.readJSONObject(this, "height_data");
			ArrayList<Map<String,String>> heightData = DataStorageManager.readJSONObject(this, "weight_data");
			String weightString = weightData.get(0).get("weight_data");
			String heightString = heightData.get(0).get("height_data");
			updateMeasurements(weightString, heightString);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		registerReceiver(broadcastReceiver, new IntentFilter(StepService.BROADCAST_ACTION));
	}
	
	private void updateMeasurements(String w, String h)	{
		// grabs the weight and height views from the listview
		View heightView = list.getChildAt(2);
		View weightView = list.getChildAt(3);
		
		// initializes the textviews from the views previously
		TextView heightChange = (TextView) heightView.findViewById(R.id.counter);
		TextView weightChange = (TextView) weightView.findViewById(R.id.counter);
		
		// updates height and weight by parsing the string data
		height = Integer.parseInt(h);
		weight = Integer.parseInt(w);
		
		// updates the UI of weight and height
		heightChange.setText((height/12)+"' "+(height%12)+"\"");
		weightChange.setText(""+weight);
		
		// updates the BMI view
		updateBMI();
	}
	
	/**
	 * Choose a Request code to send with the intent to another
	 * activity based on a switch statement
	 * 
	 * @param pos Selects the request code
	 * 
	 * @return REQUEST CODE
	 */
	public int getActivityResultCode(int pos)	{
		switch(pos)	{
			case 0: return REQUEST_STEP;
			case 2: return REQUEST_HEIGHT;
			case 3: return REQUEST_WEIGHT;
		}
		return -1;
	}
	
	/**
	 * Updates the BMI view
	 */
	public void updateBMI()	{
		// creates a number format for only two digits
		NumberFormat f = new DecimalFormat("#0.00");
		
		// updates the BMI variable using a formal
		BMI = ((double)weight/(height*height))*703.0;
		
		// grabs the BMI view in the list view and changes
		// the textview and formats the double
		View v = list.getChildAt(4);
		TextView change = (TextView) v.findViewById(R.id.counter);
		change.setText(""+f.format(BMI));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode)	{
			case 102:
				// This block of code is for the results of the Height
				// activity to change the view
				if(resultCode == RESULT_OK)	{
					View v = list.getChildAt(2);
					TextView change = (TextView) v.findViewById(R.id.counter);
					height = data.getIntExtra("Height", -1);
					change.setText((height/12)+"' "+(height%12)+"\"");
					updateBMI();
				}break;
			case 103:
				// This block of code is for the results of the Weight
				// activity to change the view
				if(resultCode == RESULT_OK)	{
					View v = list.getChildAt(3);
					TextView change = (TextView) v.findViewById(R.id.counter);
					change.setText(data.getStringExtra("Weight"));
					weight = Integer.parseInt(data.getStringExtra("Weight"));
					updateBMI();
				}break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
