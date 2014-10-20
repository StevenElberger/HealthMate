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
	
	/**
	 * Constructor used for automatic
	 * axis generation.
	 * @param xName		Name of x-axis
	 * @param yName		Name of y-axis
	 * @param fileN		Name of file containing point data
	 */
	public GraphData(String xName, String yName, String fileN) {
		xAxisName = xName;
		yAxisName = yName;
		fileName = fileN;
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
	 */
	public GraphData(String xName, String yName, String fileN, int[] xAxisV, int[] yAxisV) {
		xAxisName = xName;
		yAxisName = yName;
		xAxisValues = xAxisV;
		yAxisValues = yAxisV;
		fileName = fileN;
		autoGenerateAxes = false;
	}
}