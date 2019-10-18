package maze;
import java.util.Scanner;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;

public class Maze {
    /** Stores the layout of this maze so that layout[i][j] corresponds 
     * to row i, column j */
    private Square[][] layout;
    
    // start and finish of the maze
    private Point start;
    private Point finish;
    
    /** If the maze has been successfully initialized */
    private boolean initialized;

    public Maze(String fileName) {
    	if (!init(fileName)) {
    		initialized = false;
    	} else {
    	    initialized = true;
    	}
    }
    
    /**
     * Initializes the maze given the name of an input file. The input file
     * should have the dimensions of the maze (width and height) in the first
     * line, and subsequent lines should each contain one row of the maze, with
     * each character representing one square.
     * @param fileName The name of the file to read in.
     * @return Whether initialization was successful.
     */
    private boolean init(String fileName) {
    	Scanner scan = null;
    	try {
	        scan = new Scanner(new File(fileName));
	        // get dimensions of the maze
	        String[] strDimensions = scan.nextLine().trim().split(" ");
	        int width = Integer.parseInt(strDimensions[0]);
	        int height = Integer.parseInt(strDimensions[1]);
	        layout = new Square[height][width];
	        
	        // read in each row of the maze and add it to layout
	        String row;
	        for (int r = 0; r < height; r++) {
	            row = scan.nextLine().trim();
	            for (int c = 0; c < width; c++) {
	                layout[r][c] = Square.fromChar(row.charAt(c));
	                // if the current location is a start or end, store it 
	                // now so we don't have to calculate it later
	                if (layout[r][c] == Square.START) {
	                    start = new Point(r, c);
	                } else if (layout[r][c] == Square.FINISH) {
	                    finish = new Point(r, c);
	                }
	            }
	        }
    	} catch (Exception e) {
    	    // possible exceptions: FileNotFoundException, IllegalArgumentException
    	    layout = null;
    	    start = null;
    	    finish = null;
    		return false;
    	} finally {
    		if (scan != null) { scan.close(); }
    	}
    	return true;
    }

    public String toString() {
        if (!initialized || layout == null || layout.length == 0 || layout[0].length == 0) {
            return "0 0\n";
        }
        String result = layout.length + " " + layout[0].length + "\n";
        for (Square[] row : layout) {
            for (Square square : row) {
                result += square.toString();
            }
            result += "\n";
        }
        return result;
    }
    
    /** Returns the Square at the specified row and column */
    public Square at(int row, int col) {
        if (initialized) {
            return layout[row][col];
        } else {
            return null;
        }
    }
    
    /** Returns the width (# of columns) of this Maze. */
    public int getWidth() {
    	return !initialized || layout == null? 0 : layout.length;
    }
    
    /** Returns the height (# of rows) of this Maze. */
    public int getHeight() {
    	return !initialized || layout == null 
    	        || layout.length == 0 || layout[0] == null ? 0 : layout[0].length; 
    }
    
    /** Returns the location of the start of the maze in the format (row, col).  */
    public Point getStart() {
        return start;
    }
    
    /** Returns the location of the end of the maze in the format (row, col */
    public Point getFinish() {
        return finish;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
}