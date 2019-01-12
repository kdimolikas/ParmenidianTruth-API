package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import edu.uci.ics.jung.graph.Graph;

/**
 * 
 * @author libathos
 *h klash Table einai ths Ekaths kai afora thn sql anaparastash twn table
 *h dikia m klash table edw einai model.table gia na apofigoume ta conflicts
 *kai periexei plirofories sxetika me thn anaparastash tou table ws komvo
 *
 */
public class DBVersion  {
	
	private String versionName;
	private ArrayList<Table> tablesWithin=new ArrayList<Table>();
	private ArrayList<ForeignKey> versionForeignKeys= new ArrayList<ForeignKey>();
	private DBVersionVisualRepresentation visualizationsOfDBVersion;
	private IGraphMetrics graphMetricsOfDBVersion;
	private GraphMetricsFactory gmFactory= new GraphMetricsFactory();
	private int versionId = -1; //added on 2018-11-06 by KD
	private Map<String,Integer> attrsNum; //added on 2018-11-23 by KD


	public DBVersion(ArrayList<Table> tablesWithin,ArrayList<ForeignKey> versionForeignKeys,
			String versionNameExtended,int vrsId){
		
		//1 set VersionName				
		String[] array =versionNameExtended.split(".sql",2);
		versionName=array[0];
		
		//2 set all the tables of the current version		
		this.tablesWithin=tablesWithin;
	
		//3 set all the FK dependencies of the current version
		 this.versionForeignKeys=versionForeignKeys;

		 graphMetricsOfDBVersion = gmFactory.getGraphMetrics(tablesWithin,versionForeignKeys);
				 //new GraphMetrics(tablesWithin,versionForeignKeys);
		 visualizationsOfDBVersion = new DBVersionVisualRepresentation(this,tablesWithin,versionForeignKeys,versionName);
		 
		this.versionId = vrsId;
		 
		this.attrsNum = new TreeMap<String,Integer>();
		 
		 //setTablesBirthVersions();
		 
		// updateTablesDuration();
		
	}
	
	
//	private void setTablesBirthVersions() {
//		
//		for (Table t:this.tablesWithin) {
//			
//			if (t.getBirthVersion()<0) {
//				t.setBirthVersion(versionId);
//				t.setAttrsNumAtBirth(attrsNum.get(t.getTableName()));
//			}
//		}
//		
//	}
	
	/**
	 * 
	 * @param tableName - name of the table
	 * @return 1 if table exists in this version, 0 otherwise
	 */
	public int tableExists(String tableName) {
		
		String tName;
		
		for (Table t:this.tablesWithin) {
			tName = t.getTableName(); 
			if (tName.equals(tableName))// {
				return 1;
		}
		
		return 0;
		
	}
	
	
	public int getTableStatus(String tableName){
		
		for (Table t:tablesWithin)
			if (t.getTableName().equals(tableName))
				return t.getTableStatus();
			
		return -1;
		
	}
	
	public int getTableAttrNum(String tableName) {
		
		
		return attrsNum.get(tableName);
	}
//	
//	private void updateTablesDuration() {
//		
//		for (Table t:this.tablesWithin) {
//			
//				t.setDuration(t.getDuration()+1);
//		}
//		
//	}
	
//	public void visualizeEpisode(VisualizationViewer< String, String> visualizationViewer,DiachronicGraph diachronicGraph){
//		
//		visualizationsOfDBVersion.createEpisodes(visualizationViewer,diachronicGraph.getDictionaryOfGraph(),diachronicGraph.getUniversalFrame(),diachronicGraph.getUniversalBounds(),diachronicGraph.getUniversalCenter(),diachronicGraph.getFrameX(),diachronicGraph.getFrameY(),diachronicGraph.getScaleX(),diachronicGraph.getScaleY());
//		
//	}

	/**
	 * 
	 * @param attrs - map of tables' names and attributes
	 * @since 2018-11-23
	 */
	public void setAttrsNum(Map<String,Integer> attrs) {
		
		this.attrsNum = attrs;
		
	}
	
	
	
