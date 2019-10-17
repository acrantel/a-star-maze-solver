package maze;

public class Test {
    public static void main(String[] args) {
        Maze m = new Maze("src/test.txt");
        System.out.println(m.toString());
    }
}