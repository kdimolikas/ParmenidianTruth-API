package dataImport;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMetadata.EdgeDefault;
import model.ForeignKey;
import model.Table;

public class GraphmlLoader implements IGraphmlLoader {

	
	private ArrayList<Table>  vertices= new ArrayList<Table>();
	private ArrayList<ForeignKey> edges= new ArrayList<ForeignKey>();
	
	@SuppressWarnings({ "unused", "rawtypes" })
	public GraphmlLoader(String filename) throws FileNotFoundException{
		
		BufferedReader fileReader = new BufferedReader(new FileReader(filename));


		/* Create the Graph Transformer */
		Transformer<GraphMetadata, Graph<String, String>>
		graphTransformer = new Transformer<GraphMetadata,
		Graph<String, String>>() {

			public Graph<String, String>
			transform(GraphMetadata metadata) {
				metadata.getEdgeDefault();
				if (metadata.getEdgeDefault().equals(
						EdgeDefault.DIRECTED)) {
					return new
							DirectedSparseGraph<String, String>();
				} else {
					return new
							UndirectedSparseGraph<String, String>();
				}
			}
		};

		Transformer<NodeMetadata, String> vertexTransformer
		= new Transformer<NodeMetadata, String>() {
			public String transform(NodeMetadata metadata) {
				String v = "";//  new Table(metadata.getDescription());
				printString(metadata.getId());
				printString(metadata.getProperty("x"));
				printString(metadata.getProperty("y"));

				Table t= new Table(metadata.getId(),new Point2D.Double(Double.valueOf(metadata.getProperty("x")),Double.valueOf(metadata.getProperty("y"))));
				vertices.add(t);

				return v;
			}
		};


		/* Create the Edge Transformer */
		Transformer<EdgeMetadata, String> edgeTransformer =new Transformer<EdgeMetadata, String>() {
			public String transform(EdgeMetadata metadata) {
				String e = "";
				printString(metadata.getSource()+"------->"+metadata.getTarget());

				ForeignKey f = new ForeignKey(metadata.getSource(),metadata.getTarget());
				edges.add(f);

				return e;
			}
		};

		/* Create the Hyperedge Transformer */
		Transformer<HyperEdgeMetadata, String> hyperEdgeTransformer = new Transformer<HyperEdgeMetadata, String>() {
			public String transform(HyperEdgeMetadata metadata) {
				String e= "";
				return e;
			}
		};


		/* Create the graphMLReader2 */
		GraphMLReader2<Graph<String, String>, String, String>
		graphReader = new
		GraphMLReader2<Graph<String, String>, String, String>
		(fileReader, graphTransformer, vertexTransformer,
				edgeTransformer, hyperEdgeTransformer);

		try {
			/* Get the new graph object from the GraphML file */
			Graph g = graphReader.readGraph();
		} catch (GraphIOException ex) {}

	
	}
	
	
	@Override
	public ArrayList<Table> getNodes() {
		
		return this.vertices;
	}

	@Override
	public ArrayList<ForeignKey> getEdges() {
		
		return this.edges;
	}
	
	
	private static void printString(Object string) {

		System.out.println(string);

	}

}
