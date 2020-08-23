package gad.radix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RadixSort {
    static int key(int element, int decimalPlace) {
        if (element < 0 || decimalPlace < 0)
            throw new IllegalArgumentException("Non-positive numbers are not handled");

        int digits = 1;
        if (element > 0)
            digits = (int) Math.floor((Math.log10(element) + 1));

        if (decimalPlace + 1 > digits)
            return 0;

        element /= Math.pow(10, decimalPlace);
        return (int) (element % 10);
    }

    static void concatenate(List<Integer>[] buckets, int[] elements) {
        int index = 0;
        for (List<Integer> bucket : buckets)
            for (int element : bucket)
                elements[index++] = element;
    }

    static void kSort(int[] elements, int decimalPlace) {
        var buckets = (List<Integer>[]) new List[10];

        for (int i = 0; i < buckets.length; i++)
            buckets[i] = new ArrayList<Integer>();

        for (int number : elements)
            buckets[key(number, decimalPlace)].add(number);

        concatenate(buckets, elements);
    }

    static int getMaxDecimalPlaces(int[] elements) {
        int max = elements.length == 0 ? -1 : elements[0];
        for (int i = 1; i < elements.length; i++)
            if (elements[i] > max)
                max = elements[i];

        if (max == -1)
            return 0;
        return max == 0 ? 1 : (int) Math.floor((Math.log10(max) + 1));
    }

    public static void sort(int[] elements, Result result) {
        int d = getMaxDecimalPlaces(elements);
        for (int i = 0; i < d; i++) {
            kSort(elements, i);
            result.logArray(elements);
        }
    }

    public static void main(String[] args) {
        int SEED = 0;
        int N = 10;
        int[] numbers;
        numbers = new int[N];
        Random r = new Random(SEED);
        for (int i = 0; i < N; i++) {
            numbers[i] = r.nextInt((int) Math.pow(10, r.nextInt(4) + 3));
        }

        System.out.println("\n==== Radixsort ====\n\nSortiertest:");
        sort(numbers, new StudentResult());
    }
}
