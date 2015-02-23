package com.team9.healthmate;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

 
public class SplashScreen extends Activity {
	
	
	private static int SPLASH_TIME_OUT = 3000;
	private boolean isNetworkAvailable;
	 
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
    	
    	
    	@SuppressWarnings("deprecation")
    	public void showAlertDialogInterNet(Context context, String title, String message, Boolean status) {
    	            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
    	            alertDialog.setTitle(title);
    	            alertDialog.setMessage(message);
    	            //alertDialog.setIcon((status) ? R.drawable.ic_launcher : R.drawable.ic_launcher);
    	            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    	                public void onClick(DialogInterface dialog, int which) {
    	                SplashScreen.this.finish();
    	                }
    	            });
    	            
    	            alertDialog.show();
    
    	        }
    	
    	
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls         
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            /*
             * Will make http call here This call will download required data
             * before launching the app 
             * example: 
             * 1. Downloading and storing in SQLite 
             * 2. Downloading images 
             * 3. Fetching and parsing the xml / json 
             * 4. Sending device information to server 
             * 5. etc.,
             */                	
                try {
                	check = new CheckInterNetConnection(getApplicationContext());
            	    isInternetPresent = check.isConnectingToInternet();
            	    if(isInternetPresent){      
            	    	isNetworkAvailable = true;
            	            
            	     }else{
            	       isNetworkAvailable = false;

            	}
 
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
 
            
                return null;            
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (isNetworkAvailable){
            	   setContentView(R.layout.activity_splash);
	                new Handler().postDelayed(new Runnable() {
	                	 
	                    /*
	                     * Showing splash screen with a timer. This will be useful when you
	                     * want to show case your app logo / company
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
            }else{
            	 isNetworkAvailable = false;
             	showAlertDialogInterNet(SplashScreen.this, "No Internet",
             	                "No Internet Connection, Please try again.", false);
            }
            	
            	

        }
 
    }
	

	
	 
  
}
