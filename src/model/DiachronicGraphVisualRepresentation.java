package model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

public class DiachronicGraphVisualRepresentation {

	private ArrayList<Table> nodes = new ArrayList<Table>();
	@SuppressWarnings("unused")
	private ArrayList<ForeignKey> edges= new ArrayList<ForeignKey>();
	@SuppressWarnings("unused")
	private String inputFolder;
	private SpringLayout2<String, String> layout;
	private static DefaultModalGraphMouse<String, Number> graphMouse = new DefaultModalGraphMouse<String, Number>();
	private  VisualizationViewer<String, String> visualizationViewer;
	private Dimension universalFrame;
	private String targetFolder;
	private String outputFolder; //added on 2018/03/04
	@SuppressWarnings("rawtypes")
	private Transformer edgeType;
	private  MutableTransformer universalTransformerForTranslation;
	private  MutableTransformer universalTransformerForScaling;
	private DiachronicGraph parent;

	
	public DiachronicGraphVisualRepresentation() {}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DiachronicGraphVisualRepresentation(DiachronicGraph p,ArrayList<Table> tables,ArrayList<ForeignKey> fks,String in, String tf, int et,int mode,double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY) {		
		
		edgeType = et == 0 ? new EdgeShape.Line<String, String>(): new EdgeShape.Orthogonal<String, String>();
		parent=p;

		nodes=tables;
		edges=fks;
		inputFolder=in;
		targetFolder = tf+"//screenshots";
		outputFolder = tf;
		new File(targetFolder ).mkdir();

		layout= new SpringLayout2<String, String>(parent.getGraph());

		universalFrame =new Dimension(nodes.size()*26, nodes.size()*26);

		layout.setSize(universalFrame);
		visualizationViewer = new VisualizationViewer<String, String>(layout);
		visualizationViewer.setSize(new Dimension(universalFrame.width+300, universalFrame.height+300));

		// Setup up a new vertex to paint transformer...
		@SuppressWarnings("unused")
		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
			public Paint transform(String i) {
				return new Color(207, 247, 137, 200);
			}
		};

		visualizationViewer.getRenderContext().setVertexFillPaintTransformer((new PickableVertexPaintTransformer<String>(visualizationViewer.getPickedVertexState(),new Color(207, 247, 137, 200), Color.yellow)));
		visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(Position.N);
		visualizationViewer.setBackground(Color.WHITE);
		visualizationViewer.getRenderContext().setEdgeShapeTransformer(edgeType);

		// ---------------default graph moving
		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		visualizationViewer.setGraphMouse(graphMouse);
		
		
		universalTransformerForTranslation = visualizationViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		universalTransformerForScaling  = visualizationViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
		
