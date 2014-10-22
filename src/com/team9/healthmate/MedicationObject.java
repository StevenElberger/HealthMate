package com.team9.healthmate;

import java.io.Serializable;

public class MedicationObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public static enum FrequencyType {
		Day, Week, Month
	}
	
	String name;
	FrequencyType frequencyType; 
	int frequencyValue; //e.g. take 'frequency' times a 'lapse'
	int ammount; //in milligrams
	boolean reminderStatus = false;
	
	//Constructor
	public MedicationObject(String name, FrequencyType frequencyType, int frequencyValue, int ammount) {
		this.name = name;
		this.frequencyType = frequencyType;
		this.frequencyValue = frequencyValue;
		this.ammount = ammount;
	}
	
	public String getDescription() {
		return "Take "+frequencyValue+" every "+frequencyType.name()+" ("+ammount+"mg)";
	}
	
	public void PrintMedication() {
		Medication.Debug("Name = "+name);
		Medication.Debug("FrequencyType = "+frequencyType);
		Medication.Debug("FrequencyValue = "+frequencyValue);
	}
}