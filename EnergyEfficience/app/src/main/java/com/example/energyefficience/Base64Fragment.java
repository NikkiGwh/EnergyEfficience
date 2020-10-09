package com.example.energyefficience;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.POWER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Base64Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Base64Fragment extends Fragment implements Base64Callback {

    public static final String ARG_OBJECT = "Base64";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Base64Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Base64Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Base64Fragment newInstance(String param1, String param2) {
        Base64Fragment fragment = new Base64Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private Base64ViewModel base64ViewModel;
    Base64RecyclerViewAdapter adapter;
    List<String> statsItems = new ArrayList<String>();
    Button decode_btn;
    Button decodeShowcase_btn;
    Button clearList_btn;
    TextView showCaseResultTextView, ProgressingTextView;
    EditText plainTextShowcaseEditText;
    EditText stringSizeEditText;
    EditText numOfThreadsEditText;

    //List<Base64BlindTextEntity> li;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_base64, container, false);
        //set recyclerView and adapter(manages the layout per item)
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview);
        adapter = new Base64RecyclerViewAdapter(this.getContext());
        adapter.setElementsList(statsItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //View Model
       /* base64ViewModel = new ViewModelProvider(this).get(Base64ViewModel.class);
        base64ViewModel.getAllEntries().observe(requireActivity(), new Observer<List<Base64BlindTextEntity>>(){
            @Override
            public void onChanged(List<Base64BlindTextEntity> base64BlindTextEntities) {
                adapter.setElementsList(base64BlindTextEntities);
               // li = base64BlindTextEntities;
            }
        });
        */
        powerManager = (PowerManager) getActivity().getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakeLockTag");
        decode_btn = (Button) rootView.findViewById(R.id.encodebtn);
        decodeShowcase_btn = (Button) rootView.findViewById(R.id.EncodeShowcaseBtn);
        clearList_btn = rootView.findViewById(R.id.clearListBtn);
        plainTextShowcaseEditText = (EditText) rootView.findViewById(R.id.editTextPlainTextExample);
        numOfThreadsEditText = rootView.findViewById(R.id.editTextTextNumOfCores);
        ProgressingTextView = rootView.findViewById(R.id.ProgressingTextViewBase);
        showCaseResultTextView = (TextView) rootView.findViewById(R.id.textViewShowcase);
        decodeShowcase_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String encodedshowcase = Base64Implementation.encodeSynchronously(plainTextShowcaseEditText.getText().toString());
                showCaseResultTextView.setText(encodedshowcase);
            }
        });
       /* decode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decode_btn.setBackgroundColor(Color.GRAY);

                for (Base64BlindTextEntity x : li)
                {
                    x.setEncodedText(Base64Implementation.encodeSynchronously(x.getBlindText()));
                }
                decode_btn.setBackgroundColor(Color.RED);
                for (Base64BlindTextEntity x : li)
                {
                    base64ViewModel.insert(x);
                }
               decode_btn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        });*/
        decode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doEncodingAsynch();
            }
        });
        clearList_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statsItems.clear();
                adapter.setElementsList(statsItems);
            }
        });
        stringSizeEditText = rootView.findViewById(R.id.editTextStringSize);
        return rootView;

    }

    private int currentSize = 0;
    private int chunkPerThreadSize = 0;
    private CustomThreadPoolManager mCustomThreadPoolManager;

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onComplete(String result) {
        synchronized (this) {
            currentSize += chunkPerThreadSize;
            String encoded = result;
            if (currentSize >= totaldataInKB) {
                currentSize = 0;
                endTime = System.nanoTime();
                long duration = (endTime - startTime) / 1000000;
                Log.d("BASE64:", "runtime Asynch: " + String.valueOf(duration) + " ms");
                statsItems.add("asynchronous Base64 decoding on " + mCustomThreadPoolManager.getNumberOfCores() + " Threads:: " + totaldataInKB + " KB" + " in " + String.valueOf(duration) + " ms");
                adapter.setElementsList(statsItems);
                ProgressingTextView.setVisibility(View.INVISIBLE);
                wakeLock.release();
            }
        }
    }

    long startTime = 0;
    long endTime = 0;
    int totaldataInKB = 0;
    int chunksizeInKB = 10000;
    int threadNumber = 0;


    public void doEncodingAsynch() {
        wakeLock.acquire();
        try {
            threadNumber = Integer.parseInt(numOfThreadsEditText.getText().toString());
            totaldataInKB = Integer.parseInt(stringSizeEditText.getText().toString());
        } catch (Exception ex) {
            Log.e("ERRor", "getInteger parse error");
        }
        if (totaldataInKB < chunksizeInKB)
            totaldataInKB = 10000;
        mCustomThreadPoolManager.setNumberOfCores(threadNumber);
        mCustomThreadPoolManager = CustomThreadPoolManager.getInstance();
        ProgressingTextView.setVisibility(View.VISIBLE);
        startTime = System.nanoTime();

        chunkPerThreadSize = chunksizeInKB / mCustomThreadPoolManager.getNumberOfCores();
        for (int i = 0; i < totaldataInKB; i = i + chunkPerThreadSize) {
            Base64EncodeCallable callable = new Base64EncodeCallable(chunkPerThreadSize, this, mCustomThreadPoolManager.getMainThreadHandler());
            mCustomThreadPoolManager.addCallable(callable);
        }

    }
}