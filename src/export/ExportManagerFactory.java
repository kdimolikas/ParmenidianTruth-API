package export;

public class ExportManagerFactory {
	
	
	public IExportManager createExportManager() {
		
		return new ExportManager();
		
	}

}