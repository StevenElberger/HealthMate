package com.team9.healthmate;

import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

import com.team9.healthmate.GraphManager.GraphManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * An example of a line column dependency
 * graph using averages for each mood as the
 * column values and updating the line graph
 * with the selected column's data.
 * @author Steve
 *
 */
public class AnimatedChartDemo extends Activity {
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
		setContentView(R.layout.activity_animated_chart_demo);
		
		// Set references to objects we'll use later (cleaner code)
		context = getApplicationContext();
		view = findViewById(R.id.animated_chart_layout);
		
		lcv = (LineChartView) findViewById(R.id.chart_top);
		ccv = (ColumnChartView) findViewById(R.id.chart_bottom);
		
		GraphManager.setUpInitialLineChart(context, lcv);
		GraphManager.setUpMoodColumnGraph(context, ccv);
		
		ccv.setOnValueTouchListener(new ValueTouchListener());
	}
	
	/**
	 * Detects when a column is selected.
	 * @author Steve
	 *
	 */
	private class ValueTouchListener implements ColumnChartView.ColumnChartOnValueTouchListener {

		@Override
		public void onValueTouched(int selectedLine, int selectedValue, ColumnValue value) {
			GraphManager.updateLineData(lcv, context, value.getColor());
		}

		@Override
		public void onNothingTouched() {
			
		}
	}
}