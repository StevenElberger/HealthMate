package com.team9.healthmate;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.team9.healthmate.R;

import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class Moods extends Activity implements SeekBar.OnSeekBarChangeListener{

	private TextView label[] = new TextView[7];
	private SeekBar seek[] = new SeekBar[7];
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moods);	
		
		init();			
		
		final Button button = (Button) findViewById(R.id.cmdReset);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               for (int i = 0; i < seek.length; i++) {
				seek[i].setProgress(0);
			}
            }
        });
	}
	
	public void init(){
		label[0] = (TextView)findViewById(R.id.txtHappyCounter);		
		seek[0] = (SeekBar)findViewById(R.id.skHappy);
		seek[0].setOnSeekBarChangeListener(this);
				
		label[1] = (TextView)findViewById(R.id.txtOkCounter);		
		seek[1] = (SeekBar)findViewById(R.id.skJustOk);
		seek[1].setOnSeekBarChangeListener(this);
		
		label[2] = (TextView)findViewById(R.id.txtMotivatedCounter);		
		seek[2] = (SeekBar)findViewById(R.id.skMotivated);
		seek[2].setOnSeekBarChangeListener(this);
		
		label[3] = (TextView)findViewById(R.id.txtStressedCounter);		
		seek[3] = (SeekBar)findViewById(R.id.skStressed);
		seek[3].setOnSeekBarChangeListener(this);
		
		label[4] = (TextView)findViewById(R.id.txtAngryCounter);		
		seek[4] = (SeekBar)findViewById(R.id.skAngry);
		seek[4].setOnSeekBarChangeListener(this);
		
		label[5] = (TextView)findViewById(R.id.txtTiredCounter);		
		seek[5] = (SeekBar)findViewById(R.id.skTired);
		seek[5].setOnSeekBarChangeListener(this);
		
		label[6] = (TextView)findViewById(R.id.txtDepressedCounter);		
		seek[6] = (SeekBar)findViewById(R.id.skDepressed);
		seek[6].setOnSeekBarChangeListener(this);
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		label[0].setText(String.valueOf(seek[0].getProgress()));
		label[1].setText(String.valueOf(seek[1].getProgress()));
		label[2].setText(String.valueOf(seek[2].getProgress()));
		label[3].setText(String.valueOf(seek[3].getProgress()));
		label[4].setText(String.valueOf(seek[4].getProgress()));
		label[5].setText(String.valueOf(seek[5].getProgress()));
		label[6].setText(String.valueOf(seek[6].getProgress()));
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}
}
