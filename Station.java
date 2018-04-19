
import java.util.*;

public class Station implements Runnable{
    private String stationName;  //thread name
    private Stack<Order> ordersCompleted = new Stack<>();  //station completed not entire order
    private Stack<Order> ordersQueued  = new Stack<>();  //stations to still be processed
    private Thread t;
    
    //number of orders that have gone through the station
    private int numOfInstalls = 0;
    private boolean kill = false;   //kill all stations have all orders complete
    
    //constructor
    public Station(String nameOfStation){       
        //misc, body, powertrain, etc        
        stationName = nameOfStation;        
        
        //initiate new thread
        t = new Thread (this, nameOfStation);
        
        //initiate thread
        t.start();
    }
    
    /*
  * Input: None
  * Return: String
  * Description: Returns the station object's name
  */
    public String getStationName(){
        return stationName;
    }
    
    /*
  * Input: None
  * Return: Int
  * Description: returns number of orders waiting for the station
  */
    public int waitList(){
        return this.ordersQueued.size();
    }
    
   /*
  * Input: none
  * Return: none
  * Description: Processes the vehicle's parts to be built (stationed).
        Pops order from queued and accesses the part within the order.  Creates a loop
        from the number of cycles the part requires, increments global num, sets the Part as
        complete, and pushes the order to completed statck
 */
    private void process(){    
        
      //get order from queue
      Order currentOrder = ordersQueued.pop();
      
      Part currentPart = null;
     
      //get part that this station is working on
      if(this.stationName.contains("Body")){
          //set currentPart equal to body
          currentPart = currentOrder.getPart("Body");                  
      }     
      else{
          //set currentPart to the stationName
          currentPart = currentOrder.getPart(stationName);
      }
      
       //loop for number of cycles calling part has 
       for (int i=0; i <= currentPart.getCycles(); i++){          
           //mimick building part on vehicle with time lapse
       }  
       
       //set part to completed install
       currentPart.setInstalled(true);  
       
       //increment number for how parts went thru station
       this.numOfInstalls = this.numOfInstalls + 1;
      
       //push completed order to stack if no more stations need to be processed - and use complete to update end time
       if(currentOrder.complete()){
           this.ordersCompleted.push(currentOrder);
       }
       else{
           //System.out.println("still working on order");
       }         
    }  //end process method
    
     /*
  * Input: none
  * Return: none
  * Description: Runs while kill is false to continuallty check queue for new orders to process.
        Runnable interface that runs thread
 **/
    public void run(){
        while(!kill){
             // System.out.println("Thread running: " + this.stationName);
             //check queue for new orders
            if(!ordersQueued.isEmpty()) {
                //if there are orders in the queue then process them
                this.process();  
            }                 
        }  //end while        
    }  //end run
    
  /*
  * Input: none
  * Return: none
  * Description: Prints to console of station thread information and sets kill to true to stop thread
 **/
    public void interrupt(){
        System.out.println("Station: " + this.stationName + " completed with " 
                + this.numOfInstalls + " installations and is now shutting down.");
        
        kill = true;            
    } //end interrupt   
    
    
   /*
  * Input: none
  * Return: Stack<Order>
  * Description: Returns the completed stack of all completed orders
 **/
   public Stack<Order> getCompletedOrders(){
       //empty the queued orders (should already be empty) 
       ordersQueued.clear();
       return this.ordersCompleted;   
   }
    
   /*
  * Input: none
  * Return: none
  * Description: Add inputted order to the station's queue list
 **/
    public void addOrder(Order o){     
        //add order to queue
        ordersQueued.push(o);
    }
    
  /*
  * Input: none
  * Return: none
  * Description: Starts the station thread
 **/
    //reference:https://www.tutorialspoint.com/java/java_multithreading.htm
    public void start(){
        //t.run();
        t.start();
       // if (t==null){
        //    t = new Thread(this, stationName);
        //    //t.start();
        //   t.run();
       // }
    }
    
}  //end class