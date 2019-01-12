import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dataImport.GraphmlLoaderFactory;
import dataImport.IGraphmlLoader;
import model.DiachronicGraph;



/**
 * 
 * Testing {@link model.DiachronicGraph} class using "Egee" as dataset.
 * @author MZ - IK
 * @version {2.0 - modified by KD}
 * @since 2018-11-02
 *
 */

public class DiachronicGraphTest {
	
	private static DiachronicGraph dg;
	private static IGraphmlLoader gmlLoader;
	private static GraphmlLoaderFactory gmlFactory;
	private static final String TEST_FOLDER = "resources/Egee_test";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		dg = new DiachronicGraph();
		gmlFactory = new GraphmlLoaderFactory();
		gmlLoader = gmlFactory.createGraphmlLoader(TEST_FOLDER.concat("/layout.graphml"));
		dg.loadDiachronicGraph(gmlLoader.getNodes(), gmlLoader.getEdges(), TEST_FOLDER.concat("/processed schemata"), "./output");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDiachronicGraph() {
		assertNotNull("After setup, diachronic graph is not null", dg);
	}

	@Test
	public void testGetVertices() {
		assertNotNull("Nodes are not null", dg.getVertices());
	}

	@Test
	public void testGetEdges() {
		assertNotNull("Edges are not null", dg.getEdges());
	}

	@Test
	public void testGetDictionaryOfGraph() {
		assertNotNull("dictionary of graph not null", dg.getDictionaryOfGraph());
	}


	@Test
	public void testGetGraph() {
		assertNotNull("graph not null", dg.getGraph());
	}


	@Test
	public void testGetVersions() {
		
		assertNotNull("versions not null", dg.getVersions());
		
	}


	@Test
	public void testGetGraphMetrics() {
		
		assertNotNull("GraphMetrics not null", dg.getGraphMetrics());
		
	}

}
