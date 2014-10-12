package com.team9.healthmate;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class Medication extends Activity
{
	public ListView medicationList;
	ArrayList<MedicationObject> medications;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medication);
				
		populateMedicationList();
	}	
	
	public void populateMedicationList()
	{
		medicationList = (ListView) findViewById(R.id.listViewMedications);			
		medications = new ArrayList<MedicationObject>();
		
		for (int i=0; i<6; i++)		
		{
			MedicationObject med = new MedicationObject(("Medication "+i), MedicationObject.FrequencyType.values()[(int)(Math.random()*3)], 1, (int)(Math.random()*1000));
			medications.add(med);
		}
		
		ArrayAdapter<MedicationObject> adapter = new TwoLinesAdapter();
		medicationList.setAdapter(adapter);
	}
	
	public void createMedication(View V)
	{
		this.startActivity( new Intent(this,NewMedication.class));		
	}
	
	private class TwoLinesAdapter extends ArrayAdapter<MedicationObject>
	{
		public TwoLinesAdapter() {
			super(Medication.this, R.layout.list_item, medications);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
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
}

class MedicationObject 
{
	public static enum FrequencyType
	{
		Day, Week, Month
	}
	
	String name;
	FrequencyType frequencyType; 
	int frequencyValue; //e.g. take 'frequency' times a 'lapse'
	int ammount; //in milligrams
	
	//Constructor
	public MedicationObject(String name, FrequencyType frequencyType, int frequencyValue, int ammount) 
	{
		this.name = name;
		this.frequencyType = frequencyType;
		this.frequencyValue = frequencyValue;
		this.ammount = ammount;
	}
	
	public String getDescription()
	{
		return "Take "+frequencyValue+" every "+frequencyType.name()+" ("+ammount+"mg)";
	}
}