package com.team9.healthmate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.google.android.gms.internal.cn;
import com.google.android.gms.maps.LocationSource;
import com.team9.healthmate.R;
import com.team9.healthmate.DataManager.DataStorageManager;

/**
 * Health Mate - Step Counter
 * 
 * @author Hoxsey
 * 
 * This is the StepCounter Activity that helps the user track their steps using
 * the Sensor STEP_DETECTOR. 
 *
 */

public class StepCounter extends Activity implements SensorEventListener {
	
	public LocationManager locationManager;
	public LocationListener locationListener;
	public Sensor sensor;
	public SensorManager sensorManager;
	public TextView counter;
	public TextView goal;
	public Spinner unitTracker;
	public String unit = "Steps";
	public Location location;
	public Location lastLocation;
	public String tempGoal;
	public int stepGoal = 100;
	public ProgressBar goalProgressBar;
	public int steps = 0;
	public Chronometer stopWatch;
	public boolean isStart= false;
	public TextView startStopButton;
	public Dialog congratzDialog;
	private boolean dialogBoxActive = false;
	private static DataStorageManager dataStorageManager;
	private Calendar calendar;
	private StepCounterData stepData;
	private int saves = 1;
	private Dialog goalDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step_counter);
		init();
		
	}
	public void init()	{
		calendar = Calendar.getInstance();
		stepData = new StepCounterData(steps);
		
		//initializes the textviews
		counter = (TextView) findViewById(R.id.step_counter);
		goal = (TextView) findViewById(R.id.step_goal);
		stopWatch = (Chronometer) findViewById(R.id.stop_watch);
		startStopButton = (TextView) findViewById(R.id.start_stop_button);
		//initializes the progress bar
		goalProgressBar = (ProgressBar) findViewById(R.id.goal_progress_bar);
		goalProgressBar.setMax(stepGoal);
		goalProgressBar.setProgress(0);
		//set the sensor manager
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
		
		//loadStepRecord();
		promptGoalDialog();
	}
	
	
	
	
	/**
	 * This method is called when the goal textview is clicked. When called,
	 * creates a dialog object and initializes the contentview and title. It continues
	 * to grab all the views in the dialog so that goal's new value can be fetched.
	 * @param view - This is the view from the goal textview.
	 */
	public void onClick(View view)	{
		promptGoalDialog();
		
	}
	public void promptGoalDialog()	{
		goalDialog = new Dialog(StepCounter.this);
		goalDialog.setContentView(R.layout.goal_changer_dialog);
		goalDialog.setTitle("Choose Your Goal");
		
		final EditText editGoal = (EditText) goalDialog.findViewById(R.id.set_goal);
		editGoal.setText(""+stepGoal);
		Button dialogConfirmationButton = (Button) goalDialog.findViewById(R.id.ok_button);
		dialogConfirmationButton.setOnClickListener(new OnClickListener() {
			/**
			 * This method is called when the confirmation button is accepted.
			 * When confirmed, set the goal on the display to the chosen one. After,
			 * saves the preference of the goals and dismisses the dialog.
			 * @param v - This is the view that was clicked. This variable is not sused.
			 */
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "hit", Toast.LENGTH_SHORT).show();
				stepGoal = Integer.parseInt(editGoal.getText().toString());
				updateStepView();
				//saveStepRecord();
				goalDialog.dismiss();
			}
		});
		goalDialog.show();
		
		
	}
	
	public void onStartStopButton(View v)	{
		if(isStart)	{
			stopWatch.stop();
			startStopButton.setText("Start");
			sensorManager.unregisterListener(this);
			isStart = false;
			
		} else {
			isStart = true;
			stopWatch.start();
			startStopButton.setText("Stop");
			sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
			
		}
		
	}
	public void initCongratulationDialog()	{
		congratzDialog = new Dialog(StepCounter.this);
		congratzDialog.setContentView(R.layout.reached_goal_dialog);
		congratzDialog.setTitle("Congratulation!");
		TextView stepsAchieved = (TextView) congratzDialog.findViewById(R.id.steps_reached);
		stepsAchieved.setText(""+stepGoal);
		congratzDialog.show();
		Button save = (Button) congratzDialog.findViewById(R.id.save_button);
		Button discard = (Button) congratzDialog.findViewById(R.id.discard_button);
		
		OnClickListener onClickListener = new OnClickListener() {
			public void onClick(View v) {
				onClickGoalReachedDialog(v.getId());
			}
		};
		save.setOnClickListener(onClickListener);
		discard.setOnClickListener(onClickListener);
		
	}

	public void onClickGoalReachedDialog(int id)	{
		if(id == R.id.save_button)	{
			stepData = new StepCounterData(steps, stopWatch.getText().toString());
			String filename = "StepCounter_"+calendar.getTime().getMonth()+"-"+calendar.getTime().getDay()+"-"+calendar.getTime().getYear()+"_"+saves;
			/*try {
				DataStorageManager.writeJSONObject(getApplicationContext(),filename, stepData.getKeyMay(), false);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			saves++;
		}
		dialogBoxActive = false;
		congratzDialog.dismiss();
		resetCounter();
		
	}
	public void resetCounter()	{
		stopWatch.setText("00:00");
		stopWatch.setBase(SystemClock.elapsedRealtime());
		steps = 0;
		updateStepView();
	}
	public void updateStepView()	{
		counter.setText(""+steps);
		goal.setText("Goal :"+stepGoal);
		goalProgressBar.setProgress(steps);
		goalProgressBar.setMax(stepGoal);
	}
	
	
	
	/**
	 * This method is called when the service catches a change in the sensor which is the step counter.
	 * Increase the steps variable by one and sets the textview to the new value of steps along with the
	 * updating the progress bar then calls saveStepRecord() to save in the preferences.
	 */
	@Override
	public void onSensorChanged (SensorEvent event) {
		steps++;
		updateStepView();
	//	saveStepRecord();
		if(steps >= stepGoal && !dialogBoxActive)	{
			dialogBoxActive = true;
			stopWatch.stop();
			startStopButton.setText("Start");
			isStart = false;
			initCongratulationDialog();
		}

	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * This method saves the steps and the goals in the sharedPreferences
	 */
	public void saveStepRecord()	{
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putInt("stepKey", steps);
		editor.putInt("goalKey", stepGoal);
		editor.commit();
	}
	/**
	 * This method loads the steps and goals from the Shared Preferences.
	 * Initialize steps and goal in their respective textview
	 * 
	 */
	public void loadStepRecord()	{
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		steps = sharedPreferences.getInt("stepKey", 0);
		stepGoal = sharedPreferences.getInt("goalKey", 100);
		counter.setText(""+steps);
		goal.setText("Goal: "+stepGoal+" steps");
	}
}
