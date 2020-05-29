package gad.simplesort;

import java.util.Arrays;
import java.util.Random;

public class Mergesort {

    public static void main(String[] args) {
        int i = 10;
        int SEED = i * 3;
        int N = i;
        int MAX_V = 100;
        int[] numbers;

        numbers = new int[N];
        Random r = new Random(SEED);
        for (int j = 0; j < N; j++)
            numbers[j] = r.nextInt(MAX_V) + 1;

        System.out.println(Arrays.toString(numbers));
        System.out.println("\n==== Mergesort ====\n\nSortiertest:");
        new Mergesort().sort(numbers, new StudentResult());
        System.out.println(Arrays.toString(numbers));
    }

    public void sort(int[] numbers, Result result) {
        sort(numbers, 0, numbers.length - 1, null, result);
    }

    public void sort(int[] numbers, int left, int right, int[] helper, Result result) {
        if (left == right)
            return;
        int mid = (left + right) / 2;
        if ((left + right) % 2 == 0)
            mid--;

        sort(numbers, left, mid, null, result);
        sort(numbers, mid + 1, right, null, result);
        merge(numbers, left, mid, right, new int[right - left + 1]);

        if (result != null)
            result.logPartialArray(Arrays.copyOfRange(numbers, left, right + 1));
    }

    public void merge(int[] numbers, int left, int mid, int right, int[] helper) {
        int leftLen = mid - left + 1;
        int rightLen = right - mid;
        int i = 0, j = 0, k = 0;

        while (i < leftLen && j < rightLen) {
            int leftSmallest = numbers[left + i];
            int rightSmallest = numbers[mid + 1 + j];
            if (leftSmallest < rightSmallest) {
                helper[k] = leftSmallest;
                i++;
            } else {
                helper[k] = rightSmallest;
                j++;
            }
            k++;
        }
        while (i < leftLen) {
            helper[k] = numbers[left + i];
            i++;
            k++;
        }
        while (j < rightLen) {
            helper[k] = numbers[mid + 1 + j];
            j++;
            k++;
        }

        for (int idx = left; idx <= right; idx++)
            numbers[idx] = helper[idx - left];
    }
}