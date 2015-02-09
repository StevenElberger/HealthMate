package com.team9.healthmate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author Davit
 *	MoodsSurvey class provides Emotion recording 
	instruments. Lets user save the current taken
	survey in a local android file. From here tabs
	are created as well.
 */
public class MoodsSurvey extends Fragment 
						implements SeekBar.OnSeekBarChangeListener {

	private TextView lblCounter[] = new TextView[7]; 
	private TextView lblMoods[] = new TextView[7];
	private SeekBar seek[] = new SeekBar[6];	
	private View V;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_moods_survey, container, false);
        V = rootView;
           
        SetControlLayout();	// initializes all the view controls	
      	BtnClick();         // Calls button events				
		
        return rootView;
    }	
		
	
	/**
	*  The function calls two button events
	*  Button Reset will reset to original state of controls
	*  Button Submit will save the user data to a file 
	*/
	public void BtnClick(){	
		
		//Resets controls to original state
		final Button buttonReset = (Button) V.findViewById(R.id.cmdReset);
		//sets onClickListener for the button
        buttonReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               for (int i = 0; i < seek.length; i++) {
				seek[i].setProgress(0);
               }
            }
        });     
        
        //Saves user entered results in local Android file
        final Button buttonSubmit = (Button) V.findViewById(R.id.cmdSubmit);
        //Sets onClickListener for the button
        buttonSubmit.setOnClickListener(new View.OnClickListener() {            
			public void onClick(View v) {               
               Map<String, String> listOfItems = new HashMap<String, String>();
               try {
            	   for (int i = 0; i < seek.length; i++) {
					listOfItems.put(lblMoods[i].getText().toString().trim(), lblCounter[i].getText().toString().trim());
				}           	   
				//Overwrites or creates a new file with the user data
				DataStorageManager.writeJSONObject(getActivity(), "testdata", listOfItems, false);
				Toast toast = Toast.makeText(getActivity(), "The file is successfully writen.", Toast.LENGTH_LONG);
				toast.show();							
				//getActivity().finish();
				  for (int i = 0; i < seek.length; i++) {
						seek[i].setProgress(0);
		               }
				//Throws an exception if there is an issue creating JSON object
			} catch (JSONException e) {
				Toast toast = Toast.makeText(getActivity(), "JSON Exception", Toast.LENGTH_LONG);
				toast.show();
				Log.v("Exception Caught in Moods.btnClickFunction: ", e.toString());
				e.printStackTrace();
				//Throws an exception if there is and issue with inputing to the file
			} catch (IOException e) {
				Toast toast = Toast.makeText(getActivity(), "IO Exception", Toast.LENGTH_LONG);
				toast.show();
				Log.v("Exception Caught in Moods.btnClickFunction: ", e.toString());
				e.printStackTrace();
			} 
            }
        });
	}
		
	/** 
	 *  Setting the initial values for the controls
	 * 	SeekBars, TextViews
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
			lblCounter[i] = (TextView)V.findViewById(rsIDCounter);
			lblCounter[i].setText("0");
			lblMoods[i] = (TextView)V.findViewById(rsIDMoods);
			seek[i] = (SeekBar)V.findViewById(rsIDsk);
			seek[i].setOnSeekBarChangeListener(this);			
		}
	}	
	
	/** 
	 *  Text counter is updated when seekbar is moved
	 * 	@param 	argo 		indicates current seekbar in use
	 * 	@param 	progress 	indicates the current positoin of seekbar
	 * 	@param 	fromUser 	indicates if the user moved it
	*/
	@Override
	public void onProgressChanged(SeekBar arg0, int progress, boolean fromUser) {
		
		for (int i = 0; i < seek.length; i++) {
			lblCounter[i].setText(String.valueOf(seek[i].getProgress()));			
			//updateMoodPicture();
				}					
		}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {		
	}
	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
	}	
}
