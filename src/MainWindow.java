import org.graphstream.graph.Graph;

import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDGS;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.FileSinkImages.LayoutPolicy;
import org.graphstream.stream.file.FileSinkImages.OutputType;
import org.graphstream.stream.file.FileSinkImages.Resolutions;
import org.graphstream.stream.file.FileSourceDGS;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;


import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.graphstream.ui.swingViewer.*;

import Graph.ViewGraph;


public class MainWindow extends JFrame {

	private ViewGraph viewGraph;
	private Viewer viewer;
	private View view;
	private Graph graph = new SingleGraph("embedded");
	private JFrame mainFrame;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenuItem aboutMenu;
	private JMenuItem exitMenu;
	private JMenuItem saveMenu;
	private JMenuItem createPicMenu;
	private JMenuItem exportStatMenu;
	private JMenuItem openMenu;
	private JFileChooser fc;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel statisticsPanel;
	private GridLayout mainGrid;
	private GridLayout subGrid;
	private GridLayout statisticsGrid;
	private JTabbedPane tab;
	private JLabel numberOfNodes;
	private JLabel numberOfEdges;
	private JLabel averageBetweenness;
	private JLabel MAXBetweenness;
	private JLabel MINBetweenness;
	private JLabel AVGDegreeDeviation;
	private JLabel Density;
	private JLabel Diameter;
	private JLabel AVGClusteringCoefficient;
	private JLabel ComponentNumber;
	private JLabel BiggestComponentNumber;
	private List<JLabel> labels;


