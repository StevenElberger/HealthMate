package com.team9.healthmate;


/**
 * @author Davit
 * The class displays useful tips 
 * to teach how to control emotions.
 * Currently class is retrieving data
 * from the internet in form of RSS feeds
 */


import java.util.List;

import com.team9.healthmate.RSS.ListListener;
import com.team9.healthmate.RSS.RssItem;
import com.team9.healthmate.RSS.RssReader;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;


public class MoodFeelingTips extends Fragment {

   
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 
	        View rootView = inflater.inflate(R.layout.fragment_mood_feeling_tips, container, false);
	          	         
	        GetRSSDataTask task = new GetRSSDataTask();
	         
	        // Start download RSS task
	        task.execute("http://www.health.com/health/healthy-happy/feed");
	        
	        // Debug the thread name
	        //Log.d("RssReader", Thread.currentThread().getName());
	
	        
	        return rootView;
	}
			
		/**
		 * 
		 * @author Davit
		 * This class allows to perform background operations and publish results 
		 * on the UI thread without having to manipulate threads and/or handlers. 
		 * AsyncTask is designed to be a helper class around Thread and Handler 
		 *
		 */
		private class GetRSSDataTask extends AsyncTask<String, Void, List<RssItem> > {
	        @Override
	        protected List<RssItem> doInBackground(String... urls) {
	             
	            // Debug the task thread name
	            Log.d("RssReader", Thread.currentThread().getName());
	             
	            try {
	                // Create RSS reader
	                RssReader rssReader = new RssReader(urls[0]);
	             
	                // Parse RSS, get items
	                return rssReader.getItems();
	             
	            } catch (Exception e) {
	                Log.e("ITCRssReader", e.getMessage());
	            }
	             
	            return null;
	        }
	         
	        @Override
	        protected void onPostExecute(List<RssItem> result) {
	             
	            // Get a ListView from main view
	            ListView itcItems = (ListView) getView().findViewById(R.id.listViewDave);
	                         
	            // Create a list adapter
	            ArrayAdapter<RssItem> adapter = new ArrayAdapter<RssItem>(getActivity(),android.R.layout.simple_list_item_1, result);
	            // Set list adapter for the ListView
	            itcItems.setAdapter(adapter);
	                         
	            // Set list view item click listener
	            itcItems.setOnItemClickListener(new ListListener(result, getActivity()));
	            
	          	        }        
	        
	    }  	
		
	          }
          


