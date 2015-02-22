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

public class WeightActivity extends Activity {
	public SeekBar weightBar;
	public int maxWeight = 600;
	public TextView weight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weight);
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
				update(progress);
				
			}
		});
	}
	public void init()	{
		weightBar = (SeekBar) findViewById(R.id.height_counter);
		weightBar.setMax(maxWeight);
		weightBar.setProgress(150);
		weight = (TextView) findViewById(R.id.lbs);
		
	}
	 public void update(int progress)	{
		 weight.setText(""+(progress));
	 }
	 public void onClick(View v)	{
		Intent intent = new Intent();
		intent.putExtra("Weight", ""+weight.getText());
		setResult(RESULT_OK, intent);
		finish();
	 }
}
