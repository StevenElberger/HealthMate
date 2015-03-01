package com.team9.healthmate.StepCounter;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class StepService extends Service implements SensorEventListener {
	private static final String TAG = "StepService";
	public static final String BROADCAST_ACTION = "com.team9.healthmate.StepCounter";
	private final Handler handler = new Handler();
	Intent intent;
	int counter = 0;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "CREATED", Toast.LENGTH_SHORT).show();
    	intent = new Intent(BROADCAST_ACTION);	
    	SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    	Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    	 if (sensor != null) {
	            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	        } else { 
	            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
	        }
	}
	
    @Override
    public void onStart(Intent intent, int startId) {
    	Toast.makeText(this, "STARTED", Toast.LENGTH_SHORT).show();
    	counter = intent.getIntExtra("count", 0);
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 100); // 1 second
   
    }

    private Runnable sendUpdatesToUI = new Runnable() {
    	public void run() {
    		DisplayLoggingInfo();    		
    	    handler.postDelayed(this, 100); // 10 seconds
    	}
    };    
    
    private void DisplayLoggingInfo() {
    	Log.d(TAG, "entered DisplayLoggingInfo");
    	intent.putExtra("counter", ""+counter);
    	sendBroadcast(intent);
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {		
        handler.removeCallbacks(sendUpdatesToUI);		
		super.onDestroy();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		counter += event.values[0];
		
	}
}
