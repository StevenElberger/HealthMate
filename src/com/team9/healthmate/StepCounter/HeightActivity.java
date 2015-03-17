package com.team9.healthmate.StepCounter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.team9.healthmate.R;
import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


/**
 * This is an activity for choosing
 * height using a SeekBar
 * @author Joseph
 *
 */
public class HeightActivity extends Activity {
	public SeekBar heightBar;
	public TextView ft;
	public TextView inch;
	public int height = 0;
	public int maxHeight = 96;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_height);
		
		//calls the method to initializes some variables
		init();
		
		heightBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
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
	 * progress. Also initializes the ft and inch textview
	 */
	public void init()	{
		// initializes and sets SeekBar
		heightBar = (SeekBar) findViewById(R.id.height_counter);
		heightBar.setMax(maxHeight);
		heightBar.setProgress(0);
		
		//initializes the ft and inch textview
		ft = (TextView) findViewById(R.id.ft);
		inch = (TextView) findViewById(R.id.inch);
		
	}
	
	/**
	 * Updates the UI by changing  ft and inch textview
	 * and updates the height int variable
	 * 
	 * @param progress this is an int variable that comes
	 * 		  SeekBar.
	 */
	public void updateUI(int progress)	{
		// updates the ft and inch textview
		 ft.setText(""+(progress/12));
		 inch.setText(""+(progress%12));
		 
		 // updates the height variable
		 height = progress;
	}
	
	/**
	 * This onClick method is to submit the results of
	 * the users choice on the SeekBar. As well as finishing
	 * the activity to send the height variable using an 
	 * intent
	 * 
	 * @param v Is the view of the submit button
	 */
	public void onClick(View v)	{
		// creates an intent 
		Intent intent = new Intent();
		
		// saves the weight data to the local storage
				Map<String,String> infoPack = new HashMap<String, String>();
				infoPack.put("weight_value", ""+height);
				try {
					DataStorageManager.writeJSONObject(this, "height_data", infoPack, false);
					Toast.makeText(this, "Height data has been saved", Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					Toast.makeText(this, "Height data wasn't saved", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} catch (IOException e) {
					Toast.makeText(this, "Height data wasn't saved", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
		
		// puts the height variable into the intent
		intent.putExtra("Height", height);
		
		// sets the results to RESULT_OK 
		setResult(RESULT_OK, intent);
		finish();
	}
}
