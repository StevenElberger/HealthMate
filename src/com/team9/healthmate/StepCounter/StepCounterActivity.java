package com.team9.healthmate.StepCounter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.team9.healthmate.R;

public class StepCounterActivity extends Activity	{

	private static final String TAG = "StepCounterActivty";
	private Intent intent;
	private String counterText;
	public int stepCounter = 0;
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_step_counter2);
        stepCounter = this.getIntent().getIntExtra("mainCount", 0);
        TextView init = (TextView) findViewById(R.id.counter);  	
        init.setText(""+stepCounter);
        intent = new Intent(this, StepService.class);
        
    }
	
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	updateUI(intent);       
        }
    };    
    public void onClickStart(View v)	{
    	Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();
    	intent.putExtra("count", stepCounter);
    	startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(StepService.BROADCAST_ACTION));
    }
    
    public void onClickStop(View v)	{
    	try	{
			unregisterReceiver(broadcastReceiver);
		} catch(Exception e)	{
		}
		stopService(intent); 
    }
    
    public void onClickReset(View v)	{
    	stepCounter = 0;
    	intent.putExtra("count", stepCounter);
    	try	{
			unregisterReceiver(broadcastReceiver);
		} catch(Exception e)	{
		}
    	stopService(intent);
    	startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(StepService.BROADCAST_ACTION));
    }
    
	@Override
	public void onResume() {
		super.onResume();	
		registerReceiver(broadcastReceiver, new IntentFilter(StepService.BROADCAST_ACTION));
	}
	
	@Override
	public void onPause() {
		super.onPause();
		try	{
			unregisterReceiver(broadcastReceiver);
		} catch(Exception e)	{
		}
		
	}
	    
    private void updateUI(Intent intent) {
    	String counter = intent.getStringExtra("counter");
    	stepCounter = Integer.parseInt(counter);
    	counterText = counter;
    	TextView tv1 = (TextView) findViewById(R.id.counter);  	
    	tv1.setText(counter);
    }

    private void createGraph()	{
    	
    }
}
