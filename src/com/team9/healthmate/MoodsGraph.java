/* @author Davit Avetikyan
 * 	
 * 
 */
package com.team9.healthmate;

import com.team9.healthmate.GraphManager.ColumnGraph;
import com.team9.healthmate.GraphManager.GraphManager;
import com.team9.healthmate.GraphManager.LineGraph;

import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;;

/**
 * 
 * @author Davit
 *	The MoodsGraph Displays user entered 
 *	data in graphical charts and diagrams.
 *	The data is received from local stored 
 *	android file.
 */
public class MoodsGraph extends Fragment{
	
	View view;
	Context context;
	boolean genLineGraphs;
	LineGraph[] lineGraph;
	ColumnGraph[] columnGraph;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_moods_graph, container, false);    
         
        return rootView;
}	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}
	
//	  @Override
//	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//	        // TODO Auto-generated method stub
//	        super.onCreateOptionsMenu(menu, inflater);
//	        inflater.inflate(R.menu.graphs, menu);
//	    }
	
//	@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.graphs, (android.view.Menu) menu);	        
//        return super.onCreateOptionsMenu(menu);
//    }
	
	
	/**
	 * formIsValid will loop through 
	 * child controls and check to see 
	 * which instance it belongs to. 
	 */
	public boolean formIsValid(View layout) {
	    for (int i = 0; i < ((ViewGroup) layout).getChildCount(); i++) {
	        View v = ((ViewGroup) layout).getChildAt(i);
	        if (v instanceof EditText) {
	            //validate your EditText here
	        } else if (v instanceof RadioButton) {
	            //validate RadioButton
	        } //etc. If it fails anywhere, just return false.
	    }
	    return true;
	}

	
	
	}
