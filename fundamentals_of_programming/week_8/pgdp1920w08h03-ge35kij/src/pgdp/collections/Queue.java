package pgdp.collections;

public interface Queue<T> extends DataStructure {
    void enqueue(T x);

    default T dequeue() {
        return null;
    }

    T peek();
}