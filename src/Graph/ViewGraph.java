package Graph;

import java.util.*;
import java.io.*;

import javax.swing.JOptionPane;

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

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import pstReader.*;

/**
 * This class create the new graph, and put data to it. 
 * You can find some statistic algorithm in this class.
 * You can write these statistics to a file.
 *
 * @version  0.3
 * @author   Istvan Fodor
 */

public class ViewGraph {
	
	static final Logger logger = Logger.getLogger(ViewGraph.class);
	
	private Graph graph;
	private PSTReader email;
	private EmailDB db;
	private Random numGen = new Random();
	private Date startDate;
	private Date endDate;
	
	/**
	 * Initialized new PSTReader, SingleGraph.
	 * addEdge() function create the graph then set a new style.
	 * @param file
	 */
	public ViewGraph(String file) {
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		BasicConfigurator.configure();
		email = new PSTReader();	//hogyan tudom altalanositani, ne csak PSTReader-el mukodjon?
		//graph = new SingleGraph("Emails");
		
		email.read(file);
		//graph.setAutoCreate(true);
        //graph.setStrict(false);
        //System.out.println(email.getStartDate());
	}
	
	/**
	 * clear the graph's nodes and edges
	 */
	public void clear() {
		/*if (graph.getNodeCount() > 0 ) {
			graph.clear();
		}*/
		for ( int i = 0; i < graph.getEdgeCount(); i++) {
			graph.removeEdge(i);
		}
		for ( int i=0; i< graph.getNodeCount(); i++) {
			graph.removeNode(i);
		}
	}
	
	/**
	 * Set the networkCommunityDetectiont function
	 */
	public void setNetworkComDet() {
		for (int i=1;i<graph.getNodeCount();i++) {
        	if ( graph.getNode(i).getAttribute("ui.color") != "true" && graph.getNode(i).getDegree() > 2 ) {
        		networkCommunityDetectiont(i);
        	}
        }
	}
	
