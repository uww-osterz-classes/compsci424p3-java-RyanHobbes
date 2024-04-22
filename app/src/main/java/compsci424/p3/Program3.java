/* COMPSCI 424 Program 3
 * Name:
 * 
 * This is a template. Program3.java *must* contain the main class
 * for this program. 
 * 
 * You will need to add other classes to complete the program, but
 * there's more than one way to do this. Create a class structure
 * that works for you. Add any classes, methods, and data structures
 * that you need to solve the problem and display your solution in the
 * correct format.
 */

package compsci424.p3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Main class for this program. To help you get started, the major
 * steps for the main program are shown as comments in the main
 * method. Feel free to add more comments to help you understand
 * your code, or for any reason. Also feel free to edit this
 * comment to be more helpful.
 */
public class Program3 {
    // Declare any class/instance variables that you need here.

    /**
     * @param args Command-line arguments. 
     * 
     * args[0] should be a string, either "manual" or "auto". 
     * 
     * args[1] should be another string: the path to the setup file
     * that will be used to initialize your program's data structures. 
     * To avoid having to use full paths, put your setup files in the
     * top-level directory of this repository.
     * - For Test Case 1, use "424-p3-test1.txt".
     * - For Test Case 2, use "424-p3-test2.txt".
     */
    public static void main(String[] args) {
        // Code to test command-line argument processing.
        // You can keep, modify, or remove this. It's not required.
        if (args.length < 2) {
            System.err.println("Not enough command-line arguments provided, exiting.");
            return;
        }
        System.out.println("Selected mode: " + args[0]);
        System.out.println("Setup file location: " + args[1]);

        // 1. Open the setup file using the path in args[1]
        String currentLine;
        BufferedReader setupFileReader;
        try {
            setupFileReader = new BufferedReader(new FileReader(args[1]));
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find setup file at " + args[1] + ", exiting.");
            return;
       }

        // 2. Get the number of resources and processes from the setup
        // file, and use this info to create the Banker's Algorithm
        // data structures
        int numResources;
        int numProcesses;

        // For simplicity's sake, we'll use one try block to handle
        // possible exceptions for all code that reads the setup file.
        try {
            // Get number of resources
            currentLine = setupFileReader.readLine();
            if (currentLine == null) {
                System.err.println("Cannot find number of resources, exiting.");
                setupFileReader.close();
                return;
            }
            else {
                numResources = Integer.parseInt(currentLine.split(" ")[0]);
                System.out.println(numResources + " resources");
            }
 
            // Get number of processes
            currentLine = setupFileReader.readLine();
            if (currentLine == null) {
                System.err.println("Cannot find number of processes, exiting.");
                setupFileReader.close();
                return;
            }
            else {
                numProcesses = Integer.parseInt(currentLine.split(" ")[0]);
                System.out.println(numProcesses + " processes");
            }

            // Create the Banker's Algorithm data structures, in any
            // way you like as long as they have the correct size
            int [] available = new int [numResources];
            int [][] max = new int [numProcesses][numResources];
            int [][] allocation = new int[numProcesses][numResources];
            int [][] need = new int[numProcesses][numResources];
            int currentProc = 0;
            String section = "";
            // 3. Use the rest of the setup file to initialize the
            // data structures
            while ((currentLine = setupFileReader.readLine()) != null) {
            	if (currentLine.equals("Available") || currentLine.equals("Max") || currentLine.equals("Allocation")) { // check for a new section
            		section = currentLine;
            		continue;
            	}
            	switch (section) {
            		case "Available":
            				String[] availableResources = currentLine.split(" ");
            		case "Max":
            			
            		case "Allocation":
            			
            			default: // should never get here
            				break;
            	}
            }

            setupFileReader.close(); // done reading the file, so close it
        }
        catch (IOException e) {
            System.err.println("Something went wrong while reading setup file "
            + args[1] + ". Stack trace follows. Exiting.");
            e.printStackTrace(System.err);
            System.err.println("Exiting.");
            return;
        }

        // 4. Check initial conditions to ensure that the system is 
        // beginning in a safe state: see "Check initial conditions"
        // in the Program 3 instructions

        // 5. Go into either manual or automatic mode, depending on
        // the value of args[0]; you could implement these two modes
        // as separate methods within this class, as separate classes
        // with their own main methods, or as additional code within
        // this main method.


    }
}
