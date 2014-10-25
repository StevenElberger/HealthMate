package com.team9.healthmate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

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
	LineChartView lcv;
	ColumnChartView ccv;
	LineChartData lineData;
	ColumnGraph columnGraph;
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
		
		createGraphs();
	}
	
	/**
	 * Creates ColumnGraph and LineGraph
	 * object arrays for later calls to the
	 * GraphManager class.
	 */
	public void createGraphs() {
		lcv = (LineChartView) findViewById(R.id.chart_top);
		ccv = (ColumnChartView) findViewById(R.id.chart_bottom);
		
		columnGraph = new ColumnGraph(null, null, "testdata", null, null, false, false, 0);
		
		ccv.setOnValueTouchListener(new ValueTouchListener());
	}
	
	private class ValueTouchListener implements ColumnChartView.ColumnChartOnValueTouchListener {

		@Override
		public void onValueTouched(int selectedLine, int selectedValue, ColumnValue value) {
			generateLineData(value.getColor(), 10);
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
		// Necessary to wipe the view where the graph
		// will be displayed
		//((RelativeLayout) view).removeAllViews();

		// Generate a new ColumnGraph object with desired
		// properties depending on which option was selected
		// and then generate a new Column Graph to display
	    switch (item.getItemId()) {
	        case R.id.JustOk:
	        		setUpLineChart(context, lcv);
	        		GraphManager.setUpMoodColumnGraph(context, columnGraph, ccv);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void generateLineData(int color, float range) {
		// Cancel last animation if not finished
		lcv.cancelDataAnimation();
		
		List<PointValue> points;
		switch (color) {
			case Color.GREEN:
				points = GraphManager.getMoodLineData(context, "testdata", "Just Ok");
				lineData.getAxisYLeft().setName("Just Ok");
				break;
			case Color.YELLOW:
				points = GraphManager.getMoodLineData(context, "testdata", "Happy");
				lineData.getAxisYLeft().setName("Happy");
				break;
			case Color.CYAN:
				points = GraphManager.getMoodLineData(context, "testdata", "Motivated");
				lineData.getAxisYLeft().setName("Motivated");
				break;
			case Color.MAGENTA:
				points = GraphManager.getMoodLineData(context, "testdata", "Stressed");
				lineData.getAxisYLeft().setName("Stressed");
				break;
			case Color.RED:
				points = GraphManager.getMoodLineData(context, "testdata", "Angry");
				lineData.getAxisYLeft().setName("Angry");
				break;
			case Color.LTGRAY:
				points = GraphManager.getMoodLineData(context, "testdata", "Tired");
				lineData.getAxisYLeft().setName("Tired");
				break;
			case Color.DKGRAY:
				points = GraphManager.getMoodLineData(context, "testdata", "Depressed");
				lineData.getAxisYLeft().setName("Depressed");
				break;
			default:
				points = GraphManager.getMoodLineData(context, "testdata", "Just Ok");
				
				break;
		}
		Line newLine = new Line(points);
		// Modify data targets
		Line line = lineData.getLines().get(0);
		line.setColor(color);
		PointValue oldValue, newValue;
		for (int i = 0; i < line.getValues().size(); i++) {
			oldValue = line.getValues().get(i);
			newValue = newLine.getValues().get(i);
			oldValue.setTarget(oldValue.getX(), newValue.getY());
		}

		// Start new data animation with 300ms duration
		lcv.startDataAnimation(300);
	}
	
	/**
	 * Sets up initial line chart
	 * @param appContext
	 * @param lcv
	 */
	public void setUpLineChart(Context appContext, LineChartView lcv) {
		List<PointValue> dataPoints = GraphManager.getMoodLineData(appContext, "testdata", "Just Ok");
		
		// Generate lines from point data
		Line line = new Line(dataPoints).setColor(Color.WHITE).setCubic(true);
		line.setStrokeWidth(1);
		line.setPointRadius(3);
		line.setHasLabelsOnlyForSelected(true);
		for (PointValue value : line.getValues()) {
			value.set(value.getX(), 5);
		}
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);
		
		// Create axes
		Axis axisY = new Axis();
		Axis axisX = new Axis();
		
		int[] yAxisValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		// If not auto gen, set the axis' points
		List<AxisValue> yValues = new ArrayList<AxisValue>();
		for (int i : yAxisValues) {
			yValues.add(new AxisValue(i));
		}
		axisY.setValues(yValues);
		
		// Set name and lines
		axisY.setHasLines(true);
		axisX.setHasLines(false);
		axisY.setName("Choose a Column");
		
		lineData = new LineChartData(lines);
		
		lineData.setAxisXBottom(axisX);
		lineData.setAxisYLeft(axisY);
		
		lcv.setValueSelectionEnabled(true);
		lcv.setLineChartData(lineData);
		
		// For build-up animation you have to disable viewport recalculation.
		lcv.setViewportCalculationEnabled(false);
		
		// And set initial max viewport and current viewport- remember to set viewports after data.
		Viewport v = new Viewport(0, 10, dataPoints.size(), 0);
		lcv.setMaxViewport(v);
		lcv.setCurrentViewport(v, false);

		lcv.setZoomType(ZoomType.HORIZONTAL);		
	}
}