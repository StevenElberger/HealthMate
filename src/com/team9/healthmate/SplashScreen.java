package com.team9.healthmate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

/**
 * 
 * @author Davit
 *	SplashScree class is the very first activity
 *	that starts when the application starts. It is 
 *	used to verify the connection with the network
 */
public class SplashScreen extends Activity {
	
	
	private static int SPLASH_TIME_OUT = 3000;
	private boolean isNetworkAvailable;
	
	// Progress Dialog
    private ProgressDialog pDialog;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
 
        /**
         * Showing splashscreen while making network calls to download necessary
         * data before launching the app Will use AsyncTask to make http call
         */
        new PrefetchData().execute();
 
    }
 
    /**
     * Async Task to make http call
     */
    private class PrefetchData extends AsyncTask<Void, Void, Void> {
 
    	CheckInterNetConnection check;
    	Boolean isInternetPresent = false;    	
    	//Displays the alert dialog
//    	@SuppressWarnings("deprecation")
//    	public void showAlertDialogInterNet(Context context, String title, String message, Boolean status) {
//    	            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//    	            alertDialog.setTitle(title);
//    	            alertDialog.setMessage(message);    	            
//    	            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//    	                public void onClick(DialogInterface dialog, int which) {
//    	                SplashScreen.this.finish();
//    	                }
//    	            });
//    	            
//    	            alertDialog.show();    
//    	        }    	
    	
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
              pDialog = new ProgressDialog(SplashScreen.this);
	          pDialog.setMessage("Checking for Network Connection...");
	          pDialog.setIndeterminate(false);
	          pDialog.setCancelable(true);
	          pDialog.show();
            // before making http calls         
 
        }
        /**
         * Function is triggered once AsyncTask is declared.
         * It runs in the background. It also verifies network
         * connection.
         */
        @Override
        protected Void doInBackground(Void... arg0) {
                      	
                try {
                	check = new CheckInterNetConnection(getApplicationContext());
            	    isInternetPresent = check.isConnectingToInternet();
            	    if(isInternetPresent){      
            	    	isNetworkAvailable = true;
            	            
            	     }else{
            	       isNetworkAvailable = false;
            	     } 
                } catch (Exception e) {                   
                    e.printStackTrace();
                }
            
                return null;            
        }
        /**
         * Function is called after http request is received.
         * In here it runs a new runnable to deploy our
         * main activity if there is connection
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            setContentView(R.layout.activity_splash);
            if (isNetworkAvailable){            	   
	               
            }else{
            	 isNetworkAvailable = false;
             	Toast.makeText(SplashScreen.this, "No Internet Connection! ", Toast.LENGTH_LONG).show();             	
            }
            new Handler().postDelayed(new Runnable() {
           	 
                /*
                 * Showing splash screen with a timer. 
                 */	         
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashScreen.this, Login.class);
                    startActivity(i);
     
                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);     
            
            
            pDialog.dismiss();
            
            } 
    } 
  
}
