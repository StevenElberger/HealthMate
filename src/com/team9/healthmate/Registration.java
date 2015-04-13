package com.team9.healthmate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.team9.healthmate.DataManager.DataStorageManager;

/** 
 * Presents the registration screen to users. Allows
 * users to create a new account.
 * @author Steve
 * 
 */
public class Registration extends Activity implements OnClickListener {
	Map<String, String> accountInfo;
	public Button createAccount;
	public EditText firstName;
	public EditText lastName;
	public EditText username;
	public EditText password;
	public EditText cpassword;
	public DatePicker datePicker;
	public Spinner sexSpinner;
	
	//GCM registration variables 
	 private static final String EXTRA_MESSAGE = "message";
	 private static final String PROPERTY_REG_ID = "registration_id";
	 private static final String PROPERTY_APP_VERSION = "appVersion";
	 private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    //API-Server key
    String SENDER_ID = "757694674583";
    
    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM_TAG";
    GoogleCloudMessaging gcm;
    AtomicInteger  msgId = new AtomicInteger();
    Context context;
    String regid;
	
	/**
	 * Sets up the picker and spinner as well as
	 * references to text box values. Also adds
	 * a listener to the registration button.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// User's birthday
	    datePicker = (DatePicker) findViewById(R.id.bday);
	    
	    // User's sex
	    sexSpinner = (Spinner) findViewById(R.id.sex);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	            R.array.sex, R.layout.sex_spinner_textview);
	    adapter.setDropDownViewResource(R.layout.sex_spinner_textview);
		sexSpinner.setAdapter(adapter);
	    
		createAccount = (Button) findViewById(R.id.create_account_button);
		createAccount.setOnClickListener(this);
		
		firstName = (EditText) findViewById(R.id.first_name);
		lastName = (EditText) findViewById(R.id.last_name);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
	}
	
    @Override
    protected void onResume() {
        super.onResume();
        // Check device for Play Services APK.
        checkPlayServices();
    }
    
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    
    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    
    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
    
    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(Registration.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP.
     */
    private void sendRegistrationIdToBackend() {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://108.47.199.22/gcm_server/register.php");
		
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("name", accountInfo.get("first_name") + " "
															+ accountInfo.get("last_name")));
			nameValuePairs.add(new BasicNameValuePair("email", "none"));
			nameValuePairs.add(new BasicNameValuePair("regId", regid));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			// Execute HTTP Post Request
		    HttpResponse response = httpclient.execute(httppost);
		    HttpEntity httpEntity = response.getEntity();
		    InputStream is = httpEntity.getContent();
		    
		    try {
			    BufferedReader reader = new BufferedReader(new InputStreamReader(
	                    is, "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null)
	                sb.append(line + "\n");
	            
	            is.close();
	            Log.d(TAG, sb.toString());
	        } catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        }
		} catch (ClientProtocolException e) {
		    // TODO Auto-generated catch block
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		}
    }        
//End of GCM Registration methods    
    
//=================================================
	
	/**
	 * Launch the login activity.
	 */
	public void startAct()	{
		Intent intent = new Intent(Registration.this, Login.class);
		startActivity(intent);
	}
	
	/**
	 * Calculates user's age and writes all
	 * information provided by the user to the
	 * account file. It will overwrite any existing
	 * data so there can only be one account at a time.
	 */
	public void generateAccount() {
		// Calculate age -- sorry for the mess
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		Date age = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String dateString = sdf.format(age);
		// Write contents to file
		Context context = getApplicationContext();
		accountInfo = new HashMap<String, String>();
		accountInfo.put("username", username.getText().toString());
		accountInfo.put("password", password.getText().toString());
		accountInfo.put("first_name", firstName.getText().toString());
		accountInfo.put("last_name", lastName.getText().toString());
		accountInfo.put("sex", sexSpinner.getSelectedItem().toString());
		accountInfo.put("age", "" + dateString);
		
		try {
			DataStorageManager.writeJSONObject(context, "account", accountInfo, true);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void registrateGCM() {
		//GCM registration 
	    context = getApplicationContext();

		// Check device for Play Services APK. If check succeeds, proceed with GCM registration.
		if (checkPlayServices()) {
		    gcm = GoogleCloudMessaging.getInstance(this);
		    regid = getRegistrationId(context);
		
		    if (regid.isEmpty()) {
		        registerInBackground();
		    }
		    else {
		    	Log.i(TAG, "regid is empty!");		    
		    }
		} else {
		    Log.i(TAG, "No valid Google Play Services APK found.");
		}
	}
	public void onClick(View v) {
		generateAccount();
		registrateGCM();
		startAct();
	}
}