package Data;

import java.util.Comparator;

public class StationDistance {
  private int distance;
  private int identity;
  
  public StationDistance(int distance, int identity) {
    this.distance = distance;
    this.identity = identity;
  }
  
  //cf. https://beginnersbook.com/2013/12/java-arraylist-of-object-sort-example-comparable-and-comparator/
  //De manière descendante : commence par la plus loin, finit pas la plus proche
  public static Comparator<StationDistance> DisComparator = new Comparator<StationDistance> (){
    public int compare(StationDistance sd1, StationDistance sd2) {
      return sd2.getDistance() - sd1.getDistance();
    }
  };
  
  public int getDistance() {
    return distance;
  }
  
  public int getIdentity() {
    return identity;
  }
}
