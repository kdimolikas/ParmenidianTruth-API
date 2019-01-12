
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * Running all tests.
 * @author KD
 * @since 2018-03-04
 * @version 1.0
 *
 */


@RunWith(Suite.class)
@SuiteClasses({DiachronicGraphTest.class, GraphMetricsReportTest.class, MetricsReportEngineTest.class,
	ReportFactoryTest.class,VertexMetricsReportTest.class,DBVersionTest.class,HecateImportManagerTest.class})
public class AllTests {

	//nothing to add here

}