package Evaluation;

import Data.*;
import Simulation.*;
import java.util.ArrayList;
import java.util.Date;
import java.io.FileNotFoundException;
import java.util.GregorianCalendar;


import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EvaluatorScenario 
{
    private Scenario refScenario;

	public EvaluatorScenario(Scenario newScenario)
	{
		refScenario = newScenario;
	}
	
	//fonction qui va identifier tous les stations qui sont restes vide ou pleines pendant plus que 30 minutes
    private ArrayList<Station> identifyCriticalStations() 
    { 	
    		ArrayList<Station> criticalStations = new ArrayList<Station>();
    		ArrayList<Station> stationList = refScenario.getStationList();
    	
    		
    		for (int i = 0; i < stationList.size(); i++) 
    		{
	    		Station currentStation = stationList.get(i);
	    		EvaluatorStation.setMinimalCriticalTime(240*60*1000);
	    		if(EvaluatorStation.isCritical(currentStation ))
	    		{
	    			criticalStations.add(currentStation);
	    		}   			
	    	}   		    	
	    	return criticalStations;
    }

    public void exportCSVCriticalStationsVariation(int step,String fileName) throws FileNotFoundException
    {
    		ArrayList<Station> stations = refScenario.getStationList();
    		Date initialDate = stations.get(0).getState(0).getDate();		
    		
    		PrintWriter out = new PrintWriter( fileName) ;
    		out.println("Time, CriticalStations");
    		DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm"); 
    		
    		for(int i = 0; step*i < 24*60*60*1000; i++)
    		{
    			Date currentDate = new Date(initialDate.getTime() + i*step);
    		    String reportDate = df.format(currentDate.getTime());
    			int numberOfBadStations = 0;

    			for(int j = 0; j < stations.size() ; j++)
    			{
    				if(EvaluatorStation.isCritical(stations.get(j), currentDate))
    				{
    					numberOfBadStations += 1;
    				}
    			}
    			
    		   	out.println(reportDate + "," + numberOfBadStations);
    		}
    		out.close();
    		
    		
    		
    }

    public void exportCSVCriticalStationsNames(String fileName) throws FileNotFoundException
    {
   		PrintWriter out = new PrintWriter( fileName) ;
		out.println("StationName, StationID");
    		ArrayList<Station> criticalStations = identifyCriticalStations();
    		for(int i = 0; i < criticalStations.size(); i++)
    		{
    			out.println(i + "::" + criticalStations.get(i).getName() + "," + criticalStations.get(i).getIdentity());
    		}
    		out.close();
		
    }

    public void exportCSVStationStates(int identity,String fileName) throws FileNotFoundException
    {
    		ArrayList<Station> stationList = refScenario.getStationList();
    		Station searchedStation = null;
    		
    		
    		for(int i = 0; i < stationList.size(); i++)
    		{
    			if(stationList.get(i).getIdentity() == identity )
    			{
    				searchedStation = stationList.get(i);
    				i = stationList.size(); //ugly solution to quit the loop
    			}
    		}
    		if(searchedStation == null)
    		{
    			System.out.println("station " + identity + " not found!!!!!!!!!!!111 \n");
    			return;
    		}
    		
    		
    		EvaluatorStation.exportCSVStationStates(searchedStation,fileName);	
    }
   
    public void exportCSVStationStates(String stationName,String fileName) throws FileNotFoundException
    {
    		ArrayList<Station> stationList = refScenario.getStationList();
    		Station searchedStation = null;
    		
    		
    		for(int i = 0; i < stationList.size(); i++)
    		{
    			if(stationList.get(i).getName() == stationName )
    			{
    				searchedStation = stationList.get(i);
    				i = stationList.size(); //ugly solution to quit the loop
    			}
    		}
    		if(searchedStation == null)
    		{
    			System.out.println("station " + stationName + " not found!!!!!!!!!!!111 \n");
    			return;
    		}
    		
    		EvaluatorStation.exportCSVStationStates(searchedStation,fileName);	
    }
   
    public void visualizeStationStates(int identity)
    {
		ArrayList<Station> stationList = refScenario.getStationList();
		Station searchedStation = null;
		for(int i = 0; i < stationList.size(); i++)
		{
			if(stationList.get(i).getIdentity() == identity )
			{
				searchedStation = stationList.get(i);
				i = stationList.size(); //ugly solution to quit the loop
			}
		}
		if(searchedStation == null)
		{
			System.out.println("station " + identity + " not found!!!!!!!!!!!111 \n");
			return;
		}
		GraphStation my_graph = new GraphStation(searchedStation);
		my_graph.showWindow();
    }
    
    public void visualizeStationStates(String name)
    {
		ArrayList<Station> stationList = refScenario.getStationList();
		Station searchedStation = null;
		for(int i = 0; i < stationList.size(); i++)
		{
			if(stationList.get(i).getName() == name )
			{
				searchedStation = stationList.get(i);
				i = stationList.size(); //ugly solution to quit the loop
			}
		}
		if(searchedStation == null)
		{
			System.out.println("station " + name + " not found!!!!!!!!!!!111 \n");
			return;
		}
		GraphStation my_graph = new GraphStation(searchedStation);
		my_graph.showWindow();
    }
       
    public void visualizeCriticalStationsVariation(int step,int typeOfGraph)//1 empty stations,2 full stations, 0 critical stations
    		
    {
    		GraphScenario graph = new GraphScenario(this.refScenario,step,typeOfGraph);
    		graph.showWindow();
    }
   
    public double getCancelledTrips()
    {
    		ArrayList<Trip> tripList= refScenario.getTripList();
    		int nCancelledTrips = 0;
    		for(int i = 0; i < tripList.size(); i++)
    		{
    			if( !tripList.get(i).isValid())
    			{
    				nCancelledTrips += 1;
    			}
    		}
    		
    		return nCancelledTrips/(double)tripList.size();
    }


	public static void main(String[] args) throws FileNotFoundException 
	{
		
		//Creating stations......
		
		int identity1 = 1;
		int capacity1 = 20;
		String name1 = "Meuh";
		String address1 = "Rue Saint Jacques";
		int numberOfFreeBikes1 = 2;
		int numberOfFreeStands1 = 15;
		
		int identity2 = 1;
		int capacity2 = 20;
		String name2 = "Mines";
		String address2 = "Boulevard St. Michel";
		int numberOfFreeBikes2 = 6;
		int numberOfFreeStands2 = 14;
		
		Station station1 = new Station(identity1,name1,address1,capacity1,0,0);
		station1.setIsOpen(true);
		Station station2 = new Station(identity2,name2,address2,capacity2,0,0);
		station2.setIsOpen(true);
		ArrayList<Station> stationList = new ArrayList<Station>();
		stationList.add(station1);
		stationList.add(station2);
		
		//Setting primary states....
		
		Date dateState1 = new GregorianCalendar(1995, 02, 31,0,0).getTime();
		Date dateState2 = new GregorianCalendar(1995, 02, 31,0,0).getTime();	
		
		State state1 = new State(numberOfFreeBikes1,numberOfFreeStands1,dateState1);
		State state2 = new State(numberOfFreeBikes2,numberOfFreeStands2,dateState2);
		
		station1.setPrimaryState(state1);
		station2.setPrimaryState(state2);
				
		Date startTrip1 = new GregorianCalendar(1995, 02, 31,1,28).getTime();
		Date endTrip1 = new GregorianCalendar(1995, 02, 31,2,29).getTime();
		Date startTrip2 = new GregorianCalendar(1995, 02, 31,2,30).getTime();
		Date endTrip2 = new GregorianCalendar(1995, 02, 31,2,31).getTime();
		Date startTrip3 = new GregorianCalendar(1995, 02, 31,2,32).getTime();
		Date endTrip3 = new GregorianCalendar(1995, 02, 31,2,33).getTime();
		Date startTrip4 = new GregorianCalendar(1995, 02, 31,12,39).getTime();
		Date endTrip4 = new GregorianCalendar(1995, 02, 31,12,40).getTime();

		//Creating trips.....
		Trip trip1 = new Trip(Reason.RENT, startTrip1, station1, endTrip1, station2);
		Trip trip2 = new Trip(Reason.RENT, startTrip2 , station1,endTrip2, station2);
		Trip trip3 = new Trip(Reason.RENT, startTrip3, station1,endTrip3, station2);
		Trip trip4 = new Trip(Reason.RENT, startTrip4 , station2,endTrip4, station1);
		ArrayList<Trip> tripList = new ArrayList<Trip>();
		tripList.add(trip1);
		tripList.add(trip2);
		tripList.add(trip3);
		tripList.add(trip4);	
		//Creating scenario.....
		Scenario scenario = new Scenario(stationList,tripList,0);		
		//Starting tests.....	
		
		ArrayList<Station> stations = scenario.getStationList();
		
		scenario.runTrips();
		
		EvaluatorScenario my_evaluateur = new EvaluatorScenario(scenario);
		
		
		ArrayList<Station> criticalStations = my_evaluateur.identifyCriticalStations();
		
		System.out.println("-------------------------critical stations -----------------------\n ");
		for(int i = 0; i < criticalStations.size(); i++)
		{
			if(criticalStations.get(i).getIdentity() != identity1)
			{
				System.out.println("Error searching for critical stations. ERROR");
			}
			else
			{
				System.out.println("Successfully found the critical station id:" + criticalStations.get(i).getIdentity() + "\n");
			}
		}
		
		System.out.println("--------------after running trips------------- \n");
		
		
		stations = scenario.getStationList();
		
		for(int i = 0; i < stations.size(); i++)
		{
			System.out.println(stations.get(i));
		}
		
		if(stations.get(0).getLatestState().getNBikes() == 1)
		{
			System.out.println("Last state of first station 1 is right \n" );
		}
		else
		{
			System.out.println("Last state of first station 1 is wrong. ERROR" );
		}
		
		System.out.println("------------writting to files----------- \n");
		
		for(int i = 0; i < stations.size(); i++)
		{
			my_evaluateur.exportCSVStationStates(stations.get(i).getName(),"output/states_");		
		}
		
	    System.out.println("----------testing isEmptyOrFull-------------\n");
		
		Date date1 = new GregorianCalendar(1995, 02, 31,2,36).getTime();
		boolean isCritical =  EvaluatorStation.isCritical(station1, date1);
		System.out.println("Station 1 is critical at" + date1 + " ? " + isCritical);
		if(isCritical)
		{
			System.out.println("EvaluatorStation.isCritical seems to work \n");
		}
		else
		{
			System.out.println("EvaluatorStation.isCritical doesnt seem to work. ERROR \n");
		}
		
		Date date2 = new GregorianCalendar(1995, 02, 31,2,34).getTime();
		isCritical =  EvaluatorStation.isCritical(station2, date2);
		System.out.println("Station 2 is critical at" + date2 + " ? " + isCritical);
		
		if(!isCritical)
		{
			System.out.println("EvaluatorStation.isCritical seems to work \n");
		}
		else
		{
			System.out.println("EvaluatorStation.isCritical doesnt seem to work. ERROR");
		}
		
		System.out.println("--------------testing exportCSVCriticalStations--------\n");
		
		my_evaluateur.exportCSVCriticalStationsVariation(30*60,"src/Evaluation/variationOfCriticalStations.csv");
		
		System.out.println("-------------visualizing variation critical stations------------\n");
		
		my_evaluateur.visualizeCriticalStationsVariation(15*60*1000,0);
		
		System.out.println("-------------visualizing states variation of all stations---------\n");
		
		for(int i = 0; i < stations.size(); i++)
		{
			my_evaluateur.visualizeStationStates(stations.get(i).getName());	
		}
		
		System.out.println("-------------test ended succesfully-------------\n");
		
		return;
		
	}

}


