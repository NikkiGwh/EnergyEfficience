package com.example.energyefficience;

import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort extends RecursiveAction {
    private final int[] array;
    private  final int[] helper;
    private  final int low;
    private final int high;
    private final int MAX = 8192;
    public ParallelMergeSort(final int[] array, final int[] helper, final int low, final int high){
        this.array = array;
        this.low = low;
        this.high = high;
        this.helper = helper;

    }
    @Override
    protected void compute() {
        if (low < high) {
            if (high - low <= MAX) { // Sequential implementation
                 sort(low, high);
            } else { // Parallel implementation
                final int middle = (low + high) / 2;
                final ParallelMergeSort left =
                        new ParallelMergeSort(array,helper, low, middle);
                final ParallelMergeSort right =
                        new ParallelMergeSort(array,helper,middle + 1, high);
                invokeAll(left, right);
                merge(low, middle, high);
            }
        }
    }

    private void merge(int low, int middle, int high){
        int i, j;
        for (i = low; i <= middle; i++) {
            helper[i] = array[i];
        }
        for (j = middle + 1; j <= high; j++) {
            helper[high + middle + 1 - j] = array[j];
        }
        i = low;
        j = high;
        for (int k = low; k <= high; k++) {
            if (helper[i] <= helper[j]) {
                array[k] = helper[i];
                i++;
            } else {
                array[k] = helper[j];
                j--;
            }
        }
    }
    private void sort(int l, int r) {
        if (l < r) {
            int q = (l + r) / 2;

            sort(l, q);
            sort(q + 1, r);
            merge(l, q, r);
        }
    }
}
