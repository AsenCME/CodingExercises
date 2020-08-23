package gad.simplesort;

import java.util.Arrays;
import java.util.Random;

public class Quicksort {

    public static void main(String[] args) {
        int SEED = 1;
        int N = 10;
        int MAX_V = 100;
        int[] numbers;

        numbers = new int[N];
        Random r = new Random(SEED);
        for (int i = 0; i < N; i++)
            numbers[i] = r.nextInt(MAX_V) + 1;

        System.out.println(Arrays.toString(numbers));
        System.out.println("\n==== Quicksort ====\n\nSortiertest:");
        new Quicksort().sort(numbers, new StudentResult());
        System.out.println(Arrays.toString(numbers));
    }

    public void sort(int[] numbers, Result result) {
        sort(numbers, 0, numbers.length - 1, result);
    }

    public void sort(int[] nums, int left, int right, Result result) {
        if (left >= right)
            return;

        int pivot = nums[right];
        int idx = left - 1, k = right;
        do {
            while (nums[++idx] < pivot)
                ;
            while (--k >= left && nums[k] > pivot)
                ;
            coolSwap(nums, idx, k);
        } while (idx < k);
        coolSwap(nums, idx, right);
        sort(nums, left, idx - 1, result);
        sort(nums, idx + 1, right, result);

        if (right - left > 0)
            result.logPartialArray(Arrays.copyOfRange(nums, left, right + 1));
    }

    public void swap(int[] numbers, int i, int j) {
        int temp = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = temp;
    }

    public void coolSwap(int[] numbers, int i, int j) {
        if (i >= j)
            return;
        if (numbers[i] == numbers[j])
            return;
        numbers[i] = numbers[i] ^ numbers[j];
        numbers[j] = numbers[i] ^ numbers[j];
        numbers[i] = numbers[i] ^ numbers[j];
    }
}