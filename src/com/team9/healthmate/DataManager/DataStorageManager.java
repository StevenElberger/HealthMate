package com.team9.healthmate.DataManager;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

public class DataStorageManager {
	
	/* Method to Display a given text on the Screen of the Current Activity.
	 * @activity this is the current activity that is calling this method
	 * @id this is the resource id that will be used to format how the text will be displayed
	 * @text the information that will be displayed on the screen
	 */
	public static void displayText(Activity activity, int id, String text) {
		TextView textView = (TextView) activity.findViewById(id);
		textView.setText(text);
		}
	
	/* Method to write information to a file, this information will be written 
	 * in a JSON object format.
	 * @context the current context of the activity
	 * @listOfItems the list of key value pairs of the information that needs to be written
	 */
	public static void writeJSONObject(Context context, Map<String, String> listOfItems, boolean overwrite) 
			throws JSONException, IOException {
		
		// abstract the filename from the set of information
		String fileName = listOfItems.get("filename");
		listOfItems.remove("filename");
		JSONObject data = new JSONObject();
		
		// Go through the list of information, expanding the JSON object.
		for (String key : listOfItems.keySet()) {
			data.put(key, listOfItems.get(key));
		}
		
		// Get the JSON object information in a string format
		String text = data.toString() + "\n";

		// Write to the file with the given file name
		// If overwrite is true, the file will be overwritten if it already exists.
		// MODE_PRIVATE creates a new file, or if the file exists, it overwrites the file.
		if (overwrite){
			FileOutputStream fileOutputStream = 
					context.getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
			
			fileOutputStream.write(text.getBytes());
			
			// Close the output stream.
			fileOutputStream.close();
		}
		// MODE_APPEND causes the write to create a new file if the file does not exist,
	    // If the file does exist, the information will be appended at the end of the file.
		else
		{
			FileOutputStream fileOutputStream = 
				context.getApplicationContext().openFileOutput(fileName, Context.MODE_APPEND);
			
			fileOutputStream.write(text.getBytes());
			
			// Close the output stream.
			fileOutputStream.close();
		}
	}
	
	/* Method to read a file from internal storage. The file contains JSON formatted text.
	 * This throws an FileNotFound Exception, the Activity is required to handle the occurrence
	 * of a file not existing in the application.
	 * @context the current context of the activity
	 * @fileName the file that will be read from.
	 * @return information contained in the file that was read
	 */
	public static ArrayList<Map<String, String>> readJSONObject(Context context, String fileName) throws IOException, JSONException  {
		
		// Open the file with the given file name
		FileInputStream fileInputStream = context.getApplicationContext().openFileInput(fileName);
		
		// Buffer increases the amount of information coming in for a given read.
		BufferedInputStream bufferInputStream = new BufferedInputStream(fileInputStream);
		
		// Buffer for reading the information byte for byte.
		StringBuffer buffer = new StringBuffer();
		StringBuffer dataBuffer = new StringBuffer();
		
		// The array containing all the JSON Objects
		JSONArray dataSet;
		JSONObject currentDataPacket;
		String data;
		
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
		
		// Create a new array of JSON objects using the information read from the file.
		// Separate the JSON Objects and create a string of all the objects separated by ","
		String [] parseString = buffer.toString().split("\n");
		String appendedString = "";
		for (int i = 0; i < parseString.length - 1; i++) {
			appendedString = appendedString + parseString[i] + ", ";
		}
		// Create a JSONArray containing all the JSON Objects
		appendedString = appendedString + parseString[parseString.length - 1];
		dataSet = new JSONArray(("[" + appendedString + "]"));
		
		
		// Go through the array of JSON objects and append all the information into 
		// a single string of information, this will be just keys and values.
		for (int i = 0; i < dataSet.length(); i++) {
			
			currentDataPacket = dataSet.getJSONObject(i);
			iterator = currentDataPacket.keys();
			currentInformation = new HashMap<String, String>();
			while (iterator.hasNext()) {
				
				// Get the next key
				key = iterator.next();
				
				currentInformation.put(key, dataSet.getJSONObject(i).getString(key));
				
				// Get the value of the current key
				data = dataSet.getJSONObject(i).getString(key);
				// Append the information into a single String.
				dataBuffer.append(key + ": " + data + "\n");
			}
			dataList.add(currentInformation);
		}
		return dataList;
	}
}
