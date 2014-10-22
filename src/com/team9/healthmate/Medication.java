package com.team9.healthmate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class Medication extends Activity {
	public ListView medicationList;
	public static ArrayList<MedicationObject> medications;
	ArrayAdapter<MedicationObject> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medication);
		populateMedicationList();
		registerClickCallback();
	}	
	
	public void populateMedicationList() {
		medicationList = (ListView) findViewById(R.id.listViewMedications);			
		medications = new ArrayList<MedicationObject>(readMedicationFile());
		adapter = new TwoLinesAdapter();
		medicationList.setAdapter(adapter);
	}
	
	public void registerClickCallback() {
		ListView list = (ListView) findViewById(R.id.listViewMedications);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
				MedicationObject selectedMed = medications.get(position);
				//Call MedicationDetails Activity
				Intent intent = new Intent(Medication.this, MedicationDetails.class);
				intent.putExtra("medInfo", selectedMed);
				startActivity(intent);			
			}
		});
	}
	
	private ArrayList<MedicationObject> readMedicationFile() {
		String serializedMedicationList = "";
		ArrayList<MedicationObject> meds = new ArrayList<MedicationObject>();
		
		//Retrive HashMap from data manager
		try {
			Map<String,String> hm = new HashMap<String,String>(DataStorageManager.readJSONObject(Medication.this, "filename").get(0));
			serializedMedicationList = hm.get("filename");
			
		//deserialize			
			byte b[] = serializedMedicationList.getBytes(); 
			ByteArrayInputStream bi = new ByteArrayInputStream(b);
		    ObjectInputStream si = new ObjectInputStream(bi);
		    meds = (ArrayList<MedicationObject>) si.readObject(); //Check this...
		} catch (Exception e) {
		     new Error(e);
		}
		return meds;
	}
	
	private void saveMedicationFile() {
		//Serialize list
		String serializedMedicationList = "";
		try {
		     ByteArrayOutputStream bo = new ByteArrayOutputStream();
		     ObjectOutputStream so = new ObjectOutputStream(bo);
		     so.writeObject(medications);
		     so.flush();
		     serializedMedicationList = bo.toString();
		     
		     //Store to file
		     HashMap<String,String> hm = new HashMap<String,String>();
			 hm.put("filename", serializedMedicationList);
		     DataStorageManager.writeJSONObject(Medication.this, hm, true);
		} catch (Exception e) {
		     new Error(e);
		} 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//If data has been changed: Save to file and update list
		if (resultCode == 1)
		{
			saveMedicationFile();
			adapter.notifyDataSetChanged();
			ArrayList<MedicationObject> m = new ArrayList<MedicationObject>(readMedicationFile());
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
			//Make sure we have a view to work with (may have been given null)
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
			}
			
			// Find the medication to work with.
			MedicationObject currentMed = medications.get(position);
			
			// Fill the view
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