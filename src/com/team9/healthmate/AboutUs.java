package com.team9.healthmate;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutUs extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		String text = "Please leave us Feedback by Contacting us.\n We would like to hear from you.\n\n";
		String textBody = "Team of Developers:\n\n" + 
							"Gustavo Arce\n" + "gustavo.arce.719@my.csun.edu \n\n" + 
							"Michael Sandoval\n" + "michael.sandoval.660@my.csun.edu  \n\n" +
							"Steven Elberger\n" + "stevenelberger@gmail.com \n\n" +
							"Shiraz Yeghiazarian\n" + "shiraz.yeghiazarian.747@my.csun.edu\n\n" +
							"Joe Hoxsey\n" + "Joe.hoxsey.60@my.csun.edu \n\n" +
							"Davit Avetikyan\n" + "avetikyan.davit@gmail.com\n\n";
		
		TextView t1 = (TextView)findViewById(R.id.textView1);
		t1.setText(text);
		TextView t2 = (TextView)findViewById(R.id.textView2);
		t2.setText(textBody);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about_us, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
