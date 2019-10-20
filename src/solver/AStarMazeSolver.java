package solver;

import java.awt.Point;
import java.util.PriorityQueue;

import maze.Maze;
import maze.MazeNode;
import maze.Square;

public class AStarMazeSolver implements MazeSolver {
    class WeightedMazeNode extends MazeNode implements Comparable<WeightedMazeNode> {
        /** The cost to move to this node */
        private int movementCost;
        /** The estimated cost to move from  */
        private int heuristic;
        
        public WeightedMazeNode(Square type, Point location, MazeNode previous,
                int movementCost, int heuristic) {
            super(type, location,previous);
            this.movementCost = movementCost;
            this.heuristic = heuristic;
        }
        
        public int getTotalWeight() {
            return movementCost + heuristic;
        }
        
        public int getMovementCost() {
            return movementCost;
        }
        
        public int getHeuristic() {
            return heuristic;
        }

        @Override
        public int compareTo(WeightedMazeNode n) {
            return this.getTotalWeight() - n.getTotalWeight(); 
        }
    }
    
    private Maze maze;
    /** Whether the maze has been solved before. This is true if solve() has
     * been called or if step() has been called until terminated is true */
    private boolean solvedBefore;
    
    // state variables for the step() function
    /** The last node of the solution, which stores the full solution in a linked
     * list form. If finalNode is null and solvedBefore is true, then there was 
     * no solution to the maze */
    private WeightedMazeNode finalNode;
    // state variables for the step function
    /** Whether we are done stepping through the solver */
    private boolean terminated;
    /** Priority queue of locations to explore */
    private PriorityQueue<WeightedMazeNode> queue;
    
    /** The mazes should be relatively small, so we use a 2d boolean array
     * to store whether a location has been visited. visited[i][j] 
     * corresponds to row i, column j. */
    private boolean[][] visited;
    
    /** Initializes an a star based maze solver
     * @param maze The maze to solve.
     */
    public AStarMazeSolver(Maze maze) {
       this.maze = maze;
       queue = new PriorityQueue<WeightedMazeNode>();
       reset();
    }

    /**
     * Adds each of the neighboring nodes to the agenda of locations. 
     * @param node the node to find neighbors of
     */
    private void addNeighbors(WeightedMazeNode node) {
        if (node == null || visited.length == 0 || visited[0].length == 0) {
            return;
        }
        for (int[] offset : ADJACENTS) {
            int newCol = node.getCol() + offset[0];
            int newRow = node.getRow() + offset[1];
            if (newCol < maze.getWidth() && newCol >= 0 &&
                    newRow < maze.getHeight() && newRow >= 0 &&
                    !visited[newRow][newCol] && maze.at(newRow, newCol) != Square.WALL) {
                queue.add(new WeightedMazeNode(maze.at(newRow, newCol),
                        new Point(newCol, newRow), node,
                        node.getMovementCost(), heuristic(newRow, newCol)));
            }
        }
    }
    
    @Override
    public boolean doneStepping() {
        return terminated;
    }
    
    @Override
    public String getName() {
        return "A* Maze Solver";
    }

    @Override
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
     * representation of the maze with no solution path. */
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
    

    private int heuristic(int row, int col) {
        //TODO
        return 0;
    }

    @Override
    public void reset() {
        queue = new PriorityQueue<WeightedMazeNode>();
        visited = new boolean[maze.getHeight()][maze.getWidth()];
        for (int r = 0; r < visited.length; r++) {
            for (int c = 0; c < visited[r].length; c++) {
                visited[r][c] = false;
            }
        }
        // find the start location and add it to the queue
        queue.add(new WeightedMazeNode(Square.START, maze.getStart(), null, 
                0, heuristic(maze.getStart().y, maze.getStart().x)));
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
            PriorityQueue<WeightedMazeNode> prevAgenda = queue;
            boolean[][] prevVisited = visited;
            reset();
            while (!doneStepping()) {
                step();
            }
            terminated = prevTerminated;
            queue = prevAgenda;
            visited = prevVisited;
        }
        return finalNode != null;
    }
    
    @Override
    public void step() {
        if (terminated) { return; }
        // remove elements until we find one that is unvisited
        while (!queue.isEmpty() && visited[queue.peek().getLocation().y][queue.peek().getLocation().x]) {
            queue.remove();
        }

        // if queue is empty, then all reachable locations have been visited, 
        // so the maze is unsolvable    
        if (queue.isEmpty()) {
            terminated = true;
            solvedBefore = true;
            finalNode = null;
            return;
        }
        
        WeightedMazeNode location = queue.remove();
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
