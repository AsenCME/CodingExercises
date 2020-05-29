package pgdp.collections;

public interface DataStructure {
    int size();

    default boolean isEmpty() {
        return size() == 0;
    }
}