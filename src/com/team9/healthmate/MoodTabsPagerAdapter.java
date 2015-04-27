package com.team9.healthmate;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * 
 * @author Davit
 *	MoodTabsPagerAdapter class creates a 
 * 	custom Fragment Adapter. It is called 
 * 	by the ViewPage in the Moods activity
 */
public class MoodTabsPagerAdapter  extends FragmentPagerAdapter {

	 public MoodTabsPagerAdapter(FragmentManager fm) {
	        super(fm);
	    }
	 
	   @Override
	    public Fragment getItem(int index) {
	 
	        switch (index) {
	        case 0:
	            // Survey fragment activity
	            return new MoodsSurvey();
	        case 1:
	            // Graph fragment activity
	            return new MoodsGraph();
	        case 2:
	            // Tips fragment activity
	            return new MoodFeelingTips();
	        }
	 
	        return null;
	    }
	 
	    @Override
	    public int getCount() {
	        // get item count - equal to number of tabs
	        return 3;
	    }


	
}
