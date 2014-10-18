package com.team9.healthmate;

import com.team9.healthmate.GraphManager.GraphManager;

import android.app.Activity;
import android.os.Bundle;

public class ChartDemo extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart_demo);
		
		GraphManager.writeRandomData(getApplicationContext());
		
		GraphManager.getPointData(getApplicationContext(), findViewById(R.id.chart_layout));
	}
}