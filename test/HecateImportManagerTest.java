import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import dataImport.HecateImportManager;

/**
 * Testing {@link dataImport.HecateImportManager} class.
 * @author KD
 * @since 2018-12-09
 * @version 1.0
 */
public class HecateImportManagerTest {

	private static HecateImportManager hecateManager;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		hecateManager = new HecateImportManager(); 
	}

	@Test
	public void testHecateImportManager() {
		assertNotNull("Hecate Manager is not null", hecateManager);
	}

	@Test
	public void testGetSumUpdates() {
		assertNotNull("sumUpdates field is not null", hecateManager.getSumUpdates());
	}

}
