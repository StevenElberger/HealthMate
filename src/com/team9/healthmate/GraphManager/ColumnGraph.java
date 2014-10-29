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
	
	/**
	 * Constructor used for automatic
	 * axis generation.
	 * @param xName		Name for x-axis
	 * @param yName		Name for y-axis
	 * @param fileN		Name of file with data
	 * @param xLines	Flag for showing x-axis lines
	 * @param yLines	Flag for showing y-axis lines
	 * @param c			Color of columns
	 */
	public ColumnGraph(String xName, String yName, String fileN, boolean xLines, boolean yLines, int c) {
		super(xName, yName, fileN, xLines, yLines);
		color = c;
	}
	
	/**
	 * Constructor used for explicit
	 * axis generation.
	 * @param xName		Name for x-axis
	 * @param yName		Name for y-axis
	 * @param fileN		Name of file with data
	 * @param xAxisV	Int array for all x-axis values
	 * @param yAxisV	Int array for all y-axis values
	 * @param xLines	Flag for showing x-axis lines
	 * @param yLines	Flag for showing y-axis lines
	 * @param c			Color of columns
	 */
	public ColumnGraph(String xName, String yName, String fileN, int[] xAxisV, int[] yAxisV, boolean xLines, boolean yLines, int c) {
		super(xName, yName, fileN, xAxisV, yAxisV, xLines, yLines);
		color = c;
	}
}