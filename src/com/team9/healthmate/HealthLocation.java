package com.team9.healthmate;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.team9.healthmate.R;

public class HealthLocation extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_location);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