	/**
	 * add edge to graph
	 */
	public void addEdge() {
		graph = new SingleGraph("Emails");
		graph.setAutoCreate(true);
        graph.setStrict(false);
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
        			//e.printStackTrace();
        			logger.error(e.getMessage());
        		}
        	}
        	
        }
		setStyle();
		setNetworkComDet();
		db = new EmailDB(email);
		db.writeData(new File("alma"));
	}
	
	/**
	 * add edge to graph
	 */
	public void addEdge(Date from, Date to) {
		//clear();
		graph = new SingleGraph("Emails");
		graph.setAutoCreate(true);
        graph.setStrict(false);
		for ( int i = 0; i < email.listSize(); i++) {    	  	
        	//System.out.println(date);
        	if ( from.before(to) ) { 
    			Date date = email.getTime(i);
            	Vector<String> receiver = new Vector<String>();
            	Vector<String> recName = new Vector<String>();
            	receiver = email.getRecipient(i);
            	recName = email.getReceived(i); 
            	if (from.before(date)) {
            		if (date.before(to)) {
                		for ( int j=0; j < receiver.size(); ++j) {	
                			String edgeName = email.getSenderEmail(i)+receiver.elementAt(j);
                			//System.out.println(from.after(to));
                		
                			//add edges to graph
                			try {
                				graph.addEdge(edgeName, email.getSenderEmail(i), receiver.elementAt(j),true);
                				graph.getNode(email.getSenderEmail(i)).addAttribute("ui.label", email.getSender(i));
                				graph.getNode(receiver.elementAt(j)).addAttribute("ui.label", recName.elementAt(j));
                			} catch (IdAlreadyInUseException e) {
                				//e.printStackTrace();
                				logger.error(e.getMessage());
                			}
                		}
            		}
            	}

        	}
        	else {
        		//JOptionPane.showMessageDialog(null, "The end date must be higher than start date!");
        		//System.out.println("A kezdeti datum nem lehet nagyobb mint a vege datum ");
        	}
        }
		setStyle();
		setNetworkComDet();
		db = new EmailDB(email, from, to);
	}
	
	/**
	 * Add different colors to nodes so the groups separated inside the graph
	 * @param nodeID
	 */
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
	
	/**
	 * get the graph
	 * @return Graph
	 */
	public Graph getGraph() {
		return graph;
	}
	
	/**
	 * get the DB
	 * @return EmailDB
	 */
	public EmailDB getDB() {
		return db;
	}
	
	/**
	 * Set the graph style
	 */
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
	
	/**
	 * sleep 0-5 second
	 */
	public void sleep() {
        try { Thread.sleep(500); } catch (Exception e) {
        		//System.out.println(e.getMessage());
        		logger.error(e.getMessage());
        	}
    }
	
	/**
	 * return number of edge
	 * @return integer
	 */
	public int getEdgeCount() {
		return graph.getEdgeCount();
	}
	
	/**
	 * return number of node
	 * @return integer
	 */
	public int getNodeCount() {
		return graph.getNodeCount();
	}
	
	/**
	 * Betweenness Centrality Algorithms AVG
	 * @return double
	 */
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
	
	/**
	 * Betweenness Centrality Algorithms MAX
	 * @return double
	 */
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
	
	/**
	 * Betweenness Centrality Algorithms MIN
	 * @return double
	 */
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
	
	/** 
	 * Degree Distribution Algorithms
	 * @return int[]
	 */
	public int[] degreeDist() {
		return degreeDistribution(graph);
	}
	
	/**
	 * Degree Distribution Average- returns the average degree
	 * @return double
	 */
	public double avgDegree() {
		return averageDegree(graph);
	}
	
	/**
	 * returns the average deviation
	 * @return double
	 */
	public double degreeAVGDeviation() {
		return degreeAverageDeviation(graph);
	}
	
	/** 
	 * returns the number of links in the graph
	 * @return double
	 */
	public double graphDensity() {
		return density(graph);
	}

	/** 
	 * computes the diameter of the graph
	 * @return double
	 */
	public double graphDiameter() {
		return diameter(graph);
	}
	
	/**
	 * return the clustering coefficient of each node of the graph
	 * @return double[]
	 */
	public double[] clusteringCoeff() {
		return clusteringCoefficients(graph);
	}
	
	/** 
	 * return the average clustering coefficient for the graph
	 * @return double
	 */
	public double AVGClusteringCoeff() {
		return averageClusteringCoefficient(graph);
	}
	
	/**
	 * return number of component
	 * @return int
	 */
	public int getComponent() {
		ConnectedComponents cc = new ConnectedComponents();
		cc.init(graph);
		return cc.getConnectedComponentsCount();
	}
	
	/**
	 * the biggest connected component
	 */
	public void getGiantComponent() {
		ConnectedComponents cc = new ConnectedComponents();
		cc.init(graph);
		for (Node node : cc.getGiantComponent()) {
			node.addAttribute("ui.style", "fill-color: red;");
			sleep();
		}
	}
	
	/** 
	 * return the biggest connected component
	 * @return List<Node>
	 */
	public List<Node> getGiantComponentToList() {
		ConnectedComponents cc = new ConnectedComponents();
		cc.init(graph);
		return cc.getGiantComponent();
	}
	
	/**
	 *  return the biggest component number
	 * @return int
	 */
	public int getGiantComponentNumber() {
		ConnectedComponents cc = new ConnectedComponents();
		if (graph.getNodeCount() > 0) {
			cc.init(graph);
			return cc.getGiantComponent().size();
		} else {
			return 0;
		}
	}
	
	/**
	 * write statistics to file
	 * @param filename
	 */
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
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @return The earliest email delivery time.
	 */
	public Date getStartDate() {
		return email.getStartDate();
	}
	
	/**
	 * 
	 * @return The latest email delivery time.
	 */
	public Date getEndDate() {
		return email.getEndDate();
	}
	
}
