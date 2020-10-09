package com.example.energyefficience;

import android.os.Handler;

import java.nio.file.DirectoryIteratorException;
import java.util.concurrent.Callable;



public class MergeSortCallable implements Callable {
    int[] array;
    int[] defaultArr;
    Handler handler;
    int choice = 1;
    int Iterations;
    MergeSortCallback callback;
    public MergeSortCallable(Handler handler, int[] array, MergeSortCallback callback, int choice, int Iterations){
        this.array = array;
        initializeDefaultArray();
        this.handler = handler;
        this.callback = callback;
        this.choice = choice;
        this.Iterations = Iterations;
    }
    private void resetArray(int[] arr){
        for(int i = 0 ; i < defaultArr.length; i++){
            arr[i] = defaultArr[i];
        }
    }
    private  void initializeDefaultArray(){
        defaultArr = new int[array.length];
        for(int i =0 ; i < array.length; i++){
            defaultArr[i] = array[i];
        }
    }
    @Override
    public Object call() throws Exception {
        switch (choice){
            case 1:
                for(int i = 0; i < Iterations; i++){
                    resetArray(array);
                    MergeSortImplementation sorter = new MergeSortImplementation(array);
                    sorter.sort(0, array.length-1);
                }
                break;
            case 2:
                MergeSortImplementationIterative sorter3 = new MergeSortImplementationIterative();
                for(int i  = 0; i< Iterations; i++){
                    resetArray(array);
                    sorter3.sort(array);
                }
                break;
            default:
                break;
        }
        notifyResult();
        return null;
    }

    private void notifyResult(){

        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(Iterations);
            }
        });
    }
}
