import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDGS;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.FileSinkImages.LayoutPolicy;
import org.graphstream.stream.file.FileSinkImages.OutputType;
import org.graphstream.stream.file.FileSinkImages.Resolutions;
import org.graphstream.stream.file.FileSourceDGS;


import javax.swing.JFrame;
import java.io.IOException;


import org.graphstream.ui.swingViewer.*;

import Graph.ViewGraph;


public class MainWindow extends JFrame {

	private ViewGraph viewGraph;
	private Viewer viewer;
	private Graph graph = new SingleGraph("embedded");

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		
		/*System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		viewGraph = new ViewGraph();
		viewer = viewGraph.getGraph().display(true);
		
		createPicture("sample.JPG");
		writeDGS("proba.dgs");
		readDGS("proba.dgs");
		viewGraph.writeCSV("proba.csv");*/
		/*readDGS("proba.dgs");	//itt meg vmi nem jo
		try {
			graph.read("proba.dgs");
		} catch (Exception e) {
			System.out.println("olvasasi hiba!");
		}*/
		
		Node A = graph.addNode("A");
		Node B = graph.addNode("B");
		Node E = graph.addNode("E");
		Node C = graph.addNode("C");
		Node D = graph.addNode("D");
		
		/*A.setAttribute("xy", 20,20);
		B.setAttribute("xy", 40,20);
		C.setAttribute("xy", 60,20);
		D.setAttribute("xy", 80,20);
		E.setAttribute("xy", 200,20);*/
		

		graph.addEdge("AB", "A", "B");
		graph.addEdge("AE", "A", "E");
		graph.addEdge("AC", "A", "C");
		graph.addEdge("AD", "A", "D");
		graph.display();
		
	}
	
	//create picture 
	public void createPicture(String path) {
		FileSinkImages pic = new FileSinkImages(OutputType.JPG, Resolutions.HD1080);
		pic.setLayoutPolicy(LayoutPolicy.COMPUTED_AT_NEW_IMAGE);
		
		try {
			pic.writeAll(viewGraph.getGraph(), path);
		} catch ( IOException e) {
			e.printStackTrace();
		}
	}
	
	//write graph to DGS file
	public void writeDGS(String path) {
		FileSinkDGS f = new FileSinkDGS();
		try {
			f.writeAll(viewGraph.getGraph(), "proba.dgs");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Hiba írásnál");
		}
	}
	
	
	//read graph from DGS file
	public void readDGS(String path) {
		FileSourceDGS fs = new FileSourceDGS();
		
		fs.addSink(graph);
		
		try {
			fs.readAll(path);
		} catch (IOException e) {
			System.out.println("olvasasi hiba!");
			e.printStackTrace();
		} finally {
			fs.removeSink(graph);
		}
		
	}
	
}
