package Simulation;
import Data.*;


import java.util.ArrayList;
import java.util.Date;


public class TripGenerator {
	private ArrayList<Trip> base_trips;
	private ArrayList<Trip> simulation_trips;
	private boolean regulation;
	private double popularity_growth;
	
	public TripGenerator(ArrayList<Trip> base_trips, boolean regulation, double popularity_growth)
	{
		this.base_trips = base_trips;
		this.regulation = regulation;
		this.popularity_growth = popularity_growth;
		this.simulation_trips = new ArrayList<Trip>(base_trips);
		this.createTrips();
	}
	
	public void setRegulation(boolean regulation)
	{
		this.regulation = regulation;
	}
	
	public void setPopularity(float popularity_growth )
	{
		this.popularity_growth = popularity_growth;		
	}

	public void createTrips()
	{
		this.simulation_trips.clear();
		for(int i = 0; i < base_trips.size(); i++)
		{

			switch(base_trips.get(i).getReason()) {
				
				case REGULATION:
					if(regulation)
						simulation_trips.add(base_trips.get(i));
					break;
				case RENT:
					simulation_trips.add(base_trips.get(i));
					double aux = popularity_growth;
					while(aux > 1)
					{
						simulation_trips.add(new Trip(base_trips.get(i)));
						aux-=1;
					}
					if(Math.random() < popularity_growth)
					{
						simulation_trips.add(new Trip(base_trips.get(i))); //adds a copy of the trip 
					}
					break;
				case MAINTENANCE:
					simulation_trips.add(base_trips.get(i));
					break;
				default:
					System.out.println("Reason not initialized, ERROR!!!!!!! \n");
			}
		}

	}
	
	public ArrayList<Trip> getTrips()
	{
		return simulation_trips;
	}
	
	public static void main(String[] args) 
	{
	      Station station1 = new Station(901,"00901 - ALLEE DU BELVEDERE","ALLEE DU BELVEDERE PARIS 19 - 0 75000 Paris - 75000 PARIS",20,2.391225227186182,48.892795924112306);
	      State state1 = new State(6,14,"20131030125959");
	      station1.setIsOpen(true);
	      station1.setPrimaryState(state1);
	      
	      //Station de retourne
	      Station station2 = new Station(903,"00903 - QUAI MAURIAC  / PONT DE BERCY","FETE DE L\u0027OH (BERCY) - QUAI MAURIAC ANG PONT DE BERCY",20,2.374340554605615,48.83713368945151);
	      State state2 = new State(15,5,"20131030125959");
	      station2.setIsOpen(true);
	      station2.setPrimaryState(state2);
	      
	      Date date1 = new Date();
	      Date date2 = new Date();
	      //Créeation d'un nouveau trip
	      Trip trp = new Trip(Reason.RENT,date1,station1,date2,station2);

	      
	      //Créeation d'un nouveau trip
	      Trip trp2 = new Trip(1,"20131031000002",station1,"20131031000907",station2);
	  //    System.out.println(trp2);
	      
	      Trip trp3 = new Trip(3,"20131031000002",station1,"20131031000908",station2);
	      
	    //  System.out.println(trp3);
	      
	      ArrayList<Trip> tripList = new ArrayList<Trip> ();
	      tripList.add(trp);
	      tripList.add(trp2);
	      tripList.add(trp3);
	      tripList.add(trp);
	      tripList.add(trp2);
	      tripList.add(trp3);
	      System.out.println("#############base trips :      ");

	      for(int i = 0; i < tripList.size(); i++)
	      {

	    	  	System.out.println(tripList.get(i));
	      }
	      System.out.println("#############new trips (regulation = true, popularity_growth = 0) :      ");
	      
	      TripGenerator my_TripGenerator = new TripGenerator(tripList,true,0);     
	      my_TripGenerator.createTrips();
	      ArrayList<Trip> generated_trips = my_TripGenerator.getTrips();
	      for(int i = 0; i < generated_trips.size(); i++)
	      {
	    	  	System.out.println(generated_trips.get(i));
	      }
	      
	      System.out.println("#############new trips (regulation = false, popularity_growth = 0.3) :      ");
	      
	      my_TripGenerator.setRegulation(false);
	      my_TripGenerator.setPopularity((float)0.6);
	      my_TripGenerator.createTrips();

	      generated_trips = my_TripGenerator.getTrips();

	      for(int i = 0; i < generated_trips.size(); i++)
	      {

	    	  	System.out.println(generated_trips.get(i));
	      }
	      System.out.println("############################      \n");
	      


	}

}
