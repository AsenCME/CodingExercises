package pgdp.collections;

public class LinkedQueue<T> implements Queue<T> {

    private List<T> list;

    public LinkedQueue() {
        list = null;
    }

    public LinkedQueue(T x) {
        list = new List<T>(x);
    }

    @Override
    public int size() {
        if (list == null)
            return 0;
        return list.length();
    }

    @Override
    public void enqueue(T x) {
        if (list == null)
            list = new List<T>(x);
        else
            list.append(x);
    }

    @Override
    public T dequeue() {
        if (list.length() == 1) {
            T res = list.getInfo();
            list = null;
            return res;
        }

        T res = peek();
        list = list.getNext();
        return res;
    }

    @Override
    public T peek() {
        if (list == null)
            return null;
        return list.getInfo();
    }

    @Override
    public String toString() {
        if (list == null)
            return "[]";
        return list.toString();
    }

}