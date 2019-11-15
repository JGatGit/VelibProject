package IHM;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import Data.*;
import Evaluation.*;
import Simulation.Simulator;
import Simulation.*;

//Imports pour la fenetre
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;
import java.awt.event.*;



public class Window extends JFrame implements ActionListener
{	
	//Objets du layout de la fenetre
	private JButton button_run, button_show, button_growth, button_collaboration, button_crit_export;
	private JTextField input_collaboration_rate, input_regulation, input_popularity_growth, input_station;
	JLabel label_collaboration_rate, label_regulation, label_popularity_growth, label_station, 
	label_cancelled_trips, label_time_unbalanced;
	
	//Objects used in the simulation
	private static String stationAddressesFileName = "files/stationsAddresses.txt";
	private static String tripsFileName = "files/trips-2013-10-31.txt";
	private static String initialStatesFileName = "files/initialstates.txt";
	private static ArrayList<Station> baseStationList;
	private static ArrayList<Trip> baseTripList;
	private static Simulator simulation; 
	
	
	//User input
	private static double collaboration_rate = 0.0;
	private static boolean regulation = true;
	private static double popularity_growth = 0.0;
	private static int stationID = 901;
	
	//Defense mechanisms
	static boolean ready = false;
	static boolean alreadyRun = false;
	