		//ekteleitai mono an exei anoiksei apo arxeio graphml
		if(mode==1){
			for (int i = 0; i < nodes.size(); ++i) {
				layout.setLocation(nodes.get(i).getTableName(),nodes.get(i).getCoords());
				layout.lock(nodes.get(i).getTableName(), true);
			}
			
			
			if(scaleX<=1 && scaleY<=1){	
				universalTransformerForTranslation.setTranslate(frameX, frameY);

				universalTransformerForScaling.scale(scaleX, scaleY, new Point2D.Double(centerX,centerY));
				
			}else{
				universalTransformerForTranslation.setScale(scaleX, scaleY, new Point2D.Double(centerX,centerY) );
				universalTransformerForTranslation.setTranslate(frameX, frameY);
			}
		}

	}

	@SuppressWarnings("rawtypes")
	public VisualizationViewer show() {
		
		visualizationViewer.repaint();

		return visualizationViewer;
	}

	public void setTransformingMode() {

		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		visualizationViewer.setGraphMouse(graphMouse);

	}

	public void setPickingMode() {

		graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
		visualizationViewer.setGraphMouse(graphMouse);

	}

	@SuppressWarnings("unchecked")
	public void saveVertexCoordinates(String projectIni) throws IOException {
		//save sthn mnhmh
		for (int i = 0; i <nodes.size(); ++i)
			nodes.get(i).setCoords(layout.transform(nodes.get(i).getTableName()));
		
		//save sto .graphml arxeio
		GraphMLWriter<String,String> graphWriter = new GraphMLWriter<String, String> ();
		//modified on 2018-03-04
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFolder+"\\layout.graphml")));

		graphWriter.addVertexData("x", null, "0",
			    new Transformer<String, String>() {
			        @SuppressWarnings({ "rawtypes" })
					public String transform(String v) {
			        	
				            return Double.toString(((AbstractLayout)layout).getX(v));
			        		
			        	}		        	
			        }		    
			);
		
		
		graphWriter.addVertexData("y", null, "0",
			    new Transformer<String, String>() {
			        public String transform(String v) {
			        	
				            return Double.toString(((AbstractLayout<String, String>) layout).getY(v));
			        		
			        	}			        	
			        }		    
			);

		graphWriter.save(parent.getGraph(), out);
		
		updateIniFile(projectIni);
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void visualizeDiachronicGraph(VisualizationViewer< String, String> vv) {

		vv.setGraphLayout(layout);

		
		@SuppressWarnings("unused")
		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
			public Paint transform(String i) {
				return new Color(207, 247, 137, 200);
			}
		};

		vv.getRenderContext().setVertexFillPaintTransformer((new PickableVertexPaintTransformer<String>(vv.getPickedVertexState(),new Color(207, 247, 137, 200), Color.yellow)));
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.N);
		vv.setBackground(Color.WHITE);
		vv.getRenderContext().setEdgeShapeTransformer(edgeType);

		
		File file =new File(targetFolder + "/"+	"Diachronic Graph"  + ".jpg");
		
		int width = vv.getWidth();
		int height = vv.getHeight();
		

		BufferedImage bi = new BufferedImage(width, height,	BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bi.createGraphics();
		vv.paint(graphics);
		graphics.dispose();

		
		try {
			ImageIO.write(bi, "jpeg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}	

	public VisualizationViewer<String, String> getVv() {
		return visualizationViewer;
	}

	public String getTargetFolder() {
		return targetFolder;
	}
	
	@SuppressWarnings("rawtypes")
	public void stop(){
		((SpringLayout2)layout).lock(true);
	}
	
	
	private void updateIniFile(String projectIni) {
		
		
		try {
			
			
			BufferedReader reader = new BufferedReader(new FileReader(projectIni));
			String line;
			String restOfFile = "";
			while((line = reader.readLine()) != null )
			{
				
				if(line.contains("Project Name") || line.contains("sql@") ||line.contains("transition@")||line.contains("output@"))
					restOfFile+=line+"\n";
			    	
			}
			
			reader.close();
			
				
				PrintWriter writer;
				
				writer = new PrintWriter(new FileWriter(projectIni));
				writer.println(restOfFile);
				writer.println("graphml@"+outputFolder+"\\layout.graphml");
				writer.println("frameX@"+universalTransformerForTranslation.getTranslateX());
				writer.println("frameY@"+universalTransformerForTranslation.getTranslateY());
				writer.println("centerX@"+visualizationViewer.getCenter().getX());
				writer.println("centerY@"+visualizationViewer.getCenter().getY());
				
				if(universalTransformerForScaling.getScaleX()==1 && universalTransformerForScaling.getScaleY()==1 ){
					writer.println("scaleX@"+universalTransformerForTranslation.getScaleX());
					writer.println("scaleY@"+universalTransformerForTranslation.getScaleY());
				}else{
					writer.println("scaleX@"+universalTransformerForScaling.getScaleX());
					writer.println("scaleY@"+universalTransformerForScaling.getScaleY());
				}	
				
				
				writer.close();
				
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public  double getFrameX() {
		return universalTransformerForTranslation.getTranslateX();
		
	}

	public  double getFrameY() {
		return universalTransformerForTranslation.getTranslateY();
	}

	public double getScaleX() {
		
		if(universalTransformerForScaling.getScaleX()==1 && universalTransformerForScaling.getScaleY()==1 ){
			return universalTransformerForTranslation.getScaleX();
		}else{
			return universalTransformerForScaling.getScaleX();
		}	

	}

	public double getScaleY() {
		
		if(universalTransformerForScaling.getScaleX()==1 && universalTransformerForScaling.getScaleY()==1 ){
			return universalTransformerForTranslation.getScaleY();
		}else{
			return universalTransformerForScaling.getScaleY();
		}	
	}

	public Dimension getUniversalFrame() {
		return universalFrame;
	}

	public Point2D getUniversalCenter() {
		return visualizationViewer.getCenter();
	}

	public int getWidthOfVisualizationViewer(){
		
		return visualizationViewer.getWidth();
		
	}
	
	public int getHeightOfVisualizationViewer(){
		
		return visualizationViewer.getHeight();
		
	}

	public Component refresh(double forceMult, int repulsionRange) {
		
		layout.setForceMultiplier(forceMult);
		layout.setRepulsionRange(repulsionRange);
		
		
		visualizationViewer.removeAll();
		
		
		visualizationViewer.setGraphLayout(layout);
		
		return visualizationViewer;
	}
	
	public Rectangle getUniversalBounds(){
		
		
		return visualizationViewer.getBounds();
		
	}


}
