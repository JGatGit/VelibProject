package Data;

import java.text.*;
import java.util.*;

public class State {
    private Date date;
    private int numberOfFreeBikes;
    private int numberOfFreeStands;
    private boolean isFull;
    private boolean isEmpty;
    private boolean isCriticallyFull; // changed by Jul
    private boolean isCriticallyEmpty; //

    /* Constructeur */
    /* Date format "yyyyMMddhhmmss" */ 
    public State(int numOfFreeBikes, int numOfFreeStands, Date currentDate) {
      this.numberOfFreeBikes = numOfFreeBikes;
      this.numberOfFreeStands = numOfFreeStands;
      this.isFull = (numOfFreeStands == 0);
      this.isEmpty = (numOfFreeBikes == 0);
      this.isCriticallyFull = (numOfFreeStands <= 3); //changed by Jul
      this.isCriticallyEmpty = (numOfFreeBikes <= 3); //
      this.date = currentDate;
      if (numOfFreeStands < 0 ) {
        String info = "ATTENTION: FREE STANDS OVER DECHARGED, WITH NUMBER OF STANDS EQUALS TO";
        info = info + this.numberOfFreeStands;
        System.out.println(info);
      }
      if (numOfFreeBikes < 0 ) {
        String info = "ATTENTION: FREE BIKES OVER CHARGED, WITH NUMBER OF BIKES EQUALS TO";
        info = info + this.numberOfFreeBikes;
        System.out.println(info);
      }
    }
    
    /* Input Date String "yyyyMMddhhmmss" */ 
    /* Date format "yyyyMMddhhmmss" */ 
    public State(int numOfFreeBikes, int numOfFreeStands, String currentDateString) {
      numberOfFreeBikes = numOfFreeBikes;
      this.numberOfFreeStands = numOfFreeStands;
      this.isFull = (numOfFreeStands == 0);
      this.isEmpty = (numOfFreeBikes == 0);
      this.isCriticallyFull = (numOfFreeStands <= 3);
      this.isCriticallyEmpty = (numOfFreeBikes <= 3);
      if (currentDateString.length() == 14) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmmss");
        try {
          date = ft.parse(currentDateString);
        } catch (ParseException e) {
          System.out.println("Unparseable using" + ft);
        }
      }
    }
    
    
    /* Getters */
    public Date getDate() {
      return date;
    }
    public int getNBikes() {
      return numberOfFreeBikes;
    }
    public int getNStands() {
      return numberOfFreeStands;
    }
    public boolean isFull() {
      return isFull;
    }
    public boolean isEmpty() {
      return isEmpty;
    }
    public boolean isCriticallyFull() { //add by Jul
        return isCriticallyFull;
      }
      public boolean isCriticallyEmpty() { //add by Jul
        return isCriticallyEmpty;
      }
    
    /* Setters */
    public void setDate(final Date p1) {
      date = p1;
    }
    
 
    public String toString() {
      SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmmss");
      String info = ft.format(date);
 //     String info = date.toString();
      info = info +"\t N of freebikes: \t"+getNBikes() + "\t N of freestands: \t"+getNStands();
      if (isFull()) {
        info = info + "\t full";
        }
      else if (isEmpty()) {
        info = info + "\t empty";
      }
      else info = info + "\t ni full ni empty";
      return info;
    }

    
    /* Pour tester le code */
    public static void main(String [] args) {
    	System.out.println("Printing 3 states with 0, 5 and 10 bikes");
    	
      // Constructeur de classe Date 1 
      Date date1 = new Date();
      State st1 = new State(0,10,date1); 
      System.out.println(st1);

      // Constructeur de classe Date 2 
      Date date2 = new Date();
      State st2 = new State(5,5,date2); 
      System.out.println(st2);
      // Constructeur de String 3 
      SimpleDateFormat myFormater = new SimpleDateFormat ("yyyyMMddhhmmss");
      try {
        Date date3 = myFormater.parse("19950930091030");
        State st3 = new State(10,0,date3);
        System.out.println(st3);
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      

    }
    
}
