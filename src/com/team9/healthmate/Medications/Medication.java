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
 * the Medication component. This includes the display of the medication list,
 * call for the medication detail activity and new medication activity, as well
 * as reading and writing from the medication file stored on the phone.
 * @author Gustavo Arce
 * 
 */
public class Medication extends Activity {
	
	public static ArrayList<MedicationObject> medications;
	public static ArrayAdapter<MedicationObject> adapter;
	public ListView medicationList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medication);
		populateMedicationList();
		onItemListClick();
	}	
	
	public void populateMedicationList() {
		medicationList = (ListView) findViewById(R.id.listViewMedications);			
		medications = new ArrayList<MedicationObject>(readMedicationFile());
		adapter = new TwoLinesAdapter();
		medicationList.setAdapter(adapter);
	}
	
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
			
	static void saveMedication(Context context) {
		try {
			boolean firstElement = true;			
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
	
	public void createMedication(View V) {
		this.startActivityForResult(new Intent(this,NewMedication.class),1);		
	}
	
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
	
	public static void Debug(String msg) {
		Log.v("debugme", msg);
	}
}