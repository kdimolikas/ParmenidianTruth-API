package core;


import java.io.File;

import java.util.ArrayList;

import dataImport.HecateImportManagerFactory;
import dataImport.IHecateImportManager;
import model.DBVersion;
import model.ForeignKey;
import model.Table;
import parmenidianEnumerations.Metric_Enums;


/**
 * Handles all use cases of PT tool.
 * @author MK
 * @author KD
 * @version 2.0
 */
public class ParmenidianTruthManager implements IParmenidianTruth{
	
		private ModelManager modelManager;

			
		private IHecateImportManager importManager;
		private HecateImportManagerFactory imManagerFactory;
		private String outputFolder;
		
		
		
		public ParmenidianTruthManager(){
			
			imManagerFactory = new HecateImportManagerFactory();
					
			modelManager = ModelManager.getInstance();
			this.clear();
			importManager = imManagerFactory.createHecateManager();

			
		}
		
		
		private void clear(){
			
			modelManager.clear();
		}
		
		
		/**
		* @param sql - path for sql files.
		* @param targetFolder - output folder.
		* @return number of DB versions
		*/
		public int loadProject(String sql,String targetFolder){
			
			this.outputFolder = targetFolder;
			
			createTransitions(new File(sql));

			return modelManager.loadProject( sql,targetFolder);

		}
		
		
		
		private void createTransitions(File fileSelected){
			
			
			try {
				importManager.createTransitions(fileSelected);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
				

		//created by KD on 13/04/17
		public ArrayList<Integer> generateMetricsReport(ArrayList<Metric_Enums> metrics){

			try{

				return modelManager.generateMetricsReport(this.outputFolder, metrics);

			}catch(Exception e){

				System.out.println(e.getClass());
				return null;

			}
			
		}


		@Override
		public void visualize() {
			
			//Nothing done yet!
			
		}
		
		
		@Override
		public ArrayList<Table> getTables(){
			
			return modelManager.getTables();
		}

		@Override
		public ArrayList<ForeignKey> getForeignKeys(){
			
			return modelManager.getForeignKeys();
		}


		@Override
		public ArrayList<DBVersion> getVersions() {
			
			return modelManager.getVersions();
		}
		
		
		@Override
		public DBVersion getVersionWithId(int id) {
			
			return modelManager.getVersionWithId(id);
		}
	
}