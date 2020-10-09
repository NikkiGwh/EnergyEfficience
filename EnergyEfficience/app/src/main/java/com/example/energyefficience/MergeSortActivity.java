package com.example.energyefficience;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class MergeSortActivity extends AppCompatActivity implements MergeSortCallback {

    MyRoomDatabase db;
    MergeSortNumberDao mergeSortNumberDao;

    CustomThreadPoolManager mCustomThreadPoolManager;
    ForkJoinPool forkJoinPool;

    Button MergeSort, GenerateNewNumbers;
    EditText NumOfIterationsEditText;
    TextView ComputationTimeTextView, InProgressTextView;
    RadioButton rb_classic, rb_parallel, rb_parallel_library, rb_iterative;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    int choiceOfMerge = 0;
    int[] DefaultArray;
    int[] arr;
    long startTime = 0;
    long stopTime = 0;
    long stopTime2 = 0;
    long result = 0;
    long result2 = 0;
    int currentIteration = 0;
    int totalIterations = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_sort);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakeLockTag");
        db = MyRoomDatabase.getDatabase(this);
        mergeSortNumberDao = db.mergeSortNumberDao();
        MergeSort = findViewById(R.id.MergeSortBtn2);
        MergeSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMergeSort();
            }
        });
        GenerateNewNumbers = findViewById(R.id.generateNumbersBtn2);
        GenerateNewNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandomIntArray(3000000);
            }
        });
        NumOfIterationsEditText = findViewById(R.id.editTextNumOfIterations);
        ComputationTimeTextView = findViewById(R.id.textViewComputationTime2);
        InProgressTextView = findViewById(R.id.inProgressTextView);
        rb_classic = findViewById(R.id.radio_classic2);
        rb_classic.setOnClickListener(radioButtonOnClickListener);
        rb_iterative = findViewById(R.id.radio_iterative2);
        rb_iterative.setOnClickListener(radioButtonOnClickListener);
        rb_parallel = findViewById(R.id.radio_parallel2);
        rb_parallel.setOnClickListener(radioButtonOnClickListener);
        rb_parallel_library = findViewById(R.id.radio_parallel_library2);
        rb_parallel_library.setOnClickListener(radioButtonOnClickListener);
        mCustomThreadPoolManager.setNumberOfCores(1);
        mCustomThreadPoolManager = CustomThreadPoolManager.getInstance();
        DefaultArray = getNumbersFromDB();
        arr = new int[DefaultArray.length];
        resetArray(arr);
        forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() - 1);
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();

    private int[] getNumbersFromDB() {
        Future<MergeSortNumberEntity[]> fu = executor.submit(new MergeSortDBCallable(0, mergeSortNumberDao));
        MergeSortNumberEntity[] arr = new MergeSortNumberEntity[0];
        try {
            arr = fu.get();
            Log.i("niklas", String.valueOf(arr.length));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int[] arr2 = new int[arr.length];
        for (int i = 0; i < arr2.length; i++) {
            arr2[i] = arr[i].value;
        }
        return arr2;
    }

    @Override
    public void onComplete(int number) {
        stopTime2 = System.nanoTime();
        result2 = (stopTime2 - startTime) / 1000000;
        ComputationTimeTextView.setText(String.valueOf(result2) + " ms");
        InProgressTextView.setVisibility(View.INVISIBLE);
        wakeLock.release();
    }

    View.OnClickListener radioButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean checked = ((RadioButton) view).isChecked();
            switch (view.getId()) {
                case R.id.radio_classic2:
                    if (checked)
                        choiceOfMerge = 0;
                    break;
                case R.id.radio_parallel2:
                    choiceOfMerge = 1;
                    break;
                case R.id.radio_parallel_library2:
                    choiceOfMerge = 2;
                    break;
                case R.id.radio_iterative2:
                    choiceOfMerge = 3;
                    break;
                default:
                    choiceOfMerge = 0;
                    break;
            }
            return;
        }
    };

    private void generateRandomIntArray(int size) {
        DefaultArray = new int[size];
        executor.submit(new MergeSortDBCallable(1, mergeSortNumberDao));
        List<MergeSortNumberEntity> li2 = new ArrayList<MergeSortNumberEntity>();
        for (int i = 0; i < size; i++) {
            DefaultArray[i] = getRandomNumber(0, 9000000);
            li2.add(new MergeSortNumberEntity(i, DefaultArray[i]));
        }
        insertAll(li2);
    }

    private int getRandomNumber(int min, int max) {
        int g = (int) (Math.random() * ((max - min) + 1)) + min;
        return g;
    }

    private void insertAll(List<MergeSortNumberEntity> ob) {
        new MergeSortActivity.insertAsyncTask(mergeSortNumberDao).execute(ob);
    }

    private static class insertAsyncTask extends AsyncTask<List<MergeSortNumberEntity>, Void, Void> {
        private MergeSortNumberDao mAsyncTaskDao;

        insertAsyncTask(MergeSortNumberDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(List<MergeSortNumberEntity>... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }


    private void callMergeSort() {

        wakeLock.acquire();
        InProgressTextView.setVisibility(View.VISIBLE);
        resetArray(arr);
        totalIterations = 1;
        try {
            totalIterations = Integer.parseInt(NumOfIterationsEditText.getText().toString());
        } catch (NumberFormatException ex) {
            Log.e("Error", ex.getMessage());
        }
        startTime = System.nanoTime();
        switch (choiceOfMerge) {
            case 0:
                mCustomThreadPoolManager.addCallable(new MergeSortCallable(mCustomThreadPoolManager.getMainThreadHandler(), arr, this, 1, totalIterations));
                return;
            case 1:
                int[] helper = new int[DefaultArray.length];
                for (currentIteration = 0; currentIteration < totalIterations; currentIteration++) {
                    resetArray(arr);
                    forkJoinPool.invoke(new ParallelMergeSort(arr, helper, 0, arr.length - 1));
                }
                break;
            case 2:
                for (currentIteration = 0; currentIteration < totalIterations; currentIteration++) {
                    resetArray(arr);
                    Arrays.parallelSort(arr);
                }
                break;
            case 3:
                mCustomThreadPoolManager.addCallable(new MergeSortCallable(mCustomThreadPoolManager.getMainThreadHandler(), arr, this, 2, totalIterations));
                return;
            default:
                return;
        }
        stopTime = System.nanoTime();
        result = (stopTime - startTime) / 1000000;
        ComputationTimeTextView.setText(String.valueOf(result) + " ms");
        InProgressTextView.setVisibility(View.INVISIBLE);
        wakeLock.release();
    }

    private synchronized void resetArray(int[] arr) {
        for (int i = 0; i < DefaultArray.length; i++) {
            arr[i] = DefaultArray[i];
        }
    }

    class MergeSortDBCallable implements Callable<MergeSortNumberEntity[]> {
        int action = 0;
        MergeSortNumberDao dao;

        public MergeSortDBCallable(int action, MergeSortNumberDao dao) {
            this.action = action;
            this.dao = dao;
        }

        @Override
        public MergeSortNumberEntity[] call() throws Exception {
            switch (action) {
                case 0:
                    MergeSortNumberEntity[] arr = dao.getAll();
                    return arr;
                case 1:
                    dao.deleteAll();
                    break;
                default:
                    break;
            }
            return null;
        }
    }
}