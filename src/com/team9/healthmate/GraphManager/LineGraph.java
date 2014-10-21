package com.team9.healthmate.GraphManager;

/**
 * Stores necessary data used in creating
 * a line graph. This object is required
 * as a parameter to the GraphManager
 * class in order to generate a line graph.
 * @author Steve
 *
 */
public class LineGraph extends GraphData {
	int color;
	int strokeWidth;
	int pointWidth;
	
	/**
	 * Constructor used for automatic
	 * axis generation.
	 * @param xName		Name for x-axis
	 * @param yName		Name for y-axis
	 * @param fileN		Name of file with point data
	 * @param xLines	Flag for showing x-axis lines
	 * @param yLines	Flag for showing y-axis lines
	 * @param c			Color for lines (and points)
	 * @param sWidth	Width of line stroke
	 * @param pWidth	Width of point radius
	 */
	public LineGraph(String xName, String yName, String fileN, boolean xLines, boolean yLines, int c, int sWidth, int pWidth) {
		super(xName, yName, fileN, xLines, yLines);
		xAxisLines = xLines;
		yAxisLines = yLines;
		color = c;
		strokeWidth = sWidth;
		pointWidth = pWidth;
	}
	
	/**
	 * Constructor used for explicit
	 * axis generation.
	 * @param xName		Name for x-axis
	 * @param yName		Name for y-axis
	 * @param fileN		Name of file with point data
	 * @param xAxisV	Int array for all x-axis values
	 * @param yAxisV	Int array for all y-axis values
	 * @param xLines	Flag for showing x-axis values
	 * @param yLines	Flag for showing y-axis values
	 * @param c			Color for lines (and points)
	 * @param sWidth	Width of line stroke
	 * @param pWidth	Width of point radius
	 */
	public LineGraph(String xName, String yName, String fileN, int[] xAxisV, int[] yAxisV, boolean xLines, boolean yLines, int c, int sWidth, int pWidth) {
		super(xName, yName, fileN, xAxisV, yAxisV, xLines, yLines);
		xAxisLines = xLines;
		yAxisLines = yLines;
		color = c;
		strokeWidth = sWidth;
		pointWidth = pWidth;
	}
}