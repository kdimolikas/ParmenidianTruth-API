package model;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import parmenidianEnumerations.Status;
import edu.uci.ics.jung.graph.Graph;

/**
 * Holds data about schema evolution and manipulates diachronic graph.
 * @author MK
 * @version {2.0 - modified by KD}
 * @since 2018-03-04
 *
 */

@SuppressWarnings({ "rawtypes", "unused" })
public class DiachronicGraph implements IDiachronicGraph{

	private ArrayList<DBVersion> versions = new ArrayList<DBVersion>();
	private ArrayList<Map<String,Integer>> transitions = new ArrayList<Map<String,Integer>>();//auxiliary class for creating DiachronicGraph
	
	private static ConcurrentHashMap<String, Table> graph = new ConcurrentHashMap<String, Table>();//union of tables
	private ConcurrentHashMap<String, ForeignKey> graphEdges = new ConcurrentHashMap<String, ForeignKey>();//union of edges
	
	private ArrayList<Table>  vertices= new ArrayList<Table>();//union of tables
	private ArrayList<ForeignKey> edges= new ArrayList<ForeignKey>();//union of edges

	private IGraphMetrics graphMetricsOfDiachronicGraph;
	private GraphMetricsFactory gmFactory= new GraphMetricsFactory();
	
	private List<Map<String,Integer>> sumUpdates = new ArrayList<Map<String,Integer>>();
	
	public DiachronicGraph() {}

	//made public by KD on 2018-02-15
	@Override
	public void updateLifetimeWithTransitions(){
		
		for(int i=0;i<versions.size();++i)
			if(i==0)
				setFirstVersion(versions.get(i));
			else if(i==versions.size()-1)
				setFinalVersion(versions.get(i),i);
			else
				setIntermediateVersion(versions.get(i),i);
	}
	
	//added by KD on 2018-02-15
	@Override
	public void loadDiachronicGraph(ArrayList<Table> v, ArrayList<ForeignKey> e, String in, String tf) {
//		public void loadDiachronicGraph(ArrayList<Table> v, ArrayList<ForeignKey> e, String in, String tf, int et,double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY) {
		
		int mode;
		
		this.setVertices(v);
		this.setEdges(e);
		fixGraph();	
		mode=1;
		generateGraphMetrics();
		//setTablesBirthStats();//added by KD on 2018-11-24
		//setTablesDuration();//added by KD on 2018-11-24
		//createVisualizer(in, tf, et,mode,frameX,frameY,scaleX,scaleY,centerX,centerY);
	}
	
	
	//added by KD on 2018-02-15
	/**
	 * Creates Diachronic Graph
	 * @return number of {@link #versions}
	 */
	@Override
	public int createDiachronicGraph(String in, String tf,List<Map<String,Integer>> updates) {
		
		int mode;
		
		createDiachronicGraph();
		mode=0;
		generateGraphMetrics();
		setTablesBirthStats();//added by KD on 2018-11-24
		setTablesDuration();//added by KD on 2018-11-24
		//setTablesUpdates();//added by KD on 2018-11-27
		this.setSumUpdates(updates);
		this.setTablesATU();//added by KD on 2018-11-27
		setTablesSurvivorStatus();//added by KD on 2018-12-07
		
		return versions.size();
	}

	//added by KD on 2018-02-15
	private void generateGraphMetrics() {
		
		graphMetricsOfDiachronicGraph = gmFactory.getGraphMetrics(vertices,edges);
		
	}
	
	//added by KD on 2018-02-15
//	private void createVisualizer(String in, String tf, int et,int mode,double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY) {
//		
//		visualizationOfDiachronicGraph = new DiachronicGraphVisualRepresentation(this,vertices,edges,in,tf,et,mode,frameX,frameY,scaleX,scaleY,centerX,centerY);
//		
//	}

	
	/**
	 * Sets tables' originating version.
	 * @since 2018-11-24
	 * @author KD
	 */
	private void setTablesBirthStats() {
		
		for (DBVersion v:this.versions) {
			int id = v.getVersionId();
			for(Table t:this.vertices) {
				if ((t.getBirthVersion() == 0) && (v.tableExists(t.getTableName())==1)) {
				
					t.setBirthVersion(id);
					t.setAttrsNumAtBirth(v.getTableAttrNum(t.getTableName()));

				}
			}
		}
		
		
	}

	
	/**
	 * Updates tables' duration for each version they exist.
	 * @since 2018-11-24
	 * @author KD
	 */
	private void setTablesDuration() {
		
		for (DBVersion v:this.versions)
			for(Table t:this.vertices) 
				if (v.tableExists(t.getTableName())==1)
					t.setDuration(t.getDuration()+1);
		
	}

	/**
	 * Sets the survivor status of tables.
	 * @since 2018-12-07
	 */
	private void setTablesSurvivorStatus() {
		
		DBVersion lastVersion = this.versions.get(this.versions.size()-1);
		
		for (Table t:this.vertices)
			if (lastVersion.tableExists(t.getTableName())==1)
				t.setSurvivorStatus(1);
			else
				t.setSurvivorStatus(0);
		
		
	}
	
	/**
	 *Sets tables' sum of updates.
	 *@since 2018-11-26
	 *@author KD
	*/
	private void setTablesUpdates(){
		
		for (DBVersion v:this.versions)
			for (Table t:this.vertices)
				if (v.tableExists(t.getTableName())==1)
					if (v.getTableStatus(t.getTableName())==2)
						t.setSumUpdates(t.getSumUpdates()+1);
		
	}
	
	
	/**
	 *Sets tables' Average Transitional Update (ATU) = sum(upd.)/dur.
	 *@since 2018-11-26
	 *@author KD
	*/
	private void setTablesATU(){
		
		for (Table t:this.vertices)
			t.setATU();
		
	}
	
	
	/**
	 * Trexw thn prwth version me to prwto Dictionary kai checkarw n dw an sthn
	 * 2h version exei svistei kapoios pinakas.Me endiaferei mono to deletion
	 * An kapoioi exoun ginei updated tha tous vapsw sthn 2h ekdosh,oxi edw
	 * @param versionFirst :firstVersion
	 */
	private void setFirstVersion(DBVersion versionFirst){
		
		for(int i=0;i<versionFirst.getTables().size();++i)
			if(transitions.get(0).containsKey(versionFirst.getTables().get(i).getTableName())
			&& transitions.get(0).get(versionFirst.getTables().get(i).getTableName())==Status.DELETION.getValue())
				versionFirst.getTables().get(i).setTableStatus(Status.DELETION.getValue());		
		
	}
	
	/**
	 * Trexw thn teleutaia version mou me to teleutaio dictionary mou,h thesh tou
	 * teleutaiou dictionary mou einai mia prin apo thn thesh ths teleutaias version mou.
	 * Psaxnw gia tables pou periexontai st dictionary mou KAI DEN einai deletions,einai 
	 * dhladh mono newTable kai UpdateTable kai tous vafw analoga me thn timh pou exei to
	 * dictionary mou.
	 * @param versionFinal :finalVersion
	 * @param k :H thesh ths teleutaias Version mou sthn Lista
	 */
	private void setFinalVersion(DBVersion versionFinal,int k){
		
		for(int i=0;i<versionFinal.getTables().size();++i)
			if(transitions.get(k-1).containsKey(versionFinal.getTables().get(i).getTableName())
			&& transitions.get(k-1).get(versionFinal.getTables().get(i).getTableName())!=Status.DELETION.getValue())
				versionFinal.getTables().get(i).setTableStatus(transitions.get(k-1).get(versionFinal.getTables().get(i).getTableName()));
		
	}
	
	private void setIntermediateVersion(DBVersion version,int k){
		
		for(int i=0;i<version.getTables().size();++i){
			//koitaw to mellontiko m dictionary
			if(transitions.get(k).containsKey(version.getTables().get(i).getTableName())
			&& transitions.get(k).get(version.getTables().get(i).getTableName())==Status.DELETION.getValue())
				version.getTables().get(i).setTableStatus(Status.DELETION.getValue());
			
			//koitaw to palho m dictionary
			if(transitions.get(k-1).containsKey(version.getTables().get(i).getTableName())
			&& transitions.get(k-1).get(version.getTables().get(i).getTableName())!=Status.DELETION.getValue())
				version.getTables().get(i).setTableStatus(transitions.get(k-1).get(version.getTables().get(i).getTableName()));				
		}
	}
	
	/**
	 * 
	 * @param updates
	 * @since 2018-12-09
	 */
	private void setSumUpdates(List<Map<String,Integer>> updates) {
		
			
		for (Table t:this.vertices) {
			
			String tableName = t.getTableName();
			int updNum = 0;
			
			for (int i=0;i<updates.size();i++) {
				
				Map<String,Integer> transition = new TreeMap<String,Integer>();
				transition = updates.get(i);
				
				if (transition.containsKey(tableName)) {
					//System.out.println("Name: "+tableName+"updates: "+transition.get(tableName));
					updNum += transition.get(tableName);
					
				}
				
			}
			
			t.setSumUpdates(updNum);
			//System.out.println("Table Name: "+t.getTableName()+" --> Sum of Updates: "+t.getSumUpdates());
			
		}
		
	}
	
	

	//se periptwsh pou o UniversalGraph kataskeuazetai apto graphml
	//ektos apo tis listes exw enhmerwmeno kai to graph gia thn grhgorh
	//prospelash twn komvwn
	private void fixGraph() {
		
		for(int i=0;i<vertices.size();++i){
			graph.put(vertices.get(i).getTableName(),vertices.get(i));
		}		
	}

	private void createDiachronicGraph() {
		
		for(int i=0;i<versions.size();++i){
			
			for(Table mt : versions.get(i).getTables())
				graph.putIfAbsent(mt.getTableName(), mt);//union of tables
			
			for(ForeignKey fk : versions.get(i).getVersionForeignKeys())
				graphEdges.putIfAbsent(fk.getKey(), fk);//union of FK
		}
		
		transformNodes();
		transformEdges();
		
	}

	private void transformEdges() {

		//metatroph tou hashmap se ArrayList 
		Iterator i=graphEdges.entrySet().iterator();
		
		while(i.hasNext()){
			
			  Map.Entry entry = (Map.Entry) i.next();

			  String key = (String)entry.getKey();

			  ForeignKey value = (ForeignKey)entry.getValue();
			  
			  edges.add(value);
		}
		
		
	}

	private void transformNodes() {
		
		
		//metatroph tou hashmap se ArrayList 
		Iterator i=graph.entrySet().iterator();		
		while(i.hasNext()){
			
			  Map.Entry entry = (Map.Entry) i.next();

			  String key = (String)entry.getKey();

			  Table value = (Table)entry.getValue();
			  
			  vertices.add(value);

		}	
		
	}	

	public ArrayList<Table> getVertices() {

		return vertices;
		
	}

	public ArrayList<ForeignKey> getEdges() {
		
		return edges;

	}

	public ConcurrentHashMap<String, Table> getDictionaryOfGraph() {
		return graph;
	}

	
	public void clear(){
		
		versions.clear();
		graph.clear();
		graphEdges.clear();
		vertices.clear();
		edges.clear();
		//visualizationOfDiachronicGraph=null;
		
	}
	
//	public void setPickingMode(){
//		
//		visualizationOfDiachronicGraph.setPickingMode();
//		
//	}
//	
//	public void setTransformingMode(){
//		
//		visualizationOfDiachronicGraph.setTransformingMode();
//		
//	}
//	
//	public void saveVertexCoordinates(String projectIni) throws IOException{
//
//		visualizationOfDiachronicGraph.saveVertexCoordinates(projectIni);
//		
//	}
//	
//	public void stopConvergence(){
//		
//		visualizationOfDiachronicGraph.stop();
//	}
//
//	public String getTargetFolder(){
//		
//		return visualizationOfDiachronicGraph.getTargetFolder();
//	}
//	
//	public void visualizeDiachronicGraph(VisualizationViewer< String, String> vizualizationViewer){
//		
//		visualizationOfDiachronicGraph.visualizeDiachronicGraph(vizualizationViewer);
//		
//	}
	
//	public void visualizeIndividualDBVersions(VisualizationViewer< String, String> vizualizationViewer,String targetFolder,int edgeType){
//		
//		int width = visualizationOfDiachronicGraph.getWidthOfVisualizationViewer();
//		int height = visualizationOfDiachronicGraph.getHeightOfVisualizationViewer();
//		
//		for(int i=0;i<versions.size();++i){
//			versions.get(i).setDetails(targetFolder, edgeType,width,height);
//			versions.get(i).visualizeEpisode(vizualizationViewer,this);
//		}
//		
//	}
		
//	public VisualizationViewer show(){
//		
//		return visualizationOfDiachronicGraph.show();
//		
//	}
//	
//	
//	public Dimension getUniversalFrame(){
//		
//		return visualizationOfDiachronicGraph.getUniversalFrame();
//	}
//	
//	public Point2D getUniversalCenter(){
//		
//		return visualizationOfDiachronicGraph.getUniversalCenter();
//
//	}
//	
//	public double getScaleX(){
//		
//		return visualizationOfDiachronicGraph.getScaleX();
//	}
//	
//	public double getScaleY(){
//		
//		return visualizationOfDiachronicGraph.getScaleY();
//	}
//	
//	public  double getFrameX(){
//		
//		return visualizationOfDiachronicGraph.getFrameX();
//	}
//	
//	public  double getFrameY(){
//		
//		return visualizationOfDiachronicGraph.getFrameY();
//	}
	
	public Graph getGraph(){
		
		return graphMetricsOfDiachronicGraph.getGraph();
		
	}

//	public Component refresh(double forceMult, int repulsionRange) {
//		
//		return this.visualizationOfDiachronicGraph.refresh(forceMult,repulsionRange);
//	}
//	
//	public Rectangle getUniversalBounds(){
//		
//		
//		return visualizationOfDiachronicGraph.getUniversalBounds();
//		
//
//	}
	
//created by KD on 13/04/17	
	public ArrayList<DBVersion> getVersions(){
		
		
		return versions;
		

	}
	

	/**
	 * 
	 * @param anId
	 * @return the DBVersion with the given id.
	 */
	public DBVersion getVersionWithId(int anId) {
		
		for (DBVersion v:this.versions) 
			if (v.getVersionId() == anId)
				return v;
		
		return this.versions.get(0);
		
	}
	
//created by KD on 13/04/17	
	public IGraphMetrics getGraphMetrics(){


		return graphMetricsOfDiachronicGraph;


	}
	
	
	//created by KD on 2018-02-14
	@Override
	public void setVersions(ArrayList<DBVersion> vrs) {

		this.versions = vrs;

	}
	
	
	//created by KD on 2018-02-14
	@Override
	public void setTransitions(ArrayList<Map<String,Integer>> trs) {

		this.transitions = trs;

	}
	
	
	private void  setEdges(ArrayList<ForeignKey> e) {
		
		this.edges = e;
	}


	private void setVertices(ArrayList<Table> v) {
	
		this.vertices = v;
	}

}
