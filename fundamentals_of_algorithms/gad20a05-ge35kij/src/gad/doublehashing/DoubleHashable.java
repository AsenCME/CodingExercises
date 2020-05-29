package gad.doublehashing;

public interface DoubleHashable<K> {

    public long hash(K key);

    public long hashTick(K key);
}