	/**
	 * Create the frame.
	 */
	public MainWindow() {
		
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		mainFrame = new JFrame("Social Network Analysis");
		
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		statisticsPanel = new JPanel();
		
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		helpMenu = new JMenu("Help");
		openMenu = new JMenuItem("Open");
		exitMenu = new JMenuItem("Exit");
		saveMenu = new JMenuItem("Save");
		aboutMenu = new JMenuItem("About");
		createPicMenu = new JMenuItem("Create Picture");
		exportStatMenu = new JMenuItem("Export Statistics");
		
		tab = new JTabbedPane();
		
		mainGrid = new GridLayout(1,2);
		subGrid = new GridLayout(2,1);
		statisticsGrid = new GridLayout(11,2);
		
		labels = new ArrayList<JLabel>();
		
		fileMenu.add(openMenu);
		fileMenu.add(saveMenu);
		fileMenu.add(createPicMenu);
		fileMenu.add(exportStatMenu);
		fileMenu.add(exitMenu);
		helpMenu.add(aboutMenu);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		tab.addTab("Statistics", statisticsPanel);
		tab.addTab("Datas", new JPanel());
		
		//set the statitics labels
		numberOfNodes = new JLabel("    Number of nodes:");
		numberOfEdges = new JLabel("    Number of edges:");
		averageBetweenness = new JLabel("    Average betweenness:");
		MAXBetweenness = new JLabel("    Maximum betweenness:");
		MINBetweenness = new JLabel("    Minimum betweenness:");
		AVGDegreeDeviation = new JLabel("    Average degree deviation:");
		Density = new JLabel("    Density:");
		Diameter = new JLabel("    Diameter:");
		AVGClusteringCoefficient = new JLabel("    Average clustering coefficient:");
		ComponentNumber = new JLabel("    Component number:");
		BiggestComponentNumber = new JLabel("    Biggest component number:");
		
		for ( int i = 0; i < 11; ++i) {
			String name = "" + i;
			JLabel label = new JLabel(name); 
			labels.add(label);
		}
		
		//set new font
		Font font = new Font("name", Font.CENTER_BASELINE, 15);
		
		labels.add(numberOfNodes);
		labels.add(numberOfEdges);
		labels.add(averageBetweenness);
		labels.add(MAXBetweenness);
		labels.add(MINBetweenness);
		labels.add(AVGDegreeDeviation);
		labels.add(Density);
		labels.add(Diameter);
		labels.add(AVGClusteringCoefficient);
		labels.add(ComponentNumber);
		labels.add(BiggestComponentNumber);
		
		for ( int i = 0; i < labels.size(); i++) {
			labels.get(i).setFont(font);
			if (i < 11) {
				labels.get(i).setText("alma");
			}
		}
		
		//open menu
		openMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				fc = new JFileChooser();
				int returnVal = fc.showDialog(mainFrame, "Open");				
				if (returnVal == JFileChooser.APPROVE_OPTION) {		
					File file = fc.getSelectedFile();
					viewGraph = new ViewGraph(file.getAbsolutePath());
					viewer = viewGraph.getGraph().display();
					view = viewer.getDefaultView();
					view.setSize(leftPanel.getSize());
					//labels.get(0).setText(String.valueOf((viewGraph.getNodeCount())));
					leftPanel.add(view);
				} else {				
				}
			}
		});
		
		//about menu
		aboutMenu.addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e) {
				JOptionPane.showMessageDialog(mainFrame, "This program was created by Istvan Fodor");
			}
		});
		
		//exit program
		exitMenu.addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		
		//create picture
		createPicMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				fc = new JFileChooser();
				int returnVal = fc.showDialog(mainFrame, "Create Picture");				
				if (returnVal == JFileChooser.APPROVE_OPTION) {				
					File file = fc.getSelectedFile();
					createPicture(file.getAbsolutePath());
				} else {				
				}
			}
		});
		
		//create .dgs file
		saveMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				fc = new JFileChooser();
				int returnVal = fc.showDialog(mainFrame, "Save");				
				if (returnVal == JFileChooser.APPROVE_OPTION) {				
					File file = fc.getSelectedFile();
					writeDGS(file.getAbsolutePath());
				} else {
				}
			}
		});
		
		//create csv file
		exportStatMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				fc = new JFileChooser();
				int returnVal = fc.showDialog(null, "Export Statistics");
				if (returnVal == JFileChooser.APPROVE_OPTION) {				
					File file = fc.getSelectedFile();
					viewGraph.writeCSV(file.getAbsolutePath());
				} else {
				}
				mainFrame.repaint();
			}
		});

		leftPanel.setBackground(Color.darkGray);
		
		mainFrame.setJMenuBar(menuBar);
		mainFrame.setLayout(mainGrid);
		rightPanel.setLayout(subGrid);
		mainFrame.add(leftPanel);
		mainFrame.add(rightPanel);
		mainFrame.setVisible(true);
		mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height-40);
		rightPanel.add(new JPanel());
		rightPanel.add(tab);
		
		statisticsPanel.setLayout(statisticsGrid);
		statisticsPanel.add(numberOfNodes);
		statisticsPanel.add(labels.get(0));
		statisticsPanel.add(numberOfEdges);
		statisticsPanel.add(labels.get(1));
		statisticsPanel.add(averageBetweenness);
		statisticsPanel.add(labels.get(2));
		statisticsPanel.add(MAXBetweenness);
		statisticsPanel.add(labels.get(3));
		statisticsPanel.add(MINBetweenness);
		statisticsPanel.add(labels.get(4));
		statisticsPanel.add(AVGDegreeDeviation);
		statisticsPanel.add(labels.get(5));
		statisticsPanel.add(Density);
		statisticsPanel.add(labels.get(6));
		statisticsPanel.add(Diameter);
		statisticsPanel.add(labels.get(7));
		statisticsPanel.add(AVGClusteringCoefficient);
		statisticsPanel.add(labels.get(8));
		statisticsPanel.add(ComponentNumber);
		statisticsPanel.add(labels.get(9));
		statisticsPanel.add(BiggestComponentNumber);
		statisticsPanel.add(labels.get(10));
		
		
	}
	
	public String getExtension(File f) {
		String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
	}
		
	public boolean accept(File f) {
	    if (f.isDirectory()) {
	        return true;
	    }

	    String extension = getExtension(f);
	    if (extension != null) {
	        if (extension.equals("tiff") ||
	            extension.equals("tif") ||
	            extension.equals("gif") ||
	            extension.equals("jpeg") ||
	            extension.equals("jpg") ||
	            extension.equals("png")) {
	                return true;
	        } else {
	            return false;
	        }
	    }

	    return false;
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
			f.writeAll(viewGraph.getGraph(), path);
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


