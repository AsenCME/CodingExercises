package pgdp.threads;

import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort extends RecursiveAction {
    private static final long serialVersionUID = 7640432137464860128L;
    private final Comparable[] arr;
    private final Comparable[] helper;
    private final int low, high;

    private ParallelMergeSort(Comparable[] array, Comparable[] helper, int low, int high) {
        this.helper = helper;
        arr = array;
        this.low = low;
        this.high = high;
    }

    public ParallelMergeSort(Comparable[] array) {
        this(array, new Comparable[array.length], 0, array.length - 1);
    }

    @Override
    protected void compute() {
        if (low < high) {
            final int mid = (low + high) / 2;
            invokeAll(new ParallelMergeSort(arr, helper, low, mid), new ParallelMergeSort(arr, helper, mid + 1, high));
            MergeSort.merge(arr, helper, low, mid, high);
        }
    }

}
