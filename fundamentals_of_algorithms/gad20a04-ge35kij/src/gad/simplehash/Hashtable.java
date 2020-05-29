package gad.simplehash;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Hashtable<K, V> {
    private int size;
    private final int[] a;
    private List<Pair<K, V>>[] table;

    @SuppressWarnings("unchecked")
    public Hashtable(int minSize, int[] a) {
        this.size = getNextPowerOfTwo(minSize);
        this.a = a;
        table = (List<Pair<K, V>>[]) new List[size];
        for (int i = 0; i < this.size; i++)
            this.table[i] = new ArrayList<Pair<K, V>>();
    }

    public List<Pair<K, V>>[] getTable() {
        return table;
    }

    public static int getNextPowerOfTwo(int i) {
        int log2 = (int) Math.ceil(Math.log(i) / Math.log(2));
        return (int) Math.pow(2, log2);
    }

    public static int fastModulo(int i, int divisor) {
        return i & (divisor - 1);
    }

    private byte[] bytes(K k) {
        return k.toString().getBytes();
    }

    public int h(K k, ModuloHelper mH) {
        byte[] x = bytes(k);
        int index = 0;
        for (int i = 0; i < x.length; i++)
            index += x[i] * a[i % a.length];
        return fastModulo(index, this.size);
    }

    public int bitwiseAdd(int a, int b) {
        return 2 * (a & b) + (a ^ b);
    }

    public void insert(K k, V v, ModuloHelper mH) {
        this.table[h(k, mH)].add(new Pair<>(k, v));
    }

    public boolean remove(K k, ModuloHelper mH) {
        int index = h(k, mH);
        var sizeBefore = this.table[index].size();
        this.table[index].removeIf(p -> p.getOne().equals(k));
        var diff = sizeBefore - this.table[index].size();
        return diff != 0 ? true : false;
    }

    public Optional<V> find(K k, ModuloHelper mH) {
        int index = h(k, mH);
        var pairs = this.table[index];
        for (Pair<K, V> pair : pairs)
            if (pair.getOne().equals(k))
                return Optional.of(pair.getTwo());

        return Optional.empty();
    }

    public List<V> findAll(K k, ModuloHelper mH) {
        int index = h(k, mH);
        var pairs = this.table[index];
        return pairs.stream().filter(p -> p.getOne().equals(k)).map(p -> p.getTwo()).collect(Collectors.toList());
    }

    public int collisions() {
        return Stream.of(table).filter(ps -> ps != null)
                .mapToInt(ps -> Math.max(0, (int) ps.stream().map(Pair::getOne).distinct().count() - 1)).sum();
    }

    public Stream<Pair<K, V>> stream() {
        return Stream.of(table).filter(ps -> ps != null).flatMap(List::stream);
    }

    public Stream<K> keys() {
        return stream().map(Pair::getOne).distinct();
    }

    public Stream<V> values() {
        return stream().map(Pair::getTwo);
    }

    public static void main(String[] args) {
        Hashtable<String, Integer> hashtable = new Hashtable<>(64, new int[] { 1, 2 });
        hashtable.insert("BG", 1000, null);
        hashtable.insert("BG", 2000, null);
        hashtable.insert("IT", 2000, null);
        hashtable.insert("DE", 2000, null);
        hashtable.insert("DE", 2000, null);
        boolean res = hashtable.remove("IT", null);
        var auto = hashtable.find("BG", null);
        var all = hashtable.findAll("DE", null);
    }
}
