package com.team9.healthmate;

import com.team9.healthmate.GraphManager.ColumnGraph;
import com.team9.healthmate.GraphManager.GraphManager;
import com.team9.healthmate.GraphManager.LineGraph;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Used to show graph generation
 * features. Mainly used for debugging.
 * @author Steve
 *
 */
public class ChartDemo extends ActionBarActivity {
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart_demo);
		context = getApplicationContext();
		
		// Used for debug / example purposes
		//GraphManager.writeRandomData(getApplicationContext());
		//GraphManager.writeRandomData(getApplicationContext());
		//GraphManager.writeRandomData(getApplicationContext());
		//GraphManager.writeRandomData(getApplicationContext());
		//GraphManager.writeRandomData(getApplicationContext());
		
		// We know the values for moods are 1 - 10 so we'll explicitly set the y-axis values
		//int[] yValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		// Create a ColumnGraph object with the specified information (Color is not currently used for column graphs)
		//ColumnGraph cg = new ColumnGraph("Days", "Mood Level", "testdata", null, yValues, false, false, 11, 1, Color.BLUE);
		// Generate a column graph for the given mood
		//GraphManager.generateMoodColumnGraph(getApplicationContext(), findViewById(R.id.chart_layout), cg, "Stressed");
		
		//GraphManager.getColumnData(getApplicationContext(), findViewById(R.id.chart_layout));
		
		/* Line Graph
		// generate test data
		GraphManager.writeRandomData(getApplicationContext());
		
		int[] xValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
		int[] yValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		LineGraph lg = new LineGraph("Days", "Mood Level", "testdata", xValues, yValues, false, true, Color.BLUE, 1, 3);
		GraphManager.generateMoodLineGraph(getApplicationContext(), findViewById(R.id.chart_layout), lg);
		*/
		//GraphManager.getPointData(getApplicationContext(), findViewById(R.id.chart_layout));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.graphs, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// We know the values for moods are 1 - 10 so we'll explicitly set the y-axis values
		int[] yValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		View v = findViewById(R.id.chart_layout);
		((RelativeLayout) v).removeAllViews();
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.JustOk:
	        	
	        	// Create a ColumnGraph object with the specified information (Color is not currently used for column graphs)
	    		ColumnGraph cg = new ColumnGraph("Days", "General Wellness Level", "testdata", null, yValues, false, false, 11, 1, Color.BLUE);
	    		// Generate a column graph for the given mood
	    		GraphManager.generateMoodColumnGraph(getApplicationContext(), findViewById(R.id.chart_layout), cg, "Just Ok");
	            return true;
	        case R.id.Happy:
	        	// Create a ColumnGraph object with the specified information (Color is not currently used for column graphs)
	    		ColumnGraph cg2 = new ColumnGraph("Days", "Happiness Level", "testdata", null, yValues, false, false, 11, 1, Color.BLUE);
	    		// Generate a column graph for the given mood
	    		GraphManager.generateMoodColumnGraph(getApplicationContext(), findViewById(R.id.chart_layout), cg2, "Happy");
	            return true;
	        case R.id.Motivated:
	        	// Create a ColumnGraph object with the specified information (Color is not currently used for column graphs)
	    		ColumnGraph cg3 = new ColumnGraph("Days", "Motivation Level", "testdata", null, yValues, false, false, 11, 1, Color.BLUE);
	    		// Generate a column graph for the given mood
	    		GraphManager.generateMoodColumnGraph(getApplicationContext(), findViewById(R.id.chart_layout), cg3, "Motivated");
	        	return true;
	        case R.id.Stressed:
	        	// Create a ColumnGraph object with the specified information (Color is not currently used for column graphs)
	    		ColumnGraph cg4 = new ColumnGraph("Days", "Stress Level", "testdata", null, yValues, false, false, 11, 1, Color.BLUE);
	    		// Generate a column graph for the given mood
	    		GraphManager.generateMoodColumnGraph(getApplicationContext(), findViewById(R.id.chart_layout), cg4, "Stressed");
	        	return true;
	        case R.id.Angry:
	        	// Create a ColumnGraph object with the specified information (Color is not currently used for column graphs)
	    		ColumnGraph cg5 = new ColumnGraph("Days", "Anger Level", "testdata", null, yValues, false, false, 11, 1, Color.BLUE);
	    		// Generate a column graph for the given mood
	    		GraphManager.generateMoodColumnGraph(getApplicationContext(), findViewById(R.id.chart_layout), cg5, "Angry");
	        	return true;
	        case R.id.Tired:
	        	// Create a ColumnGraph object with the specified information (Color is not currently used for column graphs)
	    		ColumnGraph cg6 = new ColumnGraph("Days", "Tiredness Level", "testdata", null, yValues, false, false, 11, 1, Color.BLUE);
	    		// Generate a column graph for the given mood
	    		GraphManager.generateMoodColumnGraph(getApplicationContext(), findViewById(R.id.chart_layout), cg6, "Tired");
	        	return true;
	        case R.id.Depressed:
	        	// Create a ColumnGraph object with the specified information (Color is not currently used for column graphs)
	    		ColumnGraph cg7 = new ColumnGraph("Days", "Depression Level", "testdata", null, yValues, false, false, 11, 1, Color.BLUE);
	    		// Generate a column graph for the given mood
	    		GraphManager.generateMoodColumnGraph(getApplicationContext(), findViewById(R.id.chart_layout), cg7, "Depressed");
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}