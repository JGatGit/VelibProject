package Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

//import com.modeliosoft.modelio.javadesigner.annotations.objid;

public class Station extends StationExtendedDynamic {
    /* Info supplémentaires */
    private ArrayList <Integer> closestStationIdList;
    private ArrayList <Station> closestStationList;
    // RADIUS_OF_EARTH Ressource: https://rechneronline.de/earth-radius/ 
    // using 48.83713368945151 latitude, 50 meters above sea level, 
    private static double RADIUS_OF_EARTH = 6366111; 
        
    /* Constructeurs */
    /**
     * Constructeur qui prends des informations essentiels 
     * Veuillez utiliser le setPrimaryState et setIsOpen après la création d'une station impérativement
     * @param identity
     * @param capacity
     */
    public Station(int identity, int capacity) {
      super(identity,capacity);
      closestStationIdList = new ArrayList<Integer> ();
      closestStationList = new ArrayList<Station> ();
    }
    
    /**
     * Constructeur qui prend à la fois des informations essentiels et supplémentraires 
     * Veuillez utiliser impérativement le setPrimaryState() et setIsOpen() après la création d'une station
     * @param identity
     * @param capacity
     * @param name
     * @param address
     * @param longitude
     * @param latitude
     */
    public Station(int identity, String name, String address, int capacity, double longitude, double latitude) {
      super(identity,name,address,capacity,longitude,latitude);
      closestStationIdList = new ArrayList<Integer> ();
      closestStationList = new ArrayList<Station> ();
    }
    
    /**
     * Constructeur pour faire une copie
     **/
    public Station(Station station)
    {
    		super(station.getIdentity(), station.getName(), station.getAddress(), station.getCapacity(), station.getLongitude(),station.getLatitude()  );
    		super.setPrimaryState(station.getState(0));
    		
    }
    
    
    /* Operations des stations */   
    /* Assez long ! */
    /* 1.Find the Station qui porte un certain  id*/
    /**
     * Find Station selon le id
     * @param id
     * @param stationList
     * @return
     */
    public static Station findStation(int id, ArrayList<Station> stationList) {
      int i = 0;
      while(stationList.get(i).getIdentity() != id) {
        i++;
      }
      return stationList.get(i);
      
    }
    
    /* 2.1. Get Closest Station ID */
    /**
     * Trouve et enregistre les premières n identités des stations plus proches
     * @param number de stations à constituer la liste des stations plus proches
     * @param stationList
     */
    public void setClosestStationsId(int number, ArrayList<Station> stationList) {
      ArrayList<StationDistance> sDisList =  sortStationsList(stationList);
      if (number <= sDisList.size()) {
        for (StationDistance st : sDisList) {
          if (st.getIdentity() == this.identity) {
            number = number + 1;
          }
          else {
            this.closestStationIdList.add(st.getIdentity());
          }
        }
      }
      else {
        System.out.println("Number of required station depasses the size of list of stations");
      }
    }
    
