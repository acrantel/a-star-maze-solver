package solver;

import java.awt.Point;

import adt.Agenda;
import adt.MyQueue;
import adt.MyStack;
import maze.Maze;
import maze.MazeNode;
import maze.Square;
import solver.MazeSolver;

public class AgendaMazeSolver implements MazeSolver {
    /** Constant that specifies use of a stack-based solver */
    public static final char STACK = 's';

    /** Constant that specifies use of a queue-based solver */
    public static final char QUEUE = 'q';
    private Maze maze;
    
    // state variables for the step() function
    /** Whether the maze has been solved before. This is true if solve() has
     * been called or if step() has been called until terminated is true */
    private boolean solvedBefore;
    /** The last node of the solution, which stores the full solution in a linked
     * list form. If finalNode is null and solvedBefore is true, then there was 
     * no solution to the maze */
    private MazeNode finalNode;
    // state variables for the step function
    /** Whether we are done stepping through the solver */
    private boolean terminated;

    /** Agenda of locations to explore. */
    private Agenda<MazeNode> agenda;
    /** The mazes should be relatively small, so we use a 2d boolean array
     * to store whether a location has been visited. visited[i][j] 
     * corresponds to row i, column j. */
    private boolean[][] visited;
    
    /**
     * Initializes a agenda based solver
     * @param maze The maze to solve
     * @param base The type of agenda to use as a base for the solver (stack, queue, etc)
     */
    public AgendaMazeSolver(Maze maze, char base) {
        this.maze = maze;
        if (base == STACK) {
            agenda = new MyStack<MazeNode>();
        } else if (base == QUEUE) {
            agenda = new MyQueue<MazeNode>();
        }
        reset();
    }

    /**
     * Adds each of the neighboring nodes to the agenda of locations. 
     * @param node the node to find neighbors of
     */
    private void addNeighbors(MazeNode node) {
        if (node == null || visited.length == 0 || visited[0].length == 0) {
            return;
        }
        for (int[] offset : ADJACENTS) {
            int newCol = node.getCol() + offset[0];
            int newRow = node.getRow() + offset[1];
            if (newCol < maze.getWidth() && newCol >= 0 &&
                    newRow < maze.getHeight() && newRow >= 0 &&
                    !visited[newRow][newCol] && maze.at(newRow, newCol) != Square.WALL) {
                agenda.add(new MazeNode(maze.at(newRow, newCol), 
                        new Point(newCol, newRow), node));
            }
        }
    }

    /**
     * @return Whether the maze solver is done.
     */
    public boolean doneStepping() {
        return terminated;
    }

    @Override
    public String getName() {
        if (agenda instanceof MyStack) {
            return "Stack-based Maze Solver";
        } else if (agenda instanceof MyQueue) {
            return "Queue-based Maze Solver";
        } else {
            return "Agenda Maze Solver";
        }
    }

    @Override
    /** Returns the solution to the maze, or null if there is no solution.
     */
    public MazeNode getSolution() {
        if (solvedBefore) {
            return finalNode;
        } else {
            solve();
            return finalNode;
        }
    }

    @Override
    /** Returns a string representation of the maze with the solution path 
     * marked by "s". If the maze wan't solvable, just return a string 
     * representation of the maze with no solution path.
     */
    public String getSolutionString() {
        // get the initial string representation of the maze, without the path
        String result = "";
        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                result += maze.at(row, col).toString();
            }
            result += "\n";
        }
        
        // ignore the first and last nodes of the solution, because those should 
        // have finish and start symbols in the maze
        MazeNode node = getSolution();
        if (node != null) {
            node = node.getPrevious();
            while (node != null && node.getPrevious() != null) {
                // replace the character at this node with an "s", accounting for newline chars
                int charLoc = node.getRow() * (maze.getWidth()+1) + node.getCol();
                result = result.substring(0, charLoc)
                        + "s" + result.substring(charLoc+1);
                node = node.getPrevious();
            }
        }
        return result;
    }

    @Override
    /** Returns a string representation of the state of the maze solver, with
     * "v" representing visited squares. */
    public String getSolverState() {
        String result = "";
        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                Square square = maze.at(row, col);
                if (square == Square.START || square == Square.FINISH
                        || !visited[row][col]) {
                    result += maze.at(row, col).toString();
                } else {
                    result += "v";
                }
            }
            result += "\n";
        }
        return result;
    }

    @Override
    /** Resets the solver stepper by clearing the agenda of locations and 
     * resetting each square's visited status */
    public void reset() {
        if (agenda instanceof MyStack) {
            agenda = new MyStack<MazeNode>();
        } else {
            agenda = new MyQueue<MazeNode>();
        }
        visited = new boolean[maze.getHeight()][maze.getWidth()];
        for (int r = 0; r < visited.length; r++) {
            for (int c = 0; c < visited[r].length; c++) {
                visited[r][c] = false;
            }
        }
        // find the start location and add it to the agenda
        agenda.add(new MazeNode(Square.START, maze.getStart(), null));
        terminated = false;
    }
    
    @Override
    /** Solves the maze if it hasn't been solved before. 
     * Postcondition: The solver will still be at the same step of the algorithm as
     * it was before the call to solve().
     * @return Whether the maze was solvable or not
     */
    public boolean solve() {
        if (!solvedBefore) {
            // store the current state so we can go back to it later
            boolean prevTerminated = terminated;
            Agenda<MazeNode> prevAgenda = agenda;
            boolean[][] prevVisited = visited;
            reset();
            while (!doneStepping()) {
                step();
            }
            terminated = prevTerminated;
            agenda = prevAgenda;
            visited = prevVisited;
        }
        return finalNode != null;
    }
    
    @Override
    public void step() {
        if (terminated) { return; }
        // remove elements until we find one that is unvisited
        while (!agenda.isEmpty() && visited[agenda.peek().getLocation().y][agenda.peek().getLocation().x]) {
            agenda.remove();
        }
        // if agenda is empty, then all locations in it have been visited, 
        // so the maze is unsolvable    
        if (agenda.isEmpty()) {
            terminated = true;
            solvedBefore = true;
            finalNode = null;
            return;
        }

        MazeNode location = agenda.remove();
        visited[location.getRow()][location.getCol()] = true;
        if (location.getType() == Square.FINISH) {
            terminated = true;
            solvedBefore = true;
            finalNode = location;
        } else {
            addNeighbors(location);
        }

    }
}
