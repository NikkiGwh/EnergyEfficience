package com.example.energyefficience;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

interface Base64Callback{
    void onComplete(String result);
}
interface MergeSortCallback{
    void onComplete(int number);
}
public class CustomThreadPoolManager {

    private  static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 1;
    private  static final TimeUnit KEEP_ALIVE_TIME_UNIT;
    private Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private final ExecutorService mExecuterService;
    private final BlockingQueue<Runnable> mTaskQueue;
    private List<Future> mRunningTaskList;
    private static CustomThreadPoolManager singleInstance = null;

    static{
        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        singleInstance = new CustomThreadPoolManager();
    }
    private CustomThreadPoolManager(){
        mTaskQueue = new LinkedBlockingQueue<Runnable>();
        mRunningTaskList = new ArrayList<>();
        mExecuterService = new ThreadPoolExecutor(
                NUMBER_OF_CORES,
                NUMBER_OF_CORES,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                mTaskQueue,
                new BackgroundThreadFactory());
    }
    public static void setNumberOfCores(int numberOfCores) {
        if(numberOfCores > 0){
            NUMBER_OF_CORES = numberOfCores;
            singleInstance = new CustomThreadPoolManager();
        }
    }
    public void addCallable(Callable callable){
        Future future = mExecuterService.submit(callable);
        mRunningTaskList.add(future);
    }
    public Handler getMainThreadHandler(){
        return this.mainThreadHandler;
    }
    public static CustomThreadPoolManager getInstance(){
        return singleInstance;
    }
    public int getNumberOfCores(){
        return NUMBER_OF_CORES;
    }
    public void cancelAllTasks() {
        synchronized (this) {
            mTaskQueue.clear();
            for (Future task : mRunningTaskList) {
                if (!task.isDone()) {
                    task.cancel(true);
                }
            }
            mRunningTaskList.clear();
        }
    }
    private static class BackgroundThreadFactory implements ThreadFactory {
        private static int sTag = 1;
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("CustomThread" + sTag);
            sTag++;
            thread.setPriority(THREAD_PRIORITY_BACKGROUND);
            thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    Log.e("ThreadFactory", thread.getName() + " encountered an error: " + ex.getMessage());
                }
            });
            return thread;
        }
    }
}
