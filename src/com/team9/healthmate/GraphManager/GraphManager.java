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
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.ChartData;
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
import lecho.lib.hellocharts.view.PreviewColumnChartView;

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
	 * @param appContext	The context of the Activity calling this method
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
	 * @param appContext	Context of the Activity calling
	 * @param columnGraph	ColumnGraph object with necessary information
	 * @param mood			The mood to be graphed
	 * @return				Returns a list of column values for the specified mood
	 */
	public static List<ColumnValue> getMoodColumnData(Context appContext, ColumnGraph columnGraph, String mood) {
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
	 * @param appContext		The context of the Activity that called
	 * @param view				The view to display the graph in
	 * @param columnGraph		The ColumnGraph object for necessary graph information
	 * @param mood				The mood to be graphed
	 */
	public static void generateMoodColumnGraph(Context appContext, View view, ColumnGraph columnGraph, String mood) {
		// values for the mood column we want
		List<ColumnValue> givenValues = getMoodColumnData(appContext, columnGraph, mood);
		
		// create columns
		List<Column> columns = new ArrayList<Column>();
		List<ColumnValue> colValues;
		for (int i = 0; i < givenValues.size(); i++) {
			colValues = new ArrayList<ColumnValue>();
			colValues.add(givenValues.get(i));
			
			Column column = new Column(colValues);
			column.setHasLabelsOnlyForSelected(true);
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
		chart.setValueSelectionEnabled(true);
		chart.setColumnChartData(data);
		
		((RelativeLayout) view).addView(chart);
	}
	
	/**
	 * Grabs mood data from a file to 
	 * generate a line graph. Called from
	 * generateMoodLineGraph.
	 * @param appContext	Context from the Activity that called
	 * @param fileName		Name of the file containing the point data
	 * @param mood			The mood to be graphed
	 * @return				Returns the list of point values
	 */
	public static List<PointValue> getMoodLineData(Context appContext, String fileName, String mood) {
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
	 * @param appContext	Context of the Activity calling this method
	 * @param view			View to display the graph in (should be a RelativeLayout)
	 * @param lineGraph		Line Graph Data object with necessary graph information
	 * @param mood			The mood to be graphed
	 */
	public static void generateMoodLineGraph(Context appContext, View view, LineGraph lineGraph, String mood) {
		// Grab data, generate point values
		List<PointValue> dataPoints = getMoodLineData(appContext, lineGraph.fileName, mood);

		// Generate lines from point data
		Line line = new Line(dataPoints).setColor(lineGraph.color).setCubic(true);
		line.setStrokeWidth(lineGraph.strokeWidth);
		line.setPointRadius(lineGraph.pointWidth);
		line.setHasLabelsOnlyForSelected(true);
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
		chart.setValueSelectionEnabled(true);
		chart.setLineChartData(data);
		
		((RelativeLayout) view).addView(chart);
	}
	
	/**
	 * Grabs all the data for the moods activity.
	 * Used to reduce the number of times the data file
	 * needs to be read.
	 * @param appContext	Context of the Activity calling
	 * @param fileName		Name of the file with moods data
	 * @return				Returns a map (Key = mood, Value = column values for that mood)
	 */
	public static Map<String, List<ColumnValue>> getAllMoodColumnData(Context appContext, String fileName) {
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
						ColumnValue justOk = new ColumnValue(Integer.parseInt(value), Color.GREEN);
						justOkColumn.add(justOk);
					} else if (key.equals("Happy")) {
						ColumnValue happy = new ColumnValue(Integer.parseInt(value), Color.YELLOW);
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
						ColumnValue tired = new ColumnValue(Integer.parseInt(value), Color.LTGRAY);
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
	 * Calculates the average value for
	 * each data type and displays a column
	 * for each data type with its respective
	 * average.
	 * @param appContext	Context of the Activity
	 * @param ccv			ColumnChart to be created
	 */
	public static void setUpMoodColumnGraph(Context appContext, ColumnChartView ccv) {
		// values for the mood column we want
		Map<String, List<ColumnValue>> givenValues = getAllMoodColumnData(appContext, "testdata");
		
		String[] moods = {"Just Ok", "Happy", "Motivated", "Stressed", "Angry", "Tired", "Depressed"};
		int[] colors = {Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.RED, Color.LTGRAY, Color.DKGRAY};
		
		// create columns (average values per column)
		List<Column> columns = new ArrayList<Column>();
		List<ColumnValue> colValues;
		int sum;
		// iterate through each data type
		for (int i = 0; i < givenValues.size(); i++) {
			colValues = new ArrayList<ColumnValue>();
			colValues = givenValues.get(moods[i]);
			sum = 0;
			// calculate sum total of all values for this data
			for (int j = 0; j < colValues.size(); j++) {
				sum += (int) colValues.get(j).getValue();
			}
			// stick the average in a column
			List<ColumnValue> avgValue = new ArrayList<ColumnValue>();
			avgValue.add(new ColumnValue(sum / colValues.size(), colors[i]));
			Column column = new Column(avgValue);
			column.setHasLabelsOnlyForSelected(true);
			columns.add(column);
		}
		
		// set up axes
		Axis xAxis = new Axis();
		Axis yAxis = new Axis();
		
		int[] yAxisValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		List<AxisValue> yValues = new ArrayList<AxisValue>();
		for (int i : yAxisValues) {
			yValues.add(new AxisValue(i));
		}

		yAxis.setHasLines(true);
		yAxis.setName("Mood Averages");
		
		// set up chart
		ColumnChartData data = new ColumnChartData(columns);
		data.setAxisXBottom(xAxis);
		data.setAxisYLeft(yAxis);
		
		ccv.setValueSelectionEnabled(true);
		ccv.setColumnChartData(data);
	}
	
	/**
	 * Updates the line graph in a Line Column
	 * Dependence Graph when a column is selected
	 * using the data from the data type represented
	 * by that column.
	 * @param lcv			The line chart being updated
	 * @param context		Context of the Activity
	 * @param color			Color of the column selected
	 */
	public static void updateLineData(LineChartView lcv, Context context, int color) {
		// Cancel last animation if not finished
		lcv.cancelDataAnimation();
		
		ChartData lineData = lcv.getChartData();
		List<PointValue> points;
		// grab points for updating the line graph
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
				lineData.getAxisYLeft().setName("Just Ok");
				break;
		}
		
		Line newLine = new Line(points);
		// Modify data targets
		Line line = ((LineChartData) lineData).getLines().get(0);
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
	 * Sets up initial line chart in a Line Column
	 * Dependence Graph.
	 * @param appContext	Context of the Activity
	 * @param lcv			The line chart being updated
	 */
	public static void setUpInitialLineChart(Context appContext, LineChartView lcv) {
		// Doesn't actually use "Just Ok". Only uses it's x-axis values --spaghetti!!!--
		List<PointValue> dataPoints = GraphManager.getMoodLineData(appContext, "testdata", "Just Ok");
		LineChartData lineData = (LineChartData) lcv.getChartData();
		
		// Generate lines from point data
		Line line = new Line(dataPoints).setColor(Color.WHITE).setCubic(true);
		line.setStrokeWidth(1);
		line.setPointRadius(3);
		line.setHasLabelsOnlyForSelected(true);
		for (PointValue value : line.getValues()) {
			value.set(value.getX() - 1, 5);
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
		
		List<AxisValue> xValues = new ArrayList<AxisValue>();
		for (PointValue value : line.getValues()) {
			xValues.add(new AxisValue(value.getX()));
		}
		axisX.setValues(xValues);
		
		// Set name and lines
		axisY.setHasLines(true);
		axisX.setHasLines(false);
		axisY.setName("Choose a Column");
		
		lineData = new LineChartData(lines);
		
		lineData.setAxisXBottom(axisX);
		lineData.setAxisYLeft(axisY);
		
		lcv.setValueSelectionEnabled(true);
		lcv.setLineChartData(lineData);
		
		// For build-up animation you have to disable viewport recalculation
		lcv.setViewportCalculationEnabled(false);
		
		// And set initial max viewport and current viewport - remember to set viewports after data
		Viewport v = new Viewport(0, 10, dataPoints.size(), 0);
		lcv.setMaxViewport(v);
		lcv.setCurrentViewport(v, false);

		lcv.setZoomType(ZoomType.HORIZONTAL);		
	}
	
	public static List<ColumnValue> getColumnData(Context appContext, String fileName, int color, String mood) {
		List<ColumnValue> colValues = new ArrayList<ColumnValue>();
		
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
					if (key.equals(mood)) {
						ColumnValue moodValue = new ColumnValue(Integer.parseInt(value), color);
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
	 * Calculates the average value for
	 * each data type and displays a column
	 * for each data type with its respective
	 * average.
	 * @param appContext	Context of the Activity
	 * @param ccv			ColumnChart to be created
	 */
	public static void generateMoodColumnGraphWithChart(Context appContext, ColumnChartView ccv, PreviewColumnChartView pccv, int color, String mood) {
		// values for the mood column we want
		List<ColumnValue> givenValues = getColumnData(appContext, "testdata", color, mood);
		
		// create columns
		List<Column> columns = new ArrayList<Column>();
		List<ColumnValue> colValues;
		for (int i = 0; i < givenValues.size(); i++) {
			colValues = new ArrayList<ColumnValue>();
			colValues.add(givenValues.get(i));
			
			Column column = new Column(colValues);
			column.setHasLabelsOnlyForSelected(true);
			columns.add(column);
		}
		
		// set up axes
		Axis xAxis = new Axis();
		Axis yAxis = new Axis();
		
		int[] yAxisValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		List<AxisValue> yValues = new ArrayList<AxisValue>();
		for (int i : yAxisValues) {
			yValues.add(new AxisValue(i));
		}
		yAxis.setValues(yValues);
		yAxis.setHasLines(true);
		
		// set up chart
		ColumnChartData data = new ColumnChartData(columns);
		data.setAxisXBottom(xAxis);
		data.setAxisYLeft(yAxis);
		
		ColumnChartData pData = new ColumnChartData(data);
		for (Column column : pData.getColumns()) {
			for (ColumnValue value : column.getValues()) {
				value.setColor(Utils.DEFAULT_DARKEN_COLOR);
			}
		}
		pData.setAxisXBottom(xAxis);
		pData.setAxisYLeft(yAxis);
		pccv.setColumnChartData(pData);
		
		ccv.setColumnChartData(data);
		
	}
}