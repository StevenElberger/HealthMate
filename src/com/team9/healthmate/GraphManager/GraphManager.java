package com.team9.healthmate.GraphManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.team9.healthmate.DataManager.DataStorageManager;

import android.content.Context;
import android.graphics.Color;
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
import lecho.lib.hellocharts.util.Utils;
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
	 * generate some random data to a file in order to
	 * test the graph generating functions.
	 * @param appContext	The context of the app calling this method.
	 */
	public static void writeRandomData(Context appContext) {
		// Just some simple point data modeled after
		// the moods feature's data
		Map<String, String> testData = new HashMap<String, String>();
		testData.put("Day1", "1");
		testData.put("Day2", "5");
		testData.put("Day3", "9");
		testData.put("Day4", "10");
		testData.put("Day5", "6");
		testData.put("Day6", "2");
		testData.put("Day7", "1");
		testData.put("Day8", "5");
		testData.put("Day9", "4");
		testData.put("Day10", "6");
		testData.put("Day11", "7");
		
		try {
			DataStorageManager.writeJSONObject(appContext, "testdata", testData, true);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void getColumnData(Context appContext, View v) {
		int numSubcolumns = 1;
		int numColumns = 8;
		// Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
		List<Column> columns = new ArrayList<Column>();
		List<ColumnValue> values;
		for (int i = 0; i < numColumns; ++i) {

			values = new ArrayList<ColumnValue>();
			for (int j = 0; j < numSubcolumns; ++j) {
				values.add(new ColumnValue((float) Math.random() * 50f + 5, Utils.pickColor()));
			}

			Column column = new Column(values);
			columns.add(column);
		}
		
		Axis xAxis = new Axis();
		Axis yAxis = new Axis();
		xAxis.setName("Days");
		yAxis.setName("Mood Level");
		
		ColumnChartData data = new ColumnChartData(columns);
		data.setAxisXBottom(xAxis);
		data.setAxisYLeft(yAxis);
		
		ColumnChartView chart = new ColumnChartView(appContext);
		chart.setColumnChartData(data);
		
		((RelativeLayout) v).addView(chart);
		/*
		List<PointValue> values = new ArrayList<PointValue>();
		try {
			// Grab data from data file
			ArrayList<Map<String, String>> credentials = DataStorageManager.readJSONObject(appContext, "testdata");
			Iterator<Map<String, String>> iterator = credentials.iterator();
			Map<String, String> dataSet = new HashMap<String, String>();
			while (iterator.hasNext()) {
				// Go through all the keys
				dataSet = iterator.next();
				Iterator<String> it = dataSet.keySet().iterator();
				int xValues = 1;
				while (it.hasNext()) {
					// If the keys are what we're looking for
					// then put their values into points
					String key = it.next();
					String value = dataSet.get(key);
					if (key.contains("Day")) {
						PointValue point = new PointValue(xValues, Integer.parseInt(value));
						values.add(point);
						xValues++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// Generate lines from point data
		Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
		line.setStrokeWidth(1);
		line.setPointRadius(3);
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);
		
		// Set axes
		List<AxisValue> yValues = new ArrayList<AxisValue>();
		for (int i = 1; i < 11; i++) {
			yValues.add(new AxisValue(i));
		}
		List<AxisValue> xValues = new ArrayList<AxisValue>();
		for (int i = 1; i < 12; i++) {
			xValues.add(new AxisValue(i));
		}
		Axis axisX = new Axis();
		Axis axisY = new Axis();
		axisX.setName("Days");
		axisY.setName("Mood Level");
		axisY.setHasLines(true);
		axisY.setValues(yValues);
		axisX.setValues(xValues);
		
		LineChartData data = new LineChartData(lines);
		
		data.setAxisXBottom(axisX);
		data.setAxisYLeft(axisY);
		
		// Generate the graph and display it inside
		// the appropriate view
		LineChartView chart = new LineChartView(appContext);
		chart.setLineChartData(data);
		
		((RelativeLayout) v).addView(chart);
		*/
	}
	
	/**
	 * Grabs point data from the testdata file (for now) using
	 * the DataStorageManager class. Then iterates through
	 * all the key value pairs to generate PointValues which
	 * are then put into a graph.
	 * @param appContext	The context of the app calling this method.
	 * @param v				The layout the graph should be displayed in.
	 */
	public static void getPointData(Context appContext, View v) {
		List<PointValue> values = new ArrayList<PointValue>();
		try {
			// Grab data from data file
			ArrayList<Map<String, String>> credentials = DataStorageManager.readJSONObject(appContext, "testdata");
			Iterator<Map<String, String>> iterator = credentials.iterator();
			Map<String, String> dataSet = new HashMap<String, String>();
			while (iterator.hasNext()) {
				// Go through all the keys
				dataSet = iterator.next();
				Iterator<String> it = dataSet.keySet().iterator();
				int xValues = 1;
				while (it.hasNext()) {
					// If the keys are what we're looking for
					// then put their values into points
					String key = it.next();
					String value = dataSet.get(key);
					if (key.contains("Day")) {
						PointValue point = new PointValue(xValues, Integer.parseInt(value));
						values.add(point);
						xValues++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// Generate lines from point data
		Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
		line.setStrokeWidth(1);
		line.setPointRadius(3);
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);
		
		// Set axes
		List<AxisValue> yValues = new ArrayList<AxisValue>();
		for (int i = 1; i < 11; i++) {
			yValues.add(new AxisValue(i));
		}
		List<AxisValue> xValues = new ArrayList<AxisValue>();
		for (int i = 1; i < 12; i++) {
			xValues.add(new AxisValue(i));
		}
		Axis axisX = new Axis();
		Axis axisY = new Axis();
		axisX.setName("Days");
		axisY.setName("Mood Level");
		axisY.setHasLines(true);
		axisY.setValues(yValues);
		axisX.setValues(xValues);
		
		LineChartData data = new LineChartData(lines);
		
		data.setAxisXBottom(axisX);
		data.setAxisYLeft(axisY);
		
		// Generate the graph and display it inside
		// the appropriate view
		LineChartView chart = new LineChartView(appContext);
		chart.setLineChartData(data);
		
		((RelativeLayout) v).addView(chart);
	}
	
	/**
	 * Grabs point data from a file to 
	 * generate a line graph. Called from
	 * generateMoodLineGraph. Note: Needs
	 * to be made more general.
	 * @param appContext	Context from the activity creating the graph
	 * @param fileName		Name of the file containing the point data
	 * @return				Returns the list of point values
	 */
	public static List<PointValue> getMoodData(Context appContext, String fileName) {
		List<PointValue> dataPoints = new ArrayList<PointValue>();
		
		try {
			// Grab data from data file
			ArrayList<Map<String, String>> credentials = DataStorageManager.readJSONObject(appContext, fileName);
			Iterator<Map<String, String>> iterator = credentials.iterator();
			Map<String, String> dataSet = new HashMap<String, String>();
			while (iterator.hasNext()) {
				// Go through all the keys
				dataSet = iterator.next();
				Iterator<String> it = dataSet.keySet().iterator();
				int xValues = 1;
				while (it.hasNext()) {
					// If the keys are what we're looking for
					// then put their values into points
					String key = it.next();
					String value = dataSet.get(key);
					if (key.contains("Day")) {
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
	 * Generates a line graph given point data
	 * from the moods activity. Requires a Line Graph
	 * Data object to be passed with necessary information
	 * to generate the graph.
	 * @param appContext	Context from the activity calling this method
	 * @param v				View to display the graph in (should be a RelativeLayout)
	 * @param lineGraph		Line Graph Data object with necessary graph information
	 */
	public static void generateMoodLineGraph(Context appContext, View v, LineGraph lineGraph) {
		// Grab data, generate point values
		List<PointValue> dataPoints = getMoodData(appContext, lineGraph.fileName);
		
		// Generate lines from point data
		Line line = new Line(dataPoints).setColor(lineGraph.color).setCubic(true);
		line.setStrokeWidth(lineGraph.strokeWidth);
		line.setPointRadius(lineGraph.pointWidth);
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);
		
		// Create axes
		Axis axisY = new Axis();
		Axis axisX = new Axis();
		List<AxisValue> yValues = new ArrayList<AxisValue>();
		List<AxisValue> xValues = new ArrayList<AxisValue>();
		
		// If not auto gen, set the axes' points
		if (!lineGraph.autoGenerateAxes) {
			for (int i : lineGraph.yAxisValues) {
				yValues.add(new AxisValue(i));
			}
			for (int i : lineGraph.xAxisValues) {
				xValues.add(new AxisValue(i));
			}
			axisY.setValues(yValues);
			axisX.setValues(xValues);
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