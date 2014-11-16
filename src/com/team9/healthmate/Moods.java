package com.team9.healthmate;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.DialogInterface.OnClickListener;
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
							implements ActionBar.TabListener{

	private ViewPager viewPager;
    private MoodTabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private MenuInflater mn;
 // Tab titles
    private String[] tabs = { "Moods Survey", "Moods Graph", "Helpful Tips" };    
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moods);
		
		 Log.v("debugme1", "passed 1 to compile");
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
      }

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}		
}
