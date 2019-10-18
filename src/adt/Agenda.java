package adt;

public interface Agenda<T>
{
    public boolean isEmpty();
    public int size();
    public void add(T t);
    public T remove();
    public T peek();
}