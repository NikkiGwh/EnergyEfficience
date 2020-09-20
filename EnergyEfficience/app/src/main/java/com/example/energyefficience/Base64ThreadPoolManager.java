package com.example.energyefficience;

import android.os.Handler;
import android.os.Looper;
import android.renderscript.RenderScript;
import android.util.Log;

import androidx.core.os.HandlerCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
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
public class Base64ThreadPoolManager {

    private static Base64ThreadPoolManager singleInstance = null;
    private  static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 1;
    private  static final TimeUnit KEEP_ALIVE_TIME_UNIT;
    private Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private final ExecutorService mExecuterService;
    private final BlockingQueue<Runnable> mTaskQueue;
    private List<Future> mRunningTaskList;

    public void addCallable(Callable callable){
        Future future = mExecuterService.submit(callable);
        mRunningTaskList.add(future);
    }
    public Handler getMainThreadHandler(){
        return this.mainThreadHandler;
    }

    static{
        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        singleInstance = new Base64ThreadPoolManager();
    }
    private Base64ThreadPoolManager(){
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


    public static Base64ThreadPoolManager getInstance(){
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
            thread.setName("Base64CustomThread" + sTag);
            sTag++;
            thread.setPriority(THREAD_PRIORITY_BACKGROUND);

            // A exception handler is created to log the exception from threads
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
