/* COMPSCI 424 Program 3
 * Name: Ryan Ames
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
import java.util.Random;
import java.util.Scanner;
import java.math.*;

/**
 * Main class for this program. To help you get started, the major steps for the
 * main program are shown as comments in the main method. Feel free to add more
 * comments to help you understand your code, or for any reason. Also feel free
 * to edit this comment to be more helpful.
 */
public class Program3 {
	// Declare any class/instance variables that you need here.
		private static int numResources;
		private static int numProcesses;
		private static int[][] max;
		private static int[][] allocation;
		private static int[] available;
		private static int[][] need;

		/**
		 * @param args Command-line arguments.
		 * 
		 *             args[0] should be a string, either "manual" or "auto".
		 * 
		 *             args[1] should be another string: the path to the setup file that
		 *             will be used to initialize your program's data structures. To
		 *             avoid having to use full paths, put your setup files in the
		 *             top-level directory of this repository. - For Test Case 1, use
		 *             "424-p3-test1.txt". - For Test Case 2, use "424-p3-test2.txt".
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
				} else {
					numResources = Integer.parseInt(currentLine.split(" ")[0]);
					System.out.println(numResources + " resources");
				}

				// Get number of processes
				currentLine = setupFileReader.readLine();
				if (currentLine == null) {
					System.err.println("Cannot find number of processes, exiting.");
					setupFileReader.close();
					return;
				} else {
					numProcesses = Integer.parseInt(currentLine.split(" ")[0]);
					System.out.println(numProcesses + " processes");
				}

				// Create the Banker's Algorithm data structures, in any
				// way you like as long as they have the correct size
				max = new int[numProcesses][numResources];
				allocation = new int[numProcesses][numResources];
				available = new int[numResources];
				need = new int[numProcesses][numResources];
				int currentProc = 0;
				String section = "";
				// 3. Use the rest of the setup file to initialize the
				// data structures
				while ((currentLine = setupFileReader.readLine()) != null) {
					if (currentLine.equals("Available") || currentLine.equals("Max") || currentLine.equals("Allocation")) { // check
																															// for
																															// a
																															// new
																															// section
						section = currentLine;
						continue;
					}
					switch (section) {
					case "Available":
						String[] availableResources = currentLine.split(" ");
						for (int i = 0; i < numResources; i++) {
							available[i] = Integer.parseInt(availableResources[i]);
						}
						break;// end available
					case "Max":
						String[] maxResources = currentLine.split(" ");
						for (int i = 0; i < numResources; i++) {
							max[currentProc][i] = Integer.parseInt(maxResources[i]);
						}
						currentProc++; // next process
						break;

					case "Allocation":
						String[] allocationResources = currentLine.split(" ");
						for (int i = 0; i < numResources; i++) {
							allocation[currentProc][i] = Integer.parseInt(allocationResources[i]);
							// update need array
							need[currentProc][i] = max[currentProc][i] - allocation[currentProc][i];
						}
					default: // should never get here
						break;
					}
				}

				// Print the arrays to debug
				System.out.println("Available resources:");
				for (int i = 0; i < numResources; i++) {
					System.out.print(available[i] + " ");
				}
				System.out.println();

				System.out.println("Max matrix:");
				for (int i = 0; i < numProcesses; i++) {
					for (int j = 0; j < numResources; j++) {
						System.out.print(max[i][j] + " ");
					}
					System.out.println();
				}

				System.out.println("Allocation matrix:");
				for (int i = 0; i < numProcesses; i++) {
					for (int j = 0; j < numResources; j++) {
						System.out.print(allocation[i][j] + " ");
					}
					System.out.println();
				}

				System.out.println("Need matrix:");
				for (int i = 0; i < numProcesses; i++) {
					for (int j = 0; j < numResources; j++) {
						System.out.print(need[i][j] + " ");
					}
					System.out.println();
				}
				setupFileReader.close(); // done reading the file, so close it
			} catch (IOException e) {
				System.err.println(
						"Something went wrong while reading setup file " + args[1] + ". Stack trace follows. Exiting.");
				e.printStackTrace(System.err);
				System.err.println("Exiting.");
				return;
			}

			// 4. Check initial conditions to ensure that the system is
			// beginning in a safe state: see "Check initial conditions"
			// in the Program 3 instructions
			boolean isSafe = true;

			// check allocation <= max for all Ps and Rs
			for (int p = 0; p < numProcesses; p++) {
				for (int r = 0; r < numResources; r++) {
					if (allocation[p][r] > max[p][r]) {
						isSafe = false;
						System.err.println("Invalid initial allocation: Process " + p
								+ " allocated more than its max for resource " + r);
					}
				}
			}

			int[] totalResources = new int[numResources];
			for (int r = 0; r < numResources; r++) {
				for (int p = 0; p < numProcesses; p++) {
					totalResources[r] += allocation[p][r];
				}
				totalResources[r] += available[r];
			}

			for (int r = 0; r < numResources; r++) {
				if (available[r] != totalResources[r]) {
					isSafe = false;
					System.err.println(
							"Invalid initial allocation: Total available resources do not match the sum of allocations available for resource " + r);
				}
			}
			if (!safeStateCheck(allocation, available, need, numProcesses, numResources)) {
				isSafe = false;
				System.err.println("System is not in a safe state.");
			}
			if (!isSafe) {
				System.err.println("Initial conditions are not valid. Exiting.");
			}

			// 5. Go into either manual or automatic mode, depending on
			// the value of args[0]; you could implement these two modes
			// as separate methods within this class, as separate classes
			// with their own main methods, or as additional code within
			// this main method.
			if (args[0].equals("manual")) {
				manualMode(max, allocation, available, numProcesses, numResources);
			} else if (args[0].equals("automatic")) {
				automaticMode(max, allocation, available, numProcesses, numResources);
			} else {
				System.err.println("Invalid mode specified. Exiting");
				return;
			}

		}

		private static void automaticMode(int[][] max,int[][] allocation, int [] available, int numProcesses, int numResources) {
			//create the threads
	        Thread[] threads = new Thread[numProcesses];
			
			for (int i = 0; i < numProcesses; i++) {
				int processID = i;
				threads[i] = new Thread(() -> {
					Random random = new Random();
					
					for (int j = 0; j < 3; j++) { //perfor 3 requests and 3 releases per process
						int resource = random.nextInt(numResources);
						int requestAmount = random.nextInt(max[processID][resource] + 1);
						
						requestResource(processID, resource, requestAmount, max, allocation, available);
						
						resource = random.nextInt(numResources);
	                    int releaseAmount = random.nextInt(allocation[processID][resource] + 1);
	                    
	                    releaseResource(processID, resource, releaseAmount, allocation, available);
					}
				});
			}
			for (Thread thread : threads) {
				thread.start();
			}
			for (Thread thread : threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Automatic task complete");
		}// end auto mode
	    private static synchronized void requestResource(int processID, int resource, int requestAmount, int[][] max, int[][] allocation, int[] available) {
	        if (requestAmount <= available[resource] && requestAmount <= max[processID][resource] - allocation[processID][resource]) {// request check
	        	allocation[processID][resource] += requestAmount;
	        	available[resource] -= requestAmount;
	        	
	            if (!safeStateCheck(allocation, available, need, numProcesses, numResources)) {
	            	//if not safe, undo
	            	allocation[processID][resource] -= requestAmount;
	                available[resource] += requestAmount;
	                System.out.println("Process " + processID + " requests " + requestAmount + " units of resource " + resource + ": denied");
	                System.out.println("Request denied: Unsafe state.");
	            }
	            else {
	                System.out.println("Process " + processID + " requests " + requestAmount + " units of resource " + resource + ": granted");
	            }
	        }
	        else {
	            System.out.println("Process " + processID + " requests " + requestAmount + " units of resource " + resource + ": denied");
	        }
	    }
	    private static synchronized void releaseResource(int processID, int resource, int releaseAmount, int[][] allocation, int[] available) {
	    	if (releaseAmount <= allocation[processID][resource]) {
	    		allocation[processID][resource] -= releaseAmount;
	            available[resource] += releaseAmount;
	            System.out.println("Process " + processID + " releases " + releaseAmount + " units of resource " + resource);
	        } else {
	            System.out.println("Process " + processID + " releases " + releaseAmount + " units of resource " + resource);
	        }
	    }
		
		private static void manualMode(int[][] max, int[][] allocation, int[] available, int numProcesses,
				int numResources) {
			Scanner scanner = new Scanner(System.in);
			boolean endManualMode = false;

			while (!endManualMode) {
				System.out.println("Enter command (request/release/end):");
				String command = scanner.nextLine();
				switch (command.toLowerCase()) {
				case "request":
					System.out.println("Enter request (I J K):");
					String[] requestParts = scanner.nextLine().split(" ");
					int procID = Integer.parseInt(requestParts[0]);
					int resourceID = Integer.parseInt(requestParts[1]);
					int requestedAmount = Integer.parseInt(requestParts[2]);

					// handle bad requests
					if (procID < 0 || procID >= numProcesses || resourceID < 0 || resourceID >= numResources) {
						System.out.println("Invalid process or resource ID.");
						break;
					}
					if (requestedAmount < 0 || requestedAmount > max[procID][resourceID]) {
						System.out.println("Invalid request amount.");
						break;
					}

					if (requestedAmount > available[resourceID]) {
						System.out.println("Insufficient resources available.");
						break;
					}
					// allocate resources
					allocation[procID][resourceID] += requestedAmount;
					available[resourceID] -= requestedAmount;

					System.out.println("Resource request granted.");
					break;

				case "release":
	                System.out.println("Enter release (I J K):");
	                String[] releaseParts = scanner.nextLine().split(" ");
	                int releaseprocessID = Integer.parseInt(releaseParts[0]);
	                int releaseResourceId = Integer.parseInt(releaseParts[2]);
	                int releaseAmount = Integer.parseInt(releaseParts[1]);

	                // handle bad requests
	                if (releaseprocessID < 0 || releaseprocessID >= numProcesses || releaseResourceId < 0 || releaseResourceId >= numResources) {
	                    System.out.println("Invalid process or resource ID.");
	                    break;
	                }

	                if (releaseAmount < 0 || releaseAmount > allocation[releaseprocessID][releaseResourceId]) {
	                    System.out.println("Invalid release amount.");
	                    break;
	                }

	                // release resources
	                allocation[releaseprocessID][releaseResourceId] -= releaseAmount;
	                available[releaseResourceId] += releaseAmount;

	                System.out.println("Resource release successful.");
	                break;
				case "end":
					endManualMode = true;
					break;

				default:
					System.out.println("Invalid command.");

				}
			}
		}// end manualMode

		private static boolean safeStateCheck(int[][] allocation, int[] available, int[][] need, int numProcesses,
				int numResources) {
			int[] work = new int[numResources];
			boolean[] finish = new boolean[numProcesses];

			// initialize arrays
			for (int i = 0; i < numResources; i++) {
				work[i] = available[i];
			}
			for (int i = 0; i < numProcesses; i++) {
				finish[i] = false;
			}
			int count = 0;
			while (count < numProcesses) {
				boolean found = false;
				for (int p = 0; p < numProcesses; p++) {
					if (!finish[p]) {
						boolean canAllocate = true;
						for (int r = 0; r < numResources; r++) {
							if (need[p][r] > work[r]) {
								canAllocate = false;
							}
						}
						if (canAllocate) {
							for (int r = 0; r < numResources; r++) {
								work[r] += allocation[p][r];
							}
							finish[p] = true;
							found = true;
							count++;
						}
					}
				}
				if (!found) {// could not find process
					return false;
				}
			}
			return true;
		}
	}
