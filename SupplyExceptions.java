/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jill
 */
public class SupplyExceptions {
    public SupplyExceptions(){}
}

/*
* Input: none
* Return: none
* Description: Exception thrown stock.xml file not found or unable to open
*/
class StockFileNotFoundException extends Exception{

	//constructor for exception class
	public StockFileNotFoundException(String s){
		//error message displayed for this time of exception thrown
		//pass string to super so set message as default error message
		super("Stock file " + s + " is not found");
	}
}  //end StockFileNotFoundException class

        
        /*
* Input: none
* Return: none
* Description: Exception thrown stock.xml file not found or unable to open
*/
class OIDFileNotFoundException extends Exception{

	//constructor for exception class
	public OIDFileNotFoundException(String s){
		//error message displayed for this time of exception thrown
		//pass string to super so set message as default error message
		super("Stock file " + s + " is not found");
	}
}  //end OIDFileNotFoundException class
/*
* Input: none
* Return: none
* Description: Exception thrown stock.xml file not found or unable to open
*/
class PartNotFoundException extends Exception{

	//constructor for exception class
	public PartNotFoundException(String id){
		//error message displayed for this time of exception thrown
		//pass string to super so set message as default error message
		super("Part " + id + " is not defined within SupplyDump.");
	}
}  //end StringException class

/*
* Input: none
* Return: none
* Description: Exception thrown if the user input contains anything other than integers.
*/
class StringException extends Exception{

	//constructor for exception class
	public StringException(){
		//error message displayed for this time of exception thrown
		//pass string to super so set message as default error message
		super("Input must contain only numbers, not letters.");
	}
}  //end StringException class


/*
* Input: none
* Return: none
* Description: Exception thrown if the car make ID is not defined
*/
class MakeNotFoundException extends Exception{

	//constructor for exception class
	public MakeNotFoundException(){
		//error message displayed for this time of exception thrown
		//pass string to super so set message as default error message
		super("Vehicle company not found.");
	}
}  //end MakeNotFoundException class

/*
* Input: none
* Return: none
* Description: Exception thrown the input pid length does not match the Car company's length
*/
class MakeFormatException extends Exception{
	//instance variable
	//private int length;

	//constructor for exception class
	public MakeFormatException(int lengthPID){
		//error message displayed for this time of exception thrown
		//pass string to super so set message as default error message
		//length = lengthPID
		super("The Car's Product ID must have a length of " + lengthPID + " numbers");
	}
}  //end MakeFormatException class

/*
* Input: none
* Return: none
* Description: Exception thrown if the vehicle model does not exist
*/
class ModelNotFoundException extends Exception{

	//constructor for exception class
	public ModelNotFoundException(String inputID, String carCompany){
		//error message displayed for this time of exception thrown
		//pass string to super so set message as default error message
		super("Model ID: " + inputID + " does not exist for " + carCompany + ".");
	}

	// //overload constructor for beginning of user input
	// public ModelNotFoundException(int inputID, String carCompany){
	// 	//error message displayed for this time of exception thrown
	// 	//pass string to super so set message as default error message
	// 	super("Model ID: " + inputID + " does not exist for " + carCompany + ".");
	// }

}  //end ModelNotFoundException class

/*
* Input: none
* Return: none
* Description: Exception thrown if option id does not exist with option index
*/
class OptionNotFoundException extends Exception{

	//constructor for exception class
	public OptionNotFoundException(String inputID, String option, String listOfOptions){
		//error message displayed for this time of exception thrown
		//pass string to super so set message as default error message
		super("Option ID: " + inputID + " does not exist for " + option +
		", only options available are: " + listOfOptions);
	}
}  //end OptionNotFoundException class

/*
* Input: none
* Return: none
* Description: Exception thrown if option id does not exist with option index
*/
class PartIdNotDefinedException extends Exception{

	//constructor for exception class
	public PartIdNotDefinedException(String part, String id){
		//error message displayed for this time of exception thrown
		//pass string to super so set message as default error message
		super("Part " + part + " with id " + id + " is not defined within our stock.");
	}    
}  //end PartIdNotDefinedException class

/*
* Input: none
* Return: none
* Description: Exception thrown if option id does not exist with option index
*/
class PartNotDefinedException extends Exception{

	//constructor for exception class
	public PartNotDefinedException(String part){
		//error message displayed for this time of exception thrown
		//pass string to super so set message as default error message
		super("Part " + part + " is not defined within our stock.");
	}    
}  //end PartIdNotDefinedException class

/*
* Input: none
* Return: none
* Description: Exception thrown if part is out of stock
*/
class OutOfStockException extends Exception{

	//constructor for exception class
	public OutOfStockException(String part){
		//error message displayed for this time of exception thrown
		//pass string to super so set message as default error message
		super("Part " + part + " is out of stock.");
	}    
}  //end PartIdNotDefinedException class