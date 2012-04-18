package Graph;

import java.util.*;
import java.io.*;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import database.EmailDB;

import static org.graphstream.algorithm.Toolkit.*;

import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.j2dviewer.J2DGraphRenderer;

import pstReader.*;

public class ViewGraph {
	
	private Graph graph;
	private PSTReader email;
	private EmailDB db;
	private boolean stepByStep = true;
	private Random numGen = new Random();
	
	public ViewGraph(String file) {
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		email = new PSTReader();	//hogyan tudom altalanositani, ne csak PSTReader-el mukodjon?
		graph = new SingleGraph("Emails");
		
		
		//email.read("junk_email2.pst");
		//email.read("valogatott.pst");
		//email.read("november.pst");
		email.read(file);
		graph.setAutoCreate(true);
        graph.setStrict(false);
        
        addEdge();
        setStyle();
        
        for (int i=1;i<graph.getNodeCount();i++) {
        	if ( graph.getNode(i).getAttribute("ui.color") != "true" && graph.getNode(i).getDegree() > 2 ) {
        		networkCommunityDetectiont(i);
        	}
        }
        
        db = new EmailDB(email);
	}
	
	//add edge for graph
	private void addEdge() {
		for ( int i = 0; i < email.listSize(); i++) {
        	Vector<String> receiver = new Vector<String>();
        	Vector<String> recName = new Vector<String>();
        	receiver = email.getRecipient(i);
        	recName = email.getReceived(i);
        	
        	//System.out.println(email.getSenderEmail(i) + ":");
        	for ( int j=0; j < receiver.size(); ++j) {	
        		String edgeName = email.getSenderEmail(i)+receiver.elementAt(j);
        		//System.out.println("      " + receiver.elementAt(j));
        		
        		//add edges to graph
        		try {
        			graph.addEdge(edgeName, email.getSenderEmail(i), receiver.elementAt(j),true);
        			graph.getNode(email.getSenderEmail(i)).addAttribute("ui.label", email.getSender(i));
        			graph.getNode(receiver.elementAt(j)).addAttribute("ui.label", recName.elementAt(j));
        		} catch (IdAlreadyInUseException e) {
        			e.printStackTrace();
        		}
        	}
        	
        }
	}
	
	public void networkCommunityDetectiont(int nodeID) {
		int rgb1 = numGen.nextInt(256);
		int rgb2 = numGen.nextInt(256);
		int rgb3 = numGen.nextInt(256);
        
        Node node = graph.getNode(nodeID);
        
		int size = node.getDegree()+14;
    	if (size > 80) {
    		size = 80;
    	}
    	
    	node.addAttribute("ui.style", "size: "+ size +"px; fill-color: rgb("+ 
    					rgb1 + "," + rgb2 + "," + rgb3 + ");");
    	node.addAttribute("ui.color", "true");
        
    	Iterator<? extends Node> nodes = node.getNeighborNodeIterator();
    	while(nodes.hasNext()) {
    		Node node2 = nodes.next();
    		if (node2.getDegree() < 4 ) {
    			size = node2.getDegree()+14;
        		if (size > 80) {
        			size = 80;
        		}
    			node2.addAttribute("ui.style", "size: "+ size +"px; fill-color: rgb("+ 
    					rgb1 + "," + rgb2 + "," + rgb3 + ");");
    			node2.addAttribute("ui.color", "true");
    		}
    	}
	}

	
	//get the graph
	public Graph getGraph() {
		return graph;
	}
	
	//set the graph style
	public void setStyle() {
		for (Node node : graph) {
			//node.addAttribute("ui.emailCount", pair.get(node.getId()));
		    //System.out.println(node.getDegree());
		    int size = node.getDegree()+14;
		    if (size > 80) {
		    	size = 80;
		    }
		    node.addAttribute("ui.style", "size: "+ size +"px;");


		}
		
		String styleSheet = "graph { fill-color: black;}" + 
							"node { size: 15px; fill-color: grey, #44F; fill-mode: gradient-radial; stroke-mode: plain; stroke-width: 2px; stroke-color: #CCF;" +
								"shadow-mode: gradient-radial; shadow-width: 10px; shadow-color: #EEF, #000;" +
								"shadow-offset: 0px; text-visibility-mode: hidden; }" +
							"edge { fill-color: #CCF; size: 1px; arrow-shape: none; }" +
							"node:clicked { fill-color: #1e90ff; size: 120px, 120px; text-visibility-mode: normal;}" + 
							"node:selected { fill-color: red; }";
				
		graph.addAttribute("ui.stylesheet", styleSheet);
	}
	
	public void sleep() {
        try { Thread.sleep(500); } catch (Exception e) {System.out.println(e.getMessage());}
    }
	
	//return number of edge
	public int getEdgeCount() {
		return graph.getEdgeCount();
	}
	
	//return number of node
	public int getNodeCount() {
		return graph.getNodeCount();
	}
	
