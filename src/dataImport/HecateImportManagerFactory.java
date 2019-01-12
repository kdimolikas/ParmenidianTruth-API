package dataImport;


public class HecateImportManagerFactory {
	
	
	public IHecateImportManager createHecateManager() {
		
		return new HecateImportManager();
		
	}

}
