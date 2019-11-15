package Data;


public class StationStatic {
  /* Attributs statiques */
  protected int identity;
  protected int capacity;
  protected double longitude;
  protected double latitude;
  protected String name;
  protected String address;
  
  /* Constructeurs */
  /**
   * Constructeur qui prends des informations essentiels 
   * @param identity
   * @param capacity
   */
  public StationStatic(int identity, int capacity) {
    this.identity = identity;
    this.capacity = capacity;
  }
  
  /**
   * Constructeur qui prend à la fois des informations essentiels et supplémentraires 
   * @param identity
   * @param capacity
   * @param name
   * @param address
   * @param longitude
   * @param latitude
   */
  public StationStatic(int identity, String name, String address, int capacity, double longitude, double latitude) {
    this.identity = identity;
    this.capacity = capacity;
    this.name = name;
    this.address = address;
    this.longitude = longitude;
    this.latitude = latitude;

  }
  
  /**
   * Constructeur pour faire une copie
   **/
  public StationStatic(StationStatic stationStatic)
  {
      this(stationStatic.getIdentity(),stationStatic.getName(),
          stationStatic.getAddress(),stationStatic.getCapacity(),
          stationStatic.getLongitude(),stationStatic.getLatitude());
  }
  
  /* Getters */
  public int getIdentity() {
    return identity;
  }
  public int getCapacity() {
    return capacity;
  }
  public double getLongitude() {
    return longitude;
  }
  public double getLatitude() {
    return latitude;
  }
  public String getName() {
    return name;
  }
  public String getAddress() {
    return address;
  }
  
  public String toString() {
    String info = "id: "+getIdentity()+"\tnm: "+getName()+"\tad: "+getAddress();
    info = info + "\ncpct: "+getCapacity()+"\tlog: "+getLongitude()+"\tlat: "+getLatitude();
    return info;      
  }
  
  public static void main (String [] args) {
    // Création d'une station statique
    StationStatic station1 = new StationStatic(902,"00901 - ALLEE DU BELVEDERE","ALLEE DU BELVEDERE PARIS 19 - 0 75000 Paris - 75000 PARIS",
        20,2.391225227186182,48.892795924112306);
    System.out.println("Création de la 1e station");
    System.out.println(station1);
  } 

}
