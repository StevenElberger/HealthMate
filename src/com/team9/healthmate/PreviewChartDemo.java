package com.team9.healthmate;

import com.team9.healthmate.GraphManager.GraphManager;

import lecho.lib.hellocharts.ViewportChangeListener;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PreviewColumnChartView;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class PreviewChartDemo extends Activity {
	ColumnChartView ccv;
	ColumnChartData chartData;
	PreviewColumnChartView pccv;
	ColumnChartData pchartData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_chart_demo);
		
		ccv = (ColumnChartView) findViewById(R.id.column_chart_top);
		pccv = (PreviewColumnChartView) findViewById(R.id.column_chart_preview);
		
		GraphManager.generateMoodColumnGraphWithChart(getApplicationContext(), ccv, pccv, Color.GREEN, "Just Ok");
		pccv.setViewportChangeListener(new ViewportListener());
		previewX(true);
		pccv.setPreviewColor(Color.RED);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preview_chart_demo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void previewX(boolean animate) {
		Viewport tempViewport = new Viewport(ccv.getMaxViewport());
		float dx = tempViewport.width() / 4;
		tempViewport.inset(dx, 0);
		pccv.setCurrentViewport(tempViewport, animate);
		pccv.setZoomType(ZoomType.HORIZONTAL);
	}
	
	private class ViewportListener implements ViewportChangeListener {

		@Override
		public void onViewportChanged(Viewport newViewport) {
			// don't use animation, it is unnecessary when using preview chart because usually viewport changes
			// happens to often.
			ccv.setCurrentViewport(newViewport, false);
		}

	}
}
