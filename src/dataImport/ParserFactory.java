package dataImport;

public class ParserFactory {
	
	public IParser createHecateParser() {
		
		return new Parser();
		
	}
	
}