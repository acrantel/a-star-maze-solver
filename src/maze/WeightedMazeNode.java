package maze;

import java.awt.Point;

public class WeightedMazeNode extends MazeNode implements Comparable<WeightedMazeNode> {
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
