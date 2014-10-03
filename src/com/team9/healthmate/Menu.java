package com.team9.healthmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.team9.healthmate.R;

public class Menu extends Activity {
	public ListView menu;
	public Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		String [] menuString = {
				"Moods","Medication","Steps","Find a Doctor","Note","Emergency"
		};
		menu = (ListView) findViewById(R.id.menu);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,menuString);
		menu.setAdapter(adapter);
		/*This is a listener to find the users option selected */
		menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(Menu.this, ""+position, Toast.LENGTH_SHORT).show();
				changeActivity(position);
			}
		});
	}
	
	public void changeActivity(int pos)	{
		intent = new Intent(this, getMenuItem(pos));
		startActivity(intent);
	}
	
	/* switch statement to find the correct option to goto*/
	public Class<?> getMenuItem(int pos)	{
		switch(pos)	{
			case 0: return Moods.class; 
			case 1: return Medication.class;
			case 2: return StepCounter.class;
			case 3:	return HealthLocation.class;
			case 4:	return Note.class;
			case 5:	return Emergency.class;
		}
		return null;
	}
}
