package com.team9.healthmate;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.team9.healthmate.R;

public class StepCounter extends Activity implements SensorEventListener{

	
	 private SensorManager sensorManager;
	    private TextView count;
	    boolean activityRunning;
	    boolean firstRun = false;
	    static float initRun; 
	
	    @Override
	    public void onCreate(Bundle savedInstanceState) {	    	
	    
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_step_counter);
	        count = (TextView) findViewById(R.id.stepCount);
	        count.setText("");
	        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	        firstRun = true;
	    }

	    @Override
	    protected void onResume() {    	
	    	
	        super.onResume();
	        activityRunning = true;
	        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
	        if (countSensor != null) {
	            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
	        } else {
	            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
	        }

	    }

	    @Override
	    protected void onPause() {
	        super.onPause();
	        activityRunning = false;
	        // if you unregister the last listener, the hardware will stop detecting step events
//	        sensorManager.unregisterListener(this); 
	    }

	    @Override
	    public void onSensorChanged(SensorEvent event) {  
	    	
//	    	if (firstRun){
//	    	 initRun = event.values[0];	
//	    	}
//	    	firstRun = false;
	        if (activityRunning) {
	            count.setText(String.valueOf(event.values[0] - initRun));
	        }

	    }

	    @Override
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    }
}
