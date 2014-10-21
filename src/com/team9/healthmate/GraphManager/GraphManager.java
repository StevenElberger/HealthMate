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
	 * Debug purposes only
	 * @param appContext	The context of the Activity calling
	 * @param v				The view to display the graph in
	 */
	public static void getColumnData(Context appContext, View v) {
		int numSubcolumns = 1;
		int numColumns = 8;
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
	 * Grabs data from the given file name for the moods
	 * activity.
	 * @param appContext	Context of the activity calling
	 * @param fileName		Name of the file with moods data
	 * @param graphColor	Not currently being used, but the color specified in the graph object
	 * @return				Returns a map (Key = mood, Value = column values for that mood)
	 */
	public static Map<String, List<ColumnValue>> getColumnMoodData(Context appContext, String fileName, int graphColor) {
		Map<String, List<ColumnValue>> data = new HashMap<String, List<ColumnValue>>();
		List<ColumnValue> justOkColumn = new ArrayList<ColumnValue>();
		List<ColumnValue> happyColumn = new ArrayList<ColumnValue>();
		List<ColumnValue> motivatedColumn = new ArrayList<ColumnValue>();
		List<ColumnValue> stressedColumn = new ArrayList<ColumnValue>();
		List<ColumnValue> angryColumn = new ArrayList<ColumnValue>();
		List<ColumnValue> tiredColumn = new ArrayList<ColumnValue>();
		List<ColumnValue> depressedColumn = new ArrayList<ColumnValue>();
		
		try {
			// Grab data from data file
			ArrayList<Map<String, String>> moodData = DataStorageManager.readJSONObject(appContext, fileName);
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
					if (key.equals("Just Ok")) {
						ColumnValue justOk = new ColumnValue(Integer.parseInt(value), Color.GREEN); // change to graphColor eventually
						justOkColumn.add(justOk);
					} else if (key.equals("Happy")) {
						ColumnValue happy = new ColumnValue(Integer.parseInt(value), Color.BLUE);
						happyColumn.add(happy);
					} else if (key.equals("Motivated")) {
						ColumnValue motivated = new ColumnValue(Integer.parseInt(value), Color.CYAN);
						motivatedColumn.add(motivated);
					} else if (key.equals("Stressed")) {
						ColumnValue stressed = new ColumnValue(Integer.parseInt(value), Color.MAGENTA);
						stressedColumn.add(stressed);
					} else if (key.equals("Angry")) {
						ColumnValue angry = new ColumnValue(Integer.parseInt(value), Color.RED);
						angryColumn.add(angry);
					} else if (key.equals("Tired")) {
						ColumnValue tired = new ColumnValue(Integer.parseInt(value), Color.GRAY);
						tiredColumn.add(tired);
					} else if (key.equals("Depressed")) {
						ColumnValue depressed = new ColumnValue(Integer.parseInt(value), Color.DKGRAY);
						depressedColumn.add(depressed);
					}
				}
			}
			data.put("Just Ok", justOkColumn);
			data.put("Happy", happyColumn);
			data.put("Motivated", motivatedColumn);
			data.put("Stressed", stressedColumn);
			data.put("Angry", angryColumn);
			data.put("Tired", tiredColumn);
			data.put("Depressed", depressedColumn);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * Generates a mood column graph for the mood specified.
	 * @param appContext		The context of the activity
	 * @param v					The view to display the graph in
	 * @param columnGraph		The ColumnGraph object for necessary graph information
	 * @param mood				The mood to be graphed
	 */
	public static void generateMoodColumnGraph(Context appContext, View v, ColumnGraph columnGraph, String mood) {
		// list of columns
		Map<String, List<ColumnValue>> moodData = getColumnMoodData(appContext, columnGraph.fileName, columnGraph.color);
		// values for the mood column we want
		List<ColumnValue> givenValues = moodData.get(mood);
		
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