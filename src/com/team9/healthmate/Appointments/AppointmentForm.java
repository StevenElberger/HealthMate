package com.team9.healthmate.Appointments;


import java.util.Calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.team9.healthmate.JsonManager.JSONParser;
import com.team9.healthmate.R;
import com.team9.healthmate.DataManager.DataStorageManager;
import com.team9.healthmate.NotificationsManager.NotificationsManager;
import com.team9.healthmate.ServerManager.ServerStorageManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Activity Class that Displays a Form used to create Appointments. The
 * appointments are stored in the internal storage of the device when the user
 * selects the save button.
 * 
 * @author Michael Sandoval
 * 
 */
public class AppointmentForm extends Activity {
	
	 // Progress Dialog
    //private ProgressDialog pDialog;
    
    JSONParser jsonParser = new JSONParser();
    
    JSONObject json;
    
    // Save time for server
    String timestamp;
    
    // Save the new time stamp for updating
    String newtimestamp;
    
    // Flag to identify whether the task is updating or creating
    // to server
    Boolean editOrCreate = false;
    
 // JSON Node names
    private static final String TAG_SUCCESS = "success";
    
    // url to create new appointment
    //private static String url_create_appointment = "http://10.0.2.2:8080/android_connect/Send_to_Steve/create_appointment.php";
    private static String url_create_appointment = "http://108.77.76.162/HealthMateIntegration/create_appointment2.php";
    
 // url to update an appointment
    //private static String url_update_appointment = "http://10.0.2.2:8080/android_connect/Send_to_Steve/update_appointment.php";
    private static String url_update_appointment = "http://108.77.76.162/HealthMateIntegration/update_appointment.php";

	// The container of the time stamp that will be deleted
	Map<String, String> appointmentTimeStamp;
	
	// Appointment to be deleted
	Map<String, String> appointmentToDelete;
	
	// The container of the Appointment that will be saved
	Map<String, String> appointment;

	// Error message
	TextView incorrectInputMessage;

	// Error notifier
	boolean appointmentFormError = false;
	
	// The time the notification will be registered with
	Calendar timeOfAppointment;
	
	//Server side time and date
	String serverStartTime, serverEndTime, serverDate;