	public static void main(String[] args) throws FileNotFoundException
	{
		
		Window window = new Window(500, 400, "Projet Velib");
		
		//File Names
		
		
		//Construct reader
		Read read = new Read();
		
	
		ready=true;
		System.out.println("ok");
		

	}
	
	
	public Window(int width, int height, String title)
	{
		this.setSize(width, height);
		this.setTitle(title);
		this.setResizable(false);
        this.setLocation(400, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		this.add(panel);
		
		//Buttons
		button_run = new JButton("Run");
		button_run.addActionListener(this);
		button_run.setBounds(50, 120, 80, 30); //x,y,width,height
		
		button_show = new JButton("Show");
		button_show.setBounds(140, 120, 80, 30);
		button_show.addActionListener(this);
		
		button_growth = new JButton("Pop. Growth");
		button_growth.setBounds(230, 120, 80, 30);
		button_growth.addActionListener(this);
		
		button_collaboration = new JButton("Collaboration");
		button_collaboration.setBounds(320, 120, 80, 30);
		button_collaboration.addActionListener(this);
		
		button_crit_export = new JButton("Export Station");
		button_crit_export.setBounds(320, 200, 80, 30);
		button_crit_export.addActionListener(this);
		
		//Text fields
		input_collaboration_rate = new JTextField();
		input_collaboration_rate.setBounds(50, 50, 110, 20);
		input_collaboration_rate.addActionListener(this);
		
		input_regulation = new JTextField();
		input_regulation.setBounds(200, 50, 110, 20);
		input_regulation.addActionListener(this);
		
		input_popularity_growth = new JTextField();
		input_popularity_growth.setBounds(350, 50, 110, 20);
		input_popularity_growth.addActionListener(this);
		
		input_station = new JTextField();
		input_station.setBounds(250, 300, 110, 20);
		input_station.addActionListener(this);
		
		//labels
		label_collaboration_rate = new JLabel("Collab Rate (0-100)");
		label_collaboration_rate.setBounds(50, 30, 110, 20);
		
		label_regulation = new JLabel("Regulation (0/1)");
		label_regulation.setBounds(200, 30, 110, 20);
		
		label_popularity_growth = new JLabel("Popularity inc (0-100)");
		label_popularity_growth.setBounds(350, 30, 150, 20);
		
		label_station = new JLabel("Station ID: ");
		label_station.setBounds(180, 300, 110, 20);
		
		panel.add(button_run);
		panel.add(button_show);
		panel.add(button_growth);
		panel.add(button_collaboration);
		panel.add(button_crit_export);
		
		panel.add(input_collaboration_rate);
		panel.add(input_regulation);
		panel.add(input_popularity_growth);
		panel.add(input_station);
		
		panel.add(label_collaboration_rate);
		panel.add(label_regulation);
		panel.add(label_popularity_growth);
		panel.add(label_station);
		this.setVisible(true);
	}
	
	
		public void actionPerformed(ActionEvent e)
		{
			if(!ready)
			{
				System.out.println("Please wait");
			}
			else
			{
				//Event from button "run"
				if(e.getSource() == button_run)
				{
					System.out.println("Working Directory = " +
				              System.getProperty("user.dir"));
					
					Read read = new Read();
					//Creates stationList with their initial condition
					baseStationList = read.createStationList(stationAddressesFileName);
					read.defineInitalStates("files/initialstates.txt", baseStationList);
					
					//Creates trip list
					baseTripList = read.createTripsList("files/trips-2013-10-31.txt", baseStationList);
					
					simulation = new Simulator(baseTripList, baseStationList,regulation, 
							collaboration_rate, popularity_growth);
					try {
						
						simulation.simulate();
						alreadyRun = true;
						System.out.println("ready to show");
							
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
						System.out.println("File not found");
					}
					
				}
				
				if(e.getSource() == input_collaboration_rate)
				{
					String collab_input = input_collaboration_rate.getText();
					input_collaboration_rate.selectAll();
					collaboration_rate = Double.parseDouble(collab_input)/100;
					System.out.println("Collaboration rate = "+collaboration_rate);
					
				}
				
				if(e.getSource() == input_regulation)
				{
					String reg_input = input_regulation.getText();
					input_regulation.selectAll();
					
					regulation = Integer.parseInt(reg_input)==1 ? true : false;
					System.out.println(regulation);
					
				}
				
				if(e.getSource() == input_popularity_growth)
				{
					String pop_input = input_popularity_growth.getText();
					input_popularity_growth.selectAll();
					popularity_growth = Double.parseDouble(pop_input)/100;
					System.out.println(popularity_growth);
					
				}
				
				if(!alreadyRun)
				{
					System.out.println("Must run first");
				}
				else
				{
					if(e.getSource() == button_show)
					{
						
						simulation.visualizeCriticalStationsVariation();
						Read read = new Read();
						ArrayList<Station> baseStationList10days;
						baseStationList10days = read.createStationList(stationAddressesFileName);
						
						read.defineInitalStates(initialStatesFileName, baseStationList10days);
						
						//Creates trip list
						ArrayList<Trip> baseTripList10days;
						baseTripList10days = read.createTripsList(tripsFileName, baseStationList10days);
						
						Simulator simulation10days = new Simulator(baseTripList10days, baseStationList10days,
								regulation, collaboration_rate, popularity_growth);
						simulation10days.visualizeCancelledTrips10days();
					}
					
					if(e.getSource() == button_growth)
					{
						Read read = new Read();
						ArrayList<Station> baseStationListGrowth;
						baseStationListGrowth = read.createStationList(stationAddressesFileName);
						
						read.defineInitalStates(initialStatesFileName, baseStationListGrowth);
						
						//Creates trip list
						ArrayList<Trip> baseTripListGrowth;
						baseTripListGrowth = read.createTripsList(tripsFileName, baseStationListGrowth);
						
						Simulator simulationGrowth = new Simulator(baseTripListGrowth, baseStationListGrowth,
								regulation, collaboration_rate, popularity_growth);
						simulationGrowth.visualizeImpactGrowth();
					}
					
					if(e.getSource() == button_collaboration)
					{
						Read read = new Read();
						ArrayList<Station> baseStationListCollaboration;
						baseStationListCollaboration = read.createStationList(stationAddressesFileName);
						
						read.defineInitalStates(initialStatesFileName, baseStationListCollaboration);
						
						//Creates trip list
						ArrayList<Trip> baseTripListCollaboration;
						baseTripListCollaboration = read.createTripsList(tripsFileName, baseStationListCollaboration);
						
						Simulator simulationCollaboration = new Simulator(baseTripListCollaboration, baseStationListCollaboration,
								regulation, collaboration_rate, popularity_growth);
						
						simulationCollaboration.visualizeImpactCollaboration();
					}
					
					
					if(e.getSource() == input_station)
					{
						String sta_input = input_station.getText();
						input_station.selectAll();
						stationID = Integer.parseInt(sta_input);
						simulation.visualizeStationStates(stationID);
						System.out.println("StationID = "+stationID);
						
					}
					
					if(e.getSource() == button_crit_export)
					{
						try {
							System.out.println("ue2");
							simulation.exportStationStates(stationID);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
					
				}
				
			}
			
		}

}