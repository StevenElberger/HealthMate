package com.team9.healthmate.GraphManager;

public class ColumnGraph extends GraphData {
	int color;
	int numColumns, numSubColumns;
	
	public ColumnGraph(String xName, String yName, String fileN, boolean xLines, boolean yLines, int nColumns, int nSubColumns, int c) {
		super(xName, yName, fileN, xLines, yLines);
		color = c;
		numColumns = nColumns;
		numSubColumns = nSubColumns;
	}
}
