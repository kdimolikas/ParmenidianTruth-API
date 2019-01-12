package export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Provides export actions.
 * @author KD
 * @since 2018-02-13
 */

public interface IExportManager {
		
	public void createPowerPointPresentation(ArrayList<String> fileNames,String targetFolder,String projectName) throws FileNotFoundException, IOException;
	public void createVideo(File file) throws IOException;

}