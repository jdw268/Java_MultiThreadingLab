
import java.io.BufferedWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SupplyDump {

  //map to store key value pairs of part id & ArrayList of Part
  private static Map<String, ArrayList> Stock = new LinkedHashMap<>();

  public static void main(String[] args) {
    try {
      ingestStock();

      //get a set containing the mappings in this map
      //Set<Map.Entry<String, ArrayList>> parts = Stock.entrySet();

//      System.out.println("Here are the mappings");
//      for (Map.Entry<String, ArrayList> entry : parts) {
//        System.out.println("Key = " + entry.getKey());
//        System.out.println("Value = " + entry.getValue().toString());
//        System.out.println();
//      }
    } catch (StockFileNotFoundException e) {
      System.out.println(e.getMessage());
    } catch (PartNotFoundException e) {
      System.out.println(e.getMessage());
    }

    //stock ingested successfully, so call request method
    try{
        request();
    }    
    catch(OIDFileNotFoundException e){
            System.out.println(e.getMessage());
    }
  }

  /*
  * Input: None
  * Return: Void
  * Description: Requests user to input text file of order IDs and calls buildOrder for each line to start building each order.
  * Each build call will store the results of returnParts in an ArrayList.  Each request will print to the console and save the invoice file.
  * Lastly, when each OID is processed, an ArrayList is sent to Manufactoring.java.
  * Request will continue to prompt the user until the user inputs -1, then request will return.
  */
  public static void request() throws OIDFileNotFoundException {
    boolean continueUserInput = true; //variable to hold if user wants to continue entering OID#s
    String userOIDInput;  //variable to store OID#
    Scanner keyboard = new Scanner(System.in);  //create Scanner object for user input    

    //prompt user to enter 12-digit PID number until they end with -1
    while (continueUserInput) {

      //initialize userPIDInput
      userOIDInput = "";

      //prompt user
      //System.out.print("Enter a OID number (-1 to exit): ");
      System.out.print("Enter text file of order IDs (with absolute path and file extension) : ");
      userOIDInput = keyboard.nextLine();

      if (userOIDInput.equals("-1")) {
        //exit while loops and program
        System.out.println("Exiting program");
        System.exit(0);
      } else {
        try {
            //ArrayList of ArrayList of returnedParts for each oid in file
            ArrayList<ArrayList<Part>> returnedParts = new ArrayList<>();
            
            File file = new File(userOIDInput);
            //create scanner object to read lines of file
            Scanner inputFile = new Scanner(file);         
            
            //store each line of file in array list
            ArrayList<String> line = new ArrayList<String>();
            
            //read each line into arraylist
            while(inputFile.hasNext()){
                line.add(inputFile.nextLine());
            }            
            //System.out.println("oids in the file are: " + line);
            //build order for each string in arraylist
            for(int i = 0; i < line.size(); i++){
               returnedParts.add(buildOrder(line.get(i)));
            }
             
            //instantiate a manufactor object start it
         //instantiate a manufactor instance
         Manufactoring manufactor = new Manufactoring();
         manufactor.Start(returnedParts);
         
         //have to send in returnedParts elements one at a time to create order objects
         //for(int i =0; i < returnedParts.size(); i++){
           // manufactor.Start(returnedParts.get(i));
         //}
         
            
            //System.out.println("order: " + returnedParts);
          //  System.out.println("getpart: " + returnedParts.get(0).get(1).);
             
            
        } catch (Exception e) {
        //could load xml file so throw exception
        throw new OIDFileNotFoundException(userOIDInput);
        }
         
      }
    }  //end while for continueUserInput
  }  //end request method


  /*
  * Input: String oid number
  * Return: ArrayList<Part> (for order - Manufactoring.java)
  * Description: Creates the invoice with each order.  The order ID(oid) has the first 4 digits being the
  the order number, and the proceeding numbers be the product id (pid).  This method calls pidFormat and uses
  that information to determine if parts are available.  If all parts are available, then requestParts is called
  Lastly, an invoice is created.
  */
  public static ArrayList<Part> buildOrder(String oid) {
    //format the pid number where the 1st 4 digits are the order number and proceeding are the pid
    String[] oidFormatted = pidFormat(oid);

    //string array for parts needed for ordering parts
    String[] parts = {"Body", "Exterior", "Interior", "Powertrain", "Seat", "Radio", "Rim", "Tire", "Misc"};
    
    ArrayList<Part> partsRequested = new ArrayList<Part>();

    //call partsAvailable to see if everthing in stock based on oid number indexes 1 to length
    try{
      Boolean partExists = true;
      for (int p = 1; p <oidFormatted.length; p++){

        //call with current index of oidFormatted(string representation of part)
        partExists = partAvailable(parts[p-1], oidFormatted[p]);
      }
      //if true then requestParts; if false then call request
      if(!partExists){
          try{
        SupplyDump.request();
          }
          catch(OIDFileNotFoundException f){
            System.out.println(f.getMessage());
    }
      }
      //since user entered valid parts, call requestParts(check stock in this method)
      else{
        try{
          //requestParts returns an ArrayList of part objects
         // ArrayList<Part>        
           partsRequested = requestParts(oid);   

          //requestParts succeeded so call createInvoice
          createInvoice(partsRequested, oid);

          //print invoice to consol
          showInvoice(partsRequested, oid);         
                 

        }
        catch(OutOfStockException e){
          System.out.println(e.getMessage());
          //call request
          try{
          SupplyDump.request();
        }
          catch(OIDFileNotFoundException f){
            System.out.println(f.getMessage());
    }
        }
      }  
      
    }
    catch(PartIdNotDefinedException e){
      System.out.println(e.getMessage());
      //call request
       try{
        //request();
        
      SupplyDump.request();
    }
    catch(OIDFileNotFoundException f){
            System.out.println(f.getMessage());
    }
    }
    catch(PartNotDefinedException e){
      System.out.println(e.getMessage());
      //call request
      try{
      SupplyDump.request();
    }
     catch(OIDFileNotFoundException f){
            System.out.println(f.getMessage());
    } 
    }
      //return the partsRequested arraylist
        return partsRequested;
  }  //end buildOrder


  /*
  * Input: String order id number
  * Return: ArrayList of part objects
  * Description: Checks SupplyDump attribute Stock to see if the part p with id is in stock.
  If the part it is not in stock then OutOfStockException is called. If it is in stock, then parts removed and
  invoice created.
  */
  public static ArrayList<Part> requestParts(String oidNumber) throws OutOfStockException{

    //format the pid number where the 1st 4 digits are the order number and proceeding are the pid
    String[] oidFormatted = pidFormat(oidNumber);
    String orderNumber = oidFormatted[0];
        
    ArrayList<Part> partObjects = new ArrayList<Part>();  //return variable

    //string array for parts needed for ordering parts
    String[] parts = {"Body", "Exterior", "Interior", "Powertrain", "Seat", "Radio", "Rim", "Tire", "Misc"};

    //loop through parts
    for(int i = 0; i < parts.length; i++){

      //if first element "Body" special case
      if(i==0){
        //grab first key (body) from oid (first element oid is order#) and store values of it in search arraylist
      
      //  ArrayList<Part> search = Stock.get(oid[i+1]);
      ArrayList<Part> search = (ArrayList<Part>) Stock.get(oidFormatted[i+1]);
        //check the size of search (if 1, then it only contains initial part so no stock)
        if(search.size() == 1){
          //throw outofstockexception
          throw new OutOfStockException(parts[i] + ": " + oidFormatted[i+1]);
        }
        //else add partList @ last element to partObjects list then remove 1 from inventory
        else{
          //store last stored part object in stock arraylist
          search.get(search.size()-1).setOrderNumber(orderNumber);
          partObjects.add(search.get(search.size()-1));

          //remove last stored part object from stock arraylist
          Stock.get(oidFormatted[i+1]).remove(Stock.get(oidFormatted[i+1]).size() - 1);
        } //end else for part in stock
      } //end 'body' element check
      
      //check if at element misc
      else if(parts[i].equalsIgnoreCase("Misc")){
        //append strings for key to search for - misc has 2 digit misc so grab first digit for key
        String searchKey = null;
        
        //grab 1st number of misc optoin
         char miscOptionChar = oidFormatted[oidFormatted.length -1].charAt(0);
        //use case switch to determine searchKey
        switch(miscOptionChar) {
            case '1':
                //append part to id = key to search for
                searchKey = "DrivingAssist" + oidFormatted[oidFormatted.length -1];
                break;
            case '2':
                //append part to id = key to search for
                searchKey = "Roof" + oidFormatted[oidFormatted.length -1];
                break;
           case '3':
                //append part to id = key to search for
                searchKey = "Backup" + oidFormatted[oidFormatted.length -1];
                break;
           case '4':
                //append part to id = key to search for
                searchKey = "Sensor" + oidFormatted[oidFormatted.length -1];
                break;
        }  //end switch
        //String searchKey = parts[i] + oidFormatted[i+1];
        
        //grab first i key from oid and store values of it in search arraylist
        ArrayList<Part> search = Stock.get(searchKey);
        
        //check the size of search (if 1, then it only contains initial part so no stock)
        if(search.size() == 1){
          //throw outofstockexception
          throw new OutOfStockException(parts[i] + ": " + oidFormatted[i+1]);
        }
        //else add partList @ last element to partObjects list then remove 1 from inventory
        else{
          //store last stored part object in stock arraylist
          partObjects.add(search.get(search.size()-1));

          //remove last stored part object from stock arraylist
          Stock.get(searchKey).remove(Stock.get(searchKey).size() - 1);
        } //end else for part in stock     
        
        //index value = misc option while id referes to different misc options
      }

      //else load other part options into partObjects array if stock available
      else{
        //append strings for key to search for
        String searchKey = parts[i] + oidFormatted[i+1];

        //grab first i key from oid and store values of it in search arraylist
        ArrayList<Part> search = Stock.get(searchKey);

        //check the size of search (if 1, then it only contains initial part so no stock)
        if(search.size() == 1){
          //throw outofstockexception
          throw new OutOfStockException(parts[i] + ": " + oidFormatted[i+1]);
        }
        //else add partList @ last element to partObjects list then remove 1 from inventory
        else{
          //store last stored part object in stock arraylist
          partObjects.add(search.get(search.size()-1));

          //remove last stored part object from stock arraylist
          Stock.get(searchKey).remove(Stock.get(searchKey).size() - 1);
        } //end else for part in stock
      }
    } //end for loop

    return partObjects;

  }  //end requestParts


  /*
  * Input: String part, String id
  * Return: Boolean
  * Description: Checks SupplyDump attribute Stock to see if the part p with id is available(exists).
  If the part does exist, the method returns true.  All parts are initialized with a default entry so that
  user input can be checked for validity.
  *Lab 5 pdf conflict:  pdf says this method should return false if part out of stock and only call requestParts
  if method returns true.  However, requestParts contains the OutOfStockException.  Therefore, partAvailable will
  only check if the user has entered a valid id for a part (body, exterior, interior, etc.).
  */
  public static Boolean partAvailable(String part, String id) throws
  PartIdNotDefinedException, PartNotDefinedException
  {
    Boolean partExists = true;  //initialize part exists to true

    if(part.equalsIgnoreCase("Body")){
      //check Stock for string id directly (returns boolean)
      if(!Stock.containsKey(id)){
        partExists = false;
        throw new PartIdNotDefinedException(part, id);
      }
      else{
        //store the arraylist at id into search
        ArrayList<Part> search = Stock.get(id);

        //grab first element and get its type
        String searchType = search.get(0).getType();

        //check if "Body" is the type
        if(!searchType.equalsIgnoreCase(part)){
          partExists = false;
          throw new PartNotDefinedException(part);
        }
      }  //end else
    }  //end if for Body
    
    else if(part.equalsIgnoreCase("Misc")){
        char miscOptionChar = id.charAt(0);
        String searchKey = null;
        String containsSearch = null;
        
        switch (miscOptionChar){
            case '1':
                //append part to id = key to search for
                searchKey = "DrivingAssist" + id;
                containsSearch = "DrivingAssist";
                break;
            case '2':
                searchKey = "Roof" + id;
                containsSearch = "Roof";
                break;
             case '3':
                searchKey = "Backup" + id;
                containsSearch = "Backup";
                break;                
             case '4':
                searchKey = "Sensor" + id;
                containsSearch = "Sensor";
                break;
                 default:
                throw new PartIdNotDefinedException(part, id);
        } //end switch 
        
        //search if values have miscOption type in value
                ArrayList<Part> search = new ArrayList<Part>();

         //get the ArrayList associated with id
         search = Stock.get(searchKey);

         //grab first element and get its type
         String searchType = search.get(0).getType();

                //check if part exists in ArrayList
                if(!searchType.contains(containsSearch)){
                 partExists = false;
                throw new PartNotDefinedException(part);
                }    
        //check first charatcher of id against 4 types of misc options
//        switch (miscOptionChar){
//            case '1':
//                //append part to id = key to search for
//                String searchKey = "DrivingAssist" + id;
//                
//                //search if values have miscOption type in value
//                ArrayList<Part> search = new ArrayList<Part>();
//
//                //get the ArrayList associated with id
//                search = Stock.get(searchKey);
//
//                //grab first element and get its type
//                String searchType = search.get(0).getType();
//
//                //check if part exists in ArrayList
//                if(!searchType.contains("DrivingAssist")){
//                 partExists = false;
//                throw new PartNotDefinedException(part);
//                }                
//                break;
//            case '2':
//                String searchKeyR = "Roof" + id;                
//                ArrayList<Part> searchR = new ArrayList<Part>();
//                searchR = Stock.get(searchKeyR);
//                String searchTypeR = searchR.get(0).getType();
//                if(!searchTypeR.contains("Roof")){
//                 partExists = false;
//                throw new PartNotDefinedException(part);
//                }                
//                break;                
//            case '3':
//                String searchKeyB = "Backup" + id;                
//                ArrayList<Part> searchB = new ArrayList<Part>();
//                searchB = Stock.get(searchKeyB);
//                String searchTypeB = searchB.get(0).getType();
//                if(!searchTypeB.contains("Backup")){
//                 partExists = false;
//                throw new PartNotDefinedException(part);
//                }                
//                break;
//            case '4':
//                String searchKeyS = "Sensor" + id;                
//                ArrayList<Part> searchS = new ArrayList<Part>();
//                searchS = Stock.get(searchKeyS);
//                String searchTypeS = searchS.get(0).getType();
//                if(!searchTypeS.contains("Sensor")){
//                 partExists = false;
//                throw new PartNotDefinedException(part);
//                }                
//                break;            
//            default:
//                throw new PartIdNotDefinedException(part, id);
//        } //end switch       
        
    }  //end if for misc

    //else search other part options - exterior, interior, etc.
    else{
      //append part to id = key to search for
      String searchKey = part + id;

      if(!Stock.containsKey(searchKey)){
        partExists = false;
        throw new PartIdNotDefinedException(part, id);
      }
      else{
        //search if values have "Body" in value
        ArrayList<Part> search = new ArrayList<Part>();

        //get the ArrayList associated with id
        search = Stock.get(searchKey);

        //grab first element and get its type
        String searchType = search.get(0).getType();

        //check if part exists in ArrayList
        if(!searchType.equalsIgnoreCase(part)){
          partExists = false;
          throw new PartNotDefinedException(part);
        }
      }  //end else
    } //end else for other parts than 'body'

    return partExists;
  }

    /*
  * Input: String order number, ArrayList<Part> of parts requested
  * Return: Void
  * Description: Writes to file the order number, produce ID, list each part, and the total price.
  */
  public static void createInvoice(ArrayList<Part> partsRequested, String oid) {
    //store console write-out to ArrayList<String>
    ArrayList<String> writeConsole = new ArrayList<String>();
    String[] oidFormatted = pidFormat(oid);
    Double total =0.0;        //store cost
    String temp;
    String filename;

    //determine the filename
    if(oid.charAt(4) == '1'){
      temp = "ThisAuto";
    }
    else if(oid.charAt(4) == '2'){
      temp = "ThatAuto";
    }
    else{
      temp = "OtherAuto";
    }

    //update filename
    filename = temp + "_" + oidFormatted[0] + ".txt";

    //add oid number
    writeConsole.add("Order Number: " + oidFormatted[0]);

    //add pid
    writeConsole.add("\nProduct ID: " + oid.substring(4));

    //convert partsRequested ArrayList part to string
    ArrayList<String> convertedParts = new ArrayList<String>();

    //loop through partsRequested parts arraylist and convert to string arrayList
    //this will be used to grab part name
    for (int y =0; y<partsRequested.size(); y++){
      convertedParts.add(partsRequested.get(y).toString());
    }

    //loop through partsRequested and add to writeConsole ArrayList
    for(int i =0 ; i< partsRequested.size(); i++){
      String tempOption = "";
        //update total
      total = total + partsRequested.get(i).getPrice();

      //split covertedParts String array list into string elements for i
      String[] tempStrings = convertedParts.get(i).split(" ");
      
      //int priceLocation =
      //store strings from 4th element at price
      for (int y = 7; y< tempStrings.length; y++){
          tempOption = tempOption + " " + tempStrings[y];
      }
      //store last string element into temp
      //temp = tempStrings[tempStrings.length -1];

      writeConsole.add("\n" + "Part: " + partsRequested.get(i).getType() + ": " + tempOption + ", Cost: " + partsRequested.get(i).getPrice());
    }

    //add total price to writeConsole
    //create format object
    DecimalFormat formatter = new DecimalFormat("$###,###,##0.00");
    writeConsole.add("\nTotal Cost: " + formatter.format(total));

    try{
      //open the file and write to it p234
      FileWriter outputFile = new FileWriter(filename);
      BufferedWriter bOutputFile = new BufferedWriter(outputFile);

      //get data and write to the file
      //reference: https://stackoverflow.com/questions/19084352/how-to-write-new-line-character-to-a-file-in-java
      for(int t = 0; t < writeConsole.size(); t++){
        bOutputFile.write(writeConsole.get(t).toString());
        bOutputFile.newLine();
      }

      //close the file
      bOutputFile.close();
    }
    catch(Exception e){
      System.out.println(e);
    }

  }  //end createInvoice


  /*
  * Input: String order number, ArrayList<Part> of parts requested
  * Return: Void
  * Description: Writes to console the order number, produce ID, list each part, and the total price.
  */
  public static void showInvoice(ArrayList<Part> partsRequested, String oid) {
    //store console write-out to ArrayList<String>
    ArrayList<String> writeConsole = new ArrayList<String>();
    String[] oidFormatted = pidFormat(oid);
    Double total =0.0;        //store cost
    

    //add oid number
    writeConsole.add("Order Number: " + oidFormatted[0]);

    //add pid 
    writeConsole.add("\nProduct ID: " + oid.substring(4));

    //convert partsRequested ArrayList part to string
    ArrayList<String> convertedParts = new ArrayList<String>();

    //loop through partsRequested parts arraylist and convert to string arrayList
    //this will be used to grab part name
    for (int y =0; y<partsRequested.size(); y++){
      convertedParts.add(partsRequested.get(y).toString());
    }

    //loop through partsRequested and add to writeConsole ArrayList
    for(int i =0 ; i< partsRequested.size(); i++){
      String temp = "";
      //update total
      total = total + partsRequested.get(i).getPrice();

      //split covertedParts String array list into string elements for i
      String[] tempStrings = convertedParts.get(i).split(" ");

      //store last string element into temp  - 7 and on
      for (int y = 7; y< tempStrings.length; y++){
        temp = temp + " " + tempStrings[y];
      }
      //temp = tempStrings[tempStrings.length -1];
      
      writeConsole.add("\n" + "Part: " + partsRequested.get(i).getType() +  ": " + temp + ", Cost: " + partsRequested.get(i).getPrice());
    }
    //add total price to writeConsole
    //create format object
    DecimalFormat formatter = new DecimalFormat("$###,###,##0.00");
    writeConsole.add("\nTotal Cost: " + formatter.format(total));

    //print writeConsole to screen
    System.out.println("Invoice:\n" + writeConsole + "\n");
  }


  /*
  * Input: None
  * Return: Void
  * Description: Reads from stock.xml file and builds SupplyDump attribute Stock with current stock levels.
  */
  public static void ingestStock() throws StockFileNotFoundException, PartNotFoundException {

    //check for file exception
    try {
      File file = new File("stock.xml");
    } catch (Exception e) {
      //could load xml file so throw exception
      throw new StockFileNotFoundException("stock.xml");
    }

    //tags to search and extract info from in xml file
    ArrayList<String> stock = new ArrayList<String>(Arrays.asList("Body", "Exterior", "Interior", "Powertrain", "Seat", "Radio", "Rim", "Tire", "Misc"));
    try {

      File file = new File("stock.xml");
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document document = db.parse(file);
      document.getDocumentElement().normalize();

      for (String j : stock) {
        //create a nodeList of all nodes at current element in ArrayList stock
        NodeList n = document.getElementsByTagName(j);  

        //loop through Nodelist and determine which tag at to store info
        for (int t = 0; t < n.getLength(); t++) {
          //temp ArrayList to gather all quantities of same id field
          ArrayList<Part> tempParts = new ArrayList<Part>();
          Node node = n.item(t);
          Element e = (Element) node;
          
          switch (j) {
            case "Body":
            //make initial first element as Body (this is for partAvaiable exceptions test
            //since using ArrayList<Part> as value; if empty then won't get partAvailable exception so need
            //at least 1 part in each ArrayList to start; then if ArrayList only has 1 element - the part is out of stock
            Body initialB = new Body(j, "0", 0.0, "0", false, 0);
            tempParts.add(initialB);
            //if tag is Body, loop through all body tags and store in tempArray List
            for (int i = 0; i < Integer.parseInt(e.getElementsByTagName("qty").item(0).getTextContent()); i++) {
              //create a Body object of the xml content
              Body b = new Body(j, e.getAttribute("id"), Double.parseDouble(e.getElementsByTagName("Cost").item(0).getTextContent()), e.getElementsByTagName("name").item(0).getTextContent(), false, Integer.parseInt(e.getElementsByTagName("cycles").item(0).getTextContent()));

              //store that body part in the temp ArrayList
              tempParts.add(b);

              //add the key value pair to the Stock hashmap
              //so value has Part ArrayList with 10 elements (For qty); if Part ArrayList empty then out of inventory
              Stock.put(b.getId(), tempParts);
            }
            break;

            case "Exterior":
            Exterior initialE = new Exterior(j, "0", 0.0, "0", false, 0);
            tempParts.add(initialE);
            //if tag is Exterior, loop through all Exterior tags and store in tempArray List
            for (int i = 0; i < Integer.parseInt(e.getElementsByTagName("qty").item(0).getTextContent()); i++) {
              //create a exterior object of the xml content
              Exterior ext = new Exterior(j, e.getAttribute("id"), Double.parseDouble(e.getElementsByTagName("Cost").item(0).getTextContent()), e.getElementsByTagName("color").item(0).getTextContent(), false, Integer.parseInt(e.getElementsByTagName("cycles").item(0).getTextContent()));
              //store that exterior part in the temp ArrayList
              tempParts.add(ext);
              // add the key value pair to the Stock hashmap - key is "Exterior1" "Exterior2"
              Stock.put("Exterior" + ext.getId(), tempParts);
            }
            break;

            case "Interior":
            Interior initialI = new Interior(j, "0", 0.0, "0", "0", false, 0);
            tempParts.add(initialI);
            //if tag is Interior, loop through all Interior tags and store in tempArray List
            for (int i = 0; i < Integer.parseInt(e.getElementsByTagName("qty").item(0).getTextContent()); i++) {
              //create a Interior object of the xml content
              Interior interiorPart = new Interior(j, e.getAttribute("id"), Double.parseDouble(e.getElementsByTagName("Cost").item(0).getTextContent()), e.getElementsByTagName("color").item(0).getTextContent(), e.getElementsByTagName("material").item(0).getTextContent(), false, Integer.parseInt(e.getElementsByTagName("cycles").item(0).getTextContent()));
              //store that Interior part in the temp ArrayList
              tempParts.add(interiorPart);
              // add the key value pair to the Stock hashmap - key is "Exterior1" "Exterior2"
              Stock.put("Interior" + interiorPart.getId(), tempParts);
            }
            break;

            case "Powertrain":
            Powertrain initialP = new Powertrain(j, "0", 0.0, false, false, 0);
            tempParts.add(initialP);
            for (int i = 0; i < Integer.parseInt(e.getElementsByTagName("qty").item(0).getTextContent()); i++) {
              Powertrain p;
              p = new Powertrain(j, e.getAttribute("id"), Double.parseDouble(e.getElementsByTagName("Cost").item(0).getTextContent()), Boolean.parseBoolean(e.getElementsByTagName("auto").item(0).getTextContent()), false, Integer.parseInt(e.getElementsByTagName("cycles").item(0).getTextContent()));
              tempParts.add(p);
              Stock.put("Powertrain" + p.getId(), tempParts);
            }
            break;

            case "Seat":
            Seat initialS = new Seat(j, "0", 0.0, "0", false, 0);
            tempParts.add(initialS);
            for (int i = 0; i < Integer.parseInt(e.getElementsByTagName("qty").item(0).getTextContent()); i++) {
              Seat s;
              s = new Seat(j, e.getAttribute("id"), Double.parseDouble(e.getElementsByTagName("Cost").item(0).getTextContent()), e.getElementsByTagName("sType").item(0).getTextContent(), false, Integer.parseInt(e.getElementsByTagName("cycles").item(0).getTextContent()));
              tempParts.add(s);
              Stock.put("Seat" + s.getId(), tempParts);
            }
            break;
            case "Radio":
            Radio initialR = new Radio(j, "0", 0.0, "0", false, 0);
            tempParts.add(initialR);
            for (int i = 0; i < Integer.parseInt(e.getElementsByTagName("qty").item(0).getTextContent()); i++) {
              Radio r;
              r = new Radio(j, e.getAttribute("id"), Double.parseDouble(e.getElementsByTagName("Cost").item(0).getTextContent()), e.getElementsByTagName("console").item(0).getTextContent(), false, Integer.parseInt(e.getElementsByTagName("cycles").item(0).getTextContent()));
              tempParts.add(r);
              Stock.put("Radio" + r.getId(), tempParts);
            }
            break;
            case "Rim":
            Rim initialRim = new Rim(j, "0", 0.0, 0, false, 0);
            tempParts.add(initialRim);
            for (int i = 0; i < Integer.parseInt(e.getElementsByTagName("qty").item(0).getTextContent()); i++) {
              Rim rims;
              rims = new Rim(j, e.getAttribute("id"), Double.parseDouble(e.getElementsByTagName("Cost").item(0).getTextContent()), Integer.parseInt(e.getElementsByTagName("size").item(0).getTextContent()), false, Integer.parseInt(e.getElementsByTagName("cycles").item(0).getTextContent()));
              tempParts.add(rims);
              Stock.put("Rim" + rims.getId(), tempParts);
            }
            break;
            case "Tire":
            Tire initialT = new Tire(j, "0", 0.0, "0", false, 0);
            tempParts.add(initialT);
            for (int i = 0; i < Integer.parseInt(e.getElementsByTagName("qty").item(0).getTextContent()); i++) {
              Tire tires;
              tires = new Tire(j, e.getAttribute("id"), Double.parseDouble(e.getElementsByTagName("Cost").item(0).getTextContent()), e.getElementsByTagName("tType").item(0).getTextContent(), false, Integer.parseInt(e.getElementsByTagName("cycles").item(0).getTextContent()));
              tempParts.add(tires);
              Stock.put("Tire" + tires.getId(), tempParts);
            }
            break;

            case "Misc":            
            Misc initialM = new Misc(j, "initial", 0.0, 0, false, 0);
            tempParts.add(initialM);
            //Stock.put("Misc" + (t+1), ingestStockMisc(e ,t, document));          
            ingestStockMisc(e ,t, document);
            break;

            default:
            //throw excpetion
            throw new PartNotFoundException(j);
            //case that throws part not found exception!
          }
        }
      }  //end for looping through stock arraylist
    } catch (Exception e) {
      System.out.println(e);
    }
  }  //end ingestStock

  /*
  * Input: Element e, int t, Document
  * Return: ArrayList<Part>
  * Description: Reads from stock.xml file and builds SupplyDump attribute Stock with current misc stock levels
  */
 // public static ArrayList<Part> ingestStockMisc(Element e, int t, Document document){
    public static void ingestStockMisc(Element e, int t, Document document){
    //ArrayList<Part> p = new ArrayList<Part>();
    Misc misc = null;
    
    String[][] miscOptions = {{"DrivingAssist", "dType"}, {"Roof", "rType"}, {"Backup", "bType"}, {"Sensor", "sType"}};
    //String[] miscOptions = {"test1", "test2"};
            //t is the current misc node calling function is on; t = 0,1,2,3 for misc 4 options
            switch (t){
                case 0:  // driving assist misc option indexed at 0
                    //create nodelist of driving assist
                    NodeList nList = document.getElementsByTagName("DrivingAssist");        
                    
                    //loop through this nodelist
                    for (int y = 0; y<nList.getLength(); y++){
                        Node node = nList.item(y);
                        Element temp = (Element) node;
                        
                        //initiate ArrayList<Part> for each id in Misc1-Driving Assist
                        ArrayList<Part> p = new ArrayList<Part>();
                        
                        //insert empty driving assist for option check and stock inventory checks
                        Misc initialD = new DrivingAssist("DrivingAssist", "0", 0.0, 0, "0", false, 0);
                        p.add(initialD);
                         //loop through all inventory of each driving assist option
                        for (int z = 0; z <Integer.parseInt(temp.getElementsByTagName("qty").item(0).getTextContent()); z++){
                            //create a misc object of current element
                            misc = new DrivingAssist("DrivingAssist", temp.getAttribute("id"), Double.parseDouble(temp.getElementsByTagName("Cost").item(0).getTextContent()), Integer.parseInt(e.getAttribute("index")), temp.getElementsByTagName("dType").item(0).getTextContent(), false, Integer.parseInt(e.getElementsByTagName("cycles").item(0).getTextContent()));
                            
                            //add it to the part array
                            p.add(misc);
                            
                            //add array to stock
                            Stock.put("DrivingAssist" + (t+1) + misc.getId(), p);  
                           } //end for  
                    } // end for
                    break;
                case 1:   //roof -  misc option indexed a 1                   
                    NodeList mList = document.getElementsByTagName("Roof");        
                    //loop through this nodelist
                    for (int y = 0; y<mList.getLength(); y++){
                        Node node = mList.item(y);
                        Element temp = (Element) node;
                        ArrayList<Part> p = new ArrayList<Part>();
                        Misc initialR = new Roof("Roof", "0", 0.0, 0, "0", false, 0);
                        p.add(initialR);
                        for (int z = 0; z <Integer.parseInt(temp.getElementsByTagName("qty").item(0).getTextContent()); z++){
                         misc = new Roof("Roof", temp.getAttribute("id"), Double.parseDouble(temp.getElementsByTagName("Cost").item(0).getTextContent()), Integer.parseInt(e.getAttribute("index")), temp.getElementsByTagName("rType").item(0).getTextContent(), false, Integer.parseInt(e.getElementsByTagName("cycles").item(0).getTextContent()));
                            p.add(misc);
                            Stock.put("Roof" + (t+1) + misc.getId(), p);  
                           } //end for
                    } // end for
                    break;
                
                case 2:  //Backup - misc option indexed at 2 
                    NodeList bList = document.getElementsByTagName("Backup");        
                    //loop through this nodelist
                    for (int y = 0; y<bList.getLength(); y++){
                        Node node = bList.item(y);
                        Element temp = (Element) node;
                        ArrayList<Part> p = new ArrayList<Part>();
                        Misc initialB = new Backup("Backup", "0", 0.0, 0, "0", false, 0);
                        p.add(initialB);
                        for (int z = 0; z <Integer.parseInt(temp.getElementsByTagName("qty").item(0).getTextContent()); z++){
                         misc = new Backup("Backup", temp.getAttribute("id"), Double.parseDouble(temp.getElementsByTagName("Cost").item(0).getTextContent()), Integer.parseInt(e.getAttribute("index")), temp.getElementsByTagName("bType").item(0).getTextContent(), false, Integer.parseInt(e.getElementsByTagName("cycles").item(0).getTextContent()));
                            p.add(misc);
                            Stock.put("Backup" + (t+1) + misc.getId(), p);  
                           } //end for
                    } // end for
                    break;
                    
                case 3: //Backup - misc option indexed at 2 
                    NodeList sList = document.getElementsByTagName("Sensor");        
                    //loop through this nodelist
                    for (int y = 0; y<sList.getLength(); y++){
                        Node node = sList.item(y);
                        Element temp = (Element) node;
                        ArrayList<Part> p = new ArrayList<Part>();
                        Misc initialS = new Sensor("Sensor", "0", 0.0, 0, "0", false, 0);
                        p.add(initialS);
                        for (int z = 0; z <Integer.parseInt(temp.getElementsByTagName("qty").item(0).getTextContent()); z++){
                         misc = new Sensor("Sensor", temp.getAttribute("id"), Double.parseDouble(temp.getElementsByTagName("Cost").item(0).getTextContent()), Integer.parseInt(e.getAttribute("index")), temp.getElementsByTagName("sType").item(0).getTextContent(), false, Integer.parseInt(e.getElementsByTagName("cycles").item(0).getTextContent()));
                            p.add(misc);
                            Stock.put("Sensor" + (t+1) + misc.getId(), p);  
                           } //end for
                    } // end for
                    break;
                            
            } //end switch
        //return p;
    }  //end ingestMiscStock


  /*
  * Input: string oid number
  * Return: string array of oid# formatted
  * Description:  Reads int for config file and formats the oid number
  * Reference:  Joshua Aklin
  */
  public static String[] pidFormat(String oid) {
    //since all companies have same structure for PID# - just going to return string array
    String[] oidNumberFormatted = new String[10];


    try {
      //check if oid number valid
      validInput(oid);
      
        //store digit in oid#
        char vehicleTypeChar = oid.charAt(4);
    
      //first element of oid array is oid number (regardless of vehicle make)
      oidNumberFormatted[0] = oid.substring(0, 4);

      //determine config file identifier to format pid number
      switch (vehicleTypeChar) {
        case '1':
        //ThisAuto structure
        //second element of oid array is model number 4 digits
        oidNumberFormatted[1] = oid.substring(4, 8);

        //last elements are single digits for options
        for (int i = 2; i < 9; i++) {
          //start at oidNumberFormatted element and grab char at i+6 to start at options digits
          oidNumberFormatted[i] = Character.toString(oid.charAt(i + 6));
        }
        
        //last element is 2 digit misc number
        oidNumberFormatted[9] = oid.substring(15);
        break;

        case '2':
        //ThatAuto structure
        //second element of oid array is model number 6 digits
        oidNumberFormatted[1] = oid.substring(4, 10);
        //elements 3-9 are single digits
        for (int i = 2; i < 9; i++) {
          //start at oidNumberFormatted element and grab char at i+8 to start at options digits
          oidNumberFormatted[i] = Character.toString(oid.charAt(i + 8));
        }
        //last element is 2 digit misc number
        oidNumberFormatted[9] = oid.substring(17);
        break;

        case '3':
        //OtherAuto structure
        //second element of oid array is model number 4 digits
        oidNumberFormatted[1] = oid.substring(4, 8);

        //last elements are single digits for options
        for (int i = 2; i < 9; i++) {
          //start at oidNumberFormatted element and grab char at i+6 to start at options digits
          oidNumberFormatted[i] = Character.toString(oid.charAt(i + 6));
        }
        //last element is 2 digit misc number
        oidNumberFormatted[9] = oid.substring(15);
        break;
        default:
        //throw MakeNotFound Exception (should've been thrown in validInput
        //so throw general exception

        // }  //end switch for car manufactor
      }  //end else for valid oid number
    }  //end try
    //catch if there are any non-digits in PID#
    catch (StringException e) {
      System.out.println(e.getMessage());
      //call request
      try{
      SupplyDump.request();
    } //if 1,2,3 not inputted as first digit, catch and still request input
    
    catch(OIDFileNotFoundException f){
            System.out.println(f.getMessage());
    }
    }
    catch (MakeNotFoundException e) {
      System.out.println(e.getMessage());
      
try{//call request
      SupplyDump.request();
}
catch(OIDFileNotFoundException f){
            System.out.println(f.getMessage());
    }
    } //if specific make doesn't have correct length PID #
    catch (MakeFormatException e) {
      System.out.println(e.getMessage());
     try{ //call request
      SupplyDump.request();
     }
     catch(OIDFileNotFoundException f){
            System.out.println(f.getMessage());
    }
    } //if first digit of PID doesn't identify 1 of car types
    catch (ModelNotFoundException e) {
      System.out.println(e.getMessage());
      try{//call request
      SupplyDump.request();
      }
      catch(OIDFileNotFoundException f){
            System.out.println(f.getMessage());
    }
    } //last catch block
    catch (Exception e) {
      System.out.println(e.getMessage());
      System.exit(0);
    }

    return oidNumberFormatted;
  }  //end pidFormat method


  /*
  * Input: String user entered OID#
  * Return: none
  * Description: Checks that user's OID # is valid.
  * The boolean return determines if the user should be prompted again
  */
  public static void validInput(String id) throws StringException,
  MakeNotFoundException,
  MakeFormatException,
  ModelNotFoundException {

    char vehicleTypeChar;
    String makeInput = "";
    
    //first check length of oid
    if (id.length()<17){
        //throw exception
        throw new MakeNotFoundException();
    }

    //loop through characters of pid input and check if is a digit
    for (int i = 0; i < id.length(); i++) {
      //check if each character of pid string is a digit
      if (!Character.isDigit(id.charAt(i))) {
        throw new StringException();
      }
    }

    //check first digit of PID# for config file
    if (id.charAt(4) == '1') {
      //check pid for ThisAuto is correct length of 12 digits
      if (id.length() != 17) {
        //throw exception
        throw new MakeFormatException(17);
      }
      makeInput = "ThisAuto";
    } //end outer if
    else if (id.charAt(4) == '2') {

      //check pid for ThatAuto is correct length of 15 digits
      if (id.length() != 19) {
        //throw exception
        throw new MakeFormatException(19);
      }
      makeInput = "ThatAuto";
    } //end else if
    else if (id.charAt(4) == '3') {
      //check pid for ThatAuto is correct length of 15 digits
      if (id.length() != 17) {
        //throw exception
        throw new MakeFormatException(17);
      }
      makeInput = "OtherAuto";
    } //end else if
    else {
      //throw MakeNotFound Exception
      throw new MakeNotFoundException();
    }

    //now check validity of vehicle type
    //get second digit of PID#
    vehicleTypeChar = id.charAt(5);

    //throw ModelNotFoundExcept for just character-element 2 in pid input number
    if ((vehicleTypeChar != '1') && (vehicleTypeChar != '2') && (vehicleTypeChar != '3')
    && (vehicleTypeChar != '4') && (vehicleTypeChar != '5')) {

      //throw overload constructor exception given first character
      throw new ModelNotFoundException(Character.toString(vehicleTypeChar), makeInput);
    }
  }  //end validInput method
}  //end supplyDump class
