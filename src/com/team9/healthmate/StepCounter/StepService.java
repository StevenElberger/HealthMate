package com.team9.healthmate.StepCounter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.team9.healthmate.DataManager.DataStorageManager;

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

/**
 * This is a Service for counting steps in the
 * the background of the application in order
 * to save and load step data
 * @author Joseph
 *
 */
public class StepService extends Service implements SensorEventListener {
	private static final String TAG = "StepService";
	public static final String BROADCAST_ACTION = "com.team9.healthmate.StepCounter";
	private final Handler handler = new Handler();
	Intent intent;
	int counter = 0;
	int goalCount = 1000;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// toast that announces that the service has been created
		Toast.makeText(this, "CREATED", Toast.LENGTH_SHORT).show();
		
		// creates an intent so that other application can grab 
		// data from the service
    	intent = new Intent(BROADCAST_ACTION);	
    	
    	// initializes sensor manager to a sensor service along with
    	// initializing sensor to a step detector.
    	SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    	Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    	
    	// creates a check to make sure the device sensor is available
    	// if the device has the sensor, then register listener
    	if (sensor != null) {
	        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	    } else {
	    	// toast to announce that the device doesn't have a step sensor
	    	Toast.makeText(this, "This device doesn't have a step sensor", Toast.LENGTH_LONG).show();
	    }
	}
	
	/**
	 * An onStart method so that when the application
	 * starts it grabs the previous intent data and puts
	 * the right data in the right variable along with a
	 * handler.
	 */
    @Override
    public void onStart(Intent intent, int startId) {
    	Toast.makeText(this, "STARTED", Toast.LENGTH_SHORT).show();
    	counter = intent.getIntExtra("count", 0);
    	goalCount = intent.getIntExtra("goalCount", 1000);
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 100); // 1 second
   
    }
    
    /**
     * initializes a runnable and a handler to post delay 
     */
    private Runnable sendUpdatesToUI = new Runnable() {
    	public void run() {
    		DisplayLoggingInfo();    		
    	    handler.postDelayed(this, 100); // .001 second
    	}
    };    
    
    /**
     * A method put counter data in the intent and send
     * it through the broadcaster
     */
    private void DisplayLoggingInfo() {
    	intent.putExtra("counter", ""+counter);
    	intent.putExtra("goalCount",""+goalCount);
    	sendBroadcast(intent);
    }
    
    private void saveData()	{
    	// saves the weight data to the local storage
		Map<String,String> infoPack = new HashMap<String, String>();
		infoPack.put("step_value", ""+counter);
		try {
			DataStorageManager.writeJSONObject(this, "step_data", infoPack, true);
		//	Toast.makeText(this, "Steps data has been saved", Toast.LENGTH_SHORT).show();
		} catch (JSONException e) {
			Toast.makeText(this, "Steps data wasn't saved", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(this, "Steps data wasn't saved", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
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
		// updates the counter based on the step counter senosr
		counter += event.values[0];
		saveData();
		
	}
}
