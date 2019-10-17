package maze;
import java.util.Stack;

public class MyStack<T> implements Agenda<T> {
	private Stack<T> baseStack;
	public MyStack() {
		baseStack = new Stack<T>();
	}
	
	@Override
	public boolean isEmpty() {
		return baseStack.isEmpty();
	}
	@Override
	public int size() {
		return baseStack.size();
	}
	@Override
	public void add(T t) {
		baseStack.add(t);
	}
	@Override
	public T remove() {
		return baseStack.pop();
	}
	@Override
	public T peek() {
		return baseStack.peek();
	}
	
	
}