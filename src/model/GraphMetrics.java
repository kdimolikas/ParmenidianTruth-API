package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.cluster.BicomponentClusterer;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.filters.FilterUtils;
import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import edu.uci.ics.jung.algorithms.transformation.DirectionTransformer;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

@SuppressWarnings("rawtypes")
public class GraphMetrics extends BicomponentClusterer implements IGraphMetrics{
	
	private Graph<String, String> graph;

	
	public GraphMetrics(ArrayList<Table> nodes, ArrayList<ForeignKey> edges){
		
		super();
		
		graph = new DirectedSparseGraph<String, String>();
		addNodes(nodes);
		addEdges(edges);

		
	}

	public Graph<String, String> getGraph() {
		return graph;
	}
	
	private void addNodes(ArrayList<Table> nodes) {

		for (int i = 0; i <nodes.size(); ++i)
			graph.addVertex(nodes.get(i).getTableName());

	}

	private void addEdges(ArrayList<ForeignKey> edges) {

		for (int i = 0; i <edges.size(); ++i)
			graph.addEdge(edges.get(i).getSourceTable()+"|"+ edges.get(i).getTargetTable(), edges.get(i)
					.getSourceTable(), edges.get(i)
					.getTargetTable());

	}
	
	@SuppressWarnings("unchecked")
	public String generateVertexDegree(String vertex){
		
		
		vertex=vertex.replace(",","").trim();
		
		DegreeScorer ds = new DegreeScorer(graph);

	
		return ds.getVertexScore(vertex)+",";
		
	}
	
	
	@SuppressWarnings("unchecked")
	public String generateVertexBetweenness(String vertex){
		
		vertex=vertex.replace(",","").trim();
		
		
		 BetweennessCentrality ranker = new BetweennessCentrality(graph);
		 ranker.setRemoveRankScoresOnFinalize(false);
		 ranker.evaluate();
		
		return ranker.getVertexRankScore(vertex)+",";
		
	}
	
	@SuppressWarnings("unchecked")
	public String generateEdgeBetweenness(String edge){
		
		
		edge=edge.replace(",","").trim();

		 BetweennessCentrality ranker = new BetweennessCentrality(graph);
		 ranker.setRemoveRankScoresOnFinalize(false);
		 ranker.evaluate();
		
		return ranker.getEdgeRankScore(edge)+",";
		
		
	}
	
	public String generateVertexInDegree(String vertex){
		
		vertex=vertex.replace(",","").trim();
		
		return graph.inDegree(vertex)+",";
	}
	
	public String generateVertexOutDegree(String vertex){
		
		vertex=vertex.replace(",","").trim();
		
		return graph.outDegree(vertex)+",";
	}
	
	public String getVertexCount(){
		
		return graph.getVertexCount()+",";
		
	}
	
	public String getVertexCountForGcc() {
		
		WeakComponentClusterer<String, String> wcc = new WeakComponentClusterer<String, String>();
		Collection<Graph<String,String>> ccs = FilterUtils.createAllInducedSubgraphs(wcc.transform(graph),graph);
		
		@SuppressWarnings("unused")
		DistanceStatistics ds = new DistanceStatistics();

		
		Graph<String,String> giantConnectedComponent = null;
		int max=0;
		
		for(Graph<String,String> g: ccs){
			if(g.getVertexCount()>max){
				max=g.getVertexCount();
				giantConnectedComponent=g;
				
			}
			
		}
		
		return giantConnectedComponent.getVertexCount()+",";
		
	}
	
	public String getEdgeCount(){
		
		
		return graph.getEdgeCount()+",";
		
		
	}
	
	public String getEdgeCountForGcc() {
		
		WeakComponentClusterer<String, String> wcc = new WeakComponentClusterer<String, String>();
		Collection<Graph<String,String>> ccs = FilterUtils.createAllInducedSubgraphs(wcc.transform(graph),graph);
		
		@SuppressWarnings("unused")
		DistanceStatistics ds = new DistanceStatistics();

		
		Graph<String,String> giantConnectedComponent = null;
		int max=0;
		
		for(Graph<String,String> g: ccs){
			if(g.getVertexCount()>max){
				max=g.getVertexCount();
				giantConnectedComponent=g;
				
			}
			
		}
		
		return giantConnectedComponent.getEdgeCount()+",";
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	public String getGraphDiameter(){
		
		
		WeakComponentClusterer<String, String> wcc = new WeakComponentClusterer<String, String>();
		Collection<Graph<String,String>> ccs = FilterUtils.createAllInducedSubgraphs(wcc.transform(graph),graph);
		
		DistanceStatistics ds = new DistanceStatistics();

		
		Graph<String,String> giantConnectedComponent = null;
		int max=0;
		
		for(Graph<String,String> g: ccs){
			if(g.getVertexCount()>max){
				max=g.getVertexCount();
				giantConnectedComponent=g;
				
			}
			
		}
		
		DirectionTransformer directionTransformer = new DirectionTransformer();
		Factory graphFactoryUndirected = UndirectedSparseGraph.getFactory();
        Factory edgeFactoryUndirected = new Factory<Integer>() {
    		Integer edgeCountUndirected=0;

        	public Integer create() { 
				return edgeCountUndirected++; 
        	} 
        }; 
		
		return ds.diameter(directionTransformer.toUndirected(giantConnectedComponent,graphFactoryUndirected,edgeFactoryUndirected,true))+",";
		
		
	}
	
	
	public String getNumberOfConnectedComponents(){

		WeakComponentClusterer<String, String> wcc = new WeakComponentClusterer<String, String>();
		Collection<Graph<String,String>> ccs = FilterUtils.createAllInducedSubgraphs(wcc.transform(graph),graph);
		
		int numberOfConnectedComponents=0;

		for(Graph<String,String> g: ccs){
			if(g.getVertexCount()>1)
				numberOfConnectedComponents++;			
		}
		
		return numberOfConnectedComponents + ",";
		
	}
	
	public Map<String,Double> getClusteringCoefficient(){
		
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics();
		
		Map<String, Double> collection =Metrics.clusteringCoefficients(graph);
		
		return collection;
		
	}

}
