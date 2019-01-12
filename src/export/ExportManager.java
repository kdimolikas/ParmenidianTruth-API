package export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class ExportManager implements IExportManager {

	@Override
	public void createPowerPointPresentation(ArrayList<String> fileNames, String targetFolder, String projectName) throws FileNotFoundException, IOException {
		
		
		PowerPointGenerator pptx=new PowerPointGenerator(targetFolder,projectName);			
		pptx.createPresentation(fileNames);
		pptx=null;

	}

	@Override
	public void createVideo(File file) throws IOException {
		
		
		VideoGenerator videoGenerator = new VideoGenerator(file);
		videoGenerator.exportToVideo();
		videoGenerator=null;
		

	}

}
