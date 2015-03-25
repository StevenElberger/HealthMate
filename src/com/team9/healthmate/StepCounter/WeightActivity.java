package com.team9.healthmate.StepCounter;

import java.io.IOException;
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
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
	public int weight;
	private ColumnGraph[] columnGraph;
	private View view;
	
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
		weightBar.setProgress(0);
		
		//initializes the weight textview
		weightText = (TextView) findViewById(R.id.lbs);
		createGraphs();
		
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