	/**
	 * 
	 * @return map of tables' names and attributes
	 * @since 2018-11-23
	 */
	public Map<String,Integer> getAttrsNum() {
		
		return this.attrsNum;
		
	}
	
	public void setDetails(String tf, int et,int width,int height){
		
		visualizationsOfDBVersion.setDetails(tf, et,width, height);
		
	}

	public ArrayList<Table> getTables() {
		
		return tablesWithin;
	}



	public ArrayList<ForeignKey> getVersionForeignKeys() {
		return versionForeignKeys;
	}


//	public ArrayList<Table> getNodes() {
//		
//		return getTables();
//		
//	}


//	public ArrayList<ForeignKey> getEdges() {
//		
//		return getVersionForeignKeys();
//
//	}


	public String getVersion() {
		
		return this.versionName;
	}
	
	/**
	 * 
	 * @return the id of this version
	 * @since 2018-11-24
	 * 
	 */
	public int getVersionId() {
		
		return this.versionId;
	}
	
	@SuppressWarnings("rawtypes")
	public Graph getGraph(){
		
		return graphMetricsOfDBVersion.getGraph();
		
	}
	
	public String generateVertexDegree(String vertex){
		
		vertex=vertex.replace(",","");
		
		
		
		for(int i=0;i<tablesWithin.size();++i){
			if(vertex.equals(tablesWithin.get(i).getTableName())){
				vertex=vertex+",";
				return graphMetricsOfDBVersion.generateVertexDegree(vertex);
			}
		}
		
		return "*,";
	}
	
	public String generateVertexInDegree(String vertex){
		
		vertex=vertex.replace(",","");
		
		
		
		for(int i=0;i<tablesWithin.size();++i){
			if(vertex.equals(tablesWithin.get(i).getTableName())){
				vertex=vertex+",";
				return graphMetricsOfDBVersion.generateVertexInDegree(vertex);
			}
		}
		
		return "*,";	
	}
	
	public String generateVertexOutDegree(String vertex){
		
		vertex=vertex.replace(",","");
		
		
		
		for(int i=0;i<tablesWithin.size();++i){
			if(vertex.equals(tablesWithin.get(i).getTableName())){
				vertex=vertex+",";
				return graphMetricsOfDBVersion.generateVertexOutDegree(vertex);
			}
		}
		
		return "*,";
		
	}
	
	public String generateVertexBetweenness(String vertex){
		
		vertex=vertex.replace(",","");
		
		
		
		for(int i=0;i<tablesWithin.size();++i){
			if(vertex.equals(tablesWithin.get(i).getTableName())){
				vertex=vertex+",";
				return graphMetricsOfDBVersion.generateVertexBetweenness(vertex);
			}
		}
		
		return "*,";	
	}

	public String generateEdgeBetweenness(String edge) {
		
		edge=edge.replace(",","");
		
		
		
		for(int i=0;i<versionForeignKeys.size();++i){
			if(edge.equals(versionForeignKeys.get(i).getSourceTable()+"|"+versionForeignKeys.get(i).getTargetTable())){
				edge=edge+",";
				return graphMetricsOfDBVersion.generateEdgeBetweenness(edge);
			}
		}
		
		return "*,";	
	}
	
	public String getGraphDiameter(){
		
		
		return graphMetricsOfDBVersion.getGraphDiameter();
		
		
	}
	
	public String getVertexCount(){
		
		return graphMetricsOfDBVersion.getVertexCount();
		
	}
	
	public String getVertexCountForGcc() {
		
		return graphMetricsOfDBVersion.getVertexCountForGcc();
		
	}
	
	public String getEdgeCount(){
		
		
		return graphMetricsOfDBVersion.getEdgeCount();		
		
	}
	
	public String getEdgeCountForGCC(){
		
		return graphMetricsOfDBVersion.getEdgeCountForGcc();		
		
	}
	
	public String generateConnectedComponentsCountReport(){
		
		return graphMetricsOfDBVersion.getNumberOfConnectedComponents();
		
	}
	
	public Map<String,Double> getClusteringCoefficient(){
		
		return graphMetricsOfDBVersion.getClusteringCoefficient();
		
	}
	
}
