package com.team9.healthmate.GraphManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Responsible for generating graphs for different data
 * using the DataStorageManager class to grab the data
 * and generate it in a graph, giving it back to the
 * Activity that called.
 * @author Steve
 *
 */
public class GraphManager {
	
	/**
	 * Only necessary for debug purposes. This will
	 * generate some random mood data to a file in order to
	 * test the graph generating functions.
	 * @param appContext	The context of the Activity calling this method.
	 */
	public static void writeRandomData(Context appContext) {
		String[] moodKeys = {"Just Ok", "Happy", "Motivated", "Stressed", "Angry", "Tired", "Depressed"};
		// generate a data file with 31 randomly-generated surveys
		try {
			Random rand = new Random();
			Map<String, String> testData;
			for (int i = 0; i < 1; i++) {
				testData = new HashMap<String, String>();
				for (String s : moodKeys) {
					// generate a random value 1 - 10 for each mood
					int value = rand.nextInt(10) + 1;
					testData.put(s, "" + value);
				}
				DataStorageManager.writeJSONObject(appContext, "testdata", testData, false);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Grabs data from the given file name for the moods
	 * activity.
	 * @param appContext	Context of the activity calling
	 * @param columnGraph	ColumnGraph object with necessary information
	 * @param mood			The mood to be graphed
	 * @return				Returns a list of column values for the specified mood
	 */
	public static List<ColumnValue> getColumnMoodData(Context appContext, ColumnGraph columnGraph, String mood) {
		List<ColumnValue> colValues = new ArrayList<ColumnValue>();
		
		try {
			// Grab data from data file
			ArrayList<Map<String, String>> moodData = DataStorageManager.readJSONObject(appContext, columnGraph.fileName);
			Iterator<Map<String, String>> iterator = moodData.iterator();
			Map<String, String> dataSet = new HashMap<String, String>();
			
			while (iterator.hasNext()) {
				// Go through all the keys
				dataSet = iterator.next();
				Iterator<String> it = dataSet.keySet().iterator();
				while (it.hasNext()) {
					// If the keys are what we're looking for
					// then put their values into columns
					String key = it.next();
					String value = dataSet.get(key);
					if (key.equals(mood)) {
						ColumnValue moodValue = new ColumnValue(Integer.parseInt(value), columnGraph.color);
						colValues.add(moodValue);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return colValues;
	}
	
	/**
	 * Generates a mood column graph for the mood specified.
	 * @param appContext		The context of the activity
	 * @param view				The view to display the graph in
	 * @param columnGraph		The ColumnGraph object for necessary graph information
	 * @param mood				The mood to be graphed
	 */
	public static void generateMoodColumnGraph(Context appContext, View view, ColumnGraph columnGraph, String mood) {
		// values for the mood column we want
		List<ColumnValue> givenValues = getColumnMoodData(appContext, columnGraph, mood);
		
		// create columns
		List<Column> columns = new ArrayList<Column>();
		List<ColumnValue> colValues;
		for (int i = 0; i < givenValues.size(); i++) {
			colValues = new ArrayList<ColumnValue>();
			colValues.add(givenValues.get(i));
			
			Column column = new Column(colValues);
			columns.add(column);
		}
		
		// set up axes
		Axis xAxis = new Axis();
		Axis yAxis = new Axis();
		
		if (columnGraph.xAxisValues != null) {
			List<AxisValue> xValues = new ArrayList<AxisValue>();
			for (int i : columnGraph.xAxisValues) {
				xValues.add(new AxisValue(i));
			}
			xAxis.setValues(xValues);
		}
		if (columnGraph.yAxisValues != null) {
			List<AxisValue> yValues = new ArrayList<AxisValue>();
			for (int i : columnGraph.yAxisValues) {
				yValues.add(new AxisValue(i));
			}
			yAxis.setValues(yValues);
		}
		xAxis.setName(columnGraph.xAxisName);
		yAxis.setName(columnGraph.yAxisName);
		yAxis.setHasLines(true);
		
		// set up chart
		ColumnChartData data = new ColumnChartData(columns);
		data.setAxisXBottom(xAxis);
		data.setAxisYLeft(yAxis);
		
		ColumnChartView chart = new ColumnChartView(appContext);
		chart.setColumnChartData(data);
		
		((RelativeLayout) view).addView(chart);
	}
	
	/**
	 * Grabs mood data from a file to 
	 * generate a line graph. Called from
	 * generateMoodLineGraph.
	 * @param appContext	Context from the activity creating the graph
	 * @param fileName		Name of the file containing the point data
	 * @param mood			The mood to be graphed
	 * @return				Returns the list of point values
	 */
	public static List<PointValue> getLineMoodData(Context appContext, String fileName, String mood) {
		List<PointValue> dataPoints = new ArrayList<PointValue>();
		try {
			// Grab data from data file
			ArrayList<Map<String, String>> credentials = DataStorageManager.readJSONObject(appContext, fileName);
			Iterator<Map<String, String>> iterator = credentials.iterator();
			Map<String, String> dataSet = new HashMap<String, String>();
			int xValues = 1;
			while (iterator.hasNext()) {
				// Go through all the keys
				dataSet = iterator.next();
				Iterator<String> it = dataSet.keySet().iterator();
				while (it.hasNext()) {
					// If the keys are what we're looking for
					// then put their values into points
					String key = it.next();
					String value = dataSet.get(key);
					if (key.equals(mood)) {
						PointValue point = new PointValue(xValues, Integer.parseInt(value));
						dataPoints.add(point);
						xValues++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return dataPoints;
	}
	
	/**
	 * Generates a line graph given mood data
	 * from the moods activity. Requires a LineGraph
	 * object to be passed with necessary information
	 * to generate the graph.
	 * @param appContext	Context from the activity calling this method
	 * @param v				View to display the graph in (should be a RelativeLayout)
	 * @param lineGraph		Line Graph Data object with necessary graph information
	 */
	public static void generateMoodLineGraph(Context appContext, View v, LineGraph lineGraph, String mood) {
		// Grab data, generate point values
		List<PointValue> dataPoints = getLineMoodData(appContext, lineGraph.fileName, mood);

		// Generate lines from point data
		Line line = new Line(dataPoints).setColor(lineGraph.color).setCubic(true);
		line.setStrokeWidth(lineGraph.strokeWidth);
		line.setPointRadius(lineGraph.pointWidth);
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);
		
		// Create axes
		Axis axisY = new Axis();
		Axis axisX = new Axis();
		
		// If not auto gen, set the axis' points
		if (lineGraph.yAxisValues != null) {
			List<AxisValue> yValues = new ArrayList<AxisValue>();
			for (int i : lineGraph.yAxisValues) {
				yValues.add(new AxisValue(i));
			}
			axisY.setValues(yValues);
		}
		
		// Set name and lines
		axisY.setName(lineGraph.yAxisName);
		axisX.setName(lineGraph.xAxisName);
		axisY.setHasLines(lineGraph.yAxisLines);
		axisX.setHasLines(lineGraph.xAxisLines);
		
		LineChartData data = new LineChartData(lines);
		
		data.setAxisXBottom(axisX);
		data.setAxisYLeft(axisY);
		
		// Generate the graph and display it inside
		// the appropriate view
		LineChartView chart = new LineChartView(appContext);
		chart.setLineChartData(data);
		
		((RelativeLayout) v).addView(chart);
	}
}