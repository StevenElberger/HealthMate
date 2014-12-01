/* @author Davit Avetikyan
 * 	
 * 
 */
package com.team9.healthmate;

import com.team9.healthmate.GraphManager.ColumnGraph;
import com.team9.healthmate.GraphManager.GraphManager;
import com.team9.healthmate.GraphManager.LineGraph;

import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

/**
 * 
 * @author Davit
 *	The MoodsGraph Displays user entered 
 *	data in graphical charts and diagrams.
 *	The data is received from local stored 
 *	android file.
 */
@SuppressLint("ShowToast")
public class MoodsGraph extends Fragment implements OnItemSelectedListener {
	
	View viewGraph;
	Context context;
	boolean genLineGraphs;
	LineGraph[] lineGraph;
	ColumnGraph[] columnGraph;
	 Spinner spinner1;
	 //String[] paths = {"Happy", "Motivated", "Stressed", "Angry", "Tired", "Depressed"};
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_moods_graph, container, false);
					 
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.mood_arrays, android.R.layout.simple_spinner_item); 
		
		spinner1 = (Spinner)rootView.findViewById(R.id.spinner1);
		spinner1.setAdapter(adapter);
		spinner1.setOnItemSelectedListener(this);
		
		context = getActivity();
		viewGraph = rootView.findViewById(R.id.mood_graph_layout);
 
		createGraphs();
		                 
        return rootView;      
        
}
	
	public void createGraphs() {
		genLineGraphs = false;
		
		// Set up ColumnGraph objects for graph generation
		String[] moods = { "Happiness Levels", "Motivation Levels", "Stress Levels", 
				"Anger Levels", "Lethargy Levels", "Depression Levels"};
		
		// Other colors are possible
		int[] colors = {Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.RED, Color.LTGRAY};
		
		columnGraph = new ColumnGraph[moods.length];
		for (int i = 0; i < columnGraph.length; i++) {
			columnGraph[i] = new ColumnGraph("Days", moods[i], "testdata", false, false, colors[i]);
		}
				
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		((RelativeLayout) viewGraph).removeAllViews();
		
		int pos = spinner1.getSelectedItemPosition();
		
		switch(pos){
		case 0:
			GraphManager.generateMoodColumnGraph(context, viewGraph, columnGraph[0], "Happy");
			break;
		case 1:
			GraphManager.generateMoodColumnGraph(context, viewGraph, columnGraph[1], "Motivated");
			break;
		case 2:
			GraphManager.generateMoodColumnGraph(context, viewGraph, columnGraph[2], "Stressed");
			break;
		case 3:
			GraphManager.generateMoodColumnGraph(context, viewGraph, columnGraph[3], "Angry");
			break;
		case 4:
			GraphManager.generateMoodColumnGraph(context, viewGraph, columnGraph[4], "Tired");
			break;
		case 5:
			GraphManager.generateMoodColumnGraph(context, viewGraph, columnGraph[5], "Depressed");
			break;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}		
	
		
	
}
