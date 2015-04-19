package com.team9.healthmate;


/**
 * @author Davit
 * The class displays useful tips 
 * to teach how to control emotions.
 * Currently class is retrieving data
 * from the internet in form of RSS feeds
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.team9.healthmate.Medications.Medication;
import com.team9.healthmate.Medications.MedicationObject;
import com.team9.healthmate.RSS.ListListener;
import com.team9.healthmate.RSS.RssItem;
import com.team9.healthmate.RSS.RssReader;


import android.R.string;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;


public class MoodFeelingTips extends Fragment {
	
	private List<String> url;
	private Map<String, List<RssItem>> map3 = new HashMap<String, List<RssItem>>();
	private ProgressDialog progress;
	private Context context = this.getActivity();
	public static List<RssItem> result;
	public static String rssFeedVar  = "";  

	
	
   
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 
	        View rootView = inflater.inflate(R.layout.fragment_mood_feeling_tips, container, false);
	          	         
	        GetRSSDataTask task = new GetRSSDataTask();	         
	       
	        // Start download RSS task
	        if  (rssFeedVar.isEmpty()){
	        task.execute("http://www.health.com/health/healthy-happy/feed"); 
	        }
	        else{
	        	task.execute(rssFeedVar);
	        }
	        	
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
	                Log.e("RssReader", e.getMessage());
	            }
	             
	            return null;
	        }
	         
	        @Override
	        protected void onPostExecute(List<RssItem> resultRss) {	             
	        	
	        	result = resultRss;
	        	
	        	ArrayList<String> headTitle = new ArrayList<String>();
	        	
	        	for (RssItem item : result) {
					headTitle.add(item.getTitle());
				}
	        	
	            // Get a ListView from main view
	            ListView itcItems = (ListView) getView().findViewById(R.id.listViewDave);
	                         
	            // Create a list adapter
	            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, headTitle);
	            MoodAdapter adapter = new MoodAdapter();
	            // Set list adapter for the ListView
	            Log.v("Debug", "Hello Gustavo 2");
	            itcItems.setAdapter(adapter);
	            Log.v("Debug", "Hello Gustavo 3");
	            // Set list view item click listener
	            itcItems.setOnItemClickListener(new ListListener(result, getActivity()));   
	            
	          	        }                
	    }  
		
		
		private class MoodAdapter extends ArrayAdapter<RssItem> {
			
				public MoodAdapter() {
				super(getActivity(), R.layout.mood_list_items, result);
				Log.v("Debug", "Hello Gustavo");	
							
				}
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View itemView = convertView;
				if (itemView == null) {
					itemView = getActivity().getLayoutInflater().inflate(R.layout.mood_list_items, parent, false);
				}
				Log.v("Debug", "Hello!");
				
				RssItem currentItem = result.get(position);			
				TextView title = (TextView) itemView.findViewById(R.id.list_title);
				TextView subtitle = (TextView) itemView.findViewById(R.id.list_subtitle);
				ImageView image = (ImageView) itemView.findViewById(R.id.imageMoodView);
				title.setText(currentItem.getTitle());
				subtitle.setText(currentItem.getDescription());
				Log.v("Debug", "Hello2");
				return itemView;
			}
		}
		
	          }
          


