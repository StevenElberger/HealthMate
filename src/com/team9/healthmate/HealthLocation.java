package com.team9.healthmate;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HealthLocation extends Activity implements LocationListener{
	
	public String API_KEY = "AIzaSyByYEysYTmSo-_vMEkgVviy18IJbAg3dpE";
    public double longitude;
    public double latitude;
    
    //Conversion from 1 mile to meters
    public int METER_TO_MILE = 1610;
    public int radius = METER_TO_MILE;
    public String types ="hospital";
    //|doctor|fire_station|pharmacy|police
    
    public String placesSearchStr;
    private Marker userMarker;
    private Marker[] placeMarker;
    private int user_icon, hospital_icon/*,fire_station_icon, police_icon, doctor_icon, pharmacy_icon*/;
    public GoogleMap gMap;
    private final int MAX_PLACES = 20;
    private MarkerOptions[] places;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_location);

        //grabbing marker drawable ids
        user_icon = R.drawable.user_icon;
        hospital_icon = R.drawable.hospital_icon;
        /*
        fire_station_icon = R.drawable.fire_station_icon;
        police_icon = R.drawable.police_icon;
        doctor_icon = R.drawable.doctor_icon;
        pharmacy_icon = R.drawable.pharmacy_icon;
        */

        //grabs map
        gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.google_map)).getMap();
        if(gMap!= null) {
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            placeMarker = new Marker[MAX_PLACES];
            //update location along with places
            updatePlaces();
        }
        else    {
            //DEBUG CODE
            Log.v("Map:","Something is wrong with map setup or Play Service is not available");
        }

    }
    @Override
    public void onLocationChanged(Location location) {
        //DEBUG CODE
        Log.v("MyMapActivity", "location changed");
        updatePlaces();
    }
    @Override
    public void onProviderDisabled(String provider){
        //DEBUG CODE
        Log.v("MyMapActivity", "provider disabled");
    }
    @Override
    public void onProviderEnabled(String provider) {
        //DEBUG CODE
        Log.v("MyMapActivity", "provider enabled");
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //DEBUG CODE
        Log.v("MyMapActivity", "status changed");
    }
    
    //updates locations
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
        gMap.animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000, null);
        //build places query string
       placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location="+latitude+","+longitude+
                "&radius="+radius+"&sensor=true" +
                "&types="+types+
                "&key="+API_KEY;
        new GetPlaces().execute(placesSearchStr);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,30000, 100,this);
    }
    
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
                    else    {
                    }

                } catch (Exception e)   {
                    e.printStackTrace();
                }

            }
            Log.v("Place Search Debug", placesBuilder.toString());
            return placesBuilder.toString();
        }
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
                            /*=======================================================
                            Something is wrong with these. I need to investigate it
                            ========================================================
                            else if(thisType.contains("doctor")){
                                currIcon = doctor_icon;
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
                            }*/
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

        }
    }

}
