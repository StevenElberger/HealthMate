package com.team9.healthmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGridView extends BaseAdapter
{
	private Context mContext;
    private final String[] label;
    private final int[] Imageid;
      public CustomGridView(Context c,String[] label,int[] Imageid ) {
          mContext = c;
          this.Imageid = Imageid;
          this.label = label;
      }
    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return label.length;
    }
    @Override
    public Object getItem(int position) {
      // TODO Auto-generated method stub
      return null;
    }
    @Override
    public long getItemId(int position) {
      // TODO Auto-generated method stub
      return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      // TODO Auto-generated method stub
      View grid;
      LayoutInflater inflater = (LayoutInflater) mContext
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      
          if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_item, null);
            TextView textView = (TextView) grid.findViewById(R.id.item_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.item_image);
            textView.setText(label[position]);
            imageView.setImageResource(Imageid[position]);
          } else {
            grid = (View) convertView;
          }
      return grid;
    }
}
