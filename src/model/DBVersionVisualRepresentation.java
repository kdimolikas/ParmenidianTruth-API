package model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.apache.commons.collections15.Transformer;

import parmenidianEnumerations.Status;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class DBVersionVisualRepresentation {
	public Layout<String, String> layout;
	public String targetFolder;
	@SuppressWarnings("rawtypes")
	public Transformer edgeType;
	private ArrayList<Table> nodes = new ArrayList<Table>();
	@SuppressWarnings("unused")
	private ArrayList<ForeignKey> edges= new ArrayList<ForeignKey>();
	private String episodeName;
	@SuppressWarnings("unused")
	private int width,height;
	private DBVersion parent;
	private VisualizationViewer<String, String> visualizationViewer ;

	public DBVersionVisualRepresentation (DBVersion p,ArrayList<Table> tables,ArrayList<ForeignKey> fks,String versionName) {		

		parent=p;
		
		nodes=tables;
		edges=fks;

		episodeName=versionName;
	}
	
	public void setDetails(String tf, int et,int width,int height){
		
		edgeType = et == 0 ? new EdgeShape.Line<String, String>(): new EdgeShape.Orthogonal<String, String>();
		
		targetFolder = tf+"//screenshots";		
		new File(targetFolder ).mkdir();
		
		this.width=width;
		this.height=height;

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createEpisodes(VisualizationViewer< String, String> avv,ConcurrentHashMap<String, Table> graph,Dimension universalFrame,Rectangle universalBounds,Point2D universalCenter,double frameX,double frameY,double scaleX,double scaleY) {

		
		layout = new StaticLayout<String, String>(parent.getGraph());

		layout.setSize(universalFrame);
		this.visualizationViewer =  avv;
		this.visualizationViewer.setGraphLayout(layout);

		
		// Setup up a new vertex to paint transformer...
		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
			public Paint transform(String v) {

				for (int i = 0; i <nodes.size(); ++i)
					if (nodes.get(i).getTableName().equals(v))
						if (nodes.get(i).getTableStatus() == Status.UNDEFINED.getValue())
							return new Color(129, 215, 244, 200);
						else if (nodes.get(i).getTableStatus() == Status.CREATION.getValue())
							return new Color(0, 255, 0, 200);
						else if (nodes.get(i).getTableStatus() == Status.DELETION.getValue())
							return new Color(255, 0, 0, 200);
						else
							return new Color(255, 255, 0, 200);

				return Color.BLACK;

			}
		};

		
		//gia ton kathe komvo tou kathe episode,koitaw ton universalGraph
		//gia na dw th coordinates exei hdh kai ths lockarw ston grafo
		for (int i = 0; i < nodes.size(); ++i) {
			layout.setLocation(nodes.get(i).getTableName(),(graph.get(nodes.get(i).getTableName()).getCoords()));
			layout.lock(nodes.get(i).getTableName(), true);
		}

		visualizationViewer.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(Position.N);
		visualizationViewer.getRenderContext().setEdgeShapeTransformer(edgeType);
		visualizationViewer.setBackground(Color.WHITE);

		
		writeJPEGImage(visualizationViewer,new File(targetFolder + "/"+ episodeName + ".jpg"));	

	}
	
	protected void writeJPEGImage(VisualizationViewer< String, String> vv,File file) {
		
		int width = vv.getWidth();
		int height = vv.getHeight();

		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bi.createGraphics();
		
		vv.paint(graphics);
		graphics.dispose();

		
		try {
			ImageIO.write(bi, "jpeg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


}