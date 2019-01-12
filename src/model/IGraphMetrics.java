package model;

import java.util.Map;

import edu.uci.ics.jung.graph.Graph;


/**
 * Providing various graph-based metrics for Diachronic Graph of DB schema. 
 * @author KD
 * @since 2018-02-19
 *
 */

public interface IGraphMetrics {
	
	Graph<String, String> getGraph();
	String generateVertexDegree(String vertex);
	String generateVertexInDegree(String vertex);
	String generateVertexOutDegree(String vertex);
	String generateVertexBetweenness(String vertex);
	String generateEdgeBetweenness(String edge);
	String getGraphDiameter();
	String getVertexCount();
	String getVertexCountForGcc();
	String getEdgeCount();
	String getEdgeCountForGcc();
	String getNumberOfConnectedComponents();
	Map<String,Double> getClusteringCoefficient();

}
