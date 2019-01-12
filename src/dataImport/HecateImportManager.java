package dataImport;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.antlr.v4.runtime.RecognitionException;


import externalTools.Deletion;
import externalTools.Delta;
import externalTools.HecateParser;
import externalTools.Insersion;
import externalTools.Schema;
import externalTools.Table;
import externalTools.Transition;
import externalTools.TransitionList;
import externalTools.Transitions;
import externalTools.Update;
import fileFilter.SQLFileFilter;
import model.DBVersion;
import model.ForeignKey;

import org.apache.commons.io.comparator.NameFileComparator;
import parmenidianEnumerations.Status;

public class HecateImportManager implements IHecateImportManager {

	private File directorySelected;
	private File[] sqlFiles;
	private ArrayList<DBVersion> lifetime= new ArrayList<DBVersion>();
	private ArrayList<Map<String,Integer>> transitions = new ArrayList<Map<String,Integer>>();
	// added by KD on 2018-12-09
	private List<Map<String,Integer>> sumUpdates;
	
	public HecateImportManager() {
		
		sumUpdates = new ArrayList<Map<String,Integer>>();
		
		
	}
	
	
	@Override
	public void createTransitions(File f) throws Exception {
		
	
		
		Exception wrong = new Exception();

		directorySelected=f;
		sqlFiles = directorySelected.listFiles(new SQLFileFilter());
		
		if(sqlFiles.length==0){
			throw wrong;
		}	
		

		createTransitions(sqlFiles,directorySelected);

	}

	@Override
	public ArrayList<DBVersion> parseSql(String sqlFiles) {
		
		
		File[] versions = new File(sqlFiles).listFiles(new SQLFileFilter());
		Arrays.sort(versions,NameFileComparator.NAME_INSENSITIVE_COMPARATOR);
		for(int i=0;i<versions.length;++i) {
			//System.out.println(versions[i].getName());
			parseLifetime(versions[i],i+1);
		}
		
		return lifetime;
	}

	@Override
	public ArrayList<Map<String, Integer>> parseXml(String xmlFile) {
		
		
		parseTransitions(new File(xmlFile));
		
		return transitions;
	}
	
	
	@SuppressWarnings("static-access")
	private void createTransitions(File[] sqlFiles,File directorySelected){
		
		HecateParser parser= new HecateParser();
		Schema currentSchema;
		ArrayList<Schema> schemata= new ArrayList<Schema>();

		//create schema per sql and store them
		for(int i=0;i<sqlFiles.length;++i){			

			//currentSchema=HecateParser.parse(sqlFiles[i].getAbsolutePath());
			currentSchema=parser.parse(sqlFiles[i].getAbsolutePath());
			currentSchema.setTitle(String.valueOf(i));
			schemata.add(currentSchema);
		}
		
		Delta delta = new Delta();
		TransitionList tl;
		Transitions transitions = new Transitions();

		
		for(int i=0;i<schemata.size()-1;++i){
		
			//tl=(Delta.minus(schemata.get(i),schemata.get(i+1))).tl;
			tl=(delta.minus(schemata.get(i),schemata.get(i+1))).tl;
			transitions.add(tl);			
		}
		
		marshal(transitions,directorySelected);
	}
	
	
	
	/**
	 * 
	 * @return the sum of updates for each table in each transition
	 * @since 2018-12-09
	 */
	public List<Map<String,Integer>> getSumUpdates(){
		
		return this.sumUpdates;
		
	}
	
	
	
