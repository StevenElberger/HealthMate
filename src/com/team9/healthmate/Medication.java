package com.team9.healthmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.team9.healthmate.R;

//Array of options --> array adapter --> listView

public class Medication extends Activity {
	public ListView medicationList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medication);
		
		String [] medicationString = {
				"Medication A","Medication B","Medication C","Medication D","Medication E","Medication F"
		};
		
		medicationList = (ListView) findViewById(R.id.listViewMedications);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,medicationString);
		medicationList.setAdapter(adapter);		
	}	
	
	public void createMedication(View V){
		this.startActivity( new Intent(this,NewMedication.class));
	}
}

class MedicationObject {
	public static enum FrequencyLapse{
		DAY, WEEK, MONTH
	}
	
	String name;
	FrequencyLapse lapse; 
	int frequency; //e.g. take 'frequency' times a 'lapse'
	int dosage; //in milligrams
	
	//Constructor
	public MedicationObject(String name, FrequencyLapse lapse, int frequency, int dosage) {
		this.name = name;
		this.lapse = lapse;
		this.frequency = frequency;
		this.dosage = dosage;
	}
	
	//Accessors
	public String getName() { return this.name; }
	public FrequencyLapse getFrequencyLapse() { return this.lapse; }
	public int getFrequency() { return this.frequency; }
	public int getDosage() { return this.dosage; }
	public String frequencyToString(){ return this.frequency + "times a " + this.lapse; }
	
	//Mutators
	public void setName(String name) { this.name = name;  }	
	public void setFrequencyLapse(FrequencyLapse lapse) { this.lapse = lapse; }
	public void setFrequency(int frequency) { this.frequency = frequency; } 
	public void setDosage(int dosage) { this.dosage = dosage; }
}