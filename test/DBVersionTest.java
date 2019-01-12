import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import model.DBVersion;

import model.ForeignKey;


/**
 * Testing {@link model>DBVersion} class.
 * @author KD
 * @since 2018-11-25
 * @version 1.0
 */
public class DBVersionTest {

	private static DBVersion version;
	private static model.Table table_s;
	private static model.Table table_t;
	private static ArrayList<model.Table> tables;
	private static model.ForeignKey fk;
	private static ArrayList<ForeignKey> fks;
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		table_s = new model.Table("t_logical");
		table_t = new model.Table("t_job");
		tables = new ArrayList<model.Table>();
		tables.add(table_s);
		tables.add(table_t);
		fks = new ArrayList<ForeignKey>();
		fk = new ForeignKey(table_s.getTableName(),table_t.getTableName());
		fks.add(fk);
		version = new DBVersion(tables,fks,"rev_1.01.sql",1);
	}

	/**
	 * Test method for {@link model.DBVersion#tableExists(java.lang.String)}.
	 */
	@Test
	public void testTableExists() {

		assertEquals(version.tableExists("t_job"),1);
		assertEquals(version.tableExists("t_job "),0);
		
	}
	
	/**
	 * Test method for {@link model.DBVersion#getVersionId()}.
	 */
	@Test
	public void testGetVersionId() {
		
		assertTrue(version.getVersionId()>0);

	}

}
