//package IHM;

//import com.modeliosoft.modelio.javadesigner.annotations.objid;

//@objid ("13ef917a-3abc-45ed-bde2-178f34720905")
//public class Read {
//    @objid ("58175a03-1e5d-418d-961c-1e04642a1e43")
//    public void loadTrips() {
//    }

//    @objid ("9e38d89f-aca5-49e2-9182-8f1ecba2a130")
//    public void loadStations() {
//    }

//    @objid ("73907320-92d8-485f-bc54-280a64d2791a")
//    public void loadState() {
//  }
//}

//


package IHM;

import Data.*;


import java.io.*;
import java.util.ArrayList;
import java.util.Date;


public class Read 
{
	/* Class Read:
	 * Attributes: 
	 * 2 Strings representing the path of address file and trips file.
	 * Format: "src/files/file.txt"
	 * 
	 * Methods: 
	 * readTrips: REads trips file and converts the text in data.
	 * 
	 * */
	
	
	public static Station findStationWithId(int id, ArrayList<Station> stationList)
	{
		for(Station stat : stationList)
		{
			if(stat.getIdentity() == id)
			{
				return stat;
			}
		}
		return null;
	}
	
	
	
	public ArrayList<Station> createStationList(String stationsAddressesFileName)
	{
		File stationsFile = new File(stationsAddressesFileName);
		
		String line = null;
		
		//List to be returned
		ArrayList<Station> stationList = new ArrayList<Station>();
		
		
		try 
		{
			FileReader fr = new FileReader(stationsFile);
		
			BufferedReader buffer = new BufferedReader(fr);
			
			
			//Starting from index 140 to ignore some thrash at the beginning of the .txt
			int ind = 140;
			
			while((line = buffer.readLine())!= null)
			{				
				while(true)
				{
					String nb = line.substring(line.indexOf("\"nb\"", ind)+5, 
							line.indexOf(",", line.indexOf("\"nb\"", ind)+5));
					int identity = Integer.parseInt(nb);
			//		System.out.println(identity);
					
					String name = line.substring(line.indexOf("\"lb\"", ind)+5, 
							line.indexOf(",", line.indexOf("\"lb\"",ind)+5));
			//		System.out.println(name);
					
					String address = line.substring(line.indexOf("\"add\"", ind)+6, 
							line.indexOf(",", line.indexOf("\"add\"",ind)+6));
			//		System.out.println(address);
					
					String cap = line.substring(line.indexOf("\"totbs\"", ind)+8, 
							line.indexOf(",", line.indexOf("\"totbs\"",ind)+8));
					int capacity = Integer.parseInt(cap);
			//		System.out.println(capacity);
					
					String lng = line.substring(line.indexOf("\"lng\"", ind)+6, 
							line.indexOf(",", line.indexOf("\"lng\"",ind)+6));
					double longitude = Double.parseDouble(lng);
			//		System.out.println(longitude);
					
					String lat = line.substring(line.indexOf("\"lat\"", ind)+6, 
							line.indexOf(",", line.indexOf("\"lat\"",ind)+6));
					double latitude = Double.parseDouble(lat);
			//		System.out.println(latitude);
					
					Station station = new Station(identity, name, address,
							capacity, longitude, latitude);
					
					stationList.add(station);
					
					//Update the index to the latitude's (the last info of each station) index + 60
					ind = line.indexOf("\"lat\"", ind)+60; 
					
					//Stop condition: if it's the last station in the list
					//indexOf("nb", ind) won't find anything and will return -1 
					if(line.indexOf("\"nb\"", ind) < 0)
						break;
				}
			}
			
			buffer.close();
			
		}
		catch(FileNotFoundException error) 
		{
			System.out.println("File not found");
		}
		catch(IOException error) 
		{
			System.out.println("Something went wrong");
		}
		
		return stationList;
	}
	
	
	public void defineInitalStates(String statesFileName, ArrayList<Station> stationList)
	{
		File statesFile = new File(statesFileName);
		
		System.out.println(statesFileName);
		
		String line = null;
		
		try 
		{
			FileReader fr = new FileReader(statesFile);
		
			BufferedReader buffer = new BufferedReader(fr);
			
			int ind = 33;
			
			while((line = buffer.readLine())!= null)
			{	
				while(true)
				{
					String nb = line.substring(line.indexOf("\"nb\"", ind)+5, 
							line.indexOf(",", line.indexOf("\"nb\"", ind)+5));
					int identity = Integer.parseInt(nb);
				//	System.out.println(identity);
					
					String stt = line.substring(line.indexOf("\"state\"", ind)+8, 
							line.indexOf(",", line.indexOf("\"state\"",ind)+8));
					boolean isOpen = (stt.equals("\"open\"")) ? true : false;
				//	System.out.println(isOpen);
					
					String freebk = line.substring(line.indexOf("\"freebk\"", ind)+9, 
							line.indexOf(",", line.indexOf("\"freebk\"",ind)+9));
					int freeBikes = Integer.parseInt(freebk);
				//	System.out.println(freeBikes);
					
					String freebs = line.substring(line.indexOf("\"freebs\"", ind)+9, 
							line.indexOf("}", line.indexOf("\"freebs\"",ind)+9));
					int freeStands = Integer.parseInt(freebs);
				//	System.out.println(freeStands);
					
					State state1 = new State(freeBikes, freeStands, "20131031000001");
					
					//How to avoid repeating the search?
					Station dummyStation = findStationWithId(identity, stationList);
					
					dummyStation.setIsOpen(isOpen);
					dummyStation.setPrimaryState(state1);
					dummyStation.setClosestStations(5,stationList);
					
					//Update the index to the latitude's (the last info of each station) index + 60
					ind = line.indexOf("\"freebs\"", ind)+10; 
					
					//Stop condition: if it's the last station in the list
					//indexOf("nb", ind) won't find anything and will return -1 
					if(line.indexOf("\"nb\"", ind) < 0)
						break;
					
					
					
					
					
				}
			}
			
			buffer.close();
		

		}
		catch(FileNotFoundException error) 
		{
			System.out.println("File not found");
		}
		catch(IOException error) 
		{
			System.out.println("Something went wrong");
		}
	}
	
	
	public ArrayList<Trip> createTripsList(String tripsFileName, ArrayList<Station> stationList)
	{	
		File tripsFile = new File(tripsFileName);
		
		String line = null;
		
		//List to be returned
		ArrayList<Trip> tripList = new ArrayList<Trip>();
		
		try 
		{
			FileReader fr = new FileReader(tripsFile);
		
			BufferedReader buffer = new BufferedReader(fr);
			
			while((line = buffer.readLine())!= null)
			{	
				//Skip first line
				if(line.startsWith("reason"))
					continue;
				else
				{
					String[] split = line.split("\t");	
					// "\t" or "\\t", not entirely sure
					//Warning: sometimes the items are separated by " ", not "\t"
				
					int reason = Integer.parseInt(split[0]);
					String startTime = split[1];
					int startStationId = Integer.parseInt(split[2]); 
					String endTime = split[4];
					int endStationId = Integer.parseInt(split[5]); 
					
					
					//Corrigir depois pra implementar trips com station 0
					if(startStationId < 900)
					{	
						Trip trip = new Trip(reason, startTime, null, 
								endTime, findStationWithId(endStationId, stationList));
					
						tripList.add(trip);
						
						continue;
					}
					
					else if (endStationId <900)
					{
						Trip trip = new Trip(reason, startTime, findStationWithId(endStationId, stationList), 
								endTime, null);
					
						tripList.add(trip);
						
						continue;
					}
					
					//	Leaving the stands here just for precaution
					//	int startBikeStand = Integer.parseInt(split[3]);   
					//	int endBikeStand = Integer.parseInt(split[6]);
					
				
					Trip trip = new Trip(reason, startTime, findStationWithId(startStationId, stationList), 
							endTime, findStationWithId(endStationId, stationList));
				
					tripList.add(trip);
				}
			}
			
			buffer.close();
			
		}
		catch(FileNotFoundException error) 
		{
			System.out.println("File not found");
		}
		catch(IOException error) 
		{
			System.out.println("Something went wrong");
		}
		
		return tripList;
	}
	
	
	
	
	
	
	

}
