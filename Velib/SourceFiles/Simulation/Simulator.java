package Simulation;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Evaluation.*;

import Data.*;

public class Simulator {
	private ArrayList<Trip> base_trips;
	private ArrayList<Station> stations;
	private Scenario scenario;
	private EvaluatorScenario evaluatorScenario;
	private ArrayList<Double> cancelledTrips;
	private boolean regulation ;
	private double collaborationRate ;
	private double popularityGrowth ;
	
	
	public Simulator(ArrayList<Trip> base_trips, ArrayList<Station> stations,boolean regulation,double collaborationRate, double popularityGrowth)
	{
		this.regulation = regulation;
		this.collaborationRate = collaborationRate;
		this.popularityGrowth = popularityGrowth;
		this.base_trips = base_trips;
		this.stations = stations;
		cancelledTrips = new ArrayList<Double>();
	}
	
	public void simulate() throws FileNotFoundException
	{
		TripGenerator tripGenerator = new TripGenerator(this.base_trips,regulation,popularityGrowth);	
		ArrayList<Trip> simulationTrips = tripGenerator.getTrips();
		
		scenario = new Scenario(stations,simulationTrips,collaborationRate);

		scenario.runTrips();
	
		evaluatorScenario = new EvaluatorScenario(scenario);

		evaluatorScenario.exportCSVCriticalStationsNames("output/criticalStations.csv");
		//evaluatorScenario.exportCSVCriticalStationsNames("criticalStations.csv");

		//System.out.println("critical stations names exported to output/criticalStations.csv");

		this.cancelledTrips.add(evaluatorScenario.getCancelledTrips());
		
	}
	
 	public void exportStationStates(int identity) throws FileNotFoundException
	{
		EvaluatorScenario evaluatorScenario = new EvaluatorScenario(scenario);
		
		evaluatorScenario.exportCSVStationStates(identity, "output/states_");
		//evaluatorScenario.exportCSVStationStates(identity, "states_");
	}

	public void exportStationStates(String stationName) throws FileNotFoundException
	{
		EvaluatorScenario evaluatorScenario = new EvaluatorScenario(scenario);
		
		evaluatorScenario.exportCSVStationStates(stationName, "output/states_");
		//evaluatorScenario.exportCSVStationStates(stationName, "states_");
	}
	
	public void visualizeStationStates(int identity)
	{
		evaluatorScenario.visualizeStationStates(identity);
	}
	
	public void visualizeCriticalStationsVariation()
	{
		evaluatorScenario.visualizeCriticalStationsVariation(15*60*1000, 0);
	}
	
	public void visualizeCancelledTrips10days()
	{
		simulate10days();

		GraphCancelledTrips graph = new GraphCancelledTrips(1,"Days",this.cancelledTrips);
		graph.showWindow();
	}
	
	public void visualizeImpactGrowth()
	{
		TripGenerator tripGenerator;
		ArrayList<Trip> simulationTrips;
		Scenario simulationScenario;
		ArrayList<Double> cancelledTrips = new ArrayList<Double>();
		for(int i = 0; i < 5; i++)
		{
			tripGenerator = new TripGenerator(this.base_trips,regulation,i*0.5);	
			simulationTrips = tripGenerator.getTrips();
			simulationScenario = new Scenario(stations,simulationTrips,collaborationRate);
			simulationScenario.runTrips();
			EvaluatorScenario evaluatorScenario1 = new EvaluatorScenario(simulationScenario);
			cancelledTrips.add(evaluatorScenario1.getCancelledTrips());

		}
		GraphCancelledTrips graph = new GraphCancelledTrips(50,"Growth Rate (%)",cancelledTrips);
		graph.showWindow();

	}
	
	public void visualizeImpactCollaboration()
	{
		TripGenerator tripGenerator;
		ArrayList<Trip> simulationTrips;
		Scenario simulationScenario;
		ArrayList<Double> cancelledTrips = new ArrayList<Double>();
		for(int i = 0; i < 10; i++)
		{
			tripGenerator = new TripGenerator(this.base_trips,regulation,popularityGrowth);	
			simulationTrips = tripGenerator.getTrips();
			simulationScenario = new Scenario(stations,simulationTrips,0.1*i);
			simulationScenario.runTrips();
			EvaluatorScenario evaluatorScenario1 = new EvaluatorScenario(simulationScenario);
			cancelledTrips.add(evaluatorScenario1.getCancelledTrips());

		}
		GraphCancelledTrips graph = new GraphCancelledTrips(10,"Collaboration Rate (%)",cancelledTrips);
		graph.showWindow();
	}
	
	private void simulate10days()
	{

		TripGenerator tripGenerator1 = new TripGenerator(this.base_trips,regulation,popularityGrowth);	
		ArrayList<Trip> simulationTrips1 = tripGenerator1.getTrips();

		Scenario scenario1 = new Scenario(stations,simulationTrips1,collaborationRate);

		EvaluatorScenario evaluatorScenario1 = new EvaluatorScenario(scenario1);
		
		for(int i = 0; i < 10; i++)
		{
			scenario1.startNewDay();
			scenario1.runTrips();
			cancelledTrips.add(evaluatorScenario1.getCancelledTrips());
		}
		
		
	}
	
}
