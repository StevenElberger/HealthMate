package com.team9.healthmate;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
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
import android.widget.Switch;
import android.os.Build;


/** @author Davit Avetikyan
	 
*/
public class Moods extends FragmentActivity
							implements ActionBar.TabListener {

	private ViewPager viewPager;
    private MoodTabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private MenuInflater mn;    
   private Menu TabMenu;
   private Context context;
   private Intent intent2;
    
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
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			Log.v("Debug", "ER start");
			// Add the buttons
			//builder.setTitle("Would you like to make an Emergency Call?");
			builder.setPositiveButton(R.string.ok2, new DialogInterface.OnClickListener() {
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
			builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               // User cancelled the dialog
			        	   
			           }
			       });
			
			// Create the AlertDialog
			AlertDialog dialog = builder.create();	
			dialog.show();
			break;
		case R.id.mAddRssFeed:
			 AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			    // Get the layout inflater
			    LayoutInflater inflater = this.getLayoutInflater();

			    // Inflate and set the layout for the dialog
			    // Pass null as the parent view because its going in the dialog layout
			    builder2.setView(inflater.inflate(R.layout.dialog_rss_feed, null))
			    // Add action buttons
			           .setPositiveButton("Add", new DialogInterface.OnClickListener() {
			               @Override
			               public void onClick(DialogInterface dialog, int id) {
			                   // sign in the user ...
			               }
			           })
			           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int id) {
			                  
			               }
			           });      
			   builder2.create();
			builder2.show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
		
	 //@Override
//	    public boolean onCreateOptionsMenu(Menu menu) {
//	        MenuInflater inflater = getMenuInflater();
//	        inflater.inflate(R.menu.graphs, menu);	        
//	        return super.onCreateOptionsMenu(menu);
//	    }

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
//        if (TabMenu != null) {
//		switch (tab.getPosition()) {
//		case 0:		
//			
//			mn.inflate(R.menu.mood_menu_items, TabMenu);
//			break;			
//		case 1:
//			
//			mn.inflate(R.menu.graphs, TabMenu);
//			break;
//		case 2:
//			
//			break;
//
//		default:
//			
//			mn.inflate(R.menu.mood_menu_items, TabMenu);
//			break;
//		}
//        
//        }
//        
      }

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//		ActionBar action = getActionBar();
//		action.hide();
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}		
	
	
}
