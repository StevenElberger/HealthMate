package com.team9.healthmate;


/**
 * @author Davit
 * The class displays useful tips 
 * to teach how to control emotions.
 * Currently class is retrieving data
 * from the internet in form of RSS feeds
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MoodFeelingTips extends Fragment {

	TextView mRssFeed;
	
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 
	        View rootView = inflater.inflate(R.layout.fragment_mood_feeling_tips, container, false);
	        mRssFeed = (TextView) rootView.findViewById(R.id.rss_feed);
	        return rootView;
	}
		
		@Override
	       public void onStart() {
	           super.onStart();
	           new GetAndroidPitRssFeedTask().execute();
	       }
		
		//	The function loads the feeds onto UI display
		//	This makes it easier to use source code several time 
		//	rather than spending a lot of time copying and pasting
		 private String getAndroidPitRssFeed() throws IOException {
			 InputStream in = null;
	            String rssFeed = null;
	            try {
	                URL url = new URL("http://www.androidpit.com/feed/main.xml");
	                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	                in = conn.getInputStream();
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                byte[] buffer = new byte[1024];
	                for (int count; (count = in.read(buffer)) != -1; ) {
	                    out.write(buffer, 0, count);
	                }
	                byte[] response = out.toByteArray();
	                rssFeed = new String(response, "UTF-8");
	            } finally {
	                if (in != null) {
	                    in.close();
	                }
	            }
	            return rssFeed;
		 }
		
		 // This is a task class which will run in the background
		 // It will also display the result in the UI thread
		  private class GetAndroidPitRssFeedTask extends AsyncTask<Void, Void, String> {

	           @Override
	           protected String doInBackground(Void... voids) {
	               String result = "";
	               try {
	                   result = getAndroidPitRssFeed();
	               } catch (IOException e) {
	                   e.printStackTrace();
	               }
	               return result;
	           }

	           @Override
	           protected void onPostExecute(String rssFeed) {
	               mRssFeed.setText(rssFeed);
	           }
	       }
		
		
}