    /* 2.2. Méthode auxiliaire pour Get Closest Station ID */
    /**
     * Transformation de ArrayList<Staion> à ArrayList<StationDistance>
     * @param stationList
     * @return stationDistanceList
     */
    private ArrayList<StationDistance> sortStationsList(ArrayList<Station> stationList){
      ArrayList<StationDistance> sDisList = new ArrayList<StationDistance> ();
      for(Station st:stationList) {
        sDisList.add(new StationDistance(st.getDistanceToStationGiven(this),st.getIdentity()));
      }
      Collections.sort(sDisList, StationDistance.DisComparator);
      return sDisList;
    }
    
    
    /* 2.3.1. Méthode auxiliaire utilisé pendant finding closest stataion ID 
     * Find the distance between two stations*/
    /**
     * Sources des formules: https://andrew.hedges.name/experiments/haversine/
     * @param st1
     * @param st2
     * @return la distance entre les deux stations en question en mètre
     */
    public static int getDistanceBetweenTwoStations(Station st1, Station st2) {
      double lon1 = Math.toRadians(st1.getLongitude());
      double lon2 = Math.toRadians(st2.getLongitude());
      double lat1 = Math.toRadians(st1.getLatitude());
      double lat2 = Math.toRadians(st2.getLatitude());
      
      double dlon = lon2 - lon1;
      double dlat = lat2 - lat1;
      double a = Math.pow(Math.sin(dlat/2), 2) 
          + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2), 2);
      double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a));
      int d = (int) Math.round(RADIUS_OF_EARTH * c);
      return d;
    }
    
    /* 2.3.2. Méthode auxiliaire utilisé pendant finding closest stataion ID 
     * Find the distance between this station and an another*/
    /**
     * La distance entre this station et une autre station
     * @param st2
     * @return distance en mètre
     */
    public int getDistanceToStationGiven(Station st2) {
      double lon1 = longitude;
      double lon2 = Math.toRadians(st2.getLongitude());
      double lat1 = latitude;
      double lat2 = Math.toRadians(st2.getLatitude());
      
      double dlon = lon2 - lon1;
      double dlat = lat2 - lat1;
      double a = Math.pow(Math.sin(dlat/2), 2) 
          + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2), 2);
      double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a));
      int d = (int) Math.round(RADIUS_OF_EARTH * c);
      return d;
    }
    
    
    /* Une ancienne idée sur la finding closest stations*/
    /**
     * La méthode pour set toutes les stations dans un rayon de certaines mètres
     * @param radius en mètre
     * @param stationList
     */
    /*
    public void setClosestStations(int radius, ArrayList<Station> stationList) {
      for (int i = 0; i < stationList.size(); i++) {
        Station st = stationList.get(i);
        if (st.getIdentity() == this.getIdentity()) {
          // On ne fait rien
        }
        else if (Station.getDistanceBetweenTwoStations(this, st) <= radius) {
          closestStationsList.add(st);
        }
      }
    } 
   */
  
    /* 3. Find the Closest Station qui setter les informations des closest stations 
     * comme instances de Stations in the attributs*/
    public void setClosestStations(int n, ArrayList<Station> stationList) {
      if (closestStationIdList.size() == n) {
        for (int id : this.closestStationIdList ) {
          this.closestStationList.add(Station.findStation(id, stationList));
        }
      }
      else {
        this.setClosestStationsId(n, stationList);
        for (int id : this.closestStationIdList ) {
          this.closestStationList.add(Station.findStation(id, stationList));
        }
       } 
     }
    
    
    /* Setters */

    /* Getters */
    public ArrayList<Integer> getClosestStationIdList() {
      return closestStationIdList;
    }
    public ArrayList<Station> getClosestStationList(){
      return closestStationList;
    }
    
    public String toString() {
       return super.toString();
    }
    

    public static void main (String [] args) {
      // Création de la 1e station
      Station station1 = new Station(901,"00901 - ALLEE DU BELVEDERE","ALLEE DU BELVEDERE PARIS 19 - 0 75000 Paris - 75000 PARIS",
          20,2.391225227186182,48.892795924112306);
      State state1 = new State(6,14,"20131030125959");
      station1.setIsOpen(true);
      station1.setPrimaryState(state1);

      System.out.println("-------------Création de la 1e station---------------");
      System.out.println(station1);
      
      // Test de takeBike() et returnBike()
      Date date1 = new Date();
      station1.takeBike(date1);
      System.out.println();
      System.out.println("------------After taking a bike:-------------");
      System.out.println(station1);
      try        
      {
          Thread.sleep(1000); //après 1 seconde
      } 
      catch(InterruptedException ex) 
      {
          Thread.currentThread().interrupt();
      }
      
      Date date2 = new Date();
      System.out.println();
      station1.returnBike(date2);
      System.out.println("------------After returning a bike:-------------");
      System.out.println(station1);
      
      //Creation de la 2e station
      Station station2 = new Station(903,"00903 - QUAI MAURIAC  / PONT DE BERCY","FETE DE L\u0027OH (BERCY) - QUAI MAURIAC ANG PONT DE BERCY",
          20,2.374340554605615,48.83713368945151);
      State state2 = new State(15,5,"20131030125959");
      station2.setIsOpen(true);
      station2.setPrimaryState(state2);
      System.out.println();      
      System.out.println("-------------Création de la 2e station---------------");
      System.out.println(station2);
      
      
      //Creation de la 3e station
      Station station3 = new Station(904,"00904 - PLACE JOFFRE / ECOLE MILITAIRE","ECOLE MILITAIRE-AVENUE DE LA MOTTE PICQUET - 75007 PARIS",
          30,2.301961227213259,48.85213620522547);
      State state3 = new State(24,6,"20131030125959");
      station3.setIsOpen(true);
      station3.setPrimaryState(state3);
      System.out.println();      
      System.out.println("-------------Création de la 3e station---------------");
      System.out.println(station3);
      
      
      //Creation de la 4e station
      Station station4 = new Station(905,"00905 - GARE DE BERCY (STATION MOBILE 5)","GARE DE BERCY - ANGLE RUE CORBINEAU - 75012 PARIS",
          20,2.382472269083633,48.83966087889425);
      State state4 = new State(19,0,"20131030125959");
      station4.setIsOpen(true);
      station4.setPrimaryState(state4);
      System.out.println();
      System.out.println("-------------Création de la 4e station---------------");
      System.out.println(station4);
      
      //Creation d'une ArrayList de stations
      ArrayList<Station> stationList = new ArrayList<Station> ();
      stationList.add(station1);
      stationList.add(station2);
      stationList.add(station3);
      stationList.add(station4);
     
      System.out.println("Distance de la 1e station: 1-2, 1-3, 1-4:");
      System.out.println(Station.getDistanceBetweenTwoStations(station1, station2));
      System.out.println(Station.getDistanceBetweenTwoStations(station1, station3));
      System.out.println(Station.getDistanceBetweenTwoStations(station1, station4));
      station1.setClosestStationsId(3, stationList);
     
      System.out.println("Size of the closestStationIdList for station1:");
      System.out.println(station1.getClosestStationIdList().size());
      System.out.println(station1.getClosestStationIdList().get(0)); 
      System.out.println(station1.getClosestStationIdList().get(1));
      System.out.println(station1.getClosestStationIdList().get(2)); 
     
      System.out.println(Station.findStation(903, stationList)); 
      
      System.out.println();
      System.out.println();
      station1.setClosestStations(3, stationList);
      System.out.println(station1.getClosestStationList());
      
    }
}
