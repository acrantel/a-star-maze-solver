package maze;

public interface MazeSolver {
	public boolean isSolvable();
	public void reset();
	public void step();
}
