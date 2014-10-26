package com.team9.healthmate;

import java.util.HashMap;
import java.util.Map;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

public class AppointmentForm extends Activity implements OnClickListener {
	
	public ImageButton save;
	Map<String, String> appointmentDetails = new HashMap<String, String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_form);
		
		Intent intent = getIntent();
		
		save = (ImageButton) findViewById(R.id.SaveAppointment);
		save.setOnClickListener((OnClickListener) this);
		
		if (intent.getStringExtra("timestamp") != null)
		{
			EditText editInput;
			
			appointmentDetails.put("timestamp", intent.getStringExtra("timestamp"));
			
			appointmentDetails.put("title", intent.getStringExtra("title"));
			editInput = (EditText)findViewById(R.id.AppointmentFormTitle);
			editInput.setText(intent.getStringExtra("title"));
			
			appointmentDetails.put("name", intent.getStringExtra("name"));
			editInput = (EditText)findViewById(R.id.AppointmentFormName);
			editInput.setText(intent.getStringExtra("name"));
			
			appointmentDetails.put("location", intent.getStringExtra("location"));
			editInput = (EditText)findViewById(R.id.AppointmentFormAddress);
			editInput.setText(intent.getStringExtra("location"));
			
			appointmentDetails.put("phone", intent.getStringExtra("phone"));
			editInput = (EditText)findViewById(R.id.AppointmentFormPhoneNumber);
			editInput.setText(intent.getStringExtra("phone"));
			
			appointmentDetails.put("email", intent.getStringExtra("email"));
			editInput = (EditText)findViewById(R.id.AppointmentFormEmail);
			editInput.setText(intent.getStringExtra("email"));
			
			/*appointmentDetails.put("date", intent.getStringExtra("date"));
			editInput = (EditText)findViewById(R.id.AppointmentFormDate);
			editInput.setText(intent.getStringExtra("date"));
			
			appointmentDetails.put("start time", intent.getStringExtra("start time"));
			editInput = (EditText)findViewById(R.id.AppointmentFormStartTime);
			editInput.setText(intent.getStringExtra("start time"));
			
			appointmentDetails.put("end time", intent.getStringExtra("end time"));
			editInput = (EditText)findViewById(R.id.AppointmentFormEndTime);
			editInput.setText(intent.getStringExtra("end time"));*/
			
			appointmentDetails.put("comment", intent.getStringExtra("comment"));
			editInput = (EditText)findViewById(R.id.AppointmentFormEndTime);
			editInput.setText(intent.getStringExtra("comment"));
		}
		
	}
	
	public void appointmentList()	{
		Intent intent = new Intent(AppointmentForm.this, AppointmentsList.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onClick(View v) {
		
		try {
			
		Log.w("ImageButton", "Lister Active");
		Map<String, String> appointment = new HashMap<String, String>();
		EditText userInput;
		
		userInput =(EditText) findViewById(R.id.AppointmentFormTitle);
		appointment.put("title", userInput.getText().toString());
		
		userInput =(EditText) findViewById(R.id.AppointmentFormName);
		appointment.put("name", userInput.getText().toString());
		
		userInput =(EditText) findViewById(R.id.AppointmentFormAddress);
		appointment.put("location", userInput.getText().toString());
		
		userInput =(EditText) findViewById(R.id.AppointmentFormPhoneNumber);
		appointment.put("phone", userInput.getText().toString());
		
		userInput =(EditText) findViewById(R.id.AppointmentFormEmail);
		appointment.put("email", userInput.getText().toString());
		
		userInput =(EditText) findViewById(R.id.AppointmentFormComment);
		appointment.put("comment", userInput.getText().toString());
		
		TimePicker timePicker =(TimePicker) findViewById(R.id.AppointmentFormStartTime);
		timePicker.clearFocus();
		String timeOfDay = "am";
		int minutes = timePicker.getCurrentMinute();
		int hour = timePicker.getCurrentHour();
		
		if (hour > 12) {
			hour = hour - 12;
			timeOfDay = "pm";
		}
		
		if (minutes < 10){
			appointment.put("start time", hour + ":0" + minutes + timeOfDay);
		}
		else {
			appointment.put("start time", hour + ":" + minutes + timeOfDay);
		}
		
		timePicker =(TimePicker) findViewById(R.id.AppointmentFormEndTime);
		timePicker.clearFocus();
		timeOfDay = "am";
		minutes = timePicker.getCurrentMinute();
		hour = timePicker.getCurrentHour();
		
		if (hour > 12) {
			hour = hour - 12;
			timeOfDay = "pm";
		}
		if (minutes < 10){
			appointment.put("end time", hour + ":0" + minutes + timeOfDay);
		}
		else {
			appointment.put("end time", hour + ":" + minutes + timeOfDay);
		}
		
		DatePicker datePicker =(DatePicker) findViewById(R.id.AppointmentFormDate);
		int day = datePicker.getDayOfMonth() + 1;
		int month = datePicker.getMonth();
		int year = datePicker.getYear();
		String formatedDate = month + "-" + day + "-" + year;
		appointment.put("date", formatedDate);
		
		if (appointmentDetails.get("timestamp") != null) {
			DataStorageManager.deleteJSONObject(this, "appointments", appointmentDetails);
		}
		
		DataStorageManager.writeJSONObject(this, "appointments", appointment, false);
		
		appointmentList();
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

}
