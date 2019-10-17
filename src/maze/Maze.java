package maze;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Maze {
    /** Stores the layout of this maze so that layout[i][j] corresponds 
     * to row i, column j */
    private Square[][] layout;

    public Maze(String fileName) {
    	if (!init(fileName)) {
    		System.out.println("Initialization failed");
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
	            }
	        }
    	} catch (Exception e) {
    		return false;
    	} finally {
    		if (scan != null) { scan.close(); }
    	}
    	return true;
    }

    public String toString() {
        if (layout == null || layout.length == 0 || layout[0].length == 0) {
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
}