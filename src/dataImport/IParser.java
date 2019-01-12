package dataImport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.DBVersion;


/**
 * Responsible for parsing files containing info about schema evolution.
 * @author KD
 * @since 2018-02-16
 * 
 */

public interface IParser {
	
	public ArrayList<DBVersion> getLifetime(String sqlFiles);
	public ArrayList<Map<String,Integer>> getTransitions(String xmlFile);
	public List<Map<String,Integer>> getSumUpdates();
	public void createGraphmlLoader(String graphml) throws FileNotFoundException;
	public ArrayList<model.Table> getNodes();
	public ArrayList<model.ForeignKey> getEdges();
	public void createTransitions(File selectedFile) throws Exception;
	public IGraphmlLoader getGraphmlLoader() throws FileNotFoundException;//2018-10-04

}