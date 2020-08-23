package gad.doublehashing;

import java.util.HashMap;
import java.util.HashSet;

public class DoubleHashInt implements DoubleHashable<Integer> {

    private int size;

    public DoubleHashInt(int size) {
        this.size = size;
    }

    @Override
    public long hash(Integer key) {
        return Math.abs(key % this.size);
    }

    @Override
    public long hashTick(Integer key) {
        int temp = this.size - 1;
        return temp - Math.abs(key % temp);
    }

    public static void main(String[] args) {
        var size = 100;

        var map1 = new HashMap<Long, Integer>();
        var map2 = new HashMap<Long, Integer>();

        var hashing = new DoubleHashInt(size);
        for (int i = 0; i < 100; i++) {
            var part1 = hashing.hash(i * 27);
            var part2 = hashing.hashTick(i * 27);

            map1.put(part1, map1.getOrDefault(part1, 0) + 1);
            map2.put(part2, map2.getOrDefault(part2, 0) + 1);
        }

        System.out.println(map1.size());
        System.out.println(map2.size());
    }

}
