package export;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class PowerPointGenerator {
	
	
	private String targetWorkspace;
	private String presentation;
	private int width,height;

	
	public PowerPointGenerator(String workspace,String presentation){
		
		targetWorkspace=workspace;
		this.presentation=presentation;
		
	}
	
	public void createPresentation(ArrayList<String> args) throws FileNotFoundException, IOException{	
		
		XMLSlideShow ppt = new XMLSlideShow();
		
		ppt=initializePresentation(ppt,args);
		
		for(int i=0;i<args.size();++i)
			ppt=appendSlideShow(args.get(i),ppt);	
	
		
		
		FileOutputStream out = new FileOutputStream(targetWorkspace+"/"+presentation+".pptx");
        ppt.write(out);
        out.close();
		
	}
	
	private XMLSlideShow initializePresentation(XMLSlideShow ppt,ArrayList<String> files) throws IOException {

        XSLFSlideMaster defaultMaster = ppt.getSlideMasters()[0];
        
        
        for(int i=0;i<files.size();++i){
        	if(files.get(i).contains("Diachronic Graph")){
        		

                XSLFSlideLayout title = defaultMaster.getLayout(SlideLayout.TITLE);                
                XSLFSlide slide0 = ppt.createSlide(title);
                
        		
                BufferedImage bufferedImage = ImageIO.read(new File(files.get(i)));
                width = bufferedImage.getWidth();
                height = bufferedImage.getHeight(); 
                
                XSLFTextShape title1 = slide0.getPlaceholder(0);
                title1.setAnchor(new Rectangle(0,0,width,100));
                title1.setText("Diachronic Graph");
                
                
                byte[] data = IOUtils.toByteArray(new FileInputStream(files.get(i)));
                int pictureIndex = ppt.addPicture(data, XSLFPictureData.PICTURE_TYPE_JPEG);
                XSLFPictureShape shape = slide0.createPicture(pictureIndex);
                shape.setAnchor(new Rectangle(0,100,width,height));        

                
 
                ppt.setPageSize(new java.awt.Dimension(width,height));
                
        		
        	}
        }
		
        XSLFSlideLayout title = defaultMaster.getLayout(SlideLayout.TITLE_ONLY);
        XSLFSlide slide = ppt.createSlide(title);
        
        XSLFTextShape title1 = slide.getPlaceholder(0);
        title1.setAnchor(new Rectangle(0,width/4-100/2,height,100));
        title1.setText(setSlideTitle("Evolution Story"));
		
		return ppt;
	}

	private XMLSlideShow appendSlideShow(String imagePath,XMLSlideShow ppt) throws FileNotFoundException, IOException{

        if(!imagePath.contains(".jpg")|| imagePath.contains("Universal Graph"))
        	return ppt;
		
        
        XSLFSlideMaster defaultMaster = ppt.getSlideMasters()[0];

        XSLFSlideLayout title = defaultMaster.getLayout(SlideLayout.TITLE);
        XSLFSlide slide = ppt.createSlide(title);
       
        XSLFTextShape title1 = slide.getPlaceholder(0);
        title1.setAnchor(new Rectangle(0,0,width,100));
        title1.setText(setSlideTitle(imagePath));
        
        
        @SuppressWarnings("unused")
		BufferedImage bufferedImage = ImageIO.read(new File(imagePath));

        
        
        byte[] data = IOUtils.toByteArray(new FileInputStream(imagePath));
        int pictureIndex = ppt.addPicture(data, XSLFPictureData.PICTURE_TYPE_JPEG);
        XSLFPictureShape shape = slide.createPicture(pictureIndex);
        shape.setAnchor(new Rectangle(0,100,width,height));        
        

        ppt.setPageSize(new java.awt.Dimension(width,height+100));
        
        return ppt;			
		
	}

	private String setSlideTitle(String imagePath) {

		String[] leftArray = imagePath.split(".jpg",2);
		String[] rightArray = leftArray[0].split("\\\\");
		return rightArray[rightArray.length-1];

	}
	

}
