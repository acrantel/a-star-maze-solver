package solver;

import maze.MazeNode;

public interface MazeSolver {
    /** Solves the maze and returns whether it was solvable or not */
	public boolean solve();
	/** Returns the solution node of the linked list, or null if there is no solution */
	public MazeNode getSolution();
	
	/** Returns to the first step of the solver */
	public void reset();
	/** Completes one step of the solver */
	public void step();
	/** Returns if we are done stepping through */
	public boolean doneStepping();
	
	/** Returns the name of the solver */
	public String getName();
	/** Returns a "graphical" representation of the current state of the 
	 * maze solver. */
	public String getSolverState();
	/** Returns a textual representation of the solution to the maze */
	public String getSolutionString();
	
	/** Hard coded possible adjacent locations */
    public static final int[][] ADJACENTS = { {0, 1}, {1, 0}, {0, -1}, {-1, 0} };
}
