package com.team9.healthmate.ServerManager;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.team9.healthmate.R;
import com.team9.healthmate.JsonManager.JSONParser;
/**
 * The ServerStorageManager class is a class that handles
 * server exectution procedures. In this class we define 
 * AsyncTask thread classes to Update, Create and Delete data
 * on Server side.
 * @author Davit Avetikyan
 */
public  class ServerStorageManager 
					
{

	// Create an instance for JSONParser
		 JSONParser jsonParser = new JSONParser();
		 
		// Create an instance for JSONObject class
		 JSONObject json;
		 
		 // Activity controller variable
		 View[] v;
		 
		 // Map object passed from initial Activity
		 Map m;
		 
		 // An enviroment varialbe of initial Activity
		 Context contextLocal;
		 
		// JSON Node names
		 private static final String TAG_SUCCESS = "success";
		 
		  // querry string for deletion
		  public String url_delete_data;
		  
		// querry string for updating or creating
		  public String url_CreateorUpdate_data;
		  
		
		 /**
		  * 
		  * @param st
		  * @param sqlQuerry
		  * @param map
		  * @param context
		  */
		 public ServerStorageManager( String st, String sqlQuerry, Map<String, String> map, Context context){			
			 m = map; // map object from initial Activity			 
			 contextLocal = context;
			 if (st.equals("CreateorUpdate")){	
				 url_CreateorUpdate_data = sqlQuerry;
				 new CreateorUpdateData().execute(); // execute the creating or updating of data procedures to server
			 }
			 else if (st.equals("Delete")){		
				 url_delete_data = sqlQuerry;
				 new DeleteData().execute(); // execute the deletion of data procedure to server
			 }
		 }
	
	
	/**
     * Background Async Task to Create or to Update data on the Server side
     * */
    class CreateorUpdateData extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();           
        }
 
        /**
         * Creating an appointment
         * */
        protected String doInBackground(String... args) {
        	        	
        	try{            
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            
            // Loop through each value in the map object for 
            // passing the data to  tables on the server
            for (Object key: m.keySet())			
				params.add(new BasicNameValuePair((String)key, (String) m.get(key)));		         
           
              	// getting JSON Object
              	// Note that create product url accepts POST method
              	json = jsonParser.makeHttpRequest(url_CreateorUpdate_data,
                    "POST", params);                
              	 Log.d("Create Response first", "Something happened");
            // check log cat fro response
            
        	}
        	catch (Exception ex){
        		ex.printStackTrace();
        	}           
        	
 
           return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {        	
 
            try {
                int success  = json.getInt(TAG_SUCCESS);
            	// successful to create an appointment
                if (success == 1) {                   
                	Toast.makeText(contextLocal.getApplicationContext(), "Data was Saved on the Server Successfully! " , Toast.LENGTH_LONG).show();
                } else {
                    // failed to create an appointment
                	Toast.makeText(contextLocal.getApplicationContext(), "Data was Not Aaved on the Server! ", Toast.LENGTH_LONG).show();
                	
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } 
    }
    
    /*****************************************************************
     * Background Async Task to Delete data on server side
     * */
    class DeleteData extends AsyncTask<String, String, String> {    	
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();          
        }
 
        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {
 
             try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();  
                // Loop through each value in the map object for 
                // passing the data to  tables on the server
                for (Object key: m.keySet())    			
    				params.add(new BasicNameValuePair((String)key, (String)m.get(key)));    			
             
                // getting appointment details by making HTTP request
                json = jsonParser.makeHttpRequest(
                        url_delete_data, "POST", params);
 
                // check your log for json response
                Log.d("Delete data", json.toString());              
            } catch (Exception e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
        	 try {
                int success  = json.getInt(TAG_SUCCESS);
             	
                 if (success == 1) {                  
                 	Toast.makeText(contextLocal.getApplicationContext(), "Data was successfully deleted on the server! " , Toast.LENGTH_LONG).show();
                 } else {
                     // failed to create product
                 	Toast.makeText(contextLocal.getApplicationContext(), "Data was not deleted on the server! Please Contact an Administrator. ", Toast.LENGTH_LONG).show();                 	
                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }
        	 
        	
        }

}
	
	
}
