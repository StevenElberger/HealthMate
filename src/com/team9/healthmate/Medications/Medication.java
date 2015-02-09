package com.team9.healthmate.Medications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.team9.healthmate.R;
import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The Medication class implements the functionalities for the main activity of
 * the medication component. 
 * @author Gustavo Arce
 * 
 */
public class Medication extends Activity {
	
	public static ArrayList<MedicationObject> medications;
	public static ArrayAdapter<MedicationObject> adapter;
	public ListView medicationList;
	
	/**
	 * Android's onCreate method. Called on the start of the activity.
	 * Here is where all fields are filled method is called if the user 
	 * is editing an existing medication
	 * 
	 * @param Bundle savedInstaceState as requested by Android
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medication);
		populateMedicationList();
		onItemListClick();
	}	
	
	/**
	 * Populates the medication list
	 */
	public void populateMedicationList() {
		medicationList = (ListView) findViewById(R.id.listViewMedications);			
		medications = new ArrayList<MedicationObject>(readMedicationFile());
		adapter = new TwoLinesAdapter();
		medicationList.setAdapter(adapter);
	}
	
	/**
	 * Sets listeners for the every item list
	 */
	public void onItemListClick() {
		ListView list = (ListView) findViewById(R.id.listViewMedications);		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, 
									View viewClicked, 
									int position, 
									long id) {				
				MedicationObject selectedMed = medications.get(position);
				
				Intent intent = new Intent(Medication.this, MedicationDetails.class);
				intent.putExtra("medInfo", selectedMed);
				startActivity(intent);			
			}
		});
	}
	
	/**
	 * Read file from local storage to retrieve all saved medications
	 * @return an array list containing all the medications stored
	 */
	private ArrayList<MedicationObject> readMedicationFile() {			
		try {
			String name, frequencyType, frequencyValue, dosageType, dosageValue;
			MedicationObject med;
			ArrayList<Map<String, String>> mapList;
			ArrayList<MedicationObject> medicationList;			
			
			mapList = DataStorageManager.readJSONObject(Medication.this, 
					"medications");
			
			medicationList = new ArrayList<MedicationObject>();
			
			for (Map<String,String> currentMed : mapList)
			{				
				name           = currentMed.get("name");
				frequencyType  = currentMed.get("frequencyType");
				frequencyValue = currentMed.get("frequencyValue");
				dosageType     = currentMed.get("dosageType");
				dosageValue    = currentMed.get("dosageValue");
				
				med = new MedicationObject(
						name, 
						MedicationObject.FrequencyType.valueOf(frequencyType),
						Integer.parseInt(frequencyValue),
						MedicationObject.DosageType.valueOf(dosageType),
						Integer.parseInt(dosageValue));	
				
				medicationList.add(med);
			}			
			return medicationList;	
		} catch (Exception e) {
		     throw new Error(e);
		}
	}
	
	/**
	 * Saves the medication list to local storage
	 * @param context of the activity
	 */
	static void saveMedication(Context context) {
		try {
			boolean firstElement = true;
			DataStorageManager.deleteFile(context, "medications");
			for(MedicationObject med : medications)
			{		
				Map<String,String> medMap = new HashMap<String,String>();		
				
				medMap.put("name", med.name);
				medMap.put("frequencyType", med.frequencyType.name());
				medMap.put("frequencyValue", ""+med.frequencyValue);		     
				medMap.put("dosageType",med.dosageType.name());
				medMap.put("dosageValue", ""+med.dosageValue);
				 
				DataStorageManager.writeJSONObject(context, "medications", medMap, firstElement);
				firstElement = false;
			}
		} catch (Exception e) {
		     new Error(e);
		} 
	}
	
	/**
	 * Calls NewMedication activity
	 * @param View required by Android when called from an XML button
	 */
	public void createMedication(View V) {
		this.startActivityForResult(new Intent(this,NewMedication.class),1);		
	}
	
	/**
	 * Adapter private class used to create a list with both title and description
	 */
	private class TwoLinesAdapter extends ArrayAdapter<MedicationObject> {
		public TwoLinesAdapter() {
			super(Medication.this, R.layout.list_item, medications);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
			}
			
			MedicationObject currentMed = medications.get(position);			
			TextView title = (TextView) itemView.findViewById(R.id.list_title);
			TextView subtitle = (TextView) itemView.findViewById(R.id.list_subtitle);
			title.setText(currentMed.name);
			subtitle.setText(currentMed.getDescription()); 
			return itemView;
		}
	}
	
	/**
	 * Method used to debug code
	 * @param msg text to be logged
	 */
	public static void Debug(String msg) {
		Log.v("debugme", msg);
	}
}