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
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class HeightActivity extends Activity {
	public SeekBar heightBar;
	public TextView ft;
	public TextView inch;
	public int height = 0;
	public int maxHeight = 120;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_height);
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
				update(progress);
				
			}
		});
	}
	public void init()	{
		heightBar = (SeekBar) findViewById(R.id.height_counter);
		heightBar.setMax(120);
		heightBar.setProgress(60);
		ft = (TextView) findViewById(R.id.ft);
		inch = (TextView) findViewById(R.id.inch);
		
	}
	 public void update(int progress)	{
		 ft.setText(""+(progress/12));
		 inch.setText(""+(progress%12));
		 height = progress;
	 }
	 public void onClick(View v)	{
		Intent intent = new Intent();
		intent.putExtra("Height", height);
		setResult(RESULT_OK, intent);
		finish();
	 }
}
