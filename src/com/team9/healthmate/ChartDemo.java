package com.team9.healthmate;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.model.Axis;

public class ChartDemo extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart_demo);
		
		// Set points
		List<PointValue> values = new ArrayList<PointValue>();
		values.add(new PointValue(0, 2));
		values.add(new PointValue(1, 4));
		values.add(new PointValue(2, 3));
		values.add(new PointValue(3, 4));
		
		// Set lines
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
		
		
		//LineChartView chart = (LineChartView) findViewById(R.id.chart);
		LineChartView chart = new LineChartView(getApplicationContext());
		chart.setLineChartData(data);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.chart_layout);
		layout.addView(chart);
	}
}