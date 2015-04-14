package com.team9.healthmate;


import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	public String API_KEY = "AIzaSyByYEysYTmSo-_vMEkgVviy18IJbAg3dpE";
    public double longitude;
    public double latitude;
    
    //Conversion from 1 mile to meters
    public int METER_TO_MILE = 2*1610;
    public int radius = METER_TO_MILE;
    public String types ="";
    
    public String placesSearchStr;
    private Marker userMarker;
    private Marker[] placeMarker;
    private int user_icon, hospital_icon,fire_station_icon, police_icon, doctor_icon, pharmacy_icon;
    public GoogleMap gMap;
    private final int MAX_PLACES = 20;
    private MarkerOptions[] places;
    private ProgressDialog pDialog;
    
    /**
     * This method is called as soon as the activity is selected. The method init markers
     * map and after calls the init call of updatePlaces() method. In addition, it sets the
     * content view to layout activity_health_location which is in the resource folder.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_location);

        //grabbing marker drawable ids
        user_icon = R.drawable.user_icon;
        hospital_icon = R.drawable.hospital_icon;
        fire_station_icon = R.drawable.fire_station_icon;
        police_icon = R.drawable.police_icon;
        pharmacy_icon = R.drawable.pharmacy_icon;
        pDialog = new ProgressDialog(this);

        //grab map
        gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.google_map)).getMap();
        if(gMap!= null) {
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            placeMarker = new Marker[MAX_PLACES];
            //update location
            updatePlaces();
        }

    }
    /**
     * This method is a on click listener called from the xml from the image buttons.
     * Changes the type of places that is viewed on the map.
     * 
     * @param v : v is the view from the button that is being called in order for the
     * 			  method to be called.
     */
    public void onClick(View v)	{
    	pDialog.setMessage("Finding Locations");
    	try {
            pDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    	switch(v.getId())	{
    		case R.id.hospital: types = "hospital"; /*updatePlaces();*/ break;
    		case R.id.fire_station: types = "fire_station"; updatePlaces(); break;
    		case R.id.police: types = "police"; updatePlaces(); break;
    		case R.id.pharmacy: types = "pharmacy"; updatePlaces(); break;
    	}
    	
    }
    
    @Override
    public void onLocationChanged(Location location) {
        updatePlaces();
    }
    @Override
    public void onProviderDisabled(String provider){
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    
    /**
     * Calling this method grabs the user's location using a LocationManager with network provider.
     * In addition, the method clears all the markers from the map including the user's then adds
     * a new mark for the user. Along with animating the camera to go to the user's location. Then
     * builds a query string in order to find nearby location based on the specifications. Once the
     * query string is built it is sent to AsyncTask that allows a thread to run in the background.
     * Lastly, send a request to the location manager to update every 30 secs.
     *  
     */
    public void updatePlaces()   {
        //need Location Manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //need last location
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //get longitude and latitude
        double latitude = lastLocation.getLatitude();
        double longitude = lastLocation.getLongitude();
        //create a LatLng
        LatLng lastLatLng = new LatLng(latitude,longitude);

        //remove old markers
        if(userMarker !=null)   {
            userMarker.remove();
        }
        
        //adjust "|" to read as a %
        try {
            types = URLEncoder.encode(types, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        //Set marker properties
        userMarker = gMap.addMarker(new MarkerOptions()
            .position(lastLatLng)
            .title("You are here")
            .icon(BitmapDescriptorFactory.fromResource(user_icon))
            .snippet("Your last recorded location")
        );
        CameraPosition cameraPosition = new CameraPosition.Builder()
        	.target(lastLatLng)      // Sets the center of the map to Mountain View
        	.zoom(14)                   // Sets the zoom
        	.tilt(45)                   // Sets the tilt of the camera to 30 degrees
        	.build();                   // Creates a CameraPosition from the builder
        
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000, null);
        
        if(types != "")	{
	        //build places query string
	       placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
	                "json?location="+latitude+","+longitude+
	                "&radius="+radius+"&sensor=true" +
	                "&types="+types+
	                "&key="+API_KEY;
	        new GetPlaces().execute(placesSearchStr);
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,30000, 100,this);
    }
    
    /**
     * 
     * 
     * This method creates thread to run in the background of the application, completing the
     * fetching and parse of the string given in the params in order to send a http request to
     * google place api to receive a JSON object containing the information needed to find nearby
     * locations.
     * 
     *  @params string(s): This string is a place query in order for google api to send back
     *  					information. 
     *  
     *  @return string : Returns a string that will be built in the method which will be passed to the 
     * 					 onPostExecute(String results) 
     *
     */
    public class GetPlaces extends AsyncTask<String, Void, String> {
        //fetch and parse place data
        @Override
        protected String doInBackground(String... placesURL) {
            //fetch places
            StringBuilder placesBuilder = new StringBuilder();
            //process search parameter string(s)
            for(String placeSearchURL: placesURL)    {
                //execute search
                HttpClient placesClient = new DefaultHttpClient();
                try{
                    //try to fetch the data
                    HttpGet placesGet = new HttpGet(placeSearchURL);
                    //execute Get with Client - return response
                    HttpResponse placesResponse = placesClient.execute(placesGet);
                    //check response status
                    StatusLine placeSearchStatus = placesResponse.getStatusLine();
                    if(placeSearchStatus.getStatusCode() == 200)    {
                        //get response entity
                        HttpEntity placesEntity = placesResponse.getEntity();
                        //get input stream setup
                        InputStream placesContent = placesEntity.getContent();
                        //create reader
                        InputStreamReader placesInput = new InputStreamReader(placesContent);
                        //use buffered reader to process
                        BufferedReader placesReader = new BufferedReader(placesInput);
                        //read a line at a time, append to string builder
                        String lineIn;
                        while((lineIn = placesReader.readLine()) != null)   {
                            placesBuilder.append(lineIn);
                        }
                    }

                } catch (Exception e)   {
                    e.printStackTrace();
                }

            }
            return placesBuilder.toString();
        }
        
        /**
         * This method is an addition to the first thread and is ran right after doInBackground(..,).
         * In addition, this is where the parsing of the JSON Object taken from the google place api
         * and set the information to the markers including the location, icon display, etc.
         * 
         * @param results : A string that is given from the doInBackground(...) return results and
         * 					is then put in a JSON Object to grab the information needed.
         */
        protected void onPostExecute(String result)  {
            //parse place data returned from Google Places
            if(placeMarker != null) {
                for (int i = 0; i < placeMarker.length; i++)    {
                    if (placeMarker[i]!= null)  {
                        placeMarker[i].remove();
                    }
                }
            }
            try{
                //parse JSON
                JSONObject resultObject = new JSONObject(result);
                JSONArray placesArray = resultObject.getJSONArray("results");
                places = new MarkerOptions[placesArray.length()];

                for (int i =0; i<placesArray.length();i++)  {
                    //parse each place
                    boolean missingValue=false;
                    LatLng placeLL = null;
                    String placeName = "";
                    String vicinity = "";
                    int currIcon = user_icon;
                    try {
                        //attempt to retrieve place data values
                        missingValue = false;
                        JSONObject placeObject = placesArray.getJSONObject(i);
                        //fetch location cords from JSON Object
                        JSONObject location = placeObject.getJSONObject("geometry").getJSONObject("location");
                        placeLL = new LatLng(Double.valueOf(location.getString("lat")),Double.valueOf(location.getString("lng")));
                        //fetch the type found in the JSON object
                        JSONArray types = placeObject.getJSONArray("types");
                        //Loops through the type to assign an icon for each type of location
                        for (int k = 0; k <  types.length();k++)    {
                            String thisType = types.get(k).toString();
                            if(thisType.contains("hospital")){
                                currIcon = hospital_icon;
                                break;
                            }
                            else if(thisType.contains("fire_station")){
                                currIcon = fire_station_icon;
                                break;
                            }
                            else if(thisType.contains("police")){
                                currIcon = police_icon;
                                break;
                            }
                            else if(thisType.contains("pharmacy")){
                                currIcon = pharmacy_icon;
                                break;
                            }
                            else	{
                            	missingValue = false;
                            }
                        }
                        vicinity = placeObject.getString("vicinity");
                        placeName = placeObject.getString("name");

                    } catch (JSONException jse) {
                        missingValue = true;
                        jse.printStackTrace();
                    }
                    if(missingValue)    {
                        places[i] = null;
                    }
                    else    {
                        places[i] = new MarkerOptions()
                                .position(placeLL)
                                .title(placeName)
                                .icon(BitmapDescriptorFactory.fromResource(currIcon))
                                .snippet(vicinity);
                    }
                }
            } catch (Exception e)   {
                e.printStackTrace();
            }
            if(places != null && placeMarker !=null)    {
                for(int i=0; i<places.length && i < placeMarker.length; i++)    {
                    //will be null if a value was missing
                    if(places[i] != null)   {
                        placeMarker[i] = gMap.addMarker(places[i]);
                    }
                }
            }
            if(pDialog.isShowing())	{
            	pDialog.dismiss();
            }

        }
    }
    
    

}
