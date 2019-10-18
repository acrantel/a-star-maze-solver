package maze;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class MazeApp {
	private static Maze maze;
	
	/** Starts the program */
	public static void start() {
	    System.out.println("---- MAZE SOLVER ----");
	    Scanner scan = new Scanner(System.in);
	    
	    while (true) {
	        printOptions();
	        char input = getCharInput(scan);
	        if (input == 'a') {
	            loadMaze(scan);
	        } else if (input == 'e') {
                break;
            } else if (maze != null) {
	            // if maze is not null, then we can solve it
	            switch (input) {
	            case 'b': 
	                startSolver(scan, new AgendaMazeSolver(maze, AgendaMazeSolver.STACK)); 
	                break;
	            case 'c': 
	                startSolver(scan, new AgendaMazeSolver(maze, AgendaMazeSolver.QUEUE));
	                break;
	            case 'd': //TODO add a* solver
	                break;
	            }
	        } else {
	            System.out.println("Error: Invalid input.");
	        }
	    }
	    scan.close();
	}
	
	public static void printOptions() {
	    System.out.println("Options:");
	    System.out.println("a. Load file");
	    System.out.println("b. Start stack based maze solver");
	    System.out.println("c. Start queue based maze solver");
	    System.out.println("d. Start A* maze solver");
	    System.out.println("e. Quit");
	}
	
	/** Loads the maze from a file the user specifies into the Maze class 
	 * variable. Prints an error message if loading the maze fails.
	 */
	public static void loadMaze(Scanner scan) {
	    System.out.print("Enter the file name: ");
	    String fileName = scan.nextLine().trim();
	    maze = new Maze(fileName);
	    if (!maze.isInitialized()) {
	        // if we couldn't initialize the maze, print an error
	        // message and set maze to null
	        System.out.println("Error: Maze initialization failed");
	        maze = null;
	    }
	}
	
	public static void startSolver(Scanner scan, MazeSolver solver) {
	    System.out.println("Starting " + solver.getName());
	    // user can either step through the solver or get the solution right away 
	    System.out.println("Options: ")
	    //TODO finish writing function
	}

    /**
     * Returns the input from the scanner, or the null character if there was no input.
     * @param scan The scanner to get input from
     * @return The inputed character
     */
    public static char getCharInput(Scanner scan) {
        String input = scan.nextLine().trim();
        if (input.length() > 0) {
            return input.charAt(0);
        } else {
            return (char) 0;
        }
    }
}
