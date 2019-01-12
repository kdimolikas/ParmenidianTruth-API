package model;

public class ForeignKey {
	
	private String sourceTable;
	private String targetTable;
	
	public ForeignKey(String s, String t){
		sourceTable=s.trim();
		targetTable=t.trim();
	}

	public String getSourceTable() {
		return sourceTable;
	}

	public String getTargetTable() {
		return targetTable;
	}
	
	public String getKey(){
		
		String buff = new String();
		
		buff=sourceTable +" -> "+targetTable;
		
		return buff;
		
		
	}

}