package pgdp.robot;

public class Memory<T> {
    private final String label;
    private T data;

    public Memory(String label, T data) {
        this.label = label;
        this.data = data;
    }

    public T get() {
        return data;
    }

    public void set(T data) {
        this.data = data;
    }

    public String toString() {
        return String.format("%s: %s", this.label, this.data);
    }

    public static void main(String[] args) {
        var test = new Memory<Integer>("yo", 42);
        System.out.println(test.toString());
    }
}
