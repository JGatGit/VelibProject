package Evaluation;
import org.jfree.chart.ChartPanel;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame; 
import org.jfree.chart.ui.UIUtils;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class GraphCancelledTrips extends ApplicationFrame {

	private ArrayList<Double> cancelledTrips;
	private int step;
   public GraphCancelledTrips( int step,String Xtitle,ArrayList<Double> cancelledTrips) {
      super("Cancelled trips");
      this.step = step;
      this.cancelledTrips = cancelledTrips;
      JFreeChart lineChart = ChartFactory.createLineChart(
         "",
         Xtitle,"% Cancelled trips",
         createDataset(),
         PlotOrientation.VERTICAL,
         true,true,false);
	  System.out.println(cancelledTrips.get(2));

      ChartPanel chartPanel = new ChartPanel( lineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      setContentPane( chartPanel );
   }

   private DefaultCategoryDataset createDataset( ) {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
      for(int i = 0; i < cancelledTrips.size(); i+=1)
      {
    	  System.out.println("i:" + i + ":::" + cancelledTrips.get(i));
    	  	int x = i*step;
    	  	dataset.addValue((int)100*cancelledTrips.get(i), "",String.valueOf(i*step));
      }

      return dataset;
   }
   
   public void showWindow()
   {
	   this.pack();
	   UIUtils.centerFrameOnScreen( this );
	   this.setVisible( true );
   }
   public static void main( String[ ] args ) {
	   ArrayList<Double> cancelledTrips = new ArrayList<Double>();
	   
	   for(int i = 0; i < 10; i++)
	   {

		   cancelledTrips.add(0.5*i);
	   }
	   
      GraphCancelledTrips chart = new GraphCancelledTrips(10,"Days",cancelledTrips);

      chart.pack( );
      UIUtils.centerFrameOnScreen( chart );
      chart.setVisible( true );
   }
   
}