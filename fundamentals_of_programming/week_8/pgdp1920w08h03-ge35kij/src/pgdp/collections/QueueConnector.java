package pgdp.collections;

public class QueueConnector<T> implements DataStructureConnector<T> {

    private Queue<T> queue;

    public static void main(String[] args) {
        LinkedStack<Integer> a = new LinkedStack<Integer>(2);
        a.toString();
    }

    public QueueConnector(Queue<T> _queue) {
        queue = _queue;
    }

    @Override
    public boolean hasNextElement() {
        return queue.peek() != null;
    }

    @Override
    public void addElement(T x) {
        queue.enqueue(x);
    }

    @Override
    public T removeNextElement() {
        if (!hasNextElement())
            return null;
        return queue.dequeue();
    }

}