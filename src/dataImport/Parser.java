package dataImport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.DBVersion;
import model.ForeignKey;

import model.Table;

public class Parser implements IParser {

	private IHecateImportManager hecateManager;
	private HecateImportManagerFactory hecateMngrFactory;
	
	
	private IGraphmlLoader graphmlLoader;
	private GraphmlLoaderFactory gmlLoaderFactory;
	
	
	public Parser() {
		
		hecateMngrFactory = new HecateImportManagerFactory();
		hecateManager = hecateMngrFactory.createHecateManager();
		gmlLoaderFactory = new GraphmlLoaderFactory();
		
	}
	
	
	@Override
	public ArrayList<DBVersion> getLifetime(String sqlFiles) {
		
		return hecateManager.parseSql(sqlFiles);
	}

	@Override
	public ArrayList<Map<String, Integer>> getTransitions(String xmlFile) {
		
		return hecateManager.parseXml(xmlFile);
	}


	@Override
	public void createGraphmlLoader(String graphml) throws FileNotFoundException {
		
		graphmlLoader = gmlLoaderFactory.createGraphmlLoader(graphml);
	}
	
	//2018-10-04
	@Override
	public IGraphmlLoader getGraphmlLoader() throws FileNotFoundException {
		
		return graphmlLoader;
	}


	@Override
	public ArrayList<Table> getNodes() {
		
		return graphmlLoader.getNodes();
	}


	@Override
	public ArrayList<ForeignKey> getEdges() {
	
		return graphmlLoader.getEdges();
	}

	@Override
	public List<Map<String,Integer>> getSumUpdates(){
		
		return hecateManager.getSumUpdates();
		
	}

	@Override
	public void createTransitions(File selectedFile) throws Exception {
		
		hecateManager.createTransitions(selectedFile);
		
	}

}