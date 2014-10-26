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
		
		// generate graphs, add viewport change listener, set up preview box
		GraphManager.generateMoodColumnGraphWithChart(getApplicationContext(), ccv, pccv, Color.GREEN, "Just Ok");
		pccv.setViewportChangeListener(new ViewportListener());
		previewX(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.preview_chart_demo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Sets up preview box
	 * @param animate	Use animation for current viewport
	 */
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
			// unnecessary to use animation when using preview chart - viewport changes too often
			ccv.setCurrentViewport(newViewport, false);
		}

	}
}
