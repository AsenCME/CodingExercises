package gad.doublehashing;

import java.util.HashMap;
import java.util.Random;

public class DoubleHashString implements DoubleHashable<String> {

    private int size;

    public DoubleHashString(int size) {
        this.size = size;
    }

    private int[] getScalar(int len, int seed, int max) {
        int[] a = new int[len];
        Random rand = new Random(seed);
        for (int i = 0; i < len; i++)
            a[i] = rand.nextInt(max) + 1;
        return a;
    }

    @Override
    public long hash(String key) {
        long result = 0;
        var a = getScalar(key.length(), 7, this.size);
        for (int i = 0; i < key.length(); i++)
            result = (result + key.charAt(i) * a[i]) % this.size;
        return Math.abs(result % this.size);
    }

    @Override
    public long hashTick(String key) {
        long result = 0;
        var a = getScalar(key.length(), 254386, this.size);
        for (int i = 0; i < key.length(); i++)
            result = result + key.charAt(i) * a[i];

        long res1 = Math.abs(result % this.size);
        return res1 == 0 ? rehash(key) : res1;
    }

    private long rehash(String key) {
        int sum = 0;
        for (char c : key.toCharArray())
            sum += c;
        Random rand = new Random(sum);
        int value = rand.nextInt();
        int temp = this.size - 1;
        return temp - Math.abs(value % temp);
    }

    public static void main(String[] args) {
        var hasher = new DoubleHashString(20);

        var map1 = new HashMap<Long, Integer>();
        var map2 = new HashMap<Long, Integer>();

        for (int i = 1; i <= 100; i++) {
            var part1 = hasher.hash(String.valueOf(i * 6));
            var part2 = hasher.hashTick(String.valueOf(i * 6));

            map1.put(part1, map1.getOrDefault(part1, 0) + 1);
            map2.put(part2, map2.getOrDefault(part2, 0) + 1);
        }

        System.out.println(map1.size());
        System.out.println(map2.size());
    }
}
