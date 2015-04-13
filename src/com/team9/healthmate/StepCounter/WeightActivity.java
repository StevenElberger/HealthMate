package com.team9.healthmate.StepCounter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.team9.healthmate.R;
import com.team9.healthmate.R.id;
import com.team9.healthmate.R.layout;
import com.team9.healthmate.R.menu;
import com.team9.healthmate.DataManager.DataStorageManager;
import com.team9.healthmate.GraphManager.ColumnGraph;
import com.team9.healthmate.GraphManager.GraphManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

/**
 * This is an activity for choosing
 * weight using a SeekBar
 * @author Joseph
 *
 */
public class WeightActivity extends Activity {
	public SeekBar weightBar;
	public int maxWeight = 400;
	public TextView weightText;
	public TextView weightGoalText;
	public int weight;
	private ColumnGraph[] columnGraph;
	private View view;
	private int goal = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weight);
		
		//calls the method to initializes some variables
		init();
		
		weightBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				//calls the method to update the UI
				updateUI(progress);
				
			}
		});
	}
	
	/**
	 * Initializes the SeekBar and it's max and initial
	 * progress. Also initializes the weight textview
	 */
	public void init()	{
		
		view = findViewById(R.id.graph);
		// initializes and sets SeekBar
		weightBar = (SeekBar) findViewById(R.id.height_counter);
		weightBar.setMax(maxWeight);
		weightBar.setProgress(Integer.parseInt(fetchWeightData()));
		
		//initializes the weight textview
		weightText = (TextView) findViewById(R.id.lbs);
		weightText.setText(fetchWeightData());
		weightGoalText = (TextView) findViewById(R.id.goal_count);
		weightGoalText.setText(fetchWeightGoal());
		createGraphs();
		
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
	
	public String fetchWeightGoal()	{
		String goalString = "0";
		try {
			ArrayList<Map<String,String>> goalData = DataStorageManager.readJSONObject(this, "weight_goal_data");
			if(!(goalData.size() == 0))	{
				goalString = goalData.get(goalData.size()-1).get("weight_goal_value");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return goalString;
	}
	
	/**
	 * Updates the UI by changing  weight textview
	 * 
	 * @param progress this is an int variable that comes
	 * 		  SeekBar.
	 */
	public void updateUI(int progress)	{
		 weightText.setText(""+(progress));
		 weight = progress;
	}
	
	/**
	 * This onClick method is to submit the results of
	 * the users choice on the SeekBar. As well as finishing
	 * the activity to send the weight variable using an 
	 * intent
	 * 
	 * @param v Is the view of the submit button
	 */
	public void onClick(View v)	{
		// creates an intent 
		Intent intent = new Intent();
		
		// saves the weight data to the local storage
		Map<String,String> infoPack = new HashMap<String, String>();
		infoPack.put("weight_value", ""+weight);
		try {
			DataStorageManager.writeJSONObject(this, "weight_data", infoPack, false);
			Toast.makeText(this, "Weight data has been saved", Toast.LENGTH_SHORT).show();
		} catch (JSONException e) {
			Toast.makeText(this, "Weight data wasn't saved", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(this, "Weight data wasn't saved", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
		// sets the results to RESULT_OK 
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public void onClickGoalChange(View v)	{
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    	alertDialog.setTitle("Change your Goal");
    	final EditText changer = new EditText(this);
    	changer.setText(""+goal);
    	changer.setInputType(InputType.TYPE_CLASS_NUMBER);
    	alertDialog.setView(changer);
    	
    	alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
    	    new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) {
    	        	goal = Integer.parseInt(changer.getText().toString());
    	        	weightGoalText.setText(""+goal);
    	        	saveGoal();
    	            dialog.dismiss();
    	        }
    	    });
    	alertDialog.show();
	}
	
	private void saveGoal()	{
    	// saves the weight data to the local storage
		Map<String,String> infoPack = new HashMap<String, String>();
		infoPack.put("weight_goal_value", ""+goal);
		try {
			DataStorageManager.writeJSONObject(this, "weight_goal_data", infoPack, true);
		//	Toast.makeText(this, "Steps data has been saved", Toast.LENGTH_SHORT).show();
		} catch (JSONException e) {
			Toast.makeText(this, "Weight Goal data wasn't saved", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(this, "Weight Goal data wasn't saved", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
    }
	
	public void createGraphs() {
		// Other colors are possible
		int[] colors = {Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.RED, Color.LTGRAY, Color.DKGRAY};
		
		columnGraph = new ColumnGraph[3];
		for (int i = 0; i < columnGraph.length; i++) {
			columnGraph[i] = new ColumnGraph("Days", "Weight", "weight_data", true, true, colors[2]);
		}
		GraphManager.generateColumnGraph(getApplicationContext(), view, columnGraph[0], "weight_value");
	}
}
