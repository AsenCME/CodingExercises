package gad.doublehashing;

public class StringHashableFactory implements HashableFactory<String> {

    @Override
    public DoubleHashable<String> create(int size) {
        return new DoubleHashString(size);
    }

}
