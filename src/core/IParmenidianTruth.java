package core;

import java.util.ArrayList;

import model.DBVersion;
import model.ForeignKey;
import model.Table;
import parmenidianEnumerations.Metric_Enums;

/**
 * Providing main functionalities of ParmenidianTruth project.
 * @author KD
 * @since 2018-02-19 (upd. in 2018-11)
 */
public interface IParmenidianTruth {
	
	
	public int loadProject(String sql,String targetFolder);
	
	public ArrayList<Integer> generateMetricsReport(ArrayList<Metric_Enums> metrics);
	
	public void visualize();

	public ArrayList<Table> getTables();

	public ArrayList<ForeignKey> getForeignKeys();
	
	public ArrayList<DBVersion> getVersions();

	public DBVersion getVersionWithId(int id);
	
	

}