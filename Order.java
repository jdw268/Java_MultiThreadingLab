
import java.sql.Timestamp;
import java.util.ArrayList;


public class Order{
    Timestamp startTime;
    Timestamp endTime;
    ArrayList<Part> parts;
    String oid;

    //constructor
    public Order(ArrayList<Part> partsList, String orderNumber){
        setParts(partsList);
        setOid(orderNumber);

        //set time
        startTime = new Timestamp(System.currentTimeMillis());
    }

  /*
  * Input: ArrayList<Part> for an order's part list
  * Return: Void
  * Description: Sets the ArrayList<Part> to inputted value
  */
    public void setParts(ArrayList<Part> partsList){
        parts = partsList;
    }

    /*
  * Input: String for order Number
  * Return: Void
  * Description: Sets the order object's oid variable
  */
    public void setOid(String orderNumber){
        oid = orderNumber;
    }


  /*
  * Input: String OID
  * Return: part object
  * Description: Returns part object based on part name inputted
  */
    public Part getPart(String p){
        Part part = null;

        for(int i = 0; i < parts.size(); i++){
            //find match to string p for part
            if(p.contains("Misc")){
                //return last part element which is the misc part
                part = parts.get(parts.size()-1);
            }
            if(parts.get(i).getType().equalsIgnoreCase(p)){
                part = parts.get(i);
            }
        }  //end for

        //return name of the part
        return part;
    }  //end getPart


     /*
  * Input: None
  * Return: ArrayList of parts (by stations) that still need to be installed
  * Description: Searches inputed OID number against parts list for matching order number
    Search for stations that still need to be serviced for the vehicle
  */
    public ArrayList<String> getStationsLeft(){
        ArrayList<String> stationsLeft = new ArrayList<>();

        //loop through parts arraylist and check each part's installed property
        for (int i = 0; i < parts.size(); i++){

            //if the parts installed property is false then add to stationsLeft
            if(!parts.get(i).getInstalled()){
                stationsLeft.add(parts.get(i).getType());
            }
        }  //end for

        return stationsLeft;
    }  //end getStationsLeft


  /*
  * Input: None
  * Return: Boolean value if all parts have beeen installed
  * Description: Loops through parts list and checks if each part has been installed
  */
    public boolean complete(){
        boolean completed = false;

        //check if there are any Stations left for order
        if (this.getStationsLeft().isEmpty()){
            completed = true;

            //update end time stamp to completed order
            this.endTime = new Timestamp(System.currentTimeMillis());
        }
        return completed;
    }  //end complete


    /*
  * Input: None
  * Return: String
  * Description: Determines the order object's status information
  */
    public String toString(){
        String output = null;

        //check if order complete or not
        if(this.complete()){
            //variable to store time lapse from start to end in seconds -- keep ms
            //resource: https://stackoverflow.com/questions/14810084/how-to-find-the-difference-of-two-timestamps-in-java
            double timeLapse = (endTime.getTime() - startTime.getTime());

            //output complete string
            output = "Order ID: " + oid + " Total time(sec)): " + timeLapse/1000
                    + " Total number of cycles: " + this.getTotalCycles();
        }  //end if
        else{output = "Order id:  " + oid + " Start Time: " + startTime
                + " Stations not complete: " + this.getStationsLeft();
        }

        return output;
    }  //end to string

    /*
  * Input: None
  * Return: int for total number of cycles in roder
  * Description: Returns the total number of cycles an order goes through for each part
  */
    public int getTotalCycles(){
        int totalCycles = 0;

        //loop through each part in parts list
        for(int i = 0; i < parts.size(); i++){
            //get cycles value and add to total
            totalCycles = totalCycles + parts.get(i).getCycles();
        }
        return totalCycles;
    } //end getTotalCycles

}  //end Order class