	//Betweenness Centrality Algorithms AVG
	public double BetweennessAVG() {
		int piece = 0;
		double value = 0;
		BetweennessCentrality bcb = new BetweennessCentrality();
		bcb.setWeightAttributeName("weight");
		bcb.init(graph);
		bcb.compute();
		
		for (Node node : graph) {
			++piece;
			value += (double)node.getAttribute("Cb");
		}
		//System.out.println("piece: " + piece);
		//System.out.println("value: " + value);
		return value/piece;
	}
	
	//Betweenness Centrality Algorithms MAX
	public double BetweennessMAX() {
		double MAX = -1;
		BetweennessCentrality bcb = new BetweennessCentrality();
		bcb.setWeightAttributeName("weight");
		bcb.init(graph);
		bcb.compute();
		
		for (Node node : graph) {
			if ((double)node.getAttribute("Cb") > MAX) {
				MAX = (double)node.getAttribute("Cb");
			}
		}
		//System.out.println("MAX: " + MAX);
		return MAX;
	}
	
	//Betweenness Centrality Algorithms MIN
	public double BetweennessMIN() {
		double MIN = 100000;
		BetweennessCentrality bcb = new BetweennessCentrality();
		bcb.setWeightAttributeName("weight");
		bcb.init(graph);
		bcb.compute();
		
		for (Node node : graph) {
			if ((double)node.getAttribute("Cb") < MIN) {
				MIN = (double)node.getAttribute("Cb");
			}
		}
		//System.out.println("MAX: " + MAX);
		return MIN;
	}
	
	//Degree Distribution Algorithms
	public int[] degreeDist() {
		return degreeDistribution(graph);
	}
	
	//Degree Distribution Average- returns the average degree
	public double avgDegree() {
		return averageDegree(graph);
	}
	
	public double degreeAVGDeviation() {
		return degreeAverageDeviation(graph);
	}
	
	//returns the number of links in the graph
	public double graphDensity() {
		return density(graph);
	}

	//computes the diameter of the graph
	public double graphDiameter() {
		return diameter(graph);
	}
	
	//return the clustering coefficient of each node of the graph
	public double[] clusteringCoeff() {
		return clusteringCoefficients(graph);
	}
	
	//return the average clustering coefficient for the graph
	public double AVGClusteringCoeff() {
		return averageClusteringCoefficient(graph);
	}
	
	//return number of component
	public int getComponent() {
		ConnectedComponents cc = new ConnectedComponents();
		cc.init(graph);
		return cc.getConnectedComponentsCount();
	}
	
	//the biggest connected component
	public void getGiantComponent() {
		ConnectedComponents cc = new ConnectedComponents();
		cc.init(graph);
		for (Node node : cc.getGiantComponent()) {
			node.addAttribute("ui.style", "fill-color: red;");
			sleep();
		}
	}
	
	//return the biggest connected component
	public List<Node> getGiantComponentToList() {
		ConnectedComponents cc = new ConnectedComponents();
		cc.init(graph);
		return cc.getGiantComponent();
	}
	
	// return the biggest component number
	public int getGiantComponentNumber() {
		ConnectedComponents cc = new ConnectedComponents();
		cc.init(graph);
		return cc.getGiantComponent().size();
	}
	
	
	public void writeCSV(String filename) {
		String outputFile = filename;
		
		// before we open the file check to see if it already exists
		boolean alreadyExists = new File(outputFile).exists();
			
		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
			
			// if the file didn't already exist then we need to write out the header line
			if (!alreadyExists)
			{
				csvOutput.write("NumberOfNodes");
				csvOutput.write("NumberOfEdges");
				csvOutput.write("AverageBetweenness");
				csvOutput.write("MAXBetweenness");
				csvOutput.write("MINBetweenness		");
				csvOutput.write("AVG Degree Deviation");
				csvOutput.write("Density");
				csvOutput.write("Diameter");
				csvOutput.write("AVG Clustering Coefficient");
				csvOutput.write("Component Number");
				csvOutput.write("Biggest Component Number");
				csvOutput.endRecord();
			}
			// else assume that the file already has the correct header line
			
			// write out a few records
			csvOutput.write(Integer.toString(getNodeCount()));
			csvOutput.write(Integer.toString(getEdgeCount()));
			csvOutput.write(Double.toString(BetweennessAVG()));
			csvOutput.write(Double.toString(BetweennessMAX()));
			csvOutput.write(Double.toString(BetweennessMIN()));
			csvOutput.write(Double.toString(avgDegree()));
			csvOutput.write(Double.toString(graphDensity()));
			csvOutput.write(Double.toString(graphDiameter()));
			csvOutput.write(Double.toString(AVGClusteringCoeff()));
			csvOutput.write(Integer.toString(getComponent()));
			csvOutput.write(Integer.toString(getGiantComponentNumber()));
			csvOutput.endRecord();
			
			csvOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
