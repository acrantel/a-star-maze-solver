package maze;

import java.awt.Point;

/**
 * Class used to store a node in the path to the finish. 
 */
public class MazeNode {
    private Square type;
    private Point location;
    private MazeNode previous;
    
    /** Takes the square type, location in the maze, and the previous 
     * node (null if this is the starting node) */
    public MazeNode(Square type, Point location, MazeNode previous) {
        this.type = type;
        this.location = location;
        this.previous = previous;
    }
    
    public Square getType() {
        return type;
    }
    public Point getLocation() {
        return location;
    }
    public int getRow() {
        return location.y;
    }
    public int getCol() {
        return location.x;
    }
    /** Returns the previous node */
    public MazeNode getPrevious() {
        return previous;
    }
    
    
}