	/**
	 * Method that sets the listener for the save button and initializes the
	 * container for the appointment to be deleted.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_form);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Initialize map objects
		appointmentTimeStamp = new HashMap<String, String>();
		appointmentToDelete = new HashMap<String, String>();

		// Check if the user is editing a previous appointment.
		checkIfInEditorMode();

	}
	
	/**
	 * Method sets up the options available to the user.
	 * An icon of an addition sign is displayed to the user as an option. 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appointment_form, menu);
		return true;
	}
	
	/**
	 * Method to activate the user options in the Action Bar.
	 * The only option is to save an appointment, this leads
	 * to the appointment list class.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Check to see which option was selected by the user.
		if (item.getItemId() == R.id.action_save_appointment) {
			saveAppointment();
			//saveAppointmentOnServer();
			createOrupdate();
			
		}
		
		return super.onOptionsItemSelected(item);
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
	public void saveAppointment() {
		try {

			// Create a new object to hold the information entered
			// by the user.
			appointment = new HashMap<String, String>();
			ArrayList<Map<String, String>> appointmentList = new ArrayList<Map<String, String>>();
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
				incorrectInputMessage = (TextView) findViewById(R.id.AppointmentFormNameError);
				incorrectInputMessage.setText("");
				appointment.put("name", userInput.getText().toString());
				
			}
			
			userInput = (EditText) findViewById(R.id.AppointmentFormLastName);
			appointment.put("lastname", userInput.getText().toString());
			
			userInput = (EditText) findViewById(R.id.AppointmentFormId);
			appointment.put("doctorid", userInput.getText().toString());

			userInput = (EditText) findViewById(R.id.AppointmentFormAddress);
			appointment.put("location", userInput.getText().toString());
			
			userInput = (EditText) findViewById(R.id.AppointmentFormCity);
			appointment.put("city", userInput.getText().toString());
			
			userInput = (EditText) findViewById(R.id.AppointmentFormZip);
			appointment.put("zip", userInput.getText().toString());
			
			userInput = (EditText) findViewById(R.id.AppointmentFormState);
			appointment.put("state", userInput.getText().toString());

			userInput = (EditText) findViewById(R.id.AppointmentFormPhoneNumber);
			appointment.put("phone", userInput.getText().toString());

			userInput = (EditText) findViewById(R.id.AppointmentFormEmail);
			appointment.put("email", userInput.getText().toString());

			userInput = (EditText) findViewById(R.id.AppointmentFormComment);
			appointment.put("comment", userInput.getText().toString());

			// Call method to handle the storing of Date and Time
			// information entered by the user.
			saveDateAndTime();
			
			/////////////////////////////////////////////////////////////////////////////////////////////

			// Check if there is any error reported. If not, 
			// save information and delete the old appointment, if any.
			// Otherwise notify the user and do not proceed.
			if (appointmentFormError == false) {
				
				// Remove error message
				incorrectInputMessage = (TextView) findViewById(R.id.AppointmentFormError);
				incorrectInputMessage.setText("");
				String description = "";
				
				
				// Check to see if the Appointment was being edited.
				if (appointmentTimeStamp.get("timestamp") != null) {
					
					// save for updating the row
					timestamp = appointmentTimeStamp.get("timestamp");
					
					// Container for alarm information having to do with notification generation
					ArrayList<Map<String, String>> registeredNotifications;
					

					// Delete the old Appointment from the file.
					DataStorageManager.deleteJSONObject(this, "appointments", appointmentTimeStamp);
					
					// read the registered notifications for single alarms
					registeredNotifications = DataStorageManager.readJSONObject(this, "single alarms");
					
					// Go through the registered alarms and remove the alarms associated with
					// the specified appointment if any
					for (Map<String, String> map : registeredNotifications) {
						if (appointmentTimeStamp.get("timestamp").equals(map.get("application timestamp"))) {
							DataStorageManager.deleteJSONObject(this, "single alarms", map);
						}
					}
					
					// Create the notification message that will be used to delete a notification
					// with the same information
					Map<String, String> deletionMessage = new HashMap<String, String>();
					description = "Appointment with: " + appointmentToDelete.get("name") + 
							"\n You have an appointment on " + appointmentToDelete.get("date") + 
							"\n Staring at: " + appointmentToDelete.get("start time");
					deletionMessage.put("type", "appointments");
					deletionMessage.put("title", appointmentToDelete.get("title"));
					deletionMessage.put("description", description);
					
					// unregister the notification with the given message
					NotificationsManager.unregisterNotification(this, deletionMessage);
				}
					// Save the new Appointment to the "appointments" file
					DataStorageManager.writeJSONObject(this, "appointments", appointment, false);					
					
					// verify if in edit mode
					if (editOrCreate){
						//retrieve the last saved data with the new timestamp value, for updating the data
						newtimestamp = appointment.get("timestamp").toString();
					}			
					else
						//save the timestamp for server database save, when creating
						timestamp = appointment.get("timestamp").toString();
						
					
					// read the list of appointments in the updated file
					appointmentList = DataStorageManager.readJSONObject(this, "appointments");
					
								
					
					// Create Notification
					Map<String, String> message = new HashMap<String, String>();
					description = "Appointment with: " + appointment.get("name") + 
							"\n You have an appointment on " + appointment.get("date") + 
							"\n Staring at: " + appointment.get("start time");
					
					message.putAll(appointment);
					message.put("type", "appointments");
					message.put("description", description);
					
					// Find when to set the Notification time of the Appointment
					determineTimeOfNotification();
					
					// get the time stamp of the newly created appointment and add it to the message being sent
					message.put("timestamp", appointmentList.get(appointmentList.size()-1).get("timestamp"));
					NotificationsManager.registerNotification(this, message, timeOfAppointment);

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
	
	/** Method will save the user entered data on server side.
	 * @author Davit Avetikyan
	 * */
//	public void saveAppointmentOnServer(){
//	new CreateNewAppointment().execute();
//	
//	}
	
	/**
	 * Method to determine when the notification should be set for a saved appointment.
	 * The notification would be set to go off either at the start of the day when the
	 * appointment is set. Or it would be set an hour before the appointment, if the appointment
	 * is set on be on the same day in which it was created.
	 * @author Michael Sandoval
	 */
	private void determineTimeOfNotification() {
		
		// Get the current date of the system
		Calendar currentDate = Calendar.getInstance(Locale.US);
		
		
		// The desired time of the notification
		// is a the day of the appointment
		Calendar desiredNotificationDate = Calendar.getInstance(Locale.US);
		
		// Set the desired notification time to be at the start of the day when the appointment occurs
		desiredNotificationDate.set(timeOfAppointment.get(Calendar.YEAR), 
				timeOfAppointment.get(Calendar.MONTH), timeOfAppointment.get(Calendar.DAY_OF_MONTH));
		
		// Set the time of the notification to go off at 12am on the appointment date.
		desiredNotificationDate.set(Calendar.HOUR_OF_DAY, 0);
		desiredNotificationDate.set(Calendar.MINUTE, 0);
		desiredNotificationDate.set(Calendar.SECOND, 0);
		
		// If the appointment is set on a date, then set the notification to be at the start of the notification date.
		// Otherwise set the notification to be an hour before the appointments time.
		if (!desiredNotificationDate.before(currentDate)) {
			timeOfAppointment = desiredNotificationDate;
		}
		else {
			timeOfAppointment.set(Calendar.HOUR_OF_DAY, timeOfAppointment.get(Calendar.HOUR_OF_DAY) - 1);
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
			appointmentToDelete.put("title", intent.getStringExtra("title"));

			editInput = (EditText) findViewById(R.id.AppointmentFormName);
			editInput.setText(intent.getStringExtra("name"));
			appointmentToDelete.put("name", intent.getStringExtra("name"));
			
			editInput = (EditText) findViewById(R.id.AppointmentFormLastName);
			editInput.setText(intent.getStringExtra("lastname"));
			appointmentToDelete.put("lastname", intent.getStringExtra("lastname"));
			
			editInput = (EditText) findViewById(R.id.AppointmentFormId);
			editInput.setText(intent.getStringExtra("doctorid"));
			appointmentToDelete.put("doctorid", intent.getStringExtra("doctorid"));

			editInput = (EditText) findViewById(R.id.AppointmentFormAddress);
			editInput.setText(intent.getStringExtra("location"));
			appointmentToDelete.put("location", intent.getStringExtra("location"));
			
			editInput = (EditText) findViewById(R.id.AppointmentFormCity);
			editInput.setText(intent.getStringExtra("city"));
			appointmentToDelete.put("city", intent.getStringExtra("city"));
			
			editInput = (EditText) findViewById(R.id.AppointmentFormZip);
			editInput.setText(intent.getStringExtra("zip"));
			appointmentToDelete.put("zip", intent.getStringExtra("zip"));
			
			editInput = (EditText) findViewById(R.id.AppointmentFormState);
			editInput.setText(intent.getStringExtra("state"));
			appointmentToDelete.put("state", intent.getStringExtra("state"));

			editInput = (EditText) findViewById(R.id.AppointmentFormPhoneNumber);
			editInput.setText(intent.getStringExtra("phone"));
			appointmentToDelete.put("phone", intent.getStringExtra("phone"));

			editInput = (EditText) findViewById(R.id.AppointmentFormEmail);
			editInput.setText(intent.getStringExtra("email"));
			appointmentToDelete.put("email", intent.getStringExtra("email"));

			editInput = (EditText) findViewById(R.id.AppointmentFormComment);
			editInput.setText(intent.getStringExtra("comment"));
			appointmentToDelete.put("comment", intent.getStringExtra("comment"));

			// Get the reference from the Date Picker of the form
			datePicker = (DatePicker) findViewById(R.id.AppointmentFormDate);

			// Parse the text Date information sent by the intent.
			date = intent.getStringExtra("date").split("-");
			
			appointmentToDelete.put("date", intent.getStringExtra("date"));

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
			appointmentToDelete.put("start time", intent.getStringExtra("start time"));
			
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
			appointmentToDelete.put("end time", intent.getStringExtra("end time"));
			
			setHour = Integer.parseInt(time[0]);
			setMinute = Integer.parseInt(time[1].substring(0, 2));

			// Check to see if the time of day
			if (time[1].substring(2, 4).equals("pm")) {
				setHour = setHour + 12;
			}

			// Set the time that was previously entered.
			timePicker.setCurrentHour(setHour);
			timePicker.setCurrentMinute(setMinute);
			
			// Set it to true to imply it's in edit mode
			editOrCreate = true;
		}
		else
			editOrCreate = false;
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
				timeOfAppointment.set(Calendar.SECOND, 0);
				
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
					
					//This is to save to server side
					serverStartTime = startHour + ":0" + startMinutes + startTimeOfDay;
				} else {
					appointment.put("start time", startHour + ":" + startMinutes + startTimeOfDay);
					
					serverStartTime = startHour + ":" + startMinutes + startTimeOfDay;
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
					
					//This is to save to server side
					serverEndTime = endHour + ":0" + endMinutes + endTimeOfDay;
				} else {					
					appointment.put("end time", endHour + ":" + endMinutes + endTimeOfDay);
					
					serverEndTime = endHour + ":" + endMinutes + endTimeOfDay;
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
				//This is to pass on 
				serverDate = formatedDate;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
     * Background Async Task to Create or to Update appointment
     * */
	public void createOrupdate(){
		
		Map<String, String> paramUpdateCreate = new HashMap<String, String>();
		
		EditText userInput;
    	
    	try{
    	//assign the field data
    	userInput = (EditText) findViewById(R.id.AppointmentFormId);			
   		 String drId  = userInput.getText().toString();
   		 paramUpdateCreate.put("doctor_id", drId);  		    		
    		
		 userInput = (EditText) findViewById(R.id.AppointmentFormTitle);			
		 String title = userInput.getText().toString();
		 paramUpdateCreate.put("title", title);	
		
		 userInput = (EditText) findViewById(R.id.AppointmentFormName);
		 String name = userInput.getText().toString();
		 paramUpdateCreate.put("first_name", name);
		 
		 userInput = (EditText) findViewById(R.id.AppointmentFormLastName);
		 String lastname = userInput.getText().toString();
		 paramUpdateCreate.put("last_name", lastname);
    	
    	 userInput = (EditText) findViewById(R.id.AppointmentFormAddress);
    	 String location= userInput.getText().toString();
    	 paramUpdateCreate.put("address", location);
    	 
    	 userInput = (EditText) findViewById(R.id.AppointmentFormCity);
    	 String city= userInput.getText().toString();
    	 paramUpdateCreate.put("city", city);
    	 
    	 userInput = (EditText) findViewById(R.id.AppointmentFormZip);
    	 String zip= userInput.getText().toString();
    	 paramUpdateCreate.put("zip", zip);
    	 
    	 userInput = (EditText) findViewById(R.id.AppointmentFormState);
    	 String state= userInput.getText().toString();
    	 paramUpdateCreate.put("state", state);

		 userInput = (EditText) findViewById(R.id.AppointmentFormPhoneNumber);
		 String phone = userInput.getText().toString();
		 paramUpdateCreate.put("phone", phone);

		 userInput = (EditText) findViewById(R.id.AppointmentFormEmail);
		 String email = userInput.getText().toString();
		 paramUpdateCreate.put("email", email);

		 userInput = (EditText) findViewById(R.id.AppointmentFormComment);
		 String comment = userInput.getText().toString();    
		 paramUpdateCreate.put("comment", comment);        

		 paramUpdateCreate.put("start", serverStartTime);
		 paramUpdateCreate.put("end", serverEndTime);
		 paramUpdateCreate.put("date", serverDate);
		 paramUpdateCreate.put("timestamp", timestamp);
		 
		 Log.d("editorcreate", editOrCreate.toString());
        // add only if editing
        if (editOrCreate){
        	paramUpdateCreate.put("newtimestamp", newtimestamp);        
        	new ServerStorageManager("CreateorUpdate", url_update_appointment, paramUpdateCreate, this);
        }      
        else
        	new ServerStorageManager("CreateorUpdate", url_create_appointment, paramUpdateCreate, this);        
    	}
    	catch (Exception ex){
    		ex.printStackTrace();
    	}           
		
	}
	
	/**
     * Background Async Task to Create or to Update appointment
     * */
//    class CreateNewAppointment extends AsyncTask<String, String, String> {
// 
//        /**
//         * Before starting background thread Show Progress Dialog
//         * */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();           
//        }
// 
//        /**
//         * Creating an appointment
//         * */
//        protected String doInBackground(String... args) {
//        	
//        	EditText userInput;
//        	
//        	try{
//        	//assign the field data
//			userInput = (EditText) findViewById(R.id.AppointmentFormTitle);			
//			String title = userInput.getText().toString();			
//			
//			userInput = (EditText) findViewById(R.id.AppointmentFormName);
//			 String name = userInput.getText().toString();			
//        	
//        	userInput = (EditText) findViewById(R.id.AppointmentFormAddress);
//        	 String location= userInput.getText().toString();
//
//			userInput = (EditText) findViewById(R.id.AppointmentFormPhoneNumber);
//			 String phone = userInput.getText().toString();
//
//			userInput = (EditText) findViewById(R.id.AppointmentFormEmail);
//			String email = userInput.getText().toString();
//
//			userInput = (EditText) findViewById(R.id.AppointmentFormComment);
//			String comment = userInput.getText().toString();         
//            
// 
//            // Building Parameters
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("title", title));
//            params.add(new BasicNameValuePair("name", name));
//            params.add(new BasicNameValuePair("location", location));
//            params.add(new BasicNameValuePair("phone", phone));
//            params.add(new BasicNameValuePair("email", email));
//            params.add(new BasicNameValuePair("comment", comment));
//            params.add(new BasicNameValuePair("start_time", serverStartTime));
//            params.add(new BasicNameValuePair("end_time", serverEndTime));
//            params.add(new BasicNameValuePair("date", serverDate));
//            params.add(new BasicNameValuePair("timestamp", timestamp));
//            // add only if editing
//            if (editOrCreate){
//             	params.add(new BasicNameValuePair("newtimestamp", newtimestamp));            
//              	// getting JSON Object
//              	// Note that create product url accepts POST method
//              	json = jsonParser.makeHttpRequest(url_update_appointment,
//                    "POST", params);           
//            }
//            else{
//            	json = jsonParser.makeHttpRequest(url_create_appointment,
//                    "POST", params);
//            }
// 
//            // check log cat fro response
//            Log.d("Create Response", json.toString());
//        	}
//        	catch (Exception ex){
//        		ex.printStackTrace();
//        	}           
// 
//           return null;
//        }
// 
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        protected void onPostExecute(String file_url) {        	
// 
//            try {
//                int success  = json.getInt(TAG_SUCCESS);
//            	// successful to create an appointment
//                if (success == 1) {                   
//                	Toast.makeText(getApplicationContext(), "Data was Saved on the Server Successfully! " , Toast.LENGTH_LONG).show();
//                } else {
//                    // failed to create an appointment
//                	Toast.makeText(getApplicationContext(), "Data was Not Aaved on the Server! ", Toast.LENGTH_LONG).show();
//                	
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
// 
//    }
    
   	
}
