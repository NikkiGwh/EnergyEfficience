package com.example.energyefficience;

import android.os.Handler;

import java.util.concurrent.Callable;



public class MergeSortCallable implements Callable {
    int[] array;
    Handler handler;
    int choice = 1;
    MergeSortCallback callback;
    public MergeSortCallable(Handler handler, int[] array, MergeSortCallback callback, int choice){
        this.array = array;
        this.handler = handler;
        this.callback = callback;
        this.choice = choice;
    }
    @Override
    public Object call() throws Exception {
        switch (choice){
            case 1:
                MergeSortImplementation sorter = new MergeSortImplementation(array);
                sorter.sort(0, array.length-1);
                break;
            case 2:
                MergeSortImplementationIterative sorter3 = new MergeSortImplementationIterative();
                sorter3.sort(array);
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
                callback.onComplete(array);
            }
        });
    }
}
