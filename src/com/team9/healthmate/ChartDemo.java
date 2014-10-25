package com.team9.healthmate;

import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

import com.team9.healthmate.GraphManager.GraphManager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Used to show graph generation
 * features. Mainly used for debugging.
 * @author Steve
 *
 */
public class ChartDemo extends ActionBarActivity {
	View view;
	Context context;
	LineChartView lcv;
	ColumnChartView ccv;
	LineChartData lineData;
	ColumnChartData columnData;
	
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
		
		lcv = (LineChartView) findViewById(R.id.chart_top);
		ccv = (ColumnChartView) findViewById(R.id.chart_bottom);
		
		ccv.setOnValueTouchListener(new ValueTouchListener());
	}
	
	private class ValueTouchListener implements ColumnChartView.ColumnChartOnValueTouchListener {

		@Override
		public void onValueTouched(int selectedLine, int selectedValue, ColumnValue value) {
			GraphManager.updateLineData(lcv, context, value.getColor());
		}

		@Override
		public void onNothingTouched() {
			
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
		// Generate a new ColumnGraph object with desired
		// properties depending on which option was selected
		// and then generate a new Column Graph to display
	    if (item.getItemId() == R.id.JustOk) {
	    	GraphManager.setUpInitialLineChart(context, lcv);
    		GraphManager.setUpMoodColumnGraph(context, ccv);
	    }
	    return true;
	}
}