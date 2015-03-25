package com.team9.healthmate;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageListViewArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
	
	
	public ImageListViewArrayAdapter(Context context, String [] values) {
		super(context, R.layout.list_progress, values);
		this.context = context;
		this.values = values;
	}
	public View getView(int pos, View convertView, ViewGroup parent)	{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.list_progress, parent, false);
			    TextView textView = (TextView) rowView.findViewById(R.id.counter);
			    TextView textView2 = (TextView) rowView.findViewById(R.id.unit);
			    ImageView imageView = (ImageView) rowView.findViewById(R.id.image_view);
			    
		    	textView.setText(values[pos]);
		    	switch(pos)	{
		    	case 0: textView2.setText(" steps"); break;
		    	case 1: textView2.setText(" miles - UC"); textView2.setTextColor(Color.YELLOW); break;
		    	case 2: textView2.setText(" height"); break;
		    	case 3: textView2.setText(" weight"); break;
		    	case 4: textView2.setText(" BMI - UC"); textView2.setTextColor(Color.YELLOW); break;
		    	}
			    
			    imageView.setImageResource(R.drawable.blue_cross_icon);
		return rowView;
		
	}

}
