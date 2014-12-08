package com.team9.healthmate;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
/**
 * This activity is meant to display the user and health facilities around the user
 * on a map given from the google map api v2. The data to find the health facilities
 * around the user's location was given by the google place api and is parsed by the 
 * code given this activity.
 * 
 * @author Joseph E Hoxsey
 *
 */
public class HealthLocation extends Activity implements LocationListener{
    
    /**
     * This method is called as soon as the activity is selected. The method init markers
     * map and after calls the init call of updatePlaces() method. In addition, it sets the
     * content view to layout activity_health_location which is in the resource folder.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_location);
        
    }
    
    

}
