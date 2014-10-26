package com.team9.healthmate;

import com.team9.healthmate.GraphManager.ColumnGraph;
import com.team9.healthmate.GraphManager.GraphManager;
import com.team9.healthmate.GraphManager.LineGraph;

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
	View view;
	Context context;
	boolean genLineGraphs;
	LineGraph[] lineGraph;
	ColumnGraph[] columnGraph;
	
	/**
	 * Sets up references for further use.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart_demo);
		
		// Set references to objects we'll use later (cleaner code)
		context = getApplicationContext();
		view = findViewById(R.id.chart_layout);
		
		createGraphs();
	}
	
	/**
	 * Creates ColumnGraph and LineGraph
	 * object arrays for later calls to the
	 * GraphManager class.
	 */
	public void createGraphs() {
		genLineGraphs = true;
		
		// Set up ColumnGraph objects for graph generation
		String[] moods = {"General Wellness Levels", "Happiness Levels", "Motivation Levels", "Stress Levels", 
				"Anger Levels", "Lethargy Levels", "Depression Levels"};
		
		// Other colors are possible
		int[] colors = {Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.RED, Color.LTGRAY, Color.DKGRAY};
		
		columnGraph = new ColumnGraph[moods.length];
		for (int i = 0; i < columnGraph.length; i++) {
			columnGraph[i] = new ColumnGraph("Days", moods[i], "testdata", false, false, colors[i]);
		}
		
		lineGraph = new LineGraph[moods.length];
		for (int i = 0; i < lineGraph.length; i++) {
			lineGraph[i] = new LineGraph("Days", moods[i], "testdata", false, true, colors[i], 1, 3);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.graphs, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * Generates graph for the selected mood.
	 * Will generate a Line Graph or a Column
	 * Graph depending on the flag's value (to be
	 * toggled with a button on the Action Bar
	 * at a later date).
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Necessary to wipe the view where the graph
		// will be displayed
		((RelativeLayout) view).removeAllViews();

		// Generate a new ColumnGraph object with desired
		// properties depending on which option was selected
		// and then generate a new Column Graph to display
	    switch (item.getItemId()) {
	        case R.id.JustOk:
	        	if (genLineGraphs) {
	        		GraphManager.generateMoodLineGraph(context, view, lineGraph[0], "Just Ok");
	        	} else {
	        		GraphManager.generateMoodColumnGraph(context, view, columnGraph[0], "Just Ok");
	        	}
	            return true;
	        case R.id.Happy:
	        	if (genLineGraphs) {
	        		GraphManager.generateMoodLineGraph(context, view, lineGraph[1], "Happy");
	        	} else {
	        		GraphManager.generateMoodColumnGraph(context, view, columnGraph[1], "Happy");
	        	}
	            return true;
	        case R.id.Motivated:
	        	if (genLineGraphs) {
	        		GraphManager.generateMoodLineGraph(context, view, lineGraph[2], "Motivated");
	        	} else {
	        		GraphManager.generateMoodColumnGraph(context, view, columnGraph[2], "Motivated");
	        	}
	        	return true;
	        case R.id.Stressed:
	        	if (genLineGraphs) {
	        		GraphManager.generateMoodLineGraph(context, view, lineGraph[3], "Stressed");
	        	} else {
	        		GraphManager.generateMoodColumnGraph(context, view, columnGraph[3], "Stressed");
	        	}
	        	return true;
	        case R.id.Angry:
	        	if (genLineGraphs) {
	        		GraphManager.generateMoodLineGraph(context, view, lineGraph[4], "Angry");
	        	} else {
	        		GraphManager.generateMoodColumnGraph(context, view, columnGraph[4], "Angry");
	        	}
	        	return true;
	        case R.id.Tired:
	        	if (genLineGraphs) {
	        		GraphManager.generateMoodLineGraph(context, view, lineGraph[5], "Tired");
	        	} else {
	        		GraphManager.generateMoodColumnGraph(context, view, columnGraph[5], "Tired");
	        	}
	        	return true;
	        case R.id.Depressed:
	        	if (genLineGraphs) {
	        		GraphManager.generateMoodLineGraph(context, view, lineGraph[6], "Depressed");
	        	} else {
	        		GraphManager.generateMoodColumnGraph(context, view, columnGraph[6], "Depressed");
	        	}
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}