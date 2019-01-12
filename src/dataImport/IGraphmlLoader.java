package dataImport;

import java.util.ArrayList;

import model.ForeignKey;
import model.Table;


/**
 * Responsible for providing tables and foreign keys of the schema.
 * @author KD
 * @since 2018-02-16
 *
 */

public interface IGraphmlLoader {
	
	public ArrayList<Table> getNodes();
	public ArrayList<ForeignKey> getEdges();

}
