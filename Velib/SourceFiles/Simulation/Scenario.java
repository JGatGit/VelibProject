package Simulation;

import Data.*;
import Evaluation.EvaluatorScenario;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class Scenario {
	
	/* declaration of attributes */
    private double collaborationRate;
    private ArrayList<Trip> tripList;
    private ArrayList<Station> stationList;
    private ArrayList<Trip> waitingTrips ;
    
    
    /* Constructors */
    
   /** Base Scenario Constructor */
    public Scenario (ArrayList <Station> stationList, ArrayList <Trip> tripList, double collaborationRate) 
    {
    		this.collaborationRate = collaborationRate;
    		this.tripList = tripList;
	    this.stationList = stationList;
	    waitingTrips = new ArrayList <Trip> ();
   }
       
    /** regulation constructor */
    
	public ArrayList <Trip> getTripList () 
	{
		return tripList;
	}
	
	public ArrayList <Station> getStationList () 
	{
		return stationList;
	}
           
    /** function to execute trips, essential simulation tool */
    public void startNewDay()
    {
    		for(int i = 0; i < stationList.size(); i++)
    		{
    			State newFirstState    =	 stationList.get(i).getLatestState();
    			newFirstState.setDate(stationList.get(i).getState(0).getDate());
    			stationList.get(i).clearStates();
    			stationList.get(i).setPrimaryState(newFirstState);
    		}
    }
    
    public void runTrips() 
    {     
    	//	while loop runs through the tripList 
	    	int i = 0;
	    	while (i < tripList.size())	{
	    		
	    		// pointer i on the selectedTrip in the tripList
	    		Trip selectedTrip = tripList.get(i);
	    		
	    		// determines the next Trip to execute
	    		Trip currentTrip = findNextTrip(selectedTrip);
	    	
	    		/* if findNextTrip did not change currentTrip
	    		=> start Trip i and proceed to the next trip (i++) */
	    		if (selectedTrip == currentTrip) {
	    			
	    			startTrip(currentTrip);
	    			i++;
	    		}
	    	
	    		/* if the findNextTrip changes currentTrip
	    		=> end currentTrip */
	    		else
	    		{
	    			State currentEndState = currentTrip.getEndStation().getLatestState();	    			
	    			if (!currentEndState.isCriticallyEmpty()) { //peut yetre autre crit�re empty closest Station exists
	    				proposeCollaboration(currentTrip);
	    			}
	    			endTrip(currentTrip);
	    		} 
	    	} //loop end	    	
	    	// terminate trips in the waiting list
	    	while (waitingTrips.size() > 0) 
	    	{    		
	    		Trip tempTrip = waitingTrips.get(0);
	    		for (int p = 0; p < waitingTrips.size(); p++)
	    		{ 	
	        		if (tempTrip.getEndDate().after(waitingTrips.get(p).getEndDate()))
	        		{
	        			tempTrip = waitingTrips.get(p);
	        		}	
	    		}
    			State currentEndState = tempTrip.getEndStation().getLatestState();	   
    			if (!currentEndState.isCriticallyEmpty()) { //peut yetre autre crit�re empty closest Station exists
    				proposeCollaboration(tempTrip);
    			}
	    		endTrip(tempTrip);
	    	}	    	
	    	return;
    }
    
    private void proposeCollaboration (Trip currentTrip) 
    {
	    	if (Math.random() < this.collaborationRate)
	    	{
	    		ArrayList<Station> closestStations = currentTrip.getEndStation().getClosestStationList();
	    		for (int j= 0; j < closestStations.size(); j++) 
	    		{
	    		
	    			Station currentStation = closestStations.get(j);
	    			State currentState = currentStation.getLatestState();
	    			if (currentState.isEmpty() && currentStation.isOpen() &&(!currentState.isFull())) 
	    			{
	    		//		System.out.println("Station::" + currentStation.getIdentity() + " estado : " + currentState.getNBikes());
	    				currentTrip.setEndStation(currentStation);//PROBLEM HERE!!!!!!!!!!!!!!!!!1 NA LOGICA!!!!!
	    				break;
	    			}
	    		}
	    	}
	    	return;
    }
    
    /* supplementary methods for runTrips() */
    
    /** Starts a Trip */
    private void startTrip(Trip trip) 
    {
	    	if(trip.getStartStation() == null)
	    	{
	    		return;
	    	}
	    	else if (!trip.getStartStation().isOpen()) 
	    	{
			trip.cancelTrip();
		}	
		else if (trip.getStartStation().getLatestState().isEmpty())	
		{
			trip.cancelTrip();
		}			
		else 
		{
			trip.getStartStation().takeBike(trip);
			waitingTrips.add(trip);
			trip.validateTrip();
		}    	
	    	return;
    }
    
    /** Terminates a Trip */
    private void endTrip (Trip trip)	
    {  	
	    	if(trip.getEndStation() == null)
	    	{
	    		return;
	    	}
	    	//System.out.println(trip);
	    	
	    	if (!trip.getEndStation().isOpen() || trip.getEndStation().getLatestState().isFull()) {//is full => find close station
	    		
	    		// find closest station that is not already full
	    		trip.setEndStation(findNextUsableStation(trip.getEndStation()));
	    	}	
	    	trip.getEndStation().returnBike(trip);
	    	waitingTrips.remove(trip);
	    	
	    	return;
    }

    /** Finds the next Trip for execution */
    private Trip findNextTrip(Trip selectedTrip) 
    {	
	    	// findNextTrip default return is the given Trip selectedTrip
	    	Trip nextTrip = selectedTrip;
	    	// check for Trips in the waiting list
	    	if	(!waitingTrips.isEmpty())	
	    	{ 		
	    		// find the Trip in the waiting list which finishes next
	    		Trip nextEndTrip = waitingTrips.get(0);
	    		
	    		for (int i = 0; i < waitingTrips.size(); i++) 
	    		{   		
	    			if (nextEndTrip.getEndDate().after(waitingTrips.get(i).getEndDate())) 
	    			{
	    				nextEndTrip = waitingTrips.get(i);
	    			}
	    		}
	    	
	    		/* check if the default Trip starts before the next ending Trip
	    		
	    		if trips from the waiting list are passed to the method, the check is skipped since trips in the waiting list are always valid
	    		trips that have not passed startTrip will always be checked, because the default return of isValid is false
	    		
	    		it should be avoided to pass trips that have gone through startTrip, and have been cancelled
	    		(then the method findNextTrip needs to be adjusted) */
	    		if	(!selectedTrip.isValid() && nextTrip.getStartDate().after(nextEndTrip.getEndDate())) 
	    		{
	    			nextTrip = nextEndTrip;
	    		}
	    	}    	
	    	return nextTrip;
    }
    
    private Station findNextUsableStation(Station referenceStation) 
    {
    		return findNextUsableStation(referenceStation, 1);
    }
    
    /** Finds another close station based on the referenceStation*/
    private Station findNextUsableStation(Station referenceStation, int iteration) {
    	
	    	Station usableStation = referenceStation;
	    	ArrayList<Station> closestStations = referenceStation.getClosestStationList();
	    
	    	// search all the closest stations for the reference station
	    	// the list should ideally be sorted by distance to the reference station
	    	for (int i = 0; i < closestStations.size(); i++)
	    	{	
	    		// when you find a non full station => select it and break the loop
	    		if (!closestStations.get(i).getLatestState().isFull()) 
	    		{
	    			usableStation = closestStations.get(i);
	    			break;
	    		}
	    	}
	    	
	    	// catch all closest stations are full => find randomStation
	    	if (usableStation == referenceStation) 
	    	{
	    		if (iteration > 5) 
	    		{
	    			System.out.println("warning too many iterations finding a new station");
	    			//throw new NoStationsAvailableException;
	    		}    		
	    		else 
	    		{
	    			int tempMax = stationList.size() - 1;
	    			int randomIndex = (int) (tempMax * Math.random());
	    			Station randomStation = stationList.get(randomIndex);
	    			findNextUsableStation(randomStation, iteration + 1);	 //recursive!!
	    		}
	    	}
	    	return usableStation;
    }
      
    /* different Simulation Methods */
    
    /** method to simulate a Velib Scenario without regulation 
     * @throws FileNotFoundException */
  
    
    // Main method for testing
    public static void main (String args[]) throws FileNotFoundException {
    	
    	// declaration of attributes
    	Scenario baseScenario1;
    	Scenario baseScenario2;
    	Scenario baseScenario3;
    	Scenario baseScenario4;
    	
    	
    	// first Test Block
    	
    	System.out.println("First Test");
    	
    	ArrayList <Trip> testTrips1 = new ArrayList <Trip> ();
    	ArrayList <Station> testStations1 = new ArrayList <Station> ();
    	
    	//creation of test stations + states
    	Station station1_1 = new Station(901,"00901 - ALLEE DU BELVEDERE","ALLEE DU BELVEDERE PARIS 19 - 0 75000 Paris - 75000 PARIS", 20,2.391225227186182,48.892795924112306);
    	State state1_1 = new State(2,18, "20131030125959");
    	station1_1.setIsOpen(true);
    	station1_1.setPrimaryState(state1_1);
    	testStations1.add(station1_1);
    	    
    	Station station2_1 = new Station( 903, "00903 - QUAI MAURIAC  / PONT DE BERCY", "FETE DE L\u0027OH (BERCY) - QUAI MAURIAC ANG PONT DE BERCY", 20, 2.374340554605615, 48.83713368945151);
    	State state2_1 = new State(18,2,"20131030125959");
    	station2_1.setIsOpen(true);
    	station2_1.setPrimaryState(state2_1);
    	testStations1.add(station2_1);
    	
    	Station station3_1 = new Station( 904, "00904 - PLACE JOFFRE / ECOLE MILITAIRE", "ECOLE MILITAIRE-AVENUE DE LA MOTTE PICQUET - 75007 PARIS", 30, 2.301961227213259, 48.85213620522547);
    	State state3_1 = new State(24,6,"20131030125959");
    	station3_1.setIsOpen(true);
    	station3_1.setPrimaryState(state3_1);
    	testStations1.add(station3_1);
    	
    	station1_1.setClosestStations(2,testStations1);
    	station2_1.setClosestStations(2,testStations1);
    	station3_1.setClosestStations(2,testStations1);
    
    	// initializing test dates
    	Date date1_1 = new GregorianCalendar(2013, 10, 30,13,0,0).getTime();
    	Date date2_1 = new GregorianCalendar(2013, 10, 30,13,20,0).getTime();		
    	Date date3_1 = new GregorianCalendar(2013, 10, 30,13,0,2).getTime();	
    	Date date4_1 = new GregorianCalendar(2013, 10, 30,13,10,0).getTime();	
  		Date date5_1 = new GregorianCalendar(2013, 10, 30,12,0,5).getTime();
  		Date date6_1 = new GregorianCalendar(2013, 10, 30,13,0,1).getTime();
  		
    	
  		// initializing test trips
  		Trip trip1_1 = new Trip (Reason.RENT, date5_1, station2_1, date6_1, station1_1); //inverse direction
    	//testTrips1.add(trip1_1);
    	Trip trip2_1 = new Trip(Reason.RENT, date1_1, station3_1, date2_1, station2_1); // 2000s
    	testTrips1.add(trip2_1);
    	Trip trip3_1 = new Trip (Reason.RENT, date3_1, station3_1, date4_1, station2_1); // later shorter 1000s  
    	testTrips1.add(trip3_1);
    	
    	baseScenario1= new Scenario (testStations1, testTrips1,0);
    	
    	//print of Trip validation before runTrips()
    	ArrayList <Trip> baseScenarioTripList = baseScenario1.tripList;
    	for (int i = 0; i < baseScenarioTripList.size(); i++) {
    		System.out.println(baseScenarioTripList.get(i).isValid());
    	}
    	
    	baseScenario1.runTrips();
    	
    	//print of Stations after Simulation including last States
    	ArrayList <Station> baseScenarioStationList = baseScenario1.stationList;
    	for (int i = 0; i < baseScenarioStationList.size(); i++) {
    		System.out.println(baseScenarioStationList.get(i));
    	}
    	
    	//print of Trip validation after simulation
    	for (int i = 0; i < baseScenarioTripList.size(); i++) {
    		System.out.println(baseScenarioTripList.get(i).isValid());
    	}
    	
    	System.out.println();
    	
// Second Test Block
    	System.out.println("Second Test");
    	
    	ArrayList <Trip> testTrips2 = new ArrayList <Trip> ();
    	ArrayList <Station> testStations2 = new ArrayList <Station> ();
    	
    	//creation of test stations + states
    	Station station1_2 = new Station(901,"00901 - ALLEE DU BELVEDERE","ALLEE DU BELVEDERE PARIS 19 - 0 75000 Paris - 75000 PARIS", 20,2.391225227186182,48.892795924112306);
    	State state1_2 = new State(2,18, "20131030125959");
    	station1_2.setIsOpen(true);
    	station1_2.setPrimaryState(state1_2);
    	testStations2.add(station1_2);
    	    
    	Station station2_2 = new Station( 903, "00903 - QUAI MAURIAC  / PONT DE BERCY", "FETE DE L\u0027OH (BERCY) - QUAI MAURIAC ANG PONT DE BERCY", 20, 2.374340554605615, 48.83713368945151);
    	State state2_2 = new State(18,2,"20131030125959");
    	station2_2.setIsOpen(true);
    	station2_2.setPrimaryState(state2_2);
    	testStations2.add(station2_2);
    	
    	Station station3_2 = new Station( 904, "00904 - PLACE JOFFRE / ECOLE MILITAIRE", "ECOLE MILITAIRE-AVENUE DE LA MOTTE PICQUET - 75007 PARIS", 30, 2.301961227213259, 48.85213620522547);
    	State state3_2 = new State(24,6,"20131030125959");
    	station3_2.setIsOpen(true);
    	station3_2.setPrimaryState(state3_2);
    	testStations2.add(station3_2);
    	
    	station1_2.setClosestStations(2,testStations2);
    	station2_2.setClosestStations(2,testStations2);
    	station3_2.setClosestStations(2,testStations2);
    
    	// initializing test dates
    	Date date1_2 = new GregorianCalendar(2013, 10, 30,13,0,0).getTime();
    	Date date2_2 = new GregorianCalendar(2013, 10, 30,13,20,0).getTime();		
    	Date date3_2 = new GregorianCalendar(2013, 10, 30,13,0,2).getTime();	
    	Date date4_2 = new GregorianCalendar(2013, 10, 30,13,10,0).getTime();	
  		Date date5_2 = new GregorianCalendar(2013, 10, 30,12,0,5).getTime();
  		Date date6_2 = new GregorianCalendar(2013, 10, 30,13,0,1).getTime();
  		
    	
  		// initializing test trips
  		Trip trip1_2 = new Trip (Reason.RENT, date5_2, station2_2, date6_2, station1_2); //inverse direction
    	//testTrips2.add(trip1_2);
    	Trip trip2_2 = new Trip(Reason.RENT, date1_2, station3_2, date2_2, station2_2); // 2000s
    	testTrips2.add(trip2_2);
    	Trip trip3_2 = new Trip (Reason.RENT, date3_2, station3_2, date4_2, station2_2); // later shorter 1000s  
    	testTrips2.add(trip3_2);
    	
    	baseScenario2= new Scenario (testStations2, testTrips2,1);
    	
    	//print of Trip validation before runTrips()
    	baseScenarioTripList = baseScenario2.tripList;
    	for (int i = 0; i < baseScenarioTripList.size(); i++) {
    		System.out.println(baseScenarioTripList.get(i).isValid());
    	}
    	
    	baseScenario2.runTrips();
    	
    	//print of Stations after Simulation including last States
    	baseScenarioStationList = baseScenario2.stationList;
    	for (int i = 0; i < baseScenarioStationList.size(); i++) {
    		System.out.println(baseScenarioStationList.get(i));
    	}
    	
    	//print of Trip validation after simulation
    	for (int i = 0; i < baseScenarioTripList.size(); i++) {
    		System.out.println(baseScenarioTripList.get(i).isValid());
    	}
    	System.out.println();
    	
// Third Test Block
    	
    	System.out.println("Third Test");
    	
    	ArrayList <Trip> testTrips3 = new ArrayList <Trip> ();
    	ArrayList <Station> testStations3 = new ArrayList <Station> ();
    	
    	//creation of test stations + states
    	Station station1_3 = new Station(901,"00901 - ALLEE DU BELVEDERE","ALLEE DU BELVEDERE PARIS 19 - 0 75000 Paris - 75000 PARIS", 20,2.391225227186182,48.892795924112306);
    	State state1_3 = new State(2,18, "20131030125959");
    	station1_3.setIsOpen(true);
    	station1_3.setPrimaryState(state1_3);
    	testStations3.add(station1_3);
    	    
    	Station station2_3 = new Station( 903, "00903 - QUAI MAURIAC  / PONT DE BERCY", "FETE DE L\u0027OH (BERCY) - QUAI MAURIAC ANG PONT DE BERCY", 20, 2.374340554605615, 48.83713368945151);
    	State state2_3 = new State(18,2,"20131030125959");
    	station2_3.setIsOpen(true);
    	station2_3.setPrimaryState(state2_3);
    	testStations3.add(station2_3);
    	
    	Station station3_3 = new Station( 904, "00904 - PLACE JOFFRE / ECOLE MILITAIRE", "ECOLE MILITAIRE-AVENUE DE LA MOTTE PICQUET - 75007 PARIS", 30, 2.301961227213259, 48.85213620522547);
    	State state3_3 = new State(0,30,"20131030125959");
    	station3_3.setIsOpen(true);
    	station3_3.setPrimaryState(state3_3);
    	testStations3.add(station3_3);
    	
    	station1_3.setClosestStations(2,testStations3);
    	station2_3.setClosestStations(2,testStations3);
    	station3_3.setClosestStations(2,testStations3);
    
    	// initializing test dates
    	Date date1_3 = new GregorianCalendar(2013, 10, 30,13,0,0).getTime();
    	Date date2_3 = new GregorianCalendar(2013, 10, 30,13,20,0).getTime();		
    	Date date3_3 = new GregorianCalendar(2013, 10, 30,13,0,2).getTime();	
    	Date date4_3 = new GregorianCalendar(2013, 10, 30,13,10,0).getTime();	
  		Date date5_3 = new GregorianCalendar(2013, 10, 30,12,0,5).getTime();
  		Date date6_3 = new GregorianCalendar(2013, 10, 30,13,0,1).getTime();
  		
    	
  		// initializing test trips
  		Trip trip1_3 = new Trip (Reason.RENT, date5_3, station2_3, date6_3, station1_3); //inverse direction
    	testTrips3.add(trip1_3);
    	Trip trip2_3 = new Trip(Reason.RENT, date1_3, station3_3, date2_3, station2_3); // 2000s
    	testTrips3.add(trip2_3);
    	Trip trip3_3 = new Trip (Reason.RENT, date3_3, station3_3, date4_3, station2_3); // later shorter 1000s  
    	testTrips3.add(trip3_3);
    	
    	baseScenario3= new Scenario (testStations3, testTrips3,0);
    	
    	//print of Trip validation before runTrips()
    	baseScenarioTripList = baseScenario3.tripList;
    	for (int i = 0; i < baseScenarioTripList.size(); i++) {
    		System.out.println(baseScenarioTripList.get(i).isValid());
    	}
    	
    	baseScenario3.runTrips();
    	
    	//print of Stations after Simulation including last States
    	baseScenarioStationList = baseScenario3.stationList;
    	for (int i = 0; i < baseScenarioStationList.size(); i++) {
    		System.out.println(baseScenarioStationList.get(i));
    	}
    	
    	//print of Trip validation after simulation
    	for (int i = 0; i < baseScenarioTripList.size(); i++) {
    		System.out.println(baseScenarioTripList.get(i).isValid());
    	}
    	
    	System.out.println();
    	
// Fourth Test Block
    	
    	System.out.println("Fourth Test");
    	
    	ArrayList <Trip> testTrips4 = new ArrayList <Trip> ();
    	ArrayList <Station> testStations4 = new ArrayList <Station> ();
    	
    	//creation of test stations + states
    	Station station1_4 = new Station(901,"00901 - ALLEE DU BELVEDERE","ALLEE DU BELVEDERE PARIS 19 - 0 75000 Paris - 75000 PARIS", 20,2.391225227186182,48.892795924112306);
    	State state1_4 = new State(2,18, "20131030125959");
    	station1_4.setIsOpen(true);
    	station1_4.setPrimaryState(state1_4);
    	testStations4.add(station1_4);
    	    
    	Station station2_4 = new Station( 903, "00903 - QUAI MAURIAC  / PONT DE BERCY", "FETE DE L\u0027OH (BERCY) - QUAI MAURIAC ANG PONT DE BERCY", 20, 2.374340554605615, 48.83713368945151);
    	State state2_4 = new State(18,2,"20131030125959");
    	station2_4.setIsOpen(true);
    	station2_4.setPrimaryState(state2_4);
    	testStations4.add(station2_4);
    	
    	Station station3_4 = new Station( 904, "00904 - PLACE JOFFRE / ECOLE MILITAIRE", "ECOLE MILITAIRE-AVENUE DE LA MOTTE PICQUET - 75007 PARIS", 30, 2.301961227213259, 48.85213620522547);
    	State state3_4 = new State(0,30,"20131030125959");
    	station3_4.setIsOpen(true);
    	station3_4.setPrimaryState(state3_4);
    	testStations4.add(station3_4);
    	
    	station1_4.setClosestStations(2,testStations4);
    	station2_4.setClosestStations(2,testStations4);
    	station3_4.setClosestStations(2,testStations4);
    
    	// initializing test dates
    	Date date1_4 = new GregorianCalendar(2013, 10, 30,13,0,0).getTime();
    	Date date2_4 = new GregorianCalendar(2013, 10, 30,13,20,0).getTime();		
    	Date date3_4 = new GregorianCalendar(2013, 10, 30,13,0,2).getTime();	
    	Date date4_4 = new GregorianCalendar(2013, 10, 30,13,10,0).getTime();	
  		Date date5_4 = new GregorianCalendar(2013, 10, 30,12,0,3).getTime();
  		Date date6_4 = new GregorianCalendar(2013, 10, 30,12,0,50).getTime();
  		
    	
  		// initializing test trips
  		Trip trip1_4 = new Trip (Reason.RENT, date5_4, station2_4, date6_4, station1_4); //inverse direction
    	testTrips4.add(trip1_4);
    	Trip trip2_4 = new Trip (Reason.RENT, date1_4, station3_4, date2_4, station2_4); // 2000s
    	testTrips4.add(trip2_4);
    	Trip trip3_4 = new Trip (Reason.RENT, date3_4, station3_4, date4_4, station2_4); // later shorter 1000s  
    	testTrips4.add(trip3_4);
    	
    	
    	baseScenario4= new Scenario (testStations4, testTrips4,1);
    	
    	//print of Trip validation before runTrips()
    	baseScenarioTripList = baseScenario4.tripList;
    	for (int i = 0; i < baseScenarioTripList.size(); i++) {
    		System.out.println(baseScenarioTripList.get(i).isValid());
    	}
    	
    	baseScenario4.runTrips();
    	
    	//print of Stations after Simulation including last States
    	baseScenarioStationList = baseScenario4.stationList;
    	for (int i = 0; i < baseScenarioStationList.size(); i++) {
    		System.out.println(baseScenarioStationList.get(i));
    	}
    	
    	//print of Trip validation after simulation
    	for (int i = 0; i < baseScenarioTripList.size(); i++) {
    		System.out.println(baseScenarioTripList.get(i).isValid());
    	}
    	
    	System.out.println();
    	
    }

}
