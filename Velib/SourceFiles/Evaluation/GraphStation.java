package Evaluation;

import Data.*;

import Simulation.Scenario;

import java.util.ArrayList;

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

public class GraphStation extends ApplicationFrame {
	private Station station;
   public GraphStation(  Station station ) {

      super( "grafico" );   
      this.station = station;
      final XYDataset dataset = createDataset( );        
      final JFreeChart chart = createChart( dataset );       
      final ChartPanel chartPanel = new ChartPanel( chart ); 
      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 370 ) );  
      chartPanel.setMouseZoomable( true , false );         
      setContentPane( chartPanel );

   }

   private XYDataset createDataset( ) {
      final TimeSeries series = new TimeSeries( "Velib Graph" );         
   //   Second current = new Second( );   
      double value = 0;         
	   ArrayList<State> stateList = this.station.getStateList();
      for (int i = 0; i < stateList.size(); i++) {
         try {
        	 	Second current = new Second(stateList.get(i).getDate());
            value = stateList.get(i).getNBikes();              
            series.add(current, new Double( value ) );   
         //   System.out.println(stateList.get(i));
            current = ( Second ) current.next( ); 
         } catch ( SeriesException e ) {
    //        System.err.println("Error adding to series");
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
         "Station:" + station.getName(), 
         "Time",              
         "Number of bikes",              
         dataset,             
         false,              
         false,              
         false);
   }

   
   public static void main( final String[ ] args ) {

   }
}   