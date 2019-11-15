package Data;

import java.util.ArrayList;
import java.util.Date;

public class StationExtendedDynamic extends StationStatic {
  /* Attributs dynamiques */
  private ArrayList <State> stateList;
  private boolean isOpen;
  
  /* Constructeurs */
  /**
   * Constructeur qui prends des informations essentiels 
   * Veuillez utiliser le setPrimaryState et setIsOpen après la création d'une station impérativement
   * @param identity
   * @param capacity
   */
  public StationExtendedDynamic(int identity, int capacity) {
    super(identity, capacity);
    stateList = new ArrayList<State> ();
    this.isOpen = true;
    // TODO Auto-generated constructor stub
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
  public StationExtendedDynamic(int identity, String name, 
      String address, int capacity, double longitude, double latitude) {
    super(identity,name,address,capacity,longitude,latitude);
    stateList = new ArrayList<State> ();
    this.isOpen = true;
  }
  
  /**
   * Constructeur pour faire une copie
   **/
  public StationExtendedDynamic(StationExtendedDynamic station)
  {
    super(station);
  }
  
  /* Opération des trips */
  /**
   * On prends qu'un vélo chaque fois
   * Cette méthode prend une instance de Date comme entrée 
   * @param date
   */
  
  public void takeBike(Date date) {
    int numberOfFreeBikesNew = stateList.get(stateList.size()-1).getNBikes() - 1;
    int numberOfFreeStandsNew = stateList.get(stateList.size()-1).getNStands() + 1;
    stateList.add(new State(numberOfFreeBikesNew,numberOfFreeStandsNew,date));
  }
  

  /**
   * On prends qu'un vélo chaque fois 
   * Cette méthode prend une indtance de Trip comme entrée
   * @param trip
   */
  public void takeBike(Trip trip) {
    int numberOfFreeBikesNew = stateList.get(stateList.size()-1).getNBikes() - 1;
    int numberOfFreeStandsNew = stateList.get(stateList.size()-1).getNStands() + 1;
    Date date = trip.getStartDate();
    stateList.add(new State(numberOfFreeBikesNew,numberOfFreeStandsNew,date));
  }
  
  /**
   * On retourne qu'un vélo chaque fois 
   * Cette méthode prend une instance de Date comme entrée
   * @param date
   */
  
  public void returnBike(Date date) {
    int numberOfFreeBikesNew = stateList.get(stateList.size()-1).getNBikes() + 1;
    int numberOfFreeStandsNew = stateList.get(stateList.size()-1).getNStands() - 1;
    stateList.add(new State(numberOfFreeBikesNew, numberOfFreeStandsNew,date));
  }
  

  /**
   * On retourne qu'un vélo chaque fois 
   * Cette méthode prend une indtance de Trip comme entrée
   * @param trip
   */
  public void returnBike(Trip trip) {
    int numberOfFreeBikesNew = stateList.get(stateList.size()-1).getNBikes() + 1;
    int numberOfFreeStandsNew = stateList.get(stateList.size()-1).getNStands() - 1;
    Date date = trip.getEndDate();
    stateList.add(new State(numberOfFreeBikesNew, numberOfFreeStandsNew,date));
  }
  
  /* Operations des states*/
  public void deleteLatestState() {
    stateList.remove(stateList.size());
  }
  
  /* Setters */
  /**
   * Setter pour l'état primaire
   * @param primaryState
   */
  public void setPrimaryState(State primaryState) {
    this.stateList.add(primaryState);
  }

  /**
   * Setter pour isOpen
   * Open-true, Closed-false
   * @param isOpen
   */
  public void setIsOpen(boolean isOpen) {
    this.isOpen = isOpen;
  }
  
  
  /* Getters */
  public ArrayList<State> getStateList(){
    return stateList;
  }
  public int getNumberOfStates() {
    return stateList.size();
  }
  public State getLatestState() {
    return stateList.get(stateList.size()-1);
  }
  public void clearStates() {
	  stateList.clear();
  }
  public State getState(int n) {
    return stateList.get(n);
  }
  
  public boolean isOpen() {
    return isOpen;
  }
  
  public String toString() {
    String info = super.toString();
    info = info + "\nLatest state: " + getLatestState().toString();
    if (isOpen()) {
      info = info + "\tOpen";
      }
    else {
      info = info + "\tClosed";
    }
    return info;      
  }
  
  public static void main (String [] args) {
    // Création de la 1e station
    Station station1 = new Station(901,"00901 - ALLEE DU BELVEDERE","ALLEE DU BELVEDERE PARIS 19 - 0 75000 Paris - 75000 PARIS",
        20,2.391225227186182,48.892795924112306);
    State state1 = new State(6,14,"20131030125959");
    station1.setIsOpen(true);
    station1.setPrimaryState(state1);
    System.out.println("Création de la 1e station");
    System.out.println(station1);
    
    // Test de takeBike() et returnBike()
    Date date1 = new Date();
    station1.takeBike(date1);
    System.out.println();
    System.out.println("After taking a bike:");
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
    System.out.println("After returning a bike:");
    System.out.println(station1);
  } 
}
