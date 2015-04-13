package com.team9.healthmate;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class ImageListViewArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
	private final String[] goalValues;
	
	
	public ImageListViewArrayAdapter(Context context, String [] values, String [] goalValues) {
		super(context, R.layout.list_progress, values);
		this.context = context;
		this.values = values;
		this.goalValues = goalValues;
	}
	public View getView(int pos, View convertView, ViewGroup parent)	{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.list_progress, parent, false);
			    TextView textView = (TextView) rowView.findViewById(R.id.counter);
			    TextView textView2 = (TextView) rowView.findViewById(R.id.unit);
			    ImageView imageView = (ImageView) rowView.findViewById(R.id.image_view);
			    ProgressBar progBar = (ProgressBar) rowView.findViewById(R.id.progressBar1);
			    TextView goal = (TextView) rowView.findViewById(R.id.unit_goal);
			    TextView goalCounter = (TextView) rowView.findViewById(R.id.goal_counter);
			    
		    	textView.setText(values[pos]);
		    	switch(pos)	{
		    	case 0: textView2.setText(" steps"); goalCounter.setText(goalValues[0]); break;
		    	case 1: textView2.setText(" height"); progBar.setVisibility(View.INVISIBLE); 
		    			goal.setVisibility(View.INVISIBLE); goalCounter.setVisibility(View.INVISIBLE); break;
		    	case 2: textView2.setText(" weight"); goalCounter.setText("150"); progBar.setVisibility(View.INVISIBLE);
		    			goalCounter.setText(goalValues[1]); break;
		    	case 3: textView2.setText(" BMI"); goalCounter.setText("20.00"); break;
		    	}
			    
			    imageView.setImageResource(R.drawable.blue_cross_icon);
		return rowView;
		
	}

}
