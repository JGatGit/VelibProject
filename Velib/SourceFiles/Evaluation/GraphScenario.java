package Evaluation;

import Data.*;

import Simulation.Scenario;

import java.util.ArrayList;
import java.util.Date;

import org.jfree.chart.ChartFactory; 
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.general.SeriesException; 
import org.jfree.data.time.Second; 
import org.jfree.data.time.TimeSeries; 
import org.jfree.data.time.TimeSeriesCollection; 
import org.jfree.data.xy.XYDataset; 
import org.jfree.chart.ui.ApplicationFrame; 
import org.jfree.chart.ui.UIUtils;

public class GraphScenario extends ApplicationFrame {
	private Scenario scenario;
	private int step;
	private String yAxisTitle;
   public GraphScenario(  Scenario scenario, int step, int type  ) {

      super( "Critical stations variation" );   
      this.step = step;
      this.scenario = scenario;
      final XYDataset dataset;
      switch(type) {
      	case 1: dataset = createDatasetEmpty();
      		yAxisTitle = "Empty stations";
    	  		break;
      	case 2: dataset = createDatasetFull();
      		yAxisTitle = "Full stations";
      		break;
      	default: dataset = createDataset();
      			yAxisTitle = "Critical stations";
      			break;
      }
    //  final XYDataset dataset = createDataset( );        
      final JFreeChart chart = createChart( dataset );       
      final ChartPanel chartPanel = new ChartPanel( chart ); 
      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 370 ) );  
      chartPanel.setMouseZoomable( true , false );         
      setContentPane( chartPanel );

   }

   private XYDataset createDataset( ) {
      final TimeSeries series = new TimeSeries( "Velib Graph" );         
   //   Second current = new Second( );   
      ArrayList<Station> stations = scenario.getStationList();
      Date initialDate = stations.get(0).getState(0).getDate();	
	   for(int i = 0; step*i < 24*60*60*1000; i++) {
         try {
        	 	int numberOfBadStations = 0;
        	 	Date currentDate = new Date(initialDate.getTime() + i*step);
        	 	Second current = new Second(currentDate);
        		for(int j = 0; j < stations.size() ; j++)
    			{
    				if(EvaluatorStation.isCritical(stations.get(j), currentDate))
    				{
    					numberOfBadStations += 1;
    				}
    			}             
            series.add(current, new Double( numberOfBadStations ) );                 
            current = ( Second ) current.next( ); 
         } catch ( SeriesException e ) {
            System.err.println("Error adding to series");
         }
      }

      return new TimeSeriesCollection(series);
   }     

   private XYDataset createDatasetEmpty( ) {
	      final TimeSeries series = new TimeSeries( "Velib Graph" );         
	   //   Second current = new Second( );   
	      ArrayList<Station> stations = scenario.getStationList();
	      Date initialDate = stations.get(0).getState(0).getDate();	
		   for(int i = 0; step*i < 24*60*60*1000; i++) {
	         try {
	        	 	int numberOfBadStations = 0;
	        	 	Date currentDate = new Date(initialDate.getTime() + i*step);
	        	 	Second current = new Second(currentDate);
	        		for(int j = 0; j < stations.size() ; j++)
	    			{
	    				if(EvaluatorStation.isEmpty(stations.get(j), currentDate))
	    				{
	    					numberOfBadStations += 1;
	    				}
	    			}             
	            series.add(current, new Double( numberOfBadStations ) );                 
	            current = ( Second ) current.next( ); 
	         } catch ( SeriesException e ) {
	            System.err.println("Error adding to series");
	         }
	      }

	      return new TimeSeriesCollection(series);
	   }     
   
   private XYDataset createDatasetFull( ) {
	      final TimeSeries series = new TimeSeries( "Velib Graph" );         
	   //   Second current = new Second( );   
	      ArrayList<Station> stations = scenario.getStationList();
	      Date initialDate = stations.get(0).getState(0).getDate();	
		   for(int i = 0; step*i < 24*60*60*1000; i++) {
	         try {
	        	 	int numberOfBadStations = 0;
	        	 	Date currentDate = new Date(initialDate.getTime() + i*step);
	        	 	Second current = new Second(currentDate);
	        		for(int j = 0; j < stations.size() ; j++)
	    			{
	    				if(EvaluatorStation.isFull(stations.get(j), currentDate))
	    				{
	    					numberOfBadStations += 1;
	    				}
	    			}             
	            series.add(current, new Double( numberOfBadStations ) );                 
	            current = ( Second ) current.next( ); 
	         } catch ( SeriesException e ) {
	            System.err.println("Error adding to series");
	         }
	      }

	      return new TimeSeriesCollection(series);
	   }     
   
   public void showWindow()
   {
	    this.pack( );         
	    UIUtils.positionFrameRandomly( this );         
	    this.setVisible( true );
   }

   private JFreeChart createChart( final XYDataset dataset ) {
      return ChartFactory.createTimeSeriesChart(             
         yAxisTitle, 
         "Time",              
         "Number of stations",              
         dataset,             
         false,              
         false,              
         false);
   }

   
   public static void main( final String[ ] args ) {

   }
}   