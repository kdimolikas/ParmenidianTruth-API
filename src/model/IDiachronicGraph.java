package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 
 * Providing functionalities related to the model of DB schema.
 * @author KD
 * @since 2018-02-19
 * @since 2018-11-02
 *
 */

public interface IDiachronicGraph {
	
	public void clear();
	
	public ArrayList<Table> getVertices();
	
	public ArrayList<ForeignKey> getEdges();
	
	public ArrayList<DBVersion> getVersions();

	public DBVersion getVersionWithId(int anId);
	
	public IGraphMetrics getGraphMetrics();

	public void setVersions(ArrayList<DBVersion> vrs);

	public void setTransitions(ArrayList<Map<String, Integer>> trs);

	public void updateLifetimeWithTransitions();
	
	public void loadDiachronicGraph(ArrayList<Table> v,ArrayList<ForeignKey> e, String in, String tf);
	
	public int createDiachronicGraph(String in, String tf,List<Map<String,Integer>> updates);
	
	//public void setSumUpdates(List<Map<String,Integer>> updates);
}
