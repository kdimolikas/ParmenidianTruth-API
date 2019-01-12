package dataImport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.DBVersion;

/**
 * 	Responsible for utilizing data provided by Hecate ({@link externalTools} package) to create 
 *  episodes of schema evolution ({@link DBVersion}) and changes between successive versions (transitions).
 * 	@author KD
 *	@since 2018-02-13
 */

public interface IHecateImportManager {

	public void createTransitions(File folder) throws Exception;
	
	public ArrayList<DBVersion> parseSql(String sqlFiles);
	
	public ArrayList<Map<String,Integer>> parseXml(String xmlFile);

	public List<Map<String,Integer>> getSumUpdates();
	

}