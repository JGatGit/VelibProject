package Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

//import com.modeliosoft.modelio.javadesigner.annotations.objid;

public class Trip {
    private Reason reason;
    private Date startDate;
    private Date endDate;
    public Station startStation;
    public Station endStation;
    private boolean isValid;

    /* Constructeur reason: RENT, MAINTENANCE, REGULATION*/
    /* Le trip est considéré a priori invalide, c-a-d isValide = false */
    public Trip (Reason rsn, Date startDate, Station startStation, Date endDate, 
        Station endStation) {
      this.reason = rsn;
      this.startDate = startDate;
      this.endDate = endDate;
      this.startStation = startStation;
      this.endStation = endStation;
      this.isValid = false;
    }
    
    public Trip (int rsn, String startDateString, Station startStation, 
                             String endDateString, Station endStation) {
      switch(rsn) {
        case 1 : this.reason = Reason.RENT;
                  break;
        case 2 : this.reason = Reason.MAINTENANCE;
                  break;
        case 3 : this.reason = Reason.REGULATION;
      }
      SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmmss");
      if (startDateString.length() == 14) {
        try {
          this.startDate = ft.parse(startDateString);
        } catch (ParseException e) {
          System.out.println("Unparseable using" + ft);
        }
      }
      if (endDateString.length() == 14) {
        try {
          this.endDate = ft.parse(endDateString);
        } catch (ParseException e) {
          System.out.println("Unparseable using" + ft);
        }
      }
      this.startStation = startStation;
      this.endStation = endStation;
      this.isValid = false;
    }
    
    public Trip(Trip trip) {
      this(trip.getReason(), trip.getStartDate(),trip.getStartStation(), 
          trip.getEndDate(), trip.getEndStation());
    }
    
    
    /* Trip manipulation */
    public void cancelTrip() {
      isValid = false;
    }
    
    public void validateTrip() {
      isValid = true;
    }
    
    /**
     * En ordre de croissance du temps
     */
    public static Comparator<Trip> endDateComparator = new Comparator<Trip>() {
      public int compare(Trip t1, Trip t2) {
        return t1.endDate.compareTo(t2.endDate);
      }
    };
    
    public static Comparator<Trip> startDateComparator = new Comparator<Trip>() {
      public int compare(Trip t1, Trip t2) {
        return t1.startDate.compareTo(t2.startDate);
      }
    };
    
    
    /* Getters */
    public Reason getReason() {
      return reason;
    }
    public Date getStartDate() {
      return startDate;
    }
    public Date getEndDate() {
      return endDate;
    }
    public boolean isValid() {
      return isValid;
    }
    public Station getStartStation() {
      return startStation;
    }
    public Station getEndStation() {
      return endStation;
    }
    
    
    /* Setters */
    public void setStartStation(Station newStartStation) {
      startStation = newStartStation;

    }
    public void setEndStation(Station newEndStation) {
      endStation = newEndStation;

    }
   
    public void setReason(int i) {
      switch(i) {
      case 1: this.reason = Reason.RENT;
               break;
      case 2: this.reason = Reason.MAINTENANCE;
              break;
      case 3: this.reason = Reason.REGULATION;
              break;
      }
    }
    
    public void setStartSateString(String date) {
      SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmmss");
      if (date.length() == 14) {
        try {
          this.startDate = ft.parse(date);
        } catch (ParseException e) {
          System.out.println("Unparseable using" + ft);
        }
      }
    }
    
    public void setEndSateString(String date) {
      SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmmss");
      if (date.length() == 14) {
        try {
          this.endDate = ft.parse(date);
        } catch (ParseException e) {
          System.out.println("Unparseable using" + ft);
        }
      }
    }

    
    public String toString() {
      String info;
      SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmmss");
      info = getReason()+"\t"+getStartStation().getIdentity()+"\t"+ft.format(getStartDate())
                        +"\t"+getEndStation().getIdentity() + "\t"+ft.format(getEndDate());
      if (isValid) {
        info = info + "\tvalid";
        }
      else {
        info = info + "\tinvalid";
        }
      return info;
    }
    
    
    public static void main(String[] args) {
      //Station de départ
      Station station1 = new Station(901,"00901 - ALLEE DU BELVEDERE","ALLEE DU BELVEDERE PARIS 19 - 0 75000 Paris - 75000 PARIS",20,2.391225227186182,48.892795924112306);
      State state1 = new State(6,14,"20131030125959");
      station1.setIsOpen(true);
      station1.setPrimaryState(state1);
      
      //Station de retourne
      Station station2 = new Station(903,"00903 - QUAI MAURIAC  / PONT DE BERCY","FETE DE L\u0027OH (BERCY) - QUAI MAURIAC ANG PONT DE BERCY",20,2.374340554605615,48.83713368945151);
      State state2 = new State(15,5,"20131030125959");
      station2.setIsOpen(true);
      station2.setPrimaryState(state2);
      
      Date date1 = new Date();
      Date date2 = new Date();
      SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmmss");
      //Date de départ
      try {
        date1 = ft.parse("20131031000002");
      } catch (ParseException e) {
        System.out.println("Unparseable using" + ft);
      }
      //Date de retourne
      try {
        date2 = ft.parse("20131031000906");
      } catch (ParseException e) {
        System.out.println("Unparseable using" + ft);
      }
      
      //Créeation d'un nouveau trip
      Trip trp = new Trip(Reason.RENT,date1,station1,date2,station2);
      System.out.println(trp);
      
      trp.validateTrip();
      System.out.println();
      System.out.println("-----------After validation of trip:-------------");
      System.out.println();
      System.out.println(trp);
      
      trp.setEndStation(station1);
      System.out.println();
      System.out.println("-----------After change of end station, NB: the trip becomes invalid------------");
      System.out.println();
      System.out.println(trp);
      System.out.println();

      //Créeation d'un nouveau trip
      System.out.println("-----------Creating new trip------------");
      Trip trp2 = new Trip(1,"20131031000002",station1,"20131031000907",station2);
      System.out.println();
      System.out.println(trp2);
      
      Trip trp3 = new Trip(1,"20131031000002",station1,"20131031000908",station2);
      
      //Usage de comparator
      ArrayList<Trip> tripList = new ArrayList<Trip> ();
      tripList.add(trp);
      tripList.add(trp2);
      tripList.add(trp3);
      tripList.sort(endDateComparator);
      System.out.println();
      System.out.println("----------After sort by end date:-------------");
      System.out.println();
      for(int i = 0; i< tripList.size(); i++)
      {
    	  	System.out.println(tripList.get(i));
      }
    }

}
