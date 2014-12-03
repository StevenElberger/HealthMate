package com.team9.healthmate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.text.format.Time;
import android.widget.Chronometer;

public class StepCounterData {
	private String time;
	private int TOTAL_STEPS;
	
	public StepCounterData(int steps)	{
		TOTAL_STEPS = steps;
	}
	public StepCounterData(int steps, String t)	{
		TOTAL_STEPS = steps;
		time = t;
	}
	
	public void addStep()	{
		TOTAL_STEPS++;
	}
	
	private void setSteps(int value)	{
		TOTAL_STEPS = value;
	}
	private void setTime(String t)	{
		time = t;
	}
	private String getTime()	{
		return time;
	}
	
	public Map<String, String> getKeyMay()	{
		Map<String, String> tmp = new HashMap<String, String>();
		
		tmp.put("Steps", Integer.toString(TOTAL_STEPS));
		
		tmp.put("Time", time);
		
		return tmp;
	}
	private void fetchData(Map<String, String> data)	{
		
		for(int i = 0; i < data.size(); i++)	{
			if(data.containsKey("Steps"))	{
				TOTAL_STEPS = Integer.parseInt(data.get("Steps"));
			}
			else if(data.containsKey("Time"))	{
				time = data.get("Time");
			}
		}
		
	}

}

