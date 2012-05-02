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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
	private JMenuItem exportData;
	private JFileChooser fc;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel statisticsPanel;
	private JPanel dataPanel;
	private JPanel calendarPanel;
	private JPanel filterPanel;
	private JPanel cancelPanel;
	private JPanel chartPanel;
	private JPanel emptyPanel;
	private GridLayout mainGrid;
	private GridLayout subGrid;
	private GridLayout statisticsGrid;
	private GridLayout calendarGrid;
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
	private JTable table;
	private JCalendar toCalendar;
	private JCalendar fromCalendar;
	private JButton filterButton;
	private JButton cancelButton;
	private JButton nextDay;
	private JButton nextWeek;
	private JButton nextMonth;
	private JButton previousDay;
	private JButton previousWeek;
	private JButton previousMonth;
	private Date currentDate;
	private Date currentStartDate;
	private Date currentEndDate;
	private JFrame graphFrame;


	/**
	 * Create the frame.
	 */
	public MainWindow() {
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		BasicConfigurator.configure();
		mainFrame = new JFrame("Social Network Analysis");
		
		filterButton = new JButton("Filter");
		cancelButton = new JButton("Clear filter");
		nextDay = new JButton("Next Day");
		nextWeek = new JButton("Next Week");
		nextMonth = new JButton("Next Month");
		previousDay = new JButton("Previous Day");
		previousWeek = new JButton("Previous Week");
		previousMonth = new JButton("Previous Month");
		
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		statisticsPanel = new JPanel();
		dataPanel = new JPanel();
		calendarPanel = new JPanel();
		filterPanel = new JPanel();
		cancelPanel = new JPanel();
		chartPanel = new JPanel();
		emptyPanel = new JPanel();
		
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		helpMenu = new JMenu("Help");
		openMenu = new JMenuItem("Open");
		exitMenu = new JMenuItem("Exit");
		saveMenu = new JMenuItem("Save");
		aboutMenu = new JMenuItem("About");
		createPicMenu = new JMenuItem("Create Picture");
		exportStatMenu = new JMenuItem("Export Statistics");
		exportData = new JMenuItem("Export email's details");
		
		saveMenu.setEnabled(false);
		createPicMenu.setEnabled(false);
		exportStatMenu.setEnabled(false);
		exportData.setEnabled(false);
		
		toCalendar = new JCalendar();
		fromCalendar = new JCalendar();
		
		tab = new JTabbedPane();
		
		mainGrid = new GridLayout(1,2);
		subGrid = new GridLayout(2,1);
		statisticsGrid = new GridLayout(11,2);
		calendarGrid = new GridLayout(2,2);
		
		labels = new ArrayList<JLabel>();
		
		mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		fileMenu.add(openMenu);
		fileMenu.add(saveMenu);
		fileMenu.add(createPicMenu);
		fileMenu.add(exportStatMenu);
		fileMenu.add(exportData);
		fileMenu.add(exitMenu);
		helpMenu.add(aboutMenu);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		//JScrollPane scrollPane = new JScrollPane(dataPanel);
		
		tab.addTab("Chart", chartPanel);
		tab.addTab("Statistics", statisticsPanel);
		//tab.addTab("Datas", scrollPane);
		tab.addTab("Datas", dataPanel);
		
		filterPanel.setLayout(new GridLayout(3,2));
		filterPanel.add(previousDay);
		filterPanel.add(nextDay);
		filterPanel.add(previousWeek);
		filterPanel.add(nextWeek);
		filterPanel.add(previousMonth);
		filterPanel.add(nextMonth);
		
		cancelPanel.setLayout(new GridLayout(3,3));
		cancelPanel.add(emptyPanel);
		cancelPanel.add(emptyPanel);
		cancelPanel.add(emptyPanel);
		cancelPanel.add(filterButton);
		cancelPanel.add(emptyPanel);
		cancelPanel.add(cancelButton);
		cancelPanel.add(emptyPanel);
		cancelPanel.add(emptyPanel);
		cancelPanel.add(emptyPanel);

		
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
		previousMonth.setEnabled(false);
		previousWeek.setEnabled(false);
		previousDay.setEnabled(false);
		nextMonth.setEnabled(false);
		nextWeek.setEnabled(false);
		nextDay.setEnabled(false);
		
		mainFrame.setJMenuBar(menuBar);
		//mainFrame.setLayout(mainGrid);
		rightPanel.setLayout(subGrid);
		//mainFrame.add(leftPanel);
		mainFrame.add(rightPanel);
		mainFrame.setVisible(true);
		mainFrame.setBounds(0, 0, 
				Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height-40);
		//mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height-40);
		calendarPanel.setLayout(calendarGrid);	
		calendarPanel.add(fromCalendar);
		calendarPanel.add(filterPanel);
		calendarPanel.add(toCalendar);
		calendarPanel.add(cancelPanel);
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
		
		/**
		 * open menu
		 */
		openMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (openMenu.isEnabled()) {
					fc = new JFileChooser();
					int returnVal = fc.showDialog(mainFrame, "Open");				
					if (returnVal == JFileChooser.APPROVE_OPTION) {		
						mainFile = fc.getSelectedFile();
						
						fromCalendar.setEnabled(true);
						toCalendar.setEnabled(true);
						filterButton.setEnabled(true);
						cancelButton.setEnabled(true);
						previousMonth.setEnabled(true);
						previousWeek.setEnabled(true);
						previousDay.setEnabled(true);
						//nextMonth.setEnabled(true);
						//nextWeek.setEnabled(true);
						//nextDay.setEnabled(true);
						saveMenu.setEnabled(true);
						createPicMenu.setEnabled(true);
						exportStatMenu.setEnabled(true);
						exportData.setEnabled(true);
					
						viewGraph = new ViewGraph(mainFile.getAbsolutePath());
						viewGraph.addEdge();
					
						viewer = new Viewer(viewGraph.getGraph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
						viewer.enableAutoLayout();
						View view = viewer.addDefaultView(false);

						if (graphFrame != null) {
							graphFrame.dispose();
						}
					
						graphFrame = new JFrame();
						graphFrame.add(view);
						graphFrame.setVisible(true);
						graphFrame.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/2, 0, 
							Toolkit.getDefaultToolkit().getScreenSize().width/2, 
							Toolkit.getDefaultToolkit().getScreenSize().height-40);				
					
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

						initTable();
						charts();
						fromCalendar.setMinSelectableDate(viewGraph.getStartDate());
						fromCalendar.setMaxSelectableDate(viewGraph.getEndDate());
						toCalendar.setMinSelectableDate(viewGraph.getStartDate());
						toCalendar.setMaxSelectableDate(viewGraph.getEndDate());
						fromCalendar.setDate(viewGraph.getStartDate());
						toCalendar.setDate(viewGraph.getEndDate());
						currentStartDate = viewGraph.getStartDate();
						currentEndDate = viewGraph.getEndDate();
						currentDate = viewGraph.getEndDate();
					} else {				
					}
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
				if (createPicMenu.isEnabled()) {
					fc = new JFileChooser();
					int returnVal = fc.showDialog(mainFrame, "Create Picture");				
					if (returnVal == JFileChooser.APPROVE_OPTION) {				
						File file = fc.getSelectedFile();
						createPicture(file.getAbsolutePath());
					} else {				
					}
				}
			}
		});
		
		/**
		 * Export email's details to file
		 */
		exportData.addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e) {
				if (exportData.isEnabled()) {
					fc = new JFileChooser();
					int returnVal = fc.showDialog(mainFrame, "Export email's details");				
					if (returnVal == JFileChooser.APPROVE_OPTION) {				
						File file = fc.getSelectedFile();
						viewGraph.getDB().writeData(file.getAbsoluteFile());
					}
				}
			}
		});
		
		/**
		 * create .dgs file
		 */
		saveMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (saveMenu.isEnabled()) {
					fc = new JFileChooser();
					int returnVal = fc.showDialog(mainFrame, "Save");				
					if (returnVal == JFileChooser.APPROVE_OPTION) {				
						File file = fc.getSelectedFile();
						writeDGS(file.getAbsolutePath());
					} else {
					}
				}
			}
		});
		
		/**
		 * create csv file
		 */
		exportStatMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (exportStatMenu.isEnabled()) {
					fc = new JFileChooser();
					int returnVal = fc.showDialog(null, "Export Statistics");
					if (returnVal == JFileChooser.APPROVE_OPTION) {				
						File file = fc.getSelectedFile();
						viewGraph.writeCSV(file.getAbsolutePath());
					} else {
					}
					mainFrame.repaint();
				}
			}
		});
		
		/**
		 * filtering date
		 */
		filterButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (filterButton.isEnabled()) {
					try {
						//viewGraph.clear();
					} catch (Exception exc) {
						logger.error(exc.getMessage());
					}
					finally {	
						currentStartDate = fromCalendar.getDate();
						currentEndDate = toCalendar.getDate();
						
						//set next buttons
						if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+1))) {
							nextDay.setEnabled(false);
						}
						else {
							nextDay.setEnabled(true);
						}
						if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+7))) {
							nextWeek.setEnabled(false);
						}
						else {
							nextWeek.setEnabled(true);
						}
						if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+30))) {
							nextMonth.setEnabled(false);
						}
						else {
							nextMonth.setEnabled(true);
						}
						
						//set previous buttons
						if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()))) {
							previousDay.setEnabled(true);
							//System.out.println(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()));
						}
						else {
							previousDay.setEnabled(false);
							//System.out.println(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()));
						}
						if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()-6))) {
							previousWeek.setEnabled(true);
						}
						else {
							previousWeek.setEnabled(false);
						}
						if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()-29))) {
							previousMonth.setEnabled(true);
						}
						else {
							previousMonth.setEnabled(false);
						}
						
						viewGraph = new ViewGraph(mainFile.getAbsolutePath());
						viewGraph.addEdge(fromCalendar.getDate(), toCalendar.getDate());
						
						viewer = new Viewer(viewGraph.getGraph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
						viewer.enableAutoLayout();
						View view = viewer.addDefaultView(false);
						
						if (graphFrame != null) {
							graphFrame.dispose();
						}
	
						graphFrame = new JFrame();
						graphFrame.add(view);
						graphFrame.setVisible(true);
						graphFrame.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/2, 0, 
								Toolkit.getDefaultToolkit().getScreenSize().width/2, 
								Toolkit.getDefaultToolkit().getScreenSize().height-40);					
	
						initTable();
						charts();
	
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
						else {
							labels.get(0).setText("");
							labels.get(1).setText("");
							labels.get(2).setText("");
							labels.get(3).setText("");
							labels.get(4).setText("");
							labels.get(5).setText("");
							labels.get(6).setText("");
							labels.get(7).setText("");
							labels.get(8).setText("");
							labels.get(9).setText("");
							labels.get(10).setText("");
						}
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
				if (cancelButton.isEnabled()) {
					try {
						//viewGraph.clear();
					} catch (Exception exc) {
						logger.error(exc.getMessage());
					}
					finally {
						fromCalendar.setDate(viewGraph.getStartDate());
						toCalendar.setDate(viewGraph.getEndDate());
						currentStartDate = viewGraph.getStartDate();
						currentEndDate = viewGraph.getEndDate();
						
						nextDay.setEnabled(false);
						nextWeek.setEnabled(false);
						nextMonth.setEnabled(false);
	
						viewGraph = new ViewGraph(mainFile.getAbsolutePath());
						viewGraph.addEdge();
						
						viewer = new Viewer(viewGraph.getGraph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
						viewer.enableAutoLayout();
						View view = viewer.addDefaultView(false);
						
						if (graphFrame != null) {
							graphFrame.dispose();
						}
	
						graphFrame = new JFrame();
						graphFrame.add(view);
						graphFrame.setVisible(true);
						graphFrame.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/2, 0, 
								Toolkit.getDefaultToolkit().getScreenSize().width/2, 
								Toolkit.getDefaultToolkit().getScreenSize().height-40);					
	
						initTable();
						charts();
						currentDate = viewGraph.getEndDate();
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
						else {
							labels.get(0).setText("");
							labels.get(1).setText("");
							labels.get(2).setText("");
							labels.get(3).setText("");
							labels.get(4).setText("");
							labels.get(5).setText("");
							labels.get(6).setText("");
							labels.get(7).setText("");
							labels.get(8).setText("");
							labels.get(9).setText("");
							labels.get(10).setText("");
						}
					}
				}
			}
		});	
		
		/**
		 * Filtering the next day.
		 */
		nextDay.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (nextDay.isEnabled()) {
					Date from = new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate());
					Date to = new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+1);
					fromCalendar.setDate(from);
					toCalendar.setDate(to);
	
					currentEndDate = to;
					if (fromCalendar.getDate().before(viewGraph.getStartDate())) {
						previousDay.setEnabled(false);
						previousWeek.setEnabled(false);
						previousMonth.setEnabled(false);
					}
					else {
						previousDay.setEnabled(true);
						previousWeek.setEnabled(true);
						previousMonth.setEnabled(true);
					}
					
					//set next buttons
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+1))) {
						nextDay.setEnabled(false);
					}
					else {
						nextDay.setEnabled(true);
					}
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+7))) {
						nextWeek.setEnabled(false);
					}
					else {
						nextWeek.setEnabled(true);
					}
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+30))) {
						nextMonth.setEnabled(false);
					}
					else {
						nextMonth.setEnabled(true);
					}
					
					//set previous buttons
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()))) {
						previousDay.setEnabled(true);
						//System.out.println(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()));
					}
					else {
						previousDay.setEnabled(false);
						//System.out.println(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()));
					}
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()-6))) {
						previousWeek.setEnabled(true);
					}
					else {
						previousWeek.setEnabled(false);
					}
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()-29))) {
						previousMonth.setEnabled(true);
					}
					else {
						previousMonth.setEnabled(false);
					}
	
					try {
						//viewGraph.clear();
					} catch (Exception exc) {
						logger.error(exc.getMessage());
					}
					finally {										
						viewGraph = new ViewGraph(mainFile.getAbsolutePath());
						viewGraph.addEdge(from, to);
						
						viewer = new Viewer(viewGraph.getGraph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
						viewer.enableAutoLayout();
						View view = viewer.addDefaultView(false);
						
						if (graphFrame != null) {
							graphFrame.dispose();
						}
	
						graphFrame = new JFrame();
						graphFrame.add(view);
						graphFrame.setVisible(true);
						graphFrame.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/2, 0, 
								Toolkit.getDefaultToolkit().getScreenSize().width/2, 
								Toolkit.getDefaultToolkit().getScreenSize().height-40);		
	
						initTable();
						charts();
	
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
						else {
							labels.get(0).setText("");
							labels.get(1).setText("");
							labels.get(2).setText("");
							labels.get(3).setText("");
							labels.get(4).setText("");
							labels.get(5).setText("");
							labels.get(6).setText("");
							labels.get(7).setText("");
							labels.get(8).setText("");
							labels.get(9).setText("");
							labels.get(10).setText("");
						}
					}
				}
			}
		});	
		
		/**
		 * Filtering the next week.
		 */
		nextWeek.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (nextWeek.isEnabled()) {
					Date from = new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate());
					Date to = new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+7);
					fromCalendar.setDate(from);
					toCalendar.setDate(to);
	
					currentEndDate = to;
					
					if (fromCalendar.getDate().before(viewGraph.getStartDate())) {
						previousDay.setEnabled(false);
						previousWeek.setEnabled(false);
						previousMonth.setEnabled(false);
					}
					else {
						previousDay.setEnabled(true);
						previousWeek.setEnabled(true);
						previousMonth.setEnabled(true);
					}
					
					// set next buttons
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+1))) {
						nextDay.setEnabled(false);
					}
					else {
						nextDay.setEnabled(true);
					}
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+7))) {
						nextWeek.setEnabled(false);
					}
					else {
						nextWeek.setEnabled(true);
					}
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+30))) {
						nextMonth.setEnabled(false);
					}
					else {
						nextMonth.setEnabled(true);
					}
					
					//set previous buttons
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()))) {
						previousDay.setEnabled(true);
						//System.out.println(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()));
					}
					else {
						previousDay.setEnabled(false);
						//System.out.println(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()));
					}
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()-6))) {
						previousWeek.setEnabled(true);
					}
					else {
						previousWeek.setEnabled(false);
					}
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()-29))) {
						previousMonth.setEnabled(true);
					}
					else {
						previousMonth.setEnabled(false);
					}
	
					try {
						//viewGraph.clear();
					} catch (Exception exc) {
						logger.error(exc.getMessage());
					}
					finally {					
						viewGraph = new ViewGraph(mainFile.getAbsolutePath());
						viewGraph.addEdge(from, to);
						
						viewer = new Viewer(viewGraph.getGraph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
						viewer.enableAutoLayout();
						View view = viewer.addDefaultView(false);
						
						if (graphFrame != null) {
							graphFrame.dispose();
						}
	
						graphFrame = new JFrame();
						graphFrame.add(view);
						graphFrame.setVisible(true);
						graphFrame.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/2, 0, 
								Toolkit.getDefaultToolkit().getScreenSize().width/2, 
								Toolkit.getDefaultToolkit().getScreenSize().height-40);		
						
						initTable();
						charts();
						
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
						else {
							labels.get(0).setText("");
							labels.get(1).setText("");
							labels.get(2).setText("");
							labels.get(3).setText("");
							labels.get(4).setText("");
							labels.get(5).setText("");
							labels.get(6).setText("");
							labels.get(7).setText("");
							labels.get(8).setText("");
							labels.get(9).setText("");
							labels.get(10).setText("");
						}
					}
				}
			}
		});	
		
		/**
		 * Filtering the next month.
		 */
		nextMonth.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (nextMonth.isEnabled()) {
					Date from = new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate());
					Date to = new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+30);
					fromCalendar.setDate(from);
					toCalendar.setDate(to);
	
					currentEndDate = to;
					
					if (fromCalendar.getDate().before(viewGraph.getStartDate())) {
						previousDay.setEnabled(false);
						previousWeek.setEnabled(false);
						previousMonth.setEnabled(false);
					}
					else {
						previousDay.setEnabled(true);
						previousWeek.setEnabled(true);
						previousMonth.setEnabled(true);
					}
					
					//set next buttons
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+1))) {
						nextDay.setEnabled(false);
					}
					else {
						nextDay.setEnabled(true);
					}
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+7))) {
						nextWeek.setEnabled(false);
					}
					else {
						nextWeek.setEnabled(true);
					}
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+30))) {
						nextMonth.setEnabled(false);
					}
					else {
						nextMonth.setEnabled(true);
					}
					
					//set previous buttons
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()))) {
						previousDay.setEnabled(true);
						//System.out.println(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()));
					}
					else {
						previousDay.setEnabled(false);
						//System.out.println(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()));
					}
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()-6))) {
						previousWeek.setEnabled(true);
					}
					else {
						previousWeek.setEnabled(false);
					}
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()-29))) {
						previousMonth.setEnabled(true);
					}
					else {
						previousMonth.setEnabled(false);
					}
	
					try {
						//viewGraph.clear();
					} catch (Exception exc) {
						logger.error(exc.getMessage());
					}
					finally {					
						viewGraph = new ViewGraph(mainFile.getAbsolutePath());
						viewGraph.addEdge(from, to);
						
						viewer = new Viewer(viewGraph.getGraph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
						viewer.enableAutoLayout();
						View view = viewer.addDefaultView(false);
						
						if (graphFrame != null) {
							graphFrame.dispose();
						}
	
						graphFrame = new JFrame();
						graphFrame.add(view);
						graphFrame.setVisible(true);
						graphFrame.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/2, 0, 
								Toolkit.getDefaultToolkit().getScreenSize().width/2, 
								Toolkit.getDefaultToolkit().getScreenSize().height-40);		
						
						initTable();
						charts();
	
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
						else {
							labels.get(0).setText("");
							labels.get(1).setText("");
							labels.get(2).setText("");
							labels.get(3).setText("");
							labels.get(4).setText("");
							labels.get(5).setText("");
							labels.get(6).setText("");
							labels.get(7).setText("");
							labels.get(8).setText("");
							labels.get(9).setText("");
							labels.get(10).setText("");
						}
					}
				}
			}
		});	
		
		/**
		 * Filtering the previous day.
		 */
		previousDay.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (previousDay.isEnabled()) {					
					Date from = new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()-1);
					Date to = new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate());
					
					fromCalendar.setDate(from);
					toCalendar.setDate(to);
	
					currentEndDate = from;
					
					//set previous buttons
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()))) {
						previousDay.setEnabled(true);
						//System.out.println(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()));
					}
					else {
						previousDay.setEnabled(false);
						//System.out.println(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()));
					}
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()-6))) {
						previousWeek.setEnabled(true);
					}
					else {
						previousWeek.setEnabled(false);
					}
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()-29))) {
						previousMonth.setEnabled(true);
					}
					else {
						previousMonth.setEnabled(false);
					}
					
					
					//set next buttons
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+1))) {
						nextDay.setEnabled(false);
					}
					else {
						nextDay.setEnabled(true);
					}
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+7))) {
						nextWeek.setEnabled(false);
					}
					else {
						nextWeek.setEnabled(true);
					}
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+30))) {
						nextMonth.setEnabled(false);
					}
					else {
						nextMonth.setEnabled(true);
					}
	
					try {
						//viewGraph.clear();
					} catch (Exception exc) {
						System.out.println("elozo nap hiba");
						logger.error(exc.getMessage());
					}
					finally {					
						viewGraph = new ViewGraph(mainFile.getAbsolutePath());
						viewGraph.addEdge(from, to);
						
						viewer = new Viewer(viewGraph.getGraph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
						viewer.enableAutoLayout();
						View view = viewer.addDefaultView(false);
						
						if (graphFrame != null) {
							graphFrame.dispose();
						}
	
						graphFrame = new JFrame();
						graphFrame.add(view);
						graphFrame.setVisible(true);
						graphFrame.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/2, 0, 
								Toolkit.getDefaultToolkit().getScreenSize().width/2, 
								Toolkit.getDefaultToolkit().getScreenSize().height-40);		
	
						initTable();
						charts();
	
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
						else {
							labels.get(0).setText("");
							labels.get(1).setText("");
							labels.get(2).setText("");
							labels.get(3).setText("");
							labels.get(4).setText("");
							labels.get(5).setText("");
							labels.get(6).setText("");
							labels.get(7).setText("");
							labels.get(8).setText("");
							labels.get(9).setText("");
							labels.get(10).setText("");
						}
					}
				}
			}
			
		});	
		
		/**
		 * Filtering the previous week.
		 */
		previousWeek.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (previousWeek.isEnabled()) {
					
					Date from = new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()-7);
					Date to = new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate());
					
					fromCalendar.setDate(from);
					toCalendar.setDate(to);
	
					currentEndDate = from;
					
					//set previus buttons
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()))) {
						previousDay.setEnabled(true);
					}
					else {
						previousDay.setEnabled(false);
					}
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()-6))) {
						previousWeek.setEnabled(true);
					}
					else {
						previousWeek.setEnabled(false);
					}
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()-29))) {
						previousMonth.setEnabled(true);
					}
					else {
						previousMonth.setEnabled(false);
					}
					
					//set next buttons
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+1))) {
						nextDay.setEnabled(false);
					}
					else {
						nextDay.setEnabled(true);
					}
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+7))) {
						nextWeek.setEnabled(false);
					}
					else {
						nextWeek.setEnabled(true);
					}
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+30))) {
						nextMonth.setEnabled(false);
					}
					else {
						nextMonth.setEnabled(true);
					}
	
					try {
						//viewGraph.clear();
					} catch (Exception exc) {
						//System.out.println("elozo nap hiba");
						logger.error(exc.getMessage());
					}
					finally {					
						viewGraph = new ViewGraph(mainFile.getAbsolutePath());
						viewGraph.addEdge(from, to);
						
						viewer = new Viewer(viewGraph.getGraph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
						viewer.enableAutoLayout();
						View view = viewer.addDefaultView(false);
						
						if (graphFrame != null) {
							graphFrame.dispose();
						}
	
						graphFrame = new JFrame();
						graphFrame.add(view);
						graphFrame.setVisible(true);
						graphFrame.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/2, 0, 
								Toolkit.getDefaultToolkit().getScreenSize().width/2, 
								Toolkit.getDefaultToolkit().getScreenSize().height-40);		
						
						initTable();
						charts();
	
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
						else {
							labels.get(0).setText("");
							labels.get(1).setText("");
							labels.get(2).setText("");
							labels.get(3).setText("");
							labels.get(4).setText("");
							labels.get(5).setText("");
							labels.get(6).setText("");
							labels.get(7).setText("");
							labels.get(8).setText("");
							labels.get(9).setText("");
							labels.get(10).setText("");
						}
					}
				}
			}
		});	
		
		/**
		 * Filtering the previous month.
		 */
		previousMonth.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (previousMonth.isEnabled()) {
					Date from = new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()-30);
					Date to = new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate());
					
					fromCalendar.setDate(from);
					toCalendar.setDate(to);
	
					currentEndDate = to;
					
					//set prevoius buttons
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()))) {
						previousDay.setEnabled(true);
					}
					else {
						previousDay.setEnabled(false);
					}
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()-6))) {
						previousWeek.setEnabled(true);
					}
					else {
						previousWeek.setEnabled(false);
					}
					if (viewGraph.getStartDate().before(new Date(fromCalendar.getDate().getYear(),fromCalendar.getDate().getMonth(),fromCalendar.getDate().getDate()-29))) {
						previousMonth.setEnabled(true);
					}
					else {
						previousMonth.setEnabled(false);
					}
					
					//set next buttons
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+1))) {
						nextDay.setEnabled(false);
					}
					else {
						nextDay.setEnabled(true);
					}
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+7))) {
						nextWeek.setEnabled(false);
					}
					else {
						nextWeek.setEnabled(true);
					}
					if (viewGraph.getEndDate().before(new Date(currentEndDate.getYear(),currentEndDate.getMonth(),currentEndDate.getDate()+30))) {
						nextMonth.setEnabled(false);
					}
					else {
						nextMonth.setEnabled(true);
					}
					
					graphFrame.dispose();
	
					try {
						//viewGraph.clear();
					} catch (Exception exc) {
						System.out.println("elozo nap hiba");
						logger.error(exc.getMessage());
					}
					finally {					
						viewGraph = new ViewGraph(mainFile.getAbsolutePath());
						viewGraph.addEdge(from, to);
						
						viewer = new Viewer(viewGraph.getGraph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
						viewer.enableAutoLayout();
						View view = viewer.addDefaultView(false);
						
						if (graphFrame != null) {
							graphFrame.dispose();
						}
	
						graphFrame = new JFrame();
						graphFrame.add(view);
						graphFrame.setVisible(true);
						graphFrame.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/2, 0, 
								Toolkit.getDefaultToolkit().getScreenSize().width/2, 
								Toolkit.getDefaultToolkit().getScreenSize().height-40);		
						
						initTable();
						charts();
	
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
						else {
							labels.get(0).setText("");
							labels.get(1).setText("");
							labels.get(2).setText("");
							labels.get(3).setText("");
							labels.get(4).setText("");
							labels.get(5).setText("");
							labels.get(6).setText("");
							labels.get(7).setText("");
							labels.get(8).setText("");
							labels.get(9).setText("");
							labels.get(10).setText("");
						}
					}
				}
			}
		});	
		
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
		Vector<Object> datas = viewGraph.getDB().getData();
		//System.out.println(datas);
		int dinemsion = datas.size() / 7;
		int k = 0;
		//System.out.println(dinemsion);
		Object[][] data = new Object[dinemsion][7];
		for (int i = 0; i<dinemsion;i++ ) {
			for (int j = 0; j<7; ++j) {
				data[i][j] = datas.elementAt(k);
				++k;
				/*if ( j == 6 ) {
					System.out.println(data[i][j]);
				}
				else {
					System.out.print("   " + data[i][j]);
				}*/
			}
		}
		DefaultTableModel model = new DefaultTableModel(data,columnNames);
		table = new JTable(model);	
		
		JTableHeader header = table.getTableHeader();
		
		table.setCellSelectionEnabled(false);
		table.setPreferredSize(new Dimension(mainFrame.getWidth()-50,300));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		Font font = new Font("tables", Font.CENTER_BASELINE, 10);
		
		table.setFont(font);
		
		TableColumn col = table.getColumnModel().getColumn(0);
		col.setPreferredWidth(4);
		
		TableColumn col2 = table.getColumnModel().getColumn(2);
		col2.setPreferredWidth(70);
		
		JScrollPane jsp = new JScrollPane(table);
		jsp.setPreferredSize(new Dimension(mainFrame.getWidth()-20,300));
		
		dataPanel.add(jsp); 
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
	
	/**
	 * create a new chart. It is show the graph statistics.
	 */
	public void charts() {
		final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
		
        final ChartPanel chart2 = new ChartPanel(chart);
        chart2.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chart2);
        chartPanel.removeAll();
        chartPanel.add(chart2);
	}
	
	/**
     * Creates a dataset.
     *
     * @return a dataset.
     */
	private CategoryDataset createDataset() {

	        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	        dataset.addValue(viewGraph.getNodeCount(), "Number of nodes", "");   
	        dataset.addValue(viewGraph.getEdgeCount(), "Number of edges", "");   
	        dataset.addValue(viewGraph.BetweennessAVG(), "Average betweenness", "");   
	        //dataset.addValue(viewGraph.BetweennessMAX(), "Maximum betweenness", "");   
	        dataset.addValue(viewGraph.BetweennessMIN(), "Minimum betweenness", "");   
	        dataset.addValue(viewGraph.avgDegree(), "Average degree deviation", "");  
	        dataset.addValue(viewGraph.graphDensity(), "Density", "");   
	        dataset.addValue(viewGraph.graphDiameter(), "Diameter", "");   
	        dataset.addValue(viewGraph.AVGClusteringCoeff(), "Average clustering coefficient", "");   
	        dataset.addValue(viewGraph.getComponent(), "Component number", "");   
	        dataset.addValue(viewGraph.getGiantComponentNumber(), "Biggest component number", "");   
	        return dataset;

	}
	
	/**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(final CategoryDataset dataset) {
        
        final JFreeChart chart = ChartFactory.createBarChart3D(
            "Graph Statistics",       // chart title
            "Category",               // domain axis label
            "Value",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        final CategoryPlot plot = chart.getCategoryPlot();
        final CategoryAxis axis = plot.getDomainAxis();
        axis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 8.0)
        );
        
        final CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setItemLabelsVisible(true);
        final BarRenderer r = (BarRenderer) renderer;
        r.setMaximumBarWidth(0.05);
        
        return chart;

    }
	
}


