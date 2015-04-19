package com.team9.healthmate.Medications;

import java.io.Serializable;
import java.util.Calendar;

/**
 * MedicationObject class is a struct-like class which holds all the
 * pertinent information about a single medication.
 * 
 * @author Guss
 */
public class MedicationObject implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Available frequency types to pick from
	 */
	public static enum FrequencyType {
		Hour, Day, Week
	}
	
	/**
	 * Available dosage types to pick from
	 */
	public static enum DosageType {
		mg, mL, cc;
	}
	
	public String name;
	public FrequencyType frequencyType; 
	public DosageType dosageType;
	public int frequencyValue;
	public int dosageValue;
	public boolean reminderStatus = false;
	public Calendar medicationCalendar;
	
	/**
	 * Class constructor
	 * @param name of the medication
	 * @param frequencyType of the medication
	 * @param frequencyValue of the medication
	 * @param dosageType of the medcation
	 * @param dosageValue of the medication
	 */
	public MedicationObject(String name, 
							FrequencyType frequencyType, 
							int frequencyValue, 
							DosageType dosageType, 
							int dosageValue,
							boolean reminderStatus,
							Calendar medicationCalendar) {
		this.name = name;
		this.frequencyType = frequencyType;
		this.frequencyValue = frequencyValue;
		this.dosageType = dosageType;
		this.dosageValue = dosageValue;
		this.reminderStatus = reminderStatus;
		this.medicationCalendar = medicationCalendar;				
	}
	
	/**
	 * Returns the medication information in a compact manner
	 * @return medication information
	 */
	public String getDescription() {
		return "Take "+frequencyValue+" every "+frequencyType.name()+" ("+
				dosageValue+dosageType.name()+") remider: "+reminderStatus;
	}
}