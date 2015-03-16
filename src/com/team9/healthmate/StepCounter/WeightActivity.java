package com.team9.healthmate.StepCounter;

import com.team9.healthmate.R;
import com.team9.healthmate.R.id;
import com.team9.healthmate.R.layout;
import com.team9.healthmate.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * This is an activity for choosing
 * weight using a SeekBar
 * @author Joseph
 *
 */
public class WeightActivity extends Activity {
	public SeekBar weightBar;
	public int maxWeight = 400;
	public TextView weight;
	
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
		// initializes and sets SeekBar
		weightBar = (SeekBar) findViewById(R.id.height_counter);
		weightBar.setMax(maxWeight);
		weightBar.setProgress(0);
		
		//initializes the weight textview
		weight = (TextView) findViewById(R.id.lbs);
		
	}
	
	/**
	 * Updates the UI by changing  weight textview
	 * 
	 * @param progress this is an int variable that comes
	 * 		  SeekBar.
	 */
	public void updateUI(int progress)	{
		 weight.setText(""+(progress));
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
		
		// puts the height variable into the intent
		intent.putExtra("Weight", ""+weight.getText());
		
		// sets the results to RESULT_OK 
		setResult(RESULT_OK, intent);
		finish();
	}
}
