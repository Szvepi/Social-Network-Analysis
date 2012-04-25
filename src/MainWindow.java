import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

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
import javax.swing.border.Border;
import javax.swing.filechooser.*;
import javax.swing.table.TableColumn;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;


import org.graphstream.ui.swingViewer.*;

import com.toedter.calendar.JCalendar;

import Graph.ViewGraph;

/**
 * Set the main frame's compnents
 *
 * @version  0.5
 * @author   Istvan Fodor
 */

public class MainWindow extends JFrame {
	
	static final Logger logger = Logger.getLogger(MainWindow.class);

	private File mainFile;
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
	private JPanel dataPanel;
	private JPanel calendarPanel;
	private JPanel filterPanel;
	private JPanel cancelPanel;
	private GridLayout mainGrid;
	private GridLayout subGrid;
	private GridLayout statisticsGrid;
	private GridLayout calendarGrid;
	private FlowLayout flowLayout;
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
	private JLabel fromLabel;
	private JLabel toLabel;
	private List<JLabel> labels;
	private JTable table;
	private JCalendar toCalendar;
	private JCalendar fromCalendar;
	private JButton filterButton;
	private JButton cancelButton;


	/**
	 * Create the frame.
	 */
	public MainWindow() {
		
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		BasicConfigurator.configure();
		mainFrame = new JFrame("Social Network Analysis");
		
		filterButton = new JButton("Filtration");
		cancelButton = new JButton("Clear filter");
		
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		statisticsPanel = new JPanel();
		dataPanel = new JPanel();
		calendarPanel = new JPanel();
		filterPanel = new JPanel();
		cancelPanel = new JPanel();
		
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		helpMenu = new JMenu("Help");
		openMenu = new JMenuItem("Open");
		exitMenu = new JMenuItem("Exit");
		saveMenu = new JMenuItem("Save");
		aboutMenu = new JMenuItem("About");
		createPicMenu = new JMenuItem("Create Picture");
		exportStatMenu = new JMenuItem("Export Statistics");
		
		saveMenu.setEnabled(false);
		createPicMenu.setEnabled(false);
		exportStatMenu.setEnabled(false);
		
		toCalendar = new JCalendar();
		fromCalendar = new JCalendar();
		
		tab = new JTabbedPane();
		
		mainGrid = new GridLayout(1,2);
		subGrid = new GridLayout(2,1);
		statisticsGrid = new GridLayout(11,2);
		calendarGrid = new GridLayout(2,2);
		flowLayout = new FlowLayout();
		
		labels = new ArrayList<JLabel>();
		
		fileMenu.add(openMenu);
		fileMenu.add(saveMenu);
		fileMenu.add(createPicMenu);
		fileMenu.add(exportStatMenu);
		fileMenu.add(exitMenu);
		helpMenu.add(aboutMenu);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		JScrollPane scrollPane = new JScrollPane(dataPanel);
		
		tab.addTab("Statistics", statisticsPanel);
		tab.addTab("Datas", scrollPane);
		
		filterPanel.setLayout(new GridLayout(3,3));
		filterPanel.add(new JPanel());
		filterPanel.add(new JPanel());
		filterPanel.add(new JPanel());
		filterPanel.add(new JPanel());
		filterPanel.add(filterButton);
		filterPanel.add(new JPanel());
		filterPanel.add(new JPanel());
		filterPanel.add(new JPanel());
		filterPanel.add(new JPanel());
		
		cancelPanel.setLayout(new GridLayout(3,3));
		cancelPanel.add(new JPanel());
		cancelPanel.add(new JPanel());
		cancelPanel.add(new JPanel());
		cancelPanel.add(new JPanel());
		cancelPanel.add(cancelButton);
		cancelPanel.add(new JPanel());
		cancelPanel.add(new JPanel());
		cancelPanel.add(new JPanel());
		cancelPanel.add(new JPanel());
		
		fromLabel = new JLabel("From:");
		toLabel = new JLabel("To:");
		
		//set the statistics labels
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
				labels.get(i).setText("");
			}
		}
		
		fromCalendar.setEnabled(false);
		toCalendar.setEnabled(false);
		filterButton.setEnabled(false);
		cancelButton.setEnabled(false);
		
		/**
		 * open menu
		 */
		openMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//leftPanel.removeAll();
				fc = new JFileChooser();
				int returnVal = fc.showDialog(mainFrame, "Open");				
				if (returnVal == JFileChooser.APPROVE_OPTION) {		
					mainFile = fc.getSelectedFile();
					
					fromCalendar.setEnabled(true);
					toCalendar.setEnabled(true);
					filterButton.setEnabled(true);
					cancelButton.setEnabled(true);
					saveMenu.setEnabled(true);
					createPicMenu.setEnabled(true);
					exportStatMenu.setEnabled(true);
					
					viewGraph = new ViewGraph(mainFile.getAbsolutePath());
					viewGraph.addEdge();
					viewGraph.getGraph().display();
					/*viewer = viewGraph.getGraph().display(true);
					view = viewer.getDefaultView();
					view.setSize(leftPanel.getSize());*/
					
					labels.get(0).setText(String.valueOf((viewGraph.getNodeCount())));
					labels.get(1).setText(String.valueOf((viewGraph.getEdgeCount())));
					labels.get(2).setText(String.valueOf((viewGraph.BetweennessAVG())));
					labels.get(3).setText(String.valueOf((viewGraph.BetweennessMAX())));
					labels.get(4).setText(String.valueOf((viewGraph.BetweennessMIN())));
					labels.get(5).setText(String.valueOf((viewGraph.degreeAVGDeviation())));
					labels.get(6).setText(String.valueOf((viewGraph.graphDensity())));
					labels.get(7).setText(String.valueOf((viewGraph.graphDiameter())));
					labels.get(8).setText(String.valueOf((viewGraph.AVGClusteringCoeff())));
					labels.get(9).setText(String.valueOf((viewGraph.getComponent())));
					labels.get(10).setText(String.valueOf((viewGraph.getGiantComponentNumber())));
					//leftPanel.add(view);
					initTable();
				} else {				
				}
			}
		});
		
		/**
		 * about menu
		 */
		aboutMenu.addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e) {
				JOptionPane.showMessageDialog(mainFrame, "This program was created by Istvan Fodor");
			}
		});
		
		/**
		 * exit program
		 */
		exitMenu.addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		
		/**
		 * create picture
		 */
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
		
		/**
		 * create .dgs file
		 */
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
		
		/**
		 * create csv file
		 */
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
		
		/**
		 * filtering date
		 */
		filterButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//viewGraph = new ViewGraph(mainFile.getAbsolutePath());
				//viewGraph.addEdge(fromCalendar.getDate(), toCalendar.getDate());
				try {
					viewGraph.clear();
				} catch (Exception exc) {
					logger.error(exc.getMessage());
				}
				finally {
					viewGraph.addEdge(fromCalendar.getDate(), toCalendar.getDate());
					viewGraph.getGraph().display();
					//viewer = viewGraph.getGraph().display();
					/*view = viewer.getDefaultView();
					view.setSize(leftPanel.getSize());
					leftPanel.removeAll();
					leftPanel.add(view);*/
					initTable();
					if (viewGraph.getNodeCount() > 0) {
						labels.get(0).setText(String.valueOf((viewGraph.getNodeCount())));
						labels.get(1).setText(String.valueOf((viewGraph.getEdgeCount())));
						labels.get(2).setText(String.valueOf((viewGraph.BetweennessAVG())));
						labels.get(3).setText(String.valueOf((viewGraph.BetweennessMAX())));
						labels.get(4).setText(String.valueOf((viewGraph.BetweennessMIN())));
						labels.get(5).setText(String.valueOf((viewGraph.degreeAVGDeviation())));
						labels.get(6).setText(String.valueOf((viewGraph.graphDensity())));
						labels.get(7).setText(String.valueOf((viewGraph.graphDiameter())));
						labels.get(8).setText(String.valueOf((viewGraph.AVGClusteringCoeff())));
						labels.get(9).setText(String.valueOf((viewGraph.getComponent())));
						labels.get(10).setText(String.valueOf((viewGraph.getGiantComponentNumber())));
					}
				}
			}
		});
		
		/**
		 * Clear filtering
		 */
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					viewGraph.clear();
				} catch (Exception exc) {
					logger.error(exc.getMessage());
				}
				finally {
					viewGraph.addEdge();
					viewGraph.getGraph().display();
					//viewer = viewGraph.getGraph().display();
					/*view = viewer.getDefaultView();
					view.setSize(leftPanel.getSize());
					leftPanel.removeAll();
					leftPanel.add(view);*/
					initTable();
					if (viewGraph.getNodeCount() > 0) {
						labels.get(0).setText(String.valueOf((viewGraph.getNodeCount())));
						labels.get(1).setText(String.valueOf((viewGraph.getEdgeCount())));
						labels.get(2).setText(String.valueOf((viewGraph.BetweennessAVG())));
						labels.get(3).setText(String.valueOf((viewGraph.BetweennessMAX())));
						labels.get(4).setText(String.valueOf((viewGraph.BetweennessMIN())));
						labels.get(5).setText(String.valueOf((viewGraph.degreeAVGDeviation())));
						labels.get(6).setText(String.valueOf((viewGraph.graphDensity())));
						labels.get(7).setText(String.valueOf((viewGraph.graphDiameter())));
						labels.get(8).setText(String.valueOf((viewGraph.AVGClusteringCoeff())));
						labels.get(9).setText(String.valueOf((viewGraph.getComponent())));
						labels.get(10).setText(String.valueOf((viewGraph.getGiantComponentNumber())));
					}
				}
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
		calendarPanel.setLayout(calendarGrid);	
		calendarPanel.add(fromCalendar);
		calendarPanel.add(toCalendar);
		calendarPanel.add(new JPanel().add(filterPanel));
		calendarPanel.add(new JPanel().add(cancelPanel));
		rightPanel.add(calendarPanel);
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
	
	/**
	 * Create a new table and set data from database
	 */
	public void initTable() {
		String[] columnNames = {"ID",
                "senderEmail",
                "senderName",
                "receiverEmail",
                "receiverName",
                "emailSubject",
                "sendingTime"};
		int dinemsion = viewGraph.getDB().getData().size() / 7;
		int k = 0;
		System.out.println(dinemsion);
		Object[][] data = new Object[dinemsion][7];
		for (int i = 0; i<dinemsion;i++ ) {
			for (int j = 0; j<7; ++j) {
				data[i][j] = viewGraph.getDB().getData().elementAt(k);
				++k;
				//System.out.println(data[i][j]);
			}
		}
		table = new JTable(data, columnNames);	
		table.setCellSelectionEnabled(false);
		dataPanel.add(table);
	}
	
	/**
	 * 
	 * @param f
	 * @return get the file's extension
	 */
	public String getExtension(File f) {
		String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
	}
		
	/**
	 * 
	 * @param f
	 * @return return true value if the file is picture format
	 */
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
	
	/**
	 * create picture 
	 * @param path
	 */
	public void createPicture(String path) {
		FileSinkImages pic = new FileSinkImages(OutputType.JPG, Resolutions.HD1080);
		pic.setLayoutPolicy(LayoutPolicy.COMPUTED_AT_NEW_IMAGE);
		
		try {
			pic.writeAll(viewGraph.getGraph(), path);
		} catch ( IOException e) {
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * write graph to DGS file
	 * @param path
	 */
	public void writeDGS(String path) {
		FileSinkDGS f = new FileSinkDGS();
		try {
			f.writeAll(viewGraph.getGraph(), path);
		} catch (IOException e) {
			//e.printStackTrace();
			//System.out.println("Hiba írásnál");
			logger.error(e.getMessage());
		}
	}
	
	
	/**
	 * read graph from DGS file
	 * @param path
	 */
	public void readDGS(String path) {
		FileSourceDGS fs = new FileSourceDGS();
		
		fs.addSink(graph);
		
		try {
			fs.readAll(path);
		} catch (IOException e) {
			//System.out.println("olvasasi hiba!");
			//e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			fs.removeSink(graph);
		}
		
	}
	
}


