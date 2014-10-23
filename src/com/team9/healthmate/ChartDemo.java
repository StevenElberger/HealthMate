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
	View view;
	ColumnGraph[] columnGraph;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart_demo);
		
		// Set references to objects we'll use later (cleaner code)
		context = getApplicationContext();
		view = findViewById(R.id.chart_layout);
		
		// We know the values for moods are 1 - 10 so we'll explicitly set the y-axis values
		int[] yValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		
		// Set up ColumnGraph objects for graph generation
		String[] moods = {"General Wellness Levels", "Happiness Levels", "Motivation Levels", "Stress Levels", 
				"Anger Levels", "Lethargy Levels", "Depression Levels"};
		
		// Other colors are possible
		int[] colors = {Color.GREEN, Color.YELLOW, Color.CYAN, Color.WHITE, Color.RED, Color.LTGRAY, Color.DKGRAY};
		
		columnGraph = new ColumnGraph[moods.length];
		for (int i = 0; i < columnGraph.length; i++) {
			columnGraph[i] = new ColumnGraph("Days", moods[i], "testdata", null, yValues, false, false, colors[i]);
		}
		
		
		// Used for debug / example purposes
		//GraphManager.writeRandomData(context);

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
		// Necessary to wipe the view where the graph
		// will be displayed
		View v = findViewById(R.id.chart_layout);
		((RelativeLayout) v).removeAllViews();

		// Generate a new ColumnGraph object with desired
		// properties depending on which option was selected
		// and then generate a new Column Graph to display
	    switch (item.getItemId()) {
	        case R.id.JustOk:
	    		GraphManager.generateMoodColumnGraph(context, view, columnGraph[0], "Just Ok");
	            return true;
	        case R.id.Happy:
	    		GraphManager.generateMoodColumnGraph(context, view, columnGraph[1], "Happy");
	            return true;
	        case R.id.Motivated:
	    		GraphManager.generateMoodColumnGraph(context, view, columnGraph[2], "Motivated");
	        	return true;
	        case R.id.Stressed:
	    		GraphManager.generateMoodColumnGraph(context, view, columnGraph[3], "Stressed");
	        	return true;
	        case R.id.Angry:
	    		GraphManager.generateMoodColumnGraph(context, view, columnGraph[4], "Angry");
	        	return true;
	        case R.id.Tired:
	    		GraphManager.generateMoodColumnGraph(context, view, columnGraph[5], "Tired");
	        	return true;
	        case R.id.Depressed:
	    		GraphManager.generateMoodColumnGraph(context, view, columnGraph[6], "Depressed");
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}