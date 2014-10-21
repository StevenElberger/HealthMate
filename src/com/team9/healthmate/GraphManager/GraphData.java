package com.team9.healthmate.GraphManager;

/**
 * Parent class of all graph data objects.
 * Used for storing necessary information
 * of a generic graph.
 * @author Steve
 *
 */
public class GraphData {
	String xAxisName, yAxisName, fileName;
	int[] xAxisValues;
	int[] yAxisValues;
	boolean autoGenerateAxes;
	boolean xAxisLines, yAxisLines;
	
	/**
	 * Constructor used for automatic
	 * axis generation.
	 * @param xName		Name of x-axis
	 * @param yName		Name of y-axis
	 * @param fileN		Name of file containing point data
	 * @param xLines	Flag for showing x-axis lines
	 * @param yLines	Flag for showing y-axis lines
	 */
	public GraphData(String xName, String yName, String fileN, boolean xLines, boolean yLines) {
		xAxisName = xName;
		yAxisName = yName;
		fileName = fileN;
		xAxisLines = xLines;
		yAxisLines = yLines;
		autoGenerateAxes = true;
	}
	
	/**
	 * Constructor used for explicit
	 * axis generation.
	 * @param xName		Name of x-axis
	 * @param yName		Name of y-axis
	 * @param fileN		Name of file containing point data
	 * @param xAxisV	Int array of x-axis values
	 * @param yAxisV	Int array of y-axis values
	 * @param xLines	Flag for showing x-axis lines
	 * @param yLines	Flag for showing y-axis lines
	 */
	public GraphData(String xName, String yName, String fileN, int[] xAxisV, int[] yAxisV, boolean xLines, boolean yLines) {
		xAxisName = xName;
		yAxisName = yName;
		xAxisValues = xAxisV;
		yAxisValues = yAxisV;
		fileName = fileN;
		xAxisLines = xLines;
		yAxisLines = yLines;
		autoGenerateAxes = false;
	}
}