package com.team9.healthmate.StepCounter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.team9.healthmate.R;
import com.team9.healthmate.GraphManager.ColumnGraph;
import com.team9.healthmate.GraphManager.GraphManager;
/**
 * Step counter activity that only tracks
 * the step and has buttons to start the
 * step service and stop even reset the
 * step count
 * @author Joseph
 *
 */
public class StepCounterActivity extends Activity	{

	private static final String TAG = "StepCounterActivty";
	private Intent intent;
	private String counterText;
	public int stepCounter = 0;
	private View view;
	ColumnGraph [] columnGraph;
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter2);
        
        view = (View)findViewById(R.id.graph);
        
        /* 
         * grabs the data counter from the previous activity and
         * and updates this activity's step counter variable 
         * along with updating the textview of steps and
         * initializes the intent that contains step service 
         */
        stepCounter = this.getIntent().getIntExtra("mainCount", 0);
        TextView init = (TextView) findViewById(R.id.counter);  	
        init.setText(""+stepCounter);
        intent = new Intent(this, StepService.class);
        createGraphs();
    }
    
    public void createGraphs() {
		
		// Other colors are possible
		int[] colors = {Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.RED, Color.LTGRAY, Color.DKGRAY};
		
		columnGraph = new ColumnGraph[3];
		for (int i = 0; i < columnGraph.length; i++) {
			columnGraph[i] = new ColumnGraph("Days", "Steps", "step_data", true, true, colors[2]);
		}
		GraphManager.generateColumnGraph(getApplicationContext(), view, columnGraph[0], "step_value");
	}
	
    /**
     * creates a broadcast receiver and onReceive method
     * to call the updateUI method
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	updateUI(intent);       
        }
    };
    
    /**
     * OnClickStart method to call the receiver you start
     * receiving data from the step service
     * 
     * @param v
     */
    public void onClickStart(View v)	{
    	Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();
    	intent.putExtra("count", stepCounter);
    	startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(StepService.BROADCAST_ACTION));
    }
    
    /**
     * OnClickStop method to stop the receiver
     * and unregister the receiver and stop the
     * service
     * 
     * @param v
     */
    public void onClickStop(View v)	{
    	try	{
			unregisterReceiver(broadcastReceiver);
		} catch(Exception e)	{
		}
		stopService(intent); 
    }
    
    /**
     * OnClickReset to restart the service and
     * the variable containing the steps
     * 
     * @param v
     */
    public void onClickReset(View v)	{
    	stepCounter = 0;
    	intent.putExtra("count", stepCounter);
    	try	{
			unregisterReceiver(broadcastReceiver);
		} catch(Exception e)	{
			Log.d("Receiver", "There is no receiver to unregisters");
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
	
	/**
	 * Updates the UI aka all the textviews in the
	 * activity
	 * 
	 * @param intent An intent that contains the step
	 * 				 data
	 */
    private void updateUI(Intent intent) {
    	// grabs the data from the intent and updates
    	// the string counter
    	String counter = intent.getStringExtra("counter");
    	
    	// parses the counter to an integer
    	stepCounter = Integer.parseInt(counter);
    	
    	// updates the counter
    	counterText = counter;
    	
    	// updates the counter textview
    	TextView tv1 = (TextView) findViewById(R.id.counter);  	
    	tv1.setText(counter);
    	createGraphs();
    }
}
