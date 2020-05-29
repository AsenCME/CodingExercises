package pgdp.collections;

public class LinkedStack<T> implements Stack<T> {

    private List<T> list;

    public LinkedStack() {
        list = null;
    }

    public LinkedStack(T x) {
        list = new List<T>(x);
    }

    @Override
    public int size() {
        if (list == null)
            return 0;
        return list.length();
    }

    @Override
    public void push(T x) {
        if (list == null)
            list = new List<T>(x);
        else
            list.append(x);
    }

    @Override
    public T pop() {
        if (isEmpty())
            return null;

        if (list.length() == 1) {
            T res = list.getInfo();
            list = null;
            return res;
        }

        List<T> temp = list;
        while (temp.getNext().getNext() != null)
            temp = temp.getNext();
        T res = temp.getNext().getInfo();
        temp.delete();
        return res;
    }

    @Override
    public T peek() {
        T last = pop();
        if (last == null)
            return null;
        push(last);
        return last;
    }

    @Override
    public String toString() {
        if (list == null)
            return "[]";
        return list.toString();
    }

}