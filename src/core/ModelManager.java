package core;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import dataImport.ParserFactory;

import dataImport.IParser;
import model.DBVersion;
import model.DiachronicGraphFactory;
import model.ForeignKey;
import model.IDiachronicGraph;
import model.IMetricsReport;
import model.ReportFactory;
import model.Table;
import parmenidianEnumerations.Metric_Enums;

/**
 * Operating as a manager that communicates with {@link model}
 * @author MK
 * @version {2.0 - modified by KD-MZ-IK}
 * @modified by KD on 2018-10-05
 */

public class ModelManager {
	
	private static ModelManager modelManagerInstance = null;
	private IDiachronicGraph diachronicGraph=null; 
	private DiachronicGraphFactory factory = new DiachronicGraphFactory();

	private ParserFactory parserFactory;
	private IParser parser;

	
	protected ModelManager(){
		
		parserFactory = new ParserFactory();
		parser = parserFactory.createHecateParser();
		
	}
	
	
	public static ModelManager getInstance() {
		
		if (modelManagerInstance == null)
			modelManagerInstance = new ModelManager();

		
		return modelManagerInstance;
	}
	
	
	
	public void clear(){
		
		if(diachronicGraph!=null)		
			diachronicGraph.clear();
		
	}
	

	public int loadProject(String sql,String targetFolder){
		
		//File graphmlFile;
		File outputFolder = new File(targetFolder);
		
		if (!outputFolder.isDirectory())
			outputFolder.mkdirs();
		
		String xml = sql.concat("\\transitions.xml");
		File xmlFile = new File(xml);
		if (!xmlFile.exists())
			try {
				xmlFile.createNewFile();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		
		diachronicGraph=factory.createDiachronicGraph();
		this.setVersions(sql);
		this.setTransitions(xmlFile.getAbsolutePath());
		
		diachronicGraph.updateLifetimeWithTransitions();
		//parser = parserFactory.createHecateParser();//added on 2018-10-05 by KD
		

		return diachronicGraph.createDiachronicGraph(sql, outputFolder.getAbsolutePath(),parser.getSumUpdates());
		

		
	}


	//created by KD on 13/04/17
	public ArrayList<Integer> generateMetricsReport(String targetFolder, ArrayList<Metric_Enums> metrics){

		ReportFactory reportFactory = new ReportFactory();
		ArrayList<IMetricsReport> reportEngine = new ArrayList<IMetricsReport>();
		ArrayList<Integer> reportSizeList = new ArrayList<Integer>();

		for(int i=0;i<metrics.size();i++){

			reportEngine.add(reportFactory.getMetricsReportEngine(targetFolder, metrics.get(i),diachronicGraph));
			reportSizeList.add(i, reportEngine.get(i).generateMetricsReport());


		}

		reportEngine.clear();
		return reportSizeList; 
	}

	
	
	/**
	 * @author KD
	 * @param sqlFiles pathname of sql files.
	 * @since 2018-02-14
	 */
	private void setVersions(String sqlFiles) {

		diachronicGraph.setVersions(parser.getLifetime(sqlFiles));

	}
		
		
	/**
	 * @author KD
	 * @param pathname of file containing transitions.
	 * @since 2018-02-14
	 */
	private void setTransitions(String xmlFile) {

		diachronicGraph.setTransitions(parser.getTransitions(xmlFile));

	}
	
	
	public ArrayList<Table> getTables(){
		
		return diachronicGraph.getVertices();
	}

	
	public ArrayList<ForeignKey> getForeignKeys(){
		
		return diachronicGraph.getEdges();
	}


	public ArrayList<DBVersion> getVersions() {
		
		return diachronicGraph.getVersions();
	}
	

	public DBVersion getVersionWithId(int id) {
		
		return diachronicGraph.getVersionWithId(id);
	}
	
	
}