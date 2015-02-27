package com.team9.healthmate.StepCounter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.team9.healthmate.R;

public class StepCounterActivity extends Activity	{

	private static final String TAG = "BroadcastService-Healthmate";
	private Intent intent;
	private String counterText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter2);
        
        intent = new Intent(getApplicationContext(), StepService.class);
    }
	
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	updateUI(intent);       
        }
    };    
    
	@Override
	public void onResume() {
		super.onResume();	
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(StepService.BROADCAST_ACTION));
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		unregisterReceiver(broadcastReceiver);
		stopService(intent); 	
		
	}
	
	/*@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent temp = new Intent();
		temp.putExtra("counter", counterText);
		setResult(RESULT_OK, temp);
		finish();
	}*/
	    
    private void updateUI(Intent intent) {
    	String counter = intent.getStringExtra("counter"); 
    	counterText = counter;
    	
    	TextView tv1 = (TextView) findViewById(R.id.counter);  	
    	tv1.setText(counter);
    }
}
