package com.team9.healthmate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.google.android.gms.maps.LocationSource;
import com.team9.healthmate.R;

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
	public Calendar calendar;
	public String tempGoal;
	public int stepGoal = 100;
	public ProgressBar goalProgressBar;
	public int steps = 0;
	
	/**
	 * This fetches the previous intents and initializes the textviews and loads the preferences.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step_counter);
		
		counter = (TextView) findViewById(R.id.step_counter);
		goal = (TextView) findViewById(R.id.step_goal);
		
		goalProgressBar = (ProgressBar) findViewById(R.id.goal_progress_bar);
		goalProgressBar.setMax(stepGoal);
		goalProgressBar.setProgress(0);
		
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		
		loadStepRecord();
		
	}
	/**
	 * This method is called when the goal textview is clicked. When called,
	 * creates a dialog object and initializes the contentview and title. It continues
	 * to grab all the views in the dialog so that goal's new value can be fetched.
	 * @param view - This is the view from the goal textview.
	 */
	public void onClick(View view)	{
		final Dialog dialog = new Dialog(StepCounter.this);
		dialog.setContentView(R.layout.goal_changer_dialog);
		dialog.setTitle("Choose Your Goal");
		tempGoal = "";
		
		final EditText editGoal = (EditText) dialog.findViewById(R.id.set_goal);
		Button dialogConfirmationButton = (Button) dialog.findViewById(R.id.ok_button);
		dialogConfirmationButton.setOnClickListener(new OnClickListener() {
			/**
			 * This method is called when the confirmation button is accepted.
			 * When confirmed, set the goal on the display to the chosen one. After,
			 * saves the preference of the goals and dismisses the dialog.
			 * @param v - This is the view that was clicked. This variable is not sused.
			 */
			public void onClick(View v) {
				stepGoal = Integer.parseInt(editGoal.getText().toString());
				tempGoal = "Goal: "+editGoal.getText()+" steps";
				goal.setText(tempGoal);
				goalProgressBar.setMax(stepGoal);
				saveStepRecord();
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	/**
	 * This method is called when the service catches a change in the sensor which is the step counter.
	 * Increase the steps variable by one and sets the testview to the new value of steps along with the
	 * updating the progress bar then calls saveStepRecord() to save in the preferences.
	 */
	@Override
	public void onSensorChanged (SensorEvent event) {
		steps++;
		counter.setText(""+steps);
		goalProgressBar.setProgress(steps);
		saveStepRecord();

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
