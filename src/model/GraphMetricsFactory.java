package model;

import java.util.ArrayList;

public class GraphMetricsFactory {
	
	
	public IGraphMetrics getGraphMetrics(ArrayList<Table> nodes, ArrayList<ForeignKey> edges){
		
		return new GraphMetrics(nodes,edges);
	
				
	}
	
	
}