package core;

public class ParmenidianTruthManagerFactory {
	
	
	public IParmenidianTruth getManager(){
		
		return new ParmenidianTruthManager();
	}
	

}