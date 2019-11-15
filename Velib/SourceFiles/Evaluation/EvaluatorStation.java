package Evaluation;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Data.State;
import Data.Station;

public final class EvaluatorStation {
	private EvaluatorStation()
	{
		minimalCriticalTime = 30*60*1000; //30 minutes 
	}
	
	private static int minimalCriticalTime;
	
	public static void setMinimalCriticalTime(int n)
	{
		minimalCriticalTime = n;
	}
		
	public static boolean isCritical(Station station)
	{
	//	System.out.println("entering isCritical");

		int criticalTime = 0;

		if(station.getNumberOfStates() == 0)
		{
			System.out.println("empty states!!!!!!!!!!!!!!!!!!!!!!");
		}
		if(!station.isOpen())
		{
			return false;
		}
	//	System.out.println("number of states::" + station.getNumberOfStates());
		for	(int j = 0; j < station.getNumberOfStates() - 1; j++)
		{
	//		System.out.println("state::" + j);

			State currentState = station.getState(j);
			State nextState = station.getState(j + 1);
			
			if (currentState.isFull() || currentState.isEmpty()) 
			{			
				long durationState = nextState.getDate().getTime() - currentState.getDate().getTime() ;	
				criticalTime += (int)durationState;
			}
					
		}			
		
		if(criticalTime >= minimalCriticalTime)
		{

				return true;
		}
		return false;
	}
	
	public static boolean isCritical(Station station,Date date)
	{
		for(int i = 0; i < station.getNumberOfStates() - 1; i++)
		{
			State currentState = station.getState(i);
			State nextState = station.getState(i + 1);
			
			if(currentState.getDate().getTime() <= date.getTime() &&  nextState.getDate().getTime() > date.getTime() ) 
			{
				if(currentState.getNBikes() == 0 || currentState.getNStands() == 0 )
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		
		if(station.getLatestState().getDate().getTime()   <= date.getTime() )
		{
			if(station.getLatestState().getNBikes() == 0 || station.getLatestState().getNStands() == 0 )
			{
				return true;
			}
			else
			{
				return false;
			}
		}		
		
		System.out.println("INVALID DATE AT isEmptyOrFull!!!!!!!!!!!" + date);
		return false;
	}

	public static boolean isEmpty(Station station,Date date)
	{
		for(int i = 0; i < station.getNumberOfStates() - 1; i++)
		{
			State currentState = station.getState(i);
			State nextState = station.getState(i + 1);
			
			if(currentState.getDate().getTime() <= date.getTime() &&  nextState.getDate().getTime() > date.getTime() ) 
			{
				if(currentState.getNBikes() == 0  )
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		
		if(station.getLatestState().getDate().getTime()   <= date.getTime() )
		{
			if(station.getLatestState().getNBikes() == 0 )
			{
				return true;
			}
			else
			{
				return false;
			}
		}		
		
		System.out.println("INVALID DATE AT isEmptyOrFull!!!!!!!!!!!" + date);
		return false;
	}
	
	public static boolean isFull(Station station,Date date)
	{
		for(int i = 0; i < station.getNumberOfStates() - 1; i++)
		{
			State currentState = station.getState(i);
			State nextState = station.getState(i + 1);
			
			if(currentState.getDate().getTime() <= date.getTime() &&  nextState.getDate().getTime() > date.getTime() ) 
			{
				if( currentState.getNStands() == 0 )
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		
		if(station.getLatestState().getDate().getTime()   <= date.getTime() )
		{
			if(station.getLatestState().getNStands() == 0 )
			{
				return true;
			}
			else
			{
				return false;
			}
		}		
		
		System.out.println("INVALID DATE AT isEmptyOrFull!!!!!!!!!!!" + date);
		return false;
	}
	
	public static void exportCSVStationStates(Station station,String fileName) throws FileNotFoundException
	{
	
		ArrayList<State> stateList = station.getStateList();

		PrintWriter out = new PrintWriter( fileName + station.getIdentity() + ".csv")  ;
		out.println("Time, Bikes");
		for(int i = 0; i < stateList.size(); i++)
		{    		     	 
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");    //SimpleDateFormat("MM-dd-yyyy HH:mm:ss");     
		    String reportDate = df.format(stateList.get(i).getDate());
		   	out.println(reportDate + "," + stateList.get(i).getNBikes());
		}
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");    //SimpleDateFormat("MM-dd-yyyy HH:mm:ss");     
	    String reportDate = df.format(stateList.get(stateList.size() - 1).getDate());
	   	out.println(reportDate + " 23:59"+ "," + stateList.get(stateList.size() - 1).getNBikes());
		
		out.close();
	}
	
	 public static void main(String args[]) throws IOException {  

	   }
	
	
	
	
}

		
