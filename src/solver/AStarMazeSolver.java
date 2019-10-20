package solver;

import java.awt.Point;
import java.util.PriorityQueue;

import maze.Maze;
import maze.MazeNode;
import maze.Square;
import maze.WeightedMazeNode;

public class AStarMazeSolver implements MazeSolver {
    /** The maze to solve */
    private Maze maze;
    
    /** Whether the maze has been solved before. This is true if solve() has
     * been called or if step() has been called until terminated is true */
    private boolean solvedBefore;
    
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
                        node.getMovementCost()+1, heuristic(newRow, newCol)));
            }
        }
        // if node is a teleporter, add the other teleporter to the agenda
        if (maze.isTeleporter(node.getRow(), node.getCol())) {
            Point[] teleporters = maze.getTeleporters();
            Point newLoc = teleporters[0].equals(node.getLocation()) ? teleporters[1] : teleporters[0];
            queue.add(new WeightedMazeNode(maze.at(newLoc.y, newLoc.x),
                    new Point(newLoc.x, newLoc.y), node,
                    node.getMovementCost()+1, heuristic(newLoc.y, newLoc.x)));
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
     * marked by 's'. Teleportations are marked by the 't' instead. If the 
     * maze wan't solvable, just return a string representation of the maze 
     * with no solution path. */
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
                result = result.substring(0, charLoc) +
                        (maze.isTeleporter(node.getRow(), node.getCol()) ? "t" : "s") +
                        result.substring(charLoc+1);
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
    
    /**
     * Estimated movement cost to move from the passed in location to 
     * the final destination.
     * @param row The row of the current location
     * @param col The column of the current location
     * @return The estimated cost.
     */
    private int heuristic(int row, int col) {
        // if this calculation was more resource intensive we 
        // could add a memoization table
        Point curPt = new Point(col, row);
        int result = getManhattanDist(maze.getFinish(), curPt);
        if (maze.getTeleporters() != null) {
            // estimate the distance if we used teleporters
            Point[] teles = maze.getTeleporters();
            // the distance of the closer teleporter to our current position
            int distToCurPos = Math.min(getManhattanDist(teles[0], curPt),
                    getManhattanDist(teles[1], curPt));
            // shortest distance from a teleporter to the finish 
            int distToFinish = Math.min(getManhattanDist(teles[0], maze.getFinish()),
                    getManhattanDist(teles[1], maze.getFinish()));
            int totalTeleDist = distToCurPos + distToFinish + 1;
            if (totalTeleDist < result) {
                result = totalTeleDist;
            }
        }
        return result >= 8 ? 8 : result;
        
    }
    
    /** Returns the Manhattan distance between two points (the sum of the 
     * differences of their coordinates) */
    private int getManhattanDist(Point a, Point b) {
        return Math.abs(a.x - b.x)+ Math.abs(a.y - b.y); 
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
