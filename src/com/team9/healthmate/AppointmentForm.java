package com.team9.healthmate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class AppointmentForm extends Activity implements OnClickListener {
	
	public Button save;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_form);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		save = (Button) findViewById(R.id.SaveAppointment);
		save.setOnClickListener((OnClickListener) this);
	}
	
	public void appointmentList()	{
		Intent intent = new Intent(AppointmentForm.this, AppointmentsList.class);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		
		try {
		Map<String, String> appointment = new HashMap<String, String>();
		EditText userInput;
		userInput =(EditText) findViewById(R.id.AppointmentFormName);
		appointment.put("name", userInput.getText().toString());
		
		userInput =(EditText) findViewById(R.id.AppointmentFormAddress);
		appointment.put("location", userInput.getText().toString());
		
		userInput =(EditText) findViewById(R.id.AppointmentFormPhoneNumber);
		appointment.put("phone", userInput.getText().toString());
		
		userInput =(EditText) findViewById(R.id.AppointmentFormEmail);
		appointment.put("email", userInput.getText().toString());
		
		TimePicker timePicker =(TimePicker) findViewById(R.id.AppointmentFormTime);
		timePicker.clearFocus();
		int minutes = timePicker.getCurrentMinute();
		int hour = timePicker.getCurrentHour();
		appointment.put("time", hour + ":" + minutes);
		
		DatePicker datePicker =(DatePicker) findViewById(R.id.AppointmentFormDate);
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		String formatedDate = simpleDateFormat.format(new Date(year, month, day));
		appointment.put("date", formatedDate);
		
		userInput =(EditText) findViewById(R.id.AppointmentFormComment);
		appointment.put("comment", userInput.getText().toString());
		
		DataStorageManager.writeJSONObject(this, "appointments", appointment, false);
		appointmentList();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

}
