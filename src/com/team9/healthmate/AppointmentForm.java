package com.team9.healthmate;


import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.team9.healthmate.DataManager.DataStorageManager;
import com.team9.healthmate.NotificationsManager.NotificationsManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Activity Class that Displays a Form used to create Appointments. The
 * appointments are stored in the internal storage of the device when the user
 * selects the save button.
 * 
 * @author Michael Sandoval
 * 
 */
public class AppointmentForm extends Activity implements OnClickListener {

	// Button to save the Appointment
	ImageButton save;

	// The container of the time stamp that will be deleted
	Map<String, String> appointmentTimeStamp;

	// The container of the Appointment that will be saved
	Map<String, String> appointment;

	// Error message
	TextView incorrectInputMessage;

	// Error notifier
	boolean appointmentFormError = false;
	
	Calendar timeOfAppointment;

	/**
	 * Method that sets the listener for the save button and initializes the
	 * container for the appointment to be deleted.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_form);

		save = (ImageButton) findViewById(R.id.SaveAppointment);
		save.setOnClickListener((OnClickListener) this);

		appointmentTimeStamp = new HashMap<String, String>();

		// Check if the user is editing a previous appointment.
		checkIfInEditorMode();

	}

	/**
	 * Method that creates an intent to start the next activity. The next
	 * activity is the AppointmentList Activity.
	 */
	private void appointmentList() {
		Intent intent = new Intent(AppointmentForm.this, AppointmentsList.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
	
	/**
	 * Method that sends the user back to the appointment list
	 * if the back button is selected.
	 */
	@Override
	public void onBackPressed() {
		appointmentList();
	}

	/**
	 * Method that handles the event when the user clicks the save button. Input
	 * from the user is read and stored in the internal storage of the android
	 * device in a file named "appointments". There is error checking done for the
	 * user input.
	 */
	@Override
	public void onClick(View v) {
		try {

			// Create a new object to hold the information entered
			// by the user.
			appointment = new HashMap<String, String>();
			String input = "";
			appointmentFormError = false;

			// Go through all the input boxes, and store the information
			// as key/value pairs in the Map object.
			EditText userInput;

			userInput = (EditText) findViewById(R.id.AppointmentFormTitle);
			input = userInput.getText().toString();
			
			// Check if the user entered something in the input box.
			// If there is no input, notify the user that the this information
			// is required. Otherwise save the input given.
			if (input.equals("")) {
				
				// Create error message
				incorrectInputMessage = (TextView) findViewById(R.id.AppointmentFormTitleError);
				incorrectInputMessage.setText("A title is required to save Appointment");
				incorrectInputMessage.setTextColor(Color.RED);
				appointmentFormError = true;
				
			} else {
				
				// Remove error message
				incorrectInputMessage = (TextView) findViewById(R.id.AppointmentFormTitleError);
				incorrectInputMessage.setText("");
				appointment.put("title", input);
			}

			userInput = (EditText) findViewById(R.id.AppointmentFormName);
			input = userInput.getText().toString();
			
			// Check if the user entered something in the input box.
			// If there is no input, notify the user that the this information
			// is required. Otherwise save the input given.
			if (input.equals("")) {
				
				// Create error message
				incorrectInputMessage = (TextView) findViewById(R.id.AppointmentFormNameError);
				incorrectInputMessage.setText("A name is required to save Appointment");
				incorrectInputMessage.setTextColor(Color.RED);
				appointmentFormError = true;
				
			} else {
				
				// Remove error message
				incorrectInputMessage = (TextView) findViewById(R.id.AppointmentFormTitleError);
				incorrectInputMessage.setText("");
				appointment.put("name", userInput.getText().toString());
				
			}

			userInput = (EditText) findViewById(R.id.AppointmentFormAddress);
			appointment.put("location", userInput.getText().toString());

			userInput = (EditText) findViewById(R.id.AppointmentFormPhoneNumber);
			appointment.put("phone", userInput.getText().toString());

			userInput = (EditText) findViewById(R.id.AppointmentFormEmail);
			appointment.put("email", userInput.getText().toString());

			userInput = (EditText) findViewById(R.id.AppointmentFormComment);
			appointment.put("comment", userInput.getText().toString());

			// Call method to handle the storing of Date and Time
			// information entered by the user.
			saveDateAndTime();

			// Check if there is any error reported. If not, 
			// save information and delete the old appointment, if any.
			// Otherwise notify the user and do not proceed.
			if (appointmentFormError == false) {
				
				// Remove error message
				incorrectInputMessage = (TextView) findViewById(R.id.AppointmentFormError);
				incorrectInputMessage.setText("");
				
				// Create Notification
				Map<String, String> message = new HashMap<String, String>();
				String description = "Appointment with: " + appointment.get("name") + 
						"\n You have an appointment on " + appointment.get("date") + 
						"\n Staring at: " + appointment.get("start time");
				message.put("type", "appointments");
				message.put("title", appointment.get("title"));
				message.put("description", description);
				
				NotificationsManager.registerNotification(this, message, timeOfAppointment);
				
				// Check to see if the Appointment was being edited.
				if (appointmentTimeStamp.get("timestamp") != null) {

					// Delete the old Appointment from the file.
					DataStorageManager.deleteJSONObject(this, "appointments", appointmentTimeStamp);
				}
					// Save the new Appointment to the "appointments" file
					DataStorageManager.writeJSONObject(this, "appointments", appointment, false);

					// Call method to go to the Appointment List Activity.
					appointmentList();
			}  else {

				// Create error Message
				incorrectInputMessage = (TextView) findViewById(R.id.AppointmentFormError);
				incorrectInputMessage.setText("There are errors in the information given, please check input");
				incorrectInputMessage.setTextColor(Color.RED);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to check if the user is editing a previous appointment. If the
	 * user is editing an appointment, load the information into the form.
	 * 
	 * @author Michael Sandoval
	 */
	private void checkIfInEditorMode() {

		// Get the intent that activated this Activity
		Intent intent = getIntent();

		// Check if information has been sent.
		if (intent.getStringExtra("timestamp") != null) {
			EditText editInput;
			TimePicker timePicker;
			DatePicker datePicker;
			String[] date;
			String[] time;

			// Load the time stamp into a Map Object that will
			// be used to delete the old appointment settings.
			appointmentTimeStamp.put("timestamp", intent.getStringExtra("timestamp"));

			// Fill the Form Input boxes with the previous user inputs
			// Get the id of each text box, and set their values.
			editInput = (EditText) findViewById(R.id.AppointmentFormTitle);
			editInput.setText(intent.getStringExtra("title"));

			editInput = (EditText) findViewById(R.id.AppointmentFormName);
			editInput.setText(intent.getStringExtra("name"));

			editInput = (EditText) findViewById(R.id.AppointmentFormAddress);
			editInput.setText(intent.getStringExtra("location"));

			editInput = (EditText) findViewById(R.id.AppointmentFormPhoneNumber);
			editInput.setText(intent.getStringExtra("phone"));

			editInput = (EditText) findViewById(R.id.AppointmentFormEmail);
			editInput.setText(intent.getStringExtra("email"));

			editInput = (EditText) findViewById(R.id.AppointmentFormComment);
			editInput.setText(intent.getStringExtra("comment"));

			// Get the reference from the Date Picker of the form
			datePicker = (DatePicker) findViewById(R.id.AppointmentFormDate);

			// Parse the text Date information sent by the intent.
			date = intent.getStringExtra("date").split("-");

			// Set the day, month, year for the previous date entered.
			int year = Integer.parseInt(date[2]);
			int month = Integer.parseInt(date[0]) - 1;
			int dayOfMonth = Integer.parseInt(date[1]);
			datePicker.init(year, month, dayOfMonth, null);

			// Containers for the previous entered hour and minute
			int setHour;
			int setMinute;

			// Get the reference from the Start Time Time Picker of the form
			timePicker = (TimePicker) findViewById(R.id.AppointmentFormStartTime);

			// Parse the text Time information sent by the intent.
			time = intent.getStringExtra("start time").split(":");
			setHour = Integer.parseInt(time[0]);
			setMinute = Integer.parseInt(time[1].substring(0, 2));

			// Check to see if the time of day
			if (time[1].substring(2, 4).equals("pm")) {
				setHour = setHour + 12;
			}

			// Set the time that was previously entered.
			timePicker.setCurrentHour(setHour);
			timePicker.setCurrentMinute(setMinute);

			// Get the reference from the End Time Time Picker of the form
			timePicker = (TimePicker) findViewById(R.id.AppointmentFormEndTime);

			// Parse the text Time information sent by the intent.
			time = intent.getStringExtra("end time").split(":");
			setHour = Integer.parseInt(time[0]);
			setMinute = Integer.parseInt(time[1].substring(0, 2));

			// Check to see if the time of day
			if (time[1].substring(2, 4).equals("pm")) {
				setHour = setHour + 12;
			}

			// Set the time that was previously entered.
			timePicker.setCurrentHour(setHour);
			timePicker.setCurrentMinute(setMinute);
		}
	}

	/**
	 * Method to save the date and time of the user input. It formats the date
	 * and time into strings in order to store them in a file. There is error checking
	 * for the time and date. The start time has to be before the end time, the date
	 * has to be after the current date of the system.
	 */
	private void saveDateAndTime() {
		
		// Date Formatter to handle the formatting for the date and time
		SimpleDateFormat simpleDateFormat;
		
		// start time and end time are assumed to be before noon
		String startTimeOfDay = "am";
		String endTimeOfDay = "am";
		
		// Get an instance of the Calendar to set the time of the appointment
		timeOfAppointment = Calendar.getInstance();
		
		try {
			
			// Time Format of the start and end time
			simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
			
			// Get the start and end time hour and minutes
			TimePicker startTimePicker = (TimePicker) findViewById(R.id.AppointmentFormStartTime);
			startTimePicker.clearFocus();
			int startHour = startTimePicker.getCurrentHour();
			int startMinutes = startTimePicker.getCurrentMinute();
			//int scheduleHour = startHour;
			//int schduleMinute = startMinutes;
			
			TimePicker endTimePicker = (TimePicker) findViewById(R.id.AppointmentFormEndTime);
			endTimePicker.clearFocus();
			int endHour = endTimePicker.getCurrentHour();
			int endMinutes = endTimePicker.getCurrentMinute();
			
			// Format the start and time based on simpleDateFormat
			// and create dates for comparing.
			Date startTime = simpleDateFormat.parse(startHour + ":" + startMinutes);
			Date endTime = simpleDateFormat.parse(endHour + ":" + endMinutes);
			
			// If the end time is before the start time, the notify the user about
			// the error and report error.
			if (endTime.before(startTime)) {
				
				// Create error message
				incorrectInputMessage = (TextView) findViewById(R.id.AppointmentFormTimeError);
				incorrectInputMessage.setText("Start Time before End Time, please check input");
				incorrectInputMessage.setTextColor(Color.RED);
				appointmentFormError = true;
				
			} else {
				// If there is no error, set the time of the appointment, and then change the times 
				// to a specified format, and save start and end time.
				
				// Set the time of appointment
				timeOfAppointment.set(Calendar.HOUR_OF_DAY, startHour);
				timeOfAppointment.set(Calendar.MINUTE, startMinutes);
				
				// Go through the time and convert from 24 hours to 12 hour time
				if (startHour > 12) {
					startHour = startHour - 12;
					startTimeOfDay = "pm";
				}

				if (startHour == 0) {
					startHour = startHour + 12;
				}

				// Add 0 to the minutes string if it is less than 10, representation preference.
				// Add to the information that should be stored.
				if (startMinutes < 10) {
					appointment.put("start time", startHour + ":0" + startMinutes + startTimeOfDay);
				} else {
					appointment.put("start time", startHour + ":" + startMinutes + startTimeOfDay);
				}
				
				// Go through the time and convert from 24 hours to 12 hour time
				if (endHour > 12) {
					endHour = endHour - 12;
					endTimeOfDay = "pm";
				}

				if (endHour == 0) {
					endHour = endHour + 12;
				}

				// Add 0 to the minutes string if it is less than 10, representation preference.
				// Add to the information that should be stored.
				if (endMinutes < 10) {
					appointment.put("end time", endHour + ":0" + endMinutes + endTimeOfDay);
				} else {
					appointment.put("end time", endHour + ":" + endMinutes + endTimeOfDay);
				}
				
				// Remove error message.
				incorrectInputMessage = (TextView) findViewById(R.id.AppointmentFormTimeError);
				incorrectInputMessage.setText("");
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		

		// Get the reference to Date.
		DatePicker datePicker = (DatePicker) findViewById(R.id.AppointmentFormDate);
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth() + 1;
		int year = datePicker.getYear();
		
		try {
			
			// Specify the Date's Format
			simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
			
			// Get the current date of the system
			Calendar currentDate = Calendar.getInstance(Locale.US);
		
			int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
			int currentMonth = currentDate.get(Calendar.MONTH);
			int currentYear = currentDate.get(Calendar.YEAR);
			
			// Create dates with the specified format
			Date userDate = simpleDateFormat.parse(month + "-" + day + "-" + year);
			Date systemDate = simpleDateFormat.parse(currentMonth + "-" + currentDay + "-" + currentYear);
			
			// Check if the date the user entered is before the current date
			// on the system. This is considered an invalid date, if so, let
			// the user know through error message.
			if (userDate.before(systemDate)) {
				
				// Create error message
				incorrectInputMessage = (TextView) findViewById(R.id.AppointmentFormDateError);
				incorrectInputMessage.setText("Date entered has already passed");
				incorrectInputMessage.setTextColor(Color.RED);
				appointmentFormError = true;
				
			} else {
				
				// Remove the error message and save the date
				incorrectInputMessage = (TextView) findViewById(R.id.AppointmentFormDateError);
				incorrectInputMessage.setText("");
				
				// Set the date of appointment
				timeOfAppointment.set(Calendar.YEAR, year);
				timeOfAppointment.set(Calendar.MONTH, month - 1);
				timeOfAppointment.set(Calendar.DAY_OF_MONTH, day);
				
				
				// Set the format text it will be saved in.
				String formatedDate = month + "-" + day + "-" + year;
				
				// Add to the information that should be stored.
				appointment.put("date", formatedDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
