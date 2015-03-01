package com.team9.healthmate.StepCounter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.team9.healthmate.ImageListViewArrayAdapter;
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

public class StepCounter extends Activity {
	public ListView list;
	public int steps = 0;
	public double miles = 0.0;
	public int feet = 0;
	public int inches = 0;
	public int height = 0;
	public int weight= 0;
	public double BMI = 0.0;
	private static int REQUEST_STEP = 100;
	private static int REQUEST_HEIGHT = 102;
	private static int REQUEST_WEIGHT = 103;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step_counter);
		init();
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position,
					long id) {
				changeActivity(position);
		    }
		});
	}
	/**
	 * 
	 */
	public void init()	{
		list = (ListView) findViewById(R.id.list);
		
		String[] values = new String[]{
				"0","0","0' 0\"","0","0"};
		
		
		final ImageListViewArrayAdapter adapter = new ImageListViewArrayAdapter(this,values);
		list.setAdapter(adapter);
		
		
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	updateSteps(intent);       
        }
    };
    
	public void changeActivity(int pos)	{
		Intent intent = new Intent(getApplicationContext(), getMenuItem(pos));
		if(pos == 0){
			intent.putExtra("mainCount", steps);
			startActivity(intent);
		}	else	{
			intent.putExtra("BMI", BMI);
			startActivityForResult(intent, getActivityResultCode(pos));
		}
	}
	
	protected void updateSteps(Intent intent) {
		View v = list.getChildAt(0);
		TextView change = (TextView) v.findViewById(R.id.counter);
		steps = Integer.parseInt(intent.getStringExtra("counter"));
		change.setText(""+steps);
	}
	/* switch statement to find the correct option to goto*/
	public Class<?> getMenuItem(int pos)	{
		switch(pos)	{
			case 0: return StepCounterActivity.class;
			case 2: return HeightActivity.class;
			case 3: return WeightActivity.class;
			case 4: return BmiActivity.class;
		}
		return null;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(broadcastReceiver, new IntentFilter(StepService.BROADCAST_ACTION));
	}
	
	public int getActivityResultCode(int pos)	{
		switch(pos)	{
			case 0: return REQUEST_STEP = 100;
			case 2: return REQUEST_HEIGHT = 102;
			case 3: return REQUEST_WEIGHT = 103;
		}
		return -1;
	}
	
	public void updateBMI()	{
		NumberFormat f = new DecimalFormat("#0.00");
		BMI = ((double)weight/(height*height))*703.0;
		View v = list.getChildAt(4);
		TextView change = (TextView) v.findViewById(R.id.counter);
		change.setText(""+f.format(BMI));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode)	{
		
		case 102:
			if(resultCode == RESULT_OK)	{
				View v = list.getChildAt(2);
				TextView change = (TextView) v.findViewById(R.id.counter);
				height = data.getIntExtra("Height", -1);
				change.setText((height/12)+"' "+(height%12)+"\"");
				
				Toast.makeText(this, ""+height, Toast.LENGTH_LONG).show();
				updateBMI();
			}break;
		case 103:
			if(resultCode == RESULT_OK)	{
				View v = list.getChildAt(3);
				TextView change = (TextView) v.findViewById(R.id.counter);
				change.setText(data.getStringExtra("Weight"));
				Toast.makeText(this, data.getStringExtra("Weight"), Toast.LENGTH_LONG).show();
				weight = Integer.parseInt(data.getStringExtra("Weight"));
				updateBMI();
			}break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
