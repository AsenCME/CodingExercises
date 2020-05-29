package pgdp.threads;

import java.util.concurrent.RecursiveAction;

public class BetterParallelMergeSort extends RecursiveAction {
    private static final long serialVersionUID = -5176087817778882011L;
    private final Comparable[] arr, helper;
    private final int low, high;
    private static final int threshold = 512;

    private BetterParallelMergeSort(Comparable[] array, Comparable[] helper, int low, int high) {
        this.helper = helper;
        arr = array;
        this.low = low;
        this.high = high;
    }

    public BetterParallelMergeSort(Comparable[] array) {
        this(array, new Comparable[array.length], 0, array.length - 1);
    }

    @Override
    protected void compute() {
        if (high - low < threshold)
            MergeSort.mergesort(arr, helper, low, high);
        else {
            final int mid = (low + high) / 2;
            invokeAll(new BetterParallelMergeSort(arr, helper, low, mid),
                    new BetterParallelMergeSort(arr, helper, mid + 1, high));
            MergeSort.merge(arr, helper, low, mid, high);
        }
    }
}
