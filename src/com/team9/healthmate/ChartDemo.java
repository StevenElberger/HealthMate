package com.team9.healthmate;

import com.team9.healthmate.GraphManager.ColumnGraph;
import com.team9.healthmate.GraphManager.GraphManager;
import com.team9.healthmate.GraphManager.LineGraph;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

public class ChartDemo extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart_demo);
		
		GraphManager.writeRandomData(getApplicationContext());
		
		ColumnGraph cg = new ColumnGraph("Days", "Mood Level", "testdata", false, false, 11, 1, Color.BLUE);
		GraphManager.generateMoodColumnGraph(getApplicationContext(), findViewById(R.id.chart_layout), cg);
		
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
}