package maze;
import java.util.LinkedList;
import java.util.Queue;

public class MyQueue<T> implements Agenda<T> {
    private Queue<T> baseQueue;
    public MyQueue() {
        baseQueue = new LinkedList<T>();
    }

    public boolean isEmpty() {
        return baseQueue.isEmpty();
    }

    public int size() {
        return baseQueue.size();
    }

    public void add(T t) {
        baseQueue.add(t);
    }

    public T remove() {
        return baseQueue.remove();
    }

    public T peek() {
        return baseQueue.peek();
    }

}