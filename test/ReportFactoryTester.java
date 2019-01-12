

import static org.junit.Assert.*;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import model.ReportFactory;
import parmenidianEnumerations.Metric_Enums;
import model.VertexMetricsReport;

import model.DiachronicGraph;
import model.MetricsReportEngine;


/**
 * Testing {@link model.ReportFactory} behavior when the instance of {@link model.DiachronicGraph} is null.
 * @author MZ-IK
 * @version 1.0
 * @since 2017-05-23
 *
 */

public class ReportFactoryTester {
	
	private ReportFactory reportFactory = new ReportFactory();
	private Metric_Enums metric1 = Metric_Enums.CLUSTERING_COEFFICIENT;
	private DiachronicGraph diachronicGraph = null;
	private MetricsReportEngine expectedResult;
	
	@Before
	 public void before() {
		expectedResult = (MetricsReportEngine) reportFactory.getMetricsReportEngine("", metric1, diachronicGraph);
	}
	
	
	@Test
	public void testGetMetricsReportEngine() {
		
					
		assertThat(expectedResult.getClass(), CoreMatchers.instanceOf(VertexMetricsReport.class));		
	
	}

}