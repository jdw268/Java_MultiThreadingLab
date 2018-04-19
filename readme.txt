IMPORTANT:  READ THIS FILE BEFORE USING SupplyDump

Project:
SupplyDump - Lab 6

Description:
SupplyDump project takes in a user's input for a specific order of parts listed in a text file.  
This OID number is matched against a stock.xml file (that's imported into the program prior)
of the available models, options, and prices.  The OID number is ran
against the xml file options to build a specified parts invoice and output the user's
selections and associated costs.  The vehicle options are:  Sedan, Coupe, Truck, SUV, and Minivan.
Custom exceptions are built-in to let the user know specific formats for an OID number.
The invoice is outputted to screen as well as a .txt file.

Next, the program processes the orders as if a vehicle was being built in a factoring. Each order's
part is processed through the associated station(s) using threads.

Prerequisites:
minimum Java version 8 update 91

Files List:
to run: 
Manifest.mf
IrvinLab6.jar
stock.xml
SupplyDump.class
SupplyExceptions.class
Part.class
Manufactoring.class
Order.class
Station.class
oids.txt
stock.xml


to build:
SupplyDump.java
Part.java
SupplyExceptions.java
Manufactoring.java
Order.java
Part.java
Sation.java
stock.xml
oids.txt

Build Instructons:
Compile and run on the command line using the JDK compiler.  After extracting
the files to a folder, you can compile with javac *.java then run the program
using command java SupplyDump.  Make sure to include the stock.xml and oids.txt files in the
folder structure of the program.

Run instructions:
If you have java runtime environment, use the java -jar IrvinLab6.jar to run the program.
Make sure to include the stock.xml and oids.txt files in the folder structure of the program.

Authors:
Jillian Irvin
SupplyDump v2
