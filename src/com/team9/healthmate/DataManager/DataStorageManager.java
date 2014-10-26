package com.team9.healthmate.DataManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

/**
 * The DataStorageManager class is a static class that can handle writing and reading to a file.
 * The information is written in JSON notation to a file in the internal storage of android.
 * A time stamp is automatically generated when a write is done to a file, and it is stored with 
 * the information written to a file. 
 */
public class DataStorageManager {

	/**
	 * Method to Display a given text on the Screen of the Current Activity.
	 * 
	 * @activity this is the current activity that is calling this method
	 * @id this is the resource id that will be used to format how the text will
	 *     be displayed
	 * @text the information that will be displayed on the screen
	 * @author Michael Sandoval
	 */
	public static void displayText(Activity activity, int id, String text) {
		TextView textView = (TextView) activity.findViewById(id);
		textView.setText(text);
	}

	/**
	 * Method to write information to a file, this information will be written
	 * in a JSON object format.
	 * 
	 * @context the current context of the activity
	 * @filename the file that will be written to.
	 * @listOfItems the list of key value pairs of the information that needs to
	 *              be written
	 * @overwrite a boolean that determines if the file is overwritten, true to
	 *            overwrite, false to append
	 * @return true if the information was successfully written to the file,
	 *         false if the failed to write to the file.
	 * @author Michael Sandoval
	 */
	public static boolean writeJSONObject(Context context, String fileName,
			Map<String, String> informationPacket, boolean overwrite)
			throws JSONException, IOException {
		
		boolean success = false;

		// Generate a time stamp for every JSON object written to the file
		// this will be stored with each JSON object.
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss", Locale.US);
		String timestamp = dateFormat.format(new Date());
		informationPacket.put("timestamp", timestamp);

		// The JSON object that will contain the data to be written
		JSONObject data = new JSONObject();

		// Go through the information packet, and create a JSON format data set.
		for (String key : informationPacket.keySet()) {
			data.put(key, informationPacket.get(key));
		}

		// Get the JSON object information in a string format
		String text = data.toString() + "\n";
		Log.w("Data Written", text);

		// If overwrite is true, the file will be overwritten if it already
		// exists.
		// MODE_PRIVATE creates a new file, or if the file exists, it overwrites
		// the file.
		if (overwrite) {
			// open the file that will be written to
			FileOutputStream fileOutputStream = 
					context.openFileOutput(fileName, Context.MODE_PRIVATE);
			
			// write the information to the file
			fileOutputStream.write(text.getBytes());

			// Close the output stream.
			fileOutputStream.close();
			
			success = true;
		}
		
		// MODE_APPEND causes the write to create a new file if the file does
		// not exist.
		// If the file does exist, the information will be appended at the end
		// of the file.
		else {
			// open the file that will be written to
			FileOutputStream fileOutputStream = context.getApplicationContext()
					.openFileOutput(fileName, Context.MODE_APPEND);
			
			// write the information to the file
			fileOutputStream.write(text.getBytes());

			// Close the output stream.
			fileOutputStream.close();
			
			success = true;
		}
		
		return success;
	}

	/**
	 * Method to read a file from internal storage. The file contains JSON
	 * formatted text. This throws an FileNotFound Exception, the applications context 
	 * is required to handle the occurrence of a file not existing in the
	 * application.
	 * 
	 * @context the current context of the activity
	 * @fileName the file that will be read from.
	 * @return information contained in the file that was read in a ArrayList
	 * @author Michael Sandoval
	 */
	public static ArrayList<Map<String, String>> readJSONObject(
			Context context, String fileName) throws IOException, JSONException {

		FileInputStream fileInputStream;
		String emptyTextString = "";
		// Open the file with the given file name
		try {
			fileInputStream = context.openFileInput(fileName);
		}
		// If the file does not exist, Create a new Empty File.
		catch (FileNotFoundException e) {
			FileOutputStream fileOutputStream = 
					context.openFileOutput(fileName, Context.MODE_PRIVATE);
			fileOutputStream.write(emptyTextString.getBytes());
			fileOutputStream.close();
			fileInputStream = context.openFileInput(fileName);
		}

		// Buffer increases the amount of information coming in for a given
		// read.
		BufferedInputStream bufferInputStream = new BufferedInputStream(fileInputStream);

		// Buffer for reading the information byte for byte.
		StringBuffer buffer = new StringBuffer();
		//StringBuffer dataBuffer = new StringBuffer();

		// The array containing all the JSON Objects
		JSONArray dataSet;
		JSONObject currentDataPacket;
		//String data;

		Iterator<String> iterator;
		String key;

		ArrayList<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> currentInformation;

		// Go through the entire file, byte for byte,
		// and append all the information into data set.
		while (bufferInputStream.available() != 0) {
			char character = (char) bufferInputStream.read();
			buffer.append(character);
		}

		// Close the streams used to read
		bufferInputStream.close();
		fileInputStream.close();

		// Create a new array of JSON objects using the information read from
		// the file.
		// Separate the JSON Objects and create a string of all the objects
		// separated by ","
		String[] parseString = buffer.toString().split("\n");
		String appendedString = "";
		for (int i = 0; i < parseString.length - 1; i++) {
			appendedString = appendedString + parseString[i] + ", ";
		}
		// Create a JSONArray containing all the JSON Objects
		appendedString = appendedString + parseString[parseString.length - 1];
		dataSet = new JSONArray(("[" + appendedString + "]"));

		// Go through the array of JSON objects and append all the information
		// into
		// a single string of information, this will be just keys and values.
		for (int i = 0; i < dataSet.length(); i++) {

			currentDataPacket = dataSet.getJSONObject(i);
			iterator = currentDataPacket.keys();
			currentInformation = new HashMap<String, String>();
			while (iterator.hasNext()) {

				// Get the next key
				key = iterator.next();

				currentInformation.put(key, dataSet.getJSONObject(i).getString(key));
			}
			dataList.add(currentInformation);
		}
		return dataList;
	}

	/**
	 * Method to delete an existing file from the applications data folder.
	 * 
	 * @param context the current context of the activity
	 * @param fileName the file that wants to be deleted
	 * @return true if the file was successfully deleted, false if unable to
	 *         delete or file does not exist.
	 * @author Michael Sandoval
	 */
	public static boolean deleteFile(Context context, String fileName) {

		// Get the path to the internal storage location of the application
		String dir = context.getFilesDir().getAbsolutePath();

		// Create a new file using the path to the give file's location.
		File file = new File(dir, fileName);

		// Delete the file
		boolean success = file.delete();

		return success;
	}
	
	/**
	 * Method to delete a JSON Object from a specified file. The given fileName
	 * is read from the internal storage if it exists. The contents of the file 
	 * are searched for the string representation of the JSON Object to be deleted.
	 * @param context the current state of the Application
	 * @param fileName the file that JSON object will be deleted from
	 * @param informationPacket the JSON Object to be deleted
	 * @return true if the JSONObject in the file is deleted, false if the file
	 * 		   does not exist or the JSONObject does not exist.  
	 * @throws JSONException
	 * @throws IOException
	 * @author Michael Sandoval
	 */
	public static boolean deleteJSONObject(Context context, String fileName,
			Map<String, String> informationPacket) throws JSONException, IOException {
		
		FileInputStream fileInputStream;

		// Buffer for reading the information byte for byte.
		StringBuffer buffer = new StringBuffer();

		// The array containing all the JSON Objects
		JSONArray dataSet;
		
		// The JSONObjects that will be used to process the file information
		JSONObject currentDataPacket;
		JSONObject data;
		
		// Boolean to identify if the data has been removed from the file
		boolean dataRemoved = false;
		
		String informationToBeDeleted = "";
		String text = "";

		// Open the file with the given file name
		try {
			fileInputStream = context.openFileInput(fileName);
		}
		// If the file does not exist, then there is no deletion.
		catch (FileNotFoundException e) {
			return false;
		}

		// The JSON object that will contain the data to be deleted
		data = new JSONObject();

		// Go through the information packet, and create a JSON format data set.
		for (String key : informationPacket.keySet()) {
			data.put(key, informationPacket.get(key));
		}
		
		// Get the string representation of the JSON Object
		informationToBeDeleted = data.get("timestamp").toString();
		
		// Buffer increases the amount of information coming in 
		// for a given read.
		BufferedInputStream bufferInputStream = 
				new BufferedInputStream(fileInputStream);

		// Go through the entire file, byte for byte,
		// and append all the information into data set.
		while (bufferInputStream.available() != 0) {
			char character = (char) bufferInputStream.read();
			buffer.append(character);
		}

		// Close the streams used to read
		bufferInputStream.close();
		fileInputStream.close();

		// Create a new array of JSON objects using the information read from
		// the file.
		// Separate the JSON Objects and create a string of all the objects
		// separated by ","
		String[] parseString = buffer.toString().split("\n");
		String appendedString = "";
		for (int i = 0; i < parseString.length - 1; i++) {
			appendedString = appendedString + parseString[i] + ", ";
		}
		
		// Create a JSONArray containing all the JSON Objects
		appendedString = appendedString + parseString[parseString.length - 1];
		dataSet = new JSONArray(("[" + appendedString + "]"));
		Log.w("Information to Be Deleted: ", informationToBeDeleted);
		
		// Delete the old file
		deleteFile(context, fileName);
		
		// Create a new file that will be written to
		FileOutputStream fileOutputStream = 
				context.openFileOutput(fileName, Context.MODE_APPEND);

		// Go through the array of JSON objects, compare them to the
		// JSONObject that will be deleted.
		for (int i = 0; i < dataSet.length(); i++) {

			currentDataPacket = dataSet.getJSONObject(i);
			Log.w("Information in File: ", currentDataPacket.toString());
			
			// Check if the current JSONObject is the one wanted to be deleted
			// If it is, do not add it to the data that will be written to the file.
			if (informationToBeDeleted.compareTo(data.get("timestamp").toString()) == 0) {
				dataRemoved = true;
			}
			else {
				
				text = currentDataPacket.toString() + "\n";
				
				// write the information to the file
				fileOutputStream.write(text.getBytes());
			}
		}
		
		// Close the output stream.
		fileOutputStream.close();
		
		return dataRemoved;

	}
}
