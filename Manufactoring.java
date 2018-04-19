import java.util.ArrayList;
import java.util.*;

public class Manufactoring{
    //instance variable
    private ArrayList<Station> schedule = new ArrayList<>();    
    
    // no arg constructor
    public Manufactoring(){      
        //scheduleStations = null;
}
    
    /*
  * Input: orders:  ArrayList<ArrayList<Part>> from SupplyDump.java
  * Return: Void
  * Description: Turns the ArrayList<Parts> into orders, starts processing orders, when an order is complete,
    prints the toString of the order object, and when all orders are complete kills all threads and returns to 
    SupplyDump.  Add orders, remove orders, kill station -- interface with Station.java
  */
    public void Start(ArrayList<ArrayList<Part>> orderSupplyDump){
        
        //initialize stations and their threads - threads will wait until order in queue
        this.Create_Stations();  
         
        //station threads are running - next create orders
        //create arraylist of orders
        ArrayList<Order> orderList = new ArrayList<>();       
       
        //loop through orderSupplyDump and create/add order
        for(int i =0; i < orderSupplyDump.size(); i++){
            //instantiate an order object from arraylist part    
            Order order = new Order(orderSupplyDump.get(i), orderSupplyDump.get(i).get(0).getOrderNumber());            
        
            //then add this order to the order stack 
            orderList.add(order);
        }          
                  
        //add orders to schedule
        for(int i =0; i< orderList.size(); i++){    
                       
          //loop through schedule and add order to stations - starting with body
           for(int y = 0; y < schedule.size(); y++){     
            //add order to each schedule - updated each station's queued orders
            schedule.get(y).addOrder(orderList.get(i));
          }
        }  //end for loop to add orders to each station's schedule 
               
       //start processing orders and store in stack
       Stack<Stack<Order>> completedOrders = new Stack<Stack<Order>>();
       completedOrders = this.Schedule_Stations();       
       
       //store final result to console
       String result = "\nCompleted Orders: ";      
       
       //consolidate orders (since all stations hold the orders)
       //use hashset to store orders because can't store duplicates
       Set<Order> consolidatedOrders = new HashSet<Order>();
       for (Stack<Order> test: completedOrders){           
           for(Order order: test){
               consolidatedOrders.add(order);
           }       
       }
       
      //print results of hashet order info
      //Reference:  ide did for loop
        for (Iterator<Order> it = consolidatedOrders.iterator(); it.hasNext();) {
            Order printOrders = it.next();
            result = result + "\n" + printOrders.toString(); 
        }
        
       //print final result to console
       System.out.println(result);
             
    }  //end start method
       
    
    /*
  * Input: none
  * Return: Void
  * Description: Populates Schedule and initiates the threads for each station
  */
    public void Create_Stations(){        
        String[] stations = {"Body1", "Body2", "Body3", "Body4", "Exterior", "Interior", "Powertrain", "Seat", "Radio", "Tire", "Rim", "Miscellaneous"};
        
        //instantiate stations using string[] (to start with body)
        for (int i = 0; i < stations.length; i++){     
            //create station for each string and add to schedule 
            //thread is started when station object instantiated
            schedule.add(new Station(stations[i]));
        }      
    }  //end Create_Stations method
  
    
    /*
  * Input: none
  * Return: Stack completed
  * Description: Returns the completed stack of all completed orders
  */
    public Stack<Stack<Order>> Schedule_Stations(){
       String complete = null;
       Stack<Stack<Order>> completedOrders = new Stack<>();
       Boolean wait = true;
       
       //keep checking for all completed orders until waitlist is 0 for all
      while(wait){
          int waitTrue = 0;
          
       //check each station for completed products and schedules the next       
       for(int i =0; i< schedule.size(); i++){
           //check each station's orders          
           //see if order needs to be processed for station
           if(schedule.get(i).waitList() != 0){
               waitTrue = waitTrue + 1;
           }           
       }  //end for
       
       //if there were any waits then keep loop going
       if(waitTrue != 0){
           wait = true;
       }
       else{
           //all orders have been processed
           wait = false;
       }
      } //end while           
       
      //now that no orders are in queue, add to stack
       for(int y = 0; y< schedule.size(); y++){
           completedOrders.push((schedule.get(y).getCompletedOrders()));
           
           //kill the stations
           schedule.get(y).interrupt();
       }
     
       return completedOrders;
    }  //end Schedule_Stations method

}  //end Manufactoring class