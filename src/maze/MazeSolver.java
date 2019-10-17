package maze;

public interface MazeSolver {
	public void solve();
	public MazeNode getSolved();
	public void reset();
	public void step();
}
