package export;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.poi.xslf.util.PPTX2PNG;

//import com.xuggle.mediatool.IMediaWriter;
//import com.xuggle.mediatool.ToolFactory;
//import com.xuggle.xuggler.ICodec;

import fileFilter.ImageFileFilter;


public class VideoGenerator {

	private File presentationSelected;
	private String filenameOfPresentation;
    private  final double FRAME_RATE = 24;
    private  String outputFilename ;
    private String sourceFolder=new String();


	public VideoGenerator(File presentation){
		
		presentationSelected=presentation;
		filenameOfPresentation= presentationSelected.getAbsolutePath();
		
		setOutputFilename();
		setSourceFolder();
		
	}
	
	public void exportToVideo() throws IOException{
		
		extractPngFromPptx();
		createVideo();
		deleteGeneratedPng();
		
	}
	
	private void extractPngFromPptx(){

		try {
			PPTX2PNG.main(new String[]{filenameOfPresentation});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void createVideo() throws IOException{
		
		
		//IMediaWriter writer = ToolFactory.makeWriter(outputFilename);
		//Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
		
        //writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4,screenBounds.width, screenBounds.height);

//        long startTime = System.nanoTime();
//        
//        File directory = new File(sourceFolder);
//        ArrayList<File> filenames = new ArrayList<File> ();
//        
//        File[] list= directory.listFiles(new ImageFileFilter());
//		for(int i=0;i<list.length;i++){
//			filenames.add(list[i]);
//		}
//		
//
//		Collections.sort(filenames,new Comparator<File>() {
//			
//			@Override
//			public int compare(File c1,File c2) {
//				if(getFname(c1) > getFname(c2))
//					return 1;
//				else if(getFname(c1) < getFname(c2))
//					return -1;
//				else
//					return 0;
//			}
//
//			public int getFname(File file){
//
//				String[] leftArray = file.getAbsolutePath().split("\\.",2);	
//				String[] leftovers = leftArray[0].split("\\-",2);
//
//				return Integer.parseInt(leftovers[1]);
//
//			}
//
//			
//			
//		});
//        
//        for (int index = 0; index < filenames.size(); index++) {
//	            BufferedImage screen = ImageIO.read(filenames.get(index));
//		
//	            // convert to the right image type
//	            BufferedImage bgrScreen = convertToType(screen,BufferedImage.TYPE_3BYTE_BGR);
//	
//            
//	            int i=0;
//	            while(i<FRAME_RATE){
//	            	writer.encodeVideo(0, bgrScreen, System.nanoTime() - startTime, 
//	                   TimeUnit.NANOSECONDS);
//	            	++i;
//	            }
//	
//	            // sleep for frame rate milliseconds
//	            try {
//	                Thread.sleep((long) (1000 / FRAME_RATE));
//	            } 
//	            catch (InterruptedException e) {
//	            }
//            
//        }
//        
//        writer.close();		
		
	}
	
	private void deleteGeneratedPng(){
		
        File directory = new File(sourceFolder);
        
        FileFilter filter =new ImageFileFilter();
        File[] filesToBeDeleted =directory.listFiles(filter); 
		
        for(int i=0;i<filesToBeDeleted.length;++i)
        	filesToBeDeleted[i].delete();
        
		
	}
	
	private void setOutputFilename(){
		
		String[] leftArray = filenameOfPresentation.split("\\.",2);
		outputFilename=leftArray[0]+".mp4";
		
	}
	
	private void setSourceFolder() {
		
		String[] leftArray = filenameOfPresentation.split("\\.",2);	
		String[] leftovers = leftArray[0].split("\\\\");

		for(int i=0;i<leftovers.length-1;++i){
			sourceFolder+=leftovers[i]+"\\";
		}
		
	}
	
    private BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
        
        BufferedImage image;

        if (sourceImage.getType() == targetType) {
            image = sourceImage;
        }
        else {
            image = new BufferedImage(sourceImage.getWidth(), 
                 sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }

        return image;
        
    }
    
	
}