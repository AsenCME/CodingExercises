package pgdp.collections;

public class StackConnector<T> implements DataStructureConnector<T> {

    private final Stack<T> stack;

    public StackConnector(Stack<T> _stack) {
        stack = _stack;
    }

    @Override
    public boolean hasNextElement() {
        return stack.peek() != null;
    }

    @Override
    public void addElement(T x) {
        stack.push(x);
    }

    @Override
    public T removeNextElement() {
        if (!hasNextElement())
            return null;
        return stack.pop();
    }
}