/*Davit Avetikyan
 Moods activity is providing Emotion recording 
 instruments. Will also display the activity progress
 in a form of a graph.
 */


package com.team9.healthmate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.ActionBar;
import android.view.ViewGroup;

import com.team9.healthmate.DataManager.DataStorageManager;

import com.team9.healthmate.R;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Moods extends Activity implements SeekBar.OnSeekBarChangeListener {

	private TextView lblCounter[] = new TextView[7];
	private TextView lblMoods[] = new TextView[7];
	private SeekBar seek[] = new SeekBar[6];
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moods);	
		
		Init();	// initializes all the view controls				
		BtnClick();   // Calls button events
		//SetTabs();		
		
		//Creating tabs in action Bar by referencing to Tablistener even
		final ActionBar actionBar = getActionBar();
		  actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		    // Create a tab listener that is called when the user changes tabs.
		    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
				
				@Override
				public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
					
				}				
				@Override
				public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
					// TODO Auto-generated method stub					
				}
				
				@Override
				public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
					// TODO Auto-generated method stub
					
				}
			};
		//Creates 3 tabs in the action bar
		  for (int i = 0; i < 3; i++) {
  	        actionBar.addTab(
  	                actionBar.newTab()
  	                        .setText("Tab " + (i + 1))
  	                        .setTabListener(tabListener));
  	    }		  
	}			
	
	/*The function is calling two button event calls
	*Button Reset will reset to original state of controls
	*Button Submit will save the user data to a file 
	*/
	public void BtnClick(){		
		//Reset
		final Button buttonReset = (Button) findViewById(R.id.cmdReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               for (int i = 0; i < seek.length; i++) {
				seek[i].setProgress(0);
			}
            }
        });     
        
        //Submit button will submit the user entered data
        final Button buttonSubmit = (Button) findViewById(R.id.cmdSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            
			public void onClick(View v) {               
               Map<String, String> listOfItems = new HashMap<String, String>();
               try {
            	   for (int i = 0; i < seek.length; i++) {
					listOfItems.put(lblMoods[0].getText().toString().trim() + ": ", lblCounter[0].getText().toString().trim());
				}           	   
				
				DataStorageManager.writeJSONObject(getApplication(), "Moods", listOfItems, false);
				Toast toast = Toast.makeText(getApplication(), "The file is successfully writen.", Toast.LENGTH_LONG);
				toast.show();			
//				
				finish();
				
			} catch (JSONException e) {
				Toast toast = Toast.makeText(getApplication(), "JSON Exception", Toast.LENGTH_LONG);
				toast.show();
				Log.v("Exception Caught in Moods.btnClickFunction: ", e.toString());
				e.printStackTrace();
			} catch (IOException e) {
				Toast toast = Toast.makeText(getApplication(), "IO Exception", Toast.LENGTH_LONG);
				toast.show();
				Log.v("Exception Caught in Moods.btnClickFunction: ", e.toString());
				e.printStackTrace();
			} 
            }
        });
	}
		
	/*Initializing function
	 * 
	 */
	public void Init(){				
		SetControlLayout();			
	}

	/* Setting the initial values for the controls
	 * 	
	 */
	public void SetControlLayout(){
		int rsIDCounter, rsIDMoods, rsIDsk;
		for (int i = 0; i < seek.length; i++) {
			
			switch (i) {
		
			case 0:
				rsIDCounter = R.id.txtHappyCounter;
				rsIDMoods = R.id.txtHappy;
				rsIDsk = R.id.skHappy;
				break;
			case 1:
				rsIDCounter = R.id.txtMotivatedCounter;
				rsIDMoods = R.id.txtMotivated;
				rsIDsk = R.id.skMotivated;
				break;				
			case 2:				
				rsIDCounter = R.id.txtStressedCounter;
				rsIDMoods = R.id.txtStressed;
				rsIDsk = R.id.skStressed;
				break;
			case 3:
				rsIDCounter = R.id.txtAngryCounter;
				rsIDMoods = R.id.txtAngry;
				rsIDsk = R.id.skAngry;
				break;
			case 4:
				rsIDCounter = R.id.txtTiredCounter;
				rsIDMoods = R.id.txtTired;
				rsIDsk = R.id.skTired;
				break;
			case 5:
				rsIDCounter = R.id.txtDepressedCounter;
				rsIDMoods = R.id.txtDepressed;				
				rsIDsk = R.id.skDepressed;
				break;			
				
			default:
				rsIDCounter = R.id.txtHappyCounter;
				rsIDMoods = R.id.txtHappy;
				rsIDsk = R.id.skHappy;
				break;
			}
			lblCounter[i] = (TextView)findViewById(rsIDCounter);
			lblCounter[i].setText("0");
			lblMoods[i] = (TextView)findViewById(rsIDMoods);
			seek[i] = (SeekBar)findViewById(rsIDsk);
			seek[i].setOnSeekBarChangeListener(this);
			
		}
	}
	
	
	/* If seek bar is moved the text counter will be updated
	 * @argo the current seekbar used
	 * @progress indicates the current positoin of seekbar
	 * @fromUser indicates if the user moved it
	*/
	@Override
	public void onProgressChanged(SeekBar arg0, int progress, boolean fromUser) {
		if (fromUser) {
		for (int i = 0; i < seek.length; i++) {
			lblCounter[i].setText(String.valueOf(seek[i].getProgress()));			
			//updateMoodPicture();
		}
		}		
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
