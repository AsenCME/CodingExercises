package gad.doublehashing;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;

public class DoubleHashString implements DoubleHashable<String> {

    private int size;
    private long seed1 = -7865816549737130316L; // frozen islands MC seed
    private long seed2 = 6073041046072376055L; // triple island ocean monument

    public DoubleHashString(int size) {
        this.size = size;
    }

    private int[] getScalar(int len, boolean tick) {
        int[] a = new int[len];
        long seed = tick ? this.seed2 : this.seed1;
        Random rand = new Random(seed);
        for (int i = 0; i < len; i++)
            a[i] = rand.nextInt(this.size);
        return a;
    }

    @Override
    public long hash(String key) {
        long result = 0;
        var a = getScalar(key.length(), false);
        for (int i = 0; i < key.length(); i++)
            result = (result + key.charAt(i) * a[i]) % this.size;
        return Math.abs(result % this.size);
    }

    @Override
    public long hashTick(String key) {
        long result = 0;
        var a = getScalar(key.length(), true);
        for (int i = 0; i < key.length(); i++)
            result = (result + key.charAt(i) * a[i]) % this.size;

        int temp = this.size - 1;
        return 1 + Math.abs(result) % temp;
    }

    public static void main(String[] args) {
        var hasher = new DoubleHashString(13);

        var map1 = new HashMap<Long, Integer>();
        var map2 = new HashMap<Long, Integer>();

        for (int i = 0; i < 100; i++) {
            var part1 = hasher.hash(String.valueOf(i * 27));
            var part2 = hasher.hashTick(String.valueOf(i * 27));

            map1.put(part1, map1.getOrDefault(part1, 0) + 1);
            map2.put(part2, map2.getOrDefault(part2, 0) + 1);
        }

        System.out.println(map1.size());
        System.out.println(map2.size());
    }
}
