package dataImport;

import java.io.FileNotFoundException;

public class GraphmlLoaderFactory {
	
	
	public IGraphmlLoader createGraphmlLoader(String filename) throws FileNotFoundException {
		
		return new GraphmlLoader(filename);
		
	}

}
