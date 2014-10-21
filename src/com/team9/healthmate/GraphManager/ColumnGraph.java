package com.team9.healthmate.GraphManager;

/**
 * Stores necessary data used in creating
 * a column graph. This object is required
 * as a parameter to the GraphManager
 * class in order to generate a column graph.
 * @author Steve
 *
 */
public class ColumnGraph extends GraphData {
	int color;
	int numColumns, numSubColumns;
	
	public ColumnGraph(String xName, String yName, String fileN, boolean xLines, boolean yLines, int nColumns, int nSubColumns, int c) {
		super(xName, yName, fileN, xLines, yLines);
		color = c;
		numColumns = nColumns;
		numSubColumns = nSubColumns;
	}
	
	public ColumnGraph(String xName, String yName, String fileN, int[] xAxisV, int[] yAxisV, boolean xLines, boolean yLines, int nColumns, int nSubColumns, int c) {
		super(xName, yName, fileN, xAxisV, yAxisV, xLines, yLines);
		color = c;
		numColumns = nColumns;
		numSubColumns = nSubColumns;
	}
}