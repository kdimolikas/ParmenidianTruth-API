import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import dataImport.GraphmlLoaderFactory;
import dataImport.IGraphmlLoader;
import model.GraphMetricsReport;
import model.VertexMetricsReport;
import model.DiachronicGraph;

import parmenidianEnumerations.Metric_Enums;

/**
 * Testing {@link model.MetricsReportEngine} class using "Egee" as dataset.
 * @author MZ-IK
 * @since 2018-03-04
 * @version {2.0 - modified by KD}
 *
 */

public class MetricsReportEngineTest {
	
	private static GraphMetricsReport graphReport;
	private static VertexMetricsReport vertexReport;
	private static DiachronicGraph diag;
	private static Metric_Enums metric;
	private static GraphMetricsReport spyGraph;
	private static VertexMetricsReport spyVertex;
	private static IGraphmlLoader gmlLoader;
	private static GraphmlLoaderFactory gmlFactory;
	
	private static final String TEST_FOLDER = "resources/Egee_test";
	private static final String SQL_FOLDER = TEST_FOLDER.concat("/processed schemata"); 
	private static final String OUTPUT_FOLDER = TEST_FOLDER.concat("/output"); 
	private static final String GRAPHML_FOLDER = TEST_FOLDER.concat("/layout.graphml"); 
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		
		diag = new DiachronicGraph();
		gmlFactory = new GraphmlLoaderFactory();
		gmlLoader = gmlFactory.createGraphmlLoader(GRAPHML_FOLDER);
		diag.loadDiachronicGraph(gmlLoader.getNodes(), gmlLoader.getEdges(),
				SQL_FOLDER,	OUTPUT_FOLDER);
		metric=Metric_Enums.CLUSTERING_COEFFICIENT;
		graphReport = new GraphMetricsReport(OUTPUT_FOLDER,metric, diag);
		vertexReport = new VertexMetricsReport(OUTPUT_FOLDER,metric, diag);
		spyGraph = Mockito.spy(graphReport);
		spyVertex = Mockito.spy(vertexReport);
		
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
	public void testGenerateMetricsReport() {
		assertNull("graph report array is not filled", spyGraph.getReport());
		assertNull("vertex report array is not filled", spyVertex.getReport());
		int r1 = spyGraph.generateMetricsReport();
		int r2 = spyVertex.generateMetricsReport();
		assertTrue("graph report array filled", r1>0);
		assertTrue("vertex report array filled", r2>0);
	}

	@Ignore
	public void testCreateCsvFile() {
		//just opens a file
	}

	@Ignore
	public void testPopulateArray() {
		//abstract
	}

	@Ignore
	public void testPrintArrayIntoFile() {
		//just writes the array content to the file
	}

}
