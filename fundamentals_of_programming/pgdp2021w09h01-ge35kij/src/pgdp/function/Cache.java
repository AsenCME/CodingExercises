package pgdp.function;

import java.util.LinkedHashMap;

public class Cache<K, V> extends LinkedHashMap<K, V> {

    // Generated for me by VSCode
    private static final long serialVersionUID = 1098026826763848813L;
    private final int maximalCacheSize;

    public Cache(int size) {
        super(size * 10 / 7, 0.7f, true);
        this.maximalCacheSize = size;
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return super.size() > this.maximalCacheSize;
    }

    int getMaximalCacheSize() {
        return this.maximalCacheSize;
    }

}
