package gad.doublehashing;

import java.util.*;

public class DoubleHashTable<K, V> {

    private int size, maxRehashes, collisions;
    private DoubleHashable<K> hasher;
    private List<Pair<K, V>> table;

    public DoubleHashTable(int primeSize, HashableFactory<K> hashableFactory) {
        maxRehashes = 0;
        collisions = 0;
        size = primeSize;
        initializeTable();
        hasher = hashableFactory.create(this.size);
    }

    private void initializeTable() {
        table = new ArrayList<Pair<K, V>>(this.size);
        for (int i = 0; i < this.size; i++)
            table.add(null);
    }

    public int hash(K key, int i) {
        return (int) ((hasher.hash(key) + i * hasher.hashTick(key)) % this.size);
    }

    public boolean insert(K k, V v) {
        int index = hash(k, 0);
        var pair = table.get(index);

        if (pair == null || pair.getOne().equals(k)) {
            table.set(index, new Pair<>(k, v));
            return true;
        }

        collisions++;
        for (int i = 1; i < this.size; i++) {
            index = hash(k, i);
            pair = table.get(index);

            if (pair == null || pair.getOne().equals(k)) {
                if (i > maxRehashes)
                    maxRehashes = i;
                table.set(index, new Pair<>(k, v));
                return true;
            }
        }

        return false;
    }

    public Optional<V> find(K k) {
        for (int i = 0; i < this.size; i++) {
            int index = hash(k, i);
            var pair = table.get(index);
            if (pair != null && pair.getOne().equals(k))
                return Optional.of(pair.getTwo());
        }
        return Optional.empty();
    }

    public int collisions() {
        return collisions;
    }

    public int maxRehashes() {
        return maxRehashes;
    }

    public static void main(String[] args) {
        var fact = new HashableFactory<Integer>() {
            @Override
            public DoubleHashable<Integer> create(int size) {
                return new DoubleHashInt(size);
            }
        };

        var table = new DoubleHashTable<Integer, String>(13, fact);
        Optional<String> res = null;
        for (int i = 0; i < 26; i++)
            table.insert(i, String.valueOf(i));

        System.out.println(table);
    }
}
