package com.team9.healthmate.Medications;

import java.io.Serializable;

public class MedicationObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public static enum FrequencyType {
		Hour, Day, Week, Month
	}
	
	public static enum DosageType {
		mg, mL, cc;
	}
	
	public String name;
	public FrequencyType frequencyType; 
	public DosageType dosageType;
	public int frequencyValue;
	public int dosageValue;
	boolean reminderStatus = false;
	
	//Constructor
	public MedicationObject(String name, 
							FrequencyType frequencyType, 
							int frequencyValue, 
							DosageType dosageType, 
							int dosageValue) {
		this.name = name;
		this.frequencyType = frequencyType;
		this.frequencyValue = frequencyValue;
		this.dosageType = dosageType;
		this.dosageValue = dosageValue;
	}
	
	public String getDescription() {
		return "Take "+frequencyValue+" every "+frequencyType.name()+" ("+
				dosageValue+dosageType.name()+")";
	}
}