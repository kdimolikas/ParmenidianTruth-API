import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import core.ParmenidianTruthManager;
import parmenidianEnumerations.Metric_Enums;


/**
 * Testing {@link core.ParmenidianTruthManager} class using "Egee" dataset.
 * @author KD
 * @since 2018-11-02
 */
public class ParmenidianTruthManagerTest {

	
	private static ParmenidianTruthManager manager;
	private static ArrayList<Metric_Enums> metrics;
	
	private static final String TEST_FOLDER = "resources/Egee_test";
	private static final String SQL_FOLDER = TEST_FOLDER.concat("/processed schemata"); 
	private static final String OUTPUT_FOLDER = TEST_FOLDER.concat("/output"); 
	
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		manager = new ParmenidianTruthManager();
		metrics = new ArrayList<Metric_Enums>();
		metrics.add(Metric_Enums.CLUSTERING_COEFFICIENT);
		metrics.add(Metric_Enums.GRAPH_DIAMETER);
		metrics.add(Metric_Enums.VERTEX_DEGREE);
	}

	/**
	 * Test method for {@link core.ParmenidianTruthManager#ParmenidianTruthManager()}.
	 */
	@Test
	public void testParmenidianTruthManager() {
		assertNotNull("Manager is not null",manager);
	}

	/**
	 * Test method for {@link core.ParmenidianTruthManager#loadProject(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testLoadProject() {
		assertEquals(17,manager.loadProject(SQL_FOLDER, OUTPUT_FOLDER));
	}

	
	/**
	 * Test method for {@link core.ParmenidianTruthManager#getTables()}.
	 */
	@Test
	public void testGetTables() {
		assertEquals(12,manager.getTables().size());
	}
	
	
	/**
	 * Test method for {@link core.ParmenidianTruthManager#getForeignKeys()}.
	 */
	@Test
	public void testGetForeignKeys() {
		assertEquals(6,manager.getForeignKeys().size());
	}
	
	
	/**
	 * Test method for {@link core.ParmenidianTruthManager#getVersions()}.
	 */
	@Test
	public void testGetVersions() {
		assertEquals(17,manager.getVersions().size());
	}
	
	
	/**
	 * Test method for {@link core.ParmenidianTruthManager#generateMetricsReport(java.util.ArrayList)}.
	 */
	@Test
	public void testGenerateMetricsReport() {
		
		ArrayList<Integer> reportSizeList = new ArrayList<Integer>();
		reportSizeList = manager.generateMetricsReport(metrics);
		
		for (int i=0;i<metrics.size();i++)
			assertTrue("Report file "+i+"is not empty",reportSizeList.get(i)>0);
		
	}

	/**
	 * Test method for {@link core.ParmenidianTruthManager#visualize()}.
	 */
	@Ignore
	public void testVisualize() {
		fail("Not yet implemented");
	}

}