	private void marshal(Transitions transitions,File directorySelected) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class, Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(transitions, new File(directorySelected +"\\transitions.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	@SuppressWarnings("static-access")
	private void parseLifetime(File version,int versionId){
		
		
		try {		
			
		  HecateParser parser= new HecateParser();
		  Schema schema1 = parser.parse(version.getAbsolutePath());		
		  
		  ArrayList<String> tableList =schema1.getAllTables();
		  ArrayList<model.Table> tablesWithin=new ArrayList<model.Table>();
			
		  for(int i=0;i<tableList.size();++i){
			model.Table  tb = new model.Table(tableList.get(i));
			tablesWithin.add(tb);
		  }
		  
		 TreeMap<String,Table> versionTables = schema1.getTables();
		 Map<String,Integer> attrsNum = new TreeMap<String,Integer>();
         ArrayList<ForeignKey> versionForeignKeys= new ArrayList<ForeignKey>(); 

			
		 for(Map.Entry<String, Table> iterator : versionTables.entrySet()){
			
			 attrsNum.put(iterator.getKey(), iterator.getValue().getAttrs().size());//added by KD 2018-11
		 
			 for(int i=0;i<iterator.getValue().getfKey().getForeingKeys().size();++i) 

		 		 versionForeignKeys.add(iterator.getValue().getfKey().getForeingKeys().get(i));
		 }	 
		 
		 DBVersion current = new DBVersion(tablesWithin,versionForeignKeys,version.getName(),versionId);
		 current.setAttrsNum(attrsNum);//added by KD 2018-11
		 lifetime.add(current);
		  
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
		
	}
	
	
//	/**
//	 * 
//	 * @param references denote a mapping (infer a FK relation) between attributes of tables of the current version.
//	 * @return foreign keys of each schema version.
//	 * @since 2018-02-16
//	 */
//	private ArrayList<model.ForeignKey> getForeingKeys(Map<Attribute, Attribute> references){
//		
//		ArrayList<model.ForeignKey> foreingKeysOfVersion  = new ArrayList<model.ForeignKey>();
//		
//		for (Map.Entry<Attribute, Attribute> entry : references.entrySet()) {
//			Attribute or = entry.getKey();
//			Attribute re = entry.getValue();
//			model.ForeignKey fk=new model.ForeignKey(or.getTable().getName(),re.getTable().getName());
//			foreingKeysOfVersion.add(fk);
//		}
//		
//		return foreingKeysOfVersion;
//		
//		
//	}
	
	private void parseTransitions(File transition){

		// Unmarshal
		InputStream inputStream;
		Transitions t = null;
		try {
			inputStream = new FileInputStream(transition.getAbsolutePath());
			//add Transition.class as param (KD on 2018-10-04)
			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class, Deletion.class, Insersion.class,
					TransitionList.class, Transitions.class, Transition.class);
			Unmarshaller u = jaxbContext.createUnmarshaller();
			t = (Transitions)u.unmarshal( inputStream );
		} catch (Exception e) {
			e.printStackTrace();
		}

		for(TransitionList tl :t.getList()){

			//key:tableName, value:tableStatus
			Map<String,Integer>  temp = new HashMap<String,Integer>();
			Map<String,Integer> tempMap = new TreeMap<String,Integer>();
			

			//to tl.getTransitionList mporei na einai null an apo thn mia ekdosh sthn allh den uparxoun
			//allages giauto to logo vrisketai edw auth h if
			if(tl.getTransitionList()!=null){

				for(int i=0;i<tl.getTransitionList().size();++i){
					
					//added by KD on 2018-12-09
					Transition t1 = tl.getTransitionList().get(i);
					String affTable = t1.getAffTable().getName().trim();
					
					if (!tempMap.containsKey(affTable))
						tempMap.put(affTable, 0);
					
					if(!t1.getType().equals("NewTable")&&!t1.getType().equals("DeleteTable"))
						tempMap.put(affTable,tempMap.get(affTable)+t1.getAffAttributes().size());

					if(tl.getTransitionList().get(i).getType().equals("NewTable"))
						temp.put(tl.getTransitionList().get(i).getAffTable().getName(),Status.CREATION.getValue());
					else if(tl.getTransitionList().get(i).getType().equals("DeleteTable"))
						temp.put(tl.getTransitionList().get(i).getAffTable().getName(),Status.DELETION.getValue());
					else if(tl.getTransitionList().get(i).getType().equals("UpdateTable"))//sketo t else pianei k to keychange pou den me endiaferei emena
						temp.put(tl.getTransitionList().get(i).getAffTable().getName(),Status.UPDATE.getValue());

				}			
			}
			
			transitions.add(temp);
			//printUpdates(tempMap);
			this.sumUpdates.add(tempMap);

		}	
		
//		for (int j=0;j<this.sumUpdates.size();j++) {
//			System.out.println("----Transition "+j+"----");
//			printUpdates(this.sumUpdates.get(j));
//		}
	}

	
	private void printUpdates(Map<String,Integer> map) {
		
		for (Map.Entry<String, Integer> e:map.entrySet())
			System.out.println("Table name: "+e.getKey()+" --> Updates: "+e.getValue());
		
		
	}
	
}


