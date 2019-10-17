package maze;

import java.awt.Point;

public class StackMazeSolver implements MazeSolver {
    private Maze maze;

    /** Agenda of locations to explore. */
    private MyStack<MazeNode> agenda;
    /** The mazes should be relatively small, so we use a 2d boolean array
     * to store whether a location has been visited. visited[i][j] 
     * corresponds to row i, column j. */
    private boolean[][] visited;
    
    /** Whether the algorithm has terminated */
    private boolean terminated;
    /** Whether the maze was solvable. Only use this variable if terminated is true. */
    private boolean solvable;
    /** The last node of the solution, which stores the full solution in a linked
     * list form. To get the full solution, call getPrevious() until the starting
     * node is returned. */
    private MazeNode finalNode;
    
    /** Hard coded possible adjacent locations */
    private static final int[][] ADJACENTS = { {0, 1}, {1, 0}, {0, -1}, {-1, 0} };
    
    public StackMazeSolver(Maze maze) {
        this.maze = maze;
        reset();
    }

    @Override
    /** 
     * Postcondition: The solver will still be at the same step of the algorithm as
     * it was before the call to solve()
     */
    public void solve() {
        if (finalNode != null) {
            return;
        }
        //TODO
        
    }
    
    @Override
    /** Resets the solver by clearing the agenda of locations and 
     * resetting each square's visited status */
    public void reset() {
        agenda = new MyStack<MazeNode>();
        visited = new boolean[maze.getWidth()][maze.getHeight()];
        for (int r = 0; r < visited.length; r++) {
            for (int c = 0; c < visited[r].length; c++) {
                visited[r][c] = false;
            }
        }
        // find the start location and add it to the agenda
        agenda.add(new MazeNode(Square.START, maze.getStart(), null));
        terminated = false;
        solvable = false;
        finalNode = null;
    }

    @Override
    public void step() {
        if (terminated) { return; }
        // remove elements until we find one is unvisited
        while (!agenda.isEmpty() && visited[agenda.peek().getLocation().x][agenda.peek().getLocation().y]) {
            agenda.remove();
        }
        // if agenda is empty, then all locations in it have been visited, so the maze is unsolvable    
        if (agenda.isEmpty()) {
            terminated = true;
            solvable = false;
            return;
        }
        
        MazeNode location = agenda.remove();
        if (location.getType() == Square.FINISH) {
            terminated = true;
            solvable = true;
            finalNode = location;
        } else {
            addNeighbors(location);
        }
        
    }
    
    /**
     * @return Whether the maze solver is done.
     */
    public boolean isTerminated() {
        return terminated;
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
            if (newCol >= visited[0].length || newCol < 0 ||
                    newRow >= visited.length || newRow < 0 ||
                    visited[newRow][newCol]) {
                agenda.add(new MazeNode(maze.at(newRow, newCol), 
                        new Point(newRow, newCol), node));
            }
        }
    }

    @Override
    public MazeNode getSolved() {
        // TODO Auto-generated method stub
        return null;
    }
}
