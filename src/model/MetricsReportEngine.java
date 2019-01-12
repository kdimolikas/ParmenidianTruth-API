package model;

import java.io.File;
import parmenidianEnumerations.Metric_Enums;
import java.io.PrintWriter;
import java.util.ArrayList;


/**
 * Creates reports (in .csv format) containing graph-related metrics.
 * @author KD
 * @since 2017-05-23
 * @version 1.0
 *
 */

public abstract class MetricsReportEngine implements IMetricsReport{
		
	protected String targetFolder;
	private File reportFile;
	protected ArrayList<DBVersion> versions;
	protected IGraphMetrics graphMetricsOfDiachronicGraph;
	protected int lines;
	protected int columns;
	protected ArrayList<Table>  vertices;
	protected ArrayList<ForeignKey> edges;
	protected Metric_Enums metric;
	protected String[][] report; 
	private PrintWriter writer;
	
	//Template method: determines steps to create metrics reports
	/**
	 * @return the size (in bytes) of the report file
	 */
	public int generateMetricsReport(){
		
				
		createCsvFile();//create a csv report file
			
		populateArray(); //populate array
		
		return printArrayIntoFile(); //print array into report file
		
		
	}
	
	
	//defined here
	private void createCsvFile(){
		
		try{
			
			reportFile = new File(targetFolder+"\\Report of "+metric.name().replace("_", " ").toLowerCase()+".csv");
		
		}catch (Exception e){
			
			System.out.println(e.getClass());
			System.out.println("Problem opening the report file.");
		
		}
			
	}
	
	
	//defined in subclasses of this class 
	protected abstract void populateArray();
			
	//defined here
	private int printArrayIntoFile(){
		
		try{
			
			writer = new PrintWriter(reportFile);
			
		} catch (Exception e) {
			
			 System.out.println(e.getClass());
			 System.out.println("Problem writing into the report file.");
		}
		
		for(int i=0;i<lines;i++){
			for(int j=0;j<columns;j++)
				writer.print(report[i][j]);
			writer.print("\n");
		}
				
		writer.close();
		
		return (int) reportFile.length();
	}
	
	public String[][] getReport(){return report;}

}