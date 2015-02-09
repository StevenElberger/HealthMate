package com.team9.healthmate;


import java.util.ArrayList;

import com.team9.healthmate.GraphManager.GraphManager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.os.Build;


/** @author Davit Avetikyan
	 
*/
public class Moods extends FragmentActivity
							implements ActionBar.TabListener, OnItemSelectedListener {

	private ViewPager viewPager;
    private MoodTabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private MenuInflater mn;    
   private Menu TabMenu;
   private Context context;
   private Intent intent2;
   private Spinner rssSpinner;
   private String moodRssEntry;
   private Tab tabAddFeed;
   
    
 // Tab titles
    private String[] tabs = { "Moods Survey", "Moods Graph", "Helpful Tips" };    
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moods);
		
		
		  // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new MoodTabsPagerAdapter(getSupportFragmentManager());
       
         
 
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
 
        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }        
        
        /**
         * On swiping the ViewPager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            	
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            
            }
        });
        
        context = getApplicationContext();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.mood_menu_items, menu);
	    mn = inflater;
	    TabMenu = menu;
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mCallER:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			
			Log.v("Debug", "ER start");
			// Add the buttons	

			builder2.setTitle("Would you like to make an Emergency Call?");
			builder2.setPositiveButton(R.string.ok2, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               // User clicked OK button
			        	   Log.v("Debug", "ER0");
			        	OneClickEmergency ER = new OneClickEmergency();
			   			ER.makeACall(context);			
			   			ER.sendEmail(context);
			   			ER.sendSMSMessage(context);
			   			Log.v("Debug", "ER1");
			           }
			       });
			builder2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               // User cancelled the dialog
			        	   
			           }
			       });
			
			// Create the AlertDialog
			AlertDialog dialog = builder2.create();	
			dialog.show();
			break;
		case R.id.mAddRssFeed:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			Context mContext = getApplicationContext();
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
			 View layout = inflater.inflate(R.layout.dialog_rss_feed, null);
			Log.v("Debug", "ER start");
			// Add the buttons
			
			builder.setView(layout);			 
			 rssSpinner = (Spinner)layout.findViewById(R.id.Rss_mood_tips_spinner);
			 
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.rssfeed_mood_arrays));
			 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			 rssSpinner.setAdapter(adapter);
			 rssSpinner.setOnItemSelectedListener(this);
			//builder.setTitle("Would you like to make an Emergency Call?");
			builder.setPositiveButton(R.string.ok2, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			            
			        	  MoodFeelingTips.rssFeedVar = moodRssEntry ;
//			        	  tabAddFeed = actionBar.getTabAt(0);
//			        	  viewPager.setCurrentItem(tabAddFeed.getPosition());
			        	  tabAddFeed = actionBar.getTabAt(2);
			        	  viewPager.setCurrentItem(tabAddFeed.getPosition());
			        	  
			           }
			       });
			builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               // User cancelled the dialog
			        	   
			           }
			       });
			
			// Create the AlertDialog
			AlertDialog dialog2 = builder.create();	
			dialog2.show();
			break;
		case R.id.mood_tips_Exit:
			finish();		
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());

      }

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//		ActionBar action = getActionBar();
//		action.hide();
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
				
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		int pos = rssSpinner.getSelectedItemPosition();
		
		switch(pos){
			
		case 0:
			moodRssEntry = "http://www.health.com/health/diet-fitness/feed" ;					
			break;
		case 1:
			moodRssEntry = "http://www.health.com/health/beauty-style/feed" ;			
			break;
		case 2:
			moodRssEntry = "http://www.health.com/health/asthma/feed" ;
			break;
		case 3:
			moodRssEntry = "http://www.cdc.gov/tobacco/rss/tobacco.xml" ;
			break;
		case 4:
			moodRssEntry = "http://feeds.health.com/health/heart-disease" ;
			break;
		case 5:
			moodRssEntry = "http://www.health.com/health/cholesterol/feed/" ;
			break;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{
	
	}		
	
	
}
