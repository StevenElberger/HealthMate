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
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
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
	
	/**
	 * Grabs point data from the testdata file (for now) using
	 * the DataStorageManager class. Then iterates through
	 * all the key value pairs to generate PointValues which
	 * are then put into a graph.
	 * @param appContext	The context of the app calling this method.
	 * @return
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
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);
		
		// Set axes
		Axis axisX = new Axis();
		Axis axisY = new Axis();
		axisX.setName("Axis X");
		axisY.setName("Axis Y");
		
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