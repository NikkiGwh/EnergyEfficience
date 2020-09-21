package com.example.energyefficience;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MergeSortFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MergeSortFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MergeSortFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MergeSortFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MergeSortFragment newInstance(String param1, String param2) {
        MergeSortFragment fragment = new MergeSortFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    View.OnClickListener radioButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean checked = ((RadioButton) view).isChecked();
            switch (view.getId()){
                case R.id.radio_classic:
                    if(checked)
                        choiceOfMerge = 0;
                    break;
                case R.id.radio_parallel:
                    choiceOfMerge = 1;
                    break;
                case R.id.radio_parallel_library:
                    choiceOfMerge = 2;
                    break;
                default:
                    choiceOfMerge = 0;
                    break;
            }
            return;
        }
    };
    int choiceOfMerge = 0;
    MergeSortRecyclerViewAdapter adapter;
    List<String> Items = new ArrayList<String>();
    int[] ItemArray;
    Button MergeSortBtn, GenerateNumbersBtn;
    EditText CountOfFigures;
    RadioButton rb_classic, rb_parallel, rb_parallel_library;
    TextView computationTime;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_merge_sort, container, false);
        //set recyclerView and adapter(manages the layout per item)
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView_MergeSort);
        adapter = new MergeSortRecyclerViewAdapter(this.getContext());
        adapter.setElementsList(Items);
        computationTime = rootView.findViewById(R.id.textViewComputationTime);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        MergeSortBtn = rootView.findViewById(R.id.MergeSortBtn);
        rb_classic = rootView.findViewById(R.id.radio_classic);
        rb_classic.setOnClickListener(radioButtonOnClickListener);
        rb_parallel = rootView.findViewById(R.id.radio_parallel);
        rb_parallel.setOnClickListener(radioButtonOnClickListener);
        rb_parallel_library = rootView.findViewById(R.id.radio_parallel_library);
        rb_parallel_library.setOnClickListener(radioButtonOnClickListener);

        GenerateNumbersBtn = rootView.findViewById(R.id.generateNumbersBtn);
        CountOfFigures = (EditText)rootView.findViewById(R.id.editTextCountOfFigures);
        GenerateNumbersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              ItemArray = generateRandomIntArray(Integer.parseInt(CountOfFigures.getText().toString()));
            }
        });
        MergeSortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callMergeSort();
            }
        });
        return rootView;
    }
    private int[] generateRandomIntArray(int size){
        int[] IntArray = new int[size];
        Items.clear();
        for(int i = 0; i < size; i++){
            IntArray[i] = getRandomNumber(0, 9000000);
            Items.add(String.valueOf(IntArray[i]));
        }
        adapter.setElementsList(Items);
        return IntArray;
    }
    private int getRandomNumber(int min, int max){
        int g = (int)(Math.random()*((max-min)+1)) + min;
        return g;
    }

    private void callMergeSort()
    {

        Items.clear();
        long startTime = System.nanoTime();
        switch (choiceOfMerge){
            case 0:
                MergeSortImplementation sorter = new MergeSortImplementation(ItemArray);
                sorter.sort(0, ItemArray.length-1);
                break;
            case 1:
                final ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() - 1);
                forkJoinPool.invoke(new ParallelMergeSort(ItemArray, 0, ItemArray.length - 1));
                break;
            case 2:
                Arrays.parallelSort(ItemArray);
                break;
            default:
                break;
        }
        long stopTime = System.nanoTime();
        long result = (stopTime - startTime)/1000000;
        computationTime.setText(String.valueOf(result) + " ms");

        for(int i = 0; i < ItemArray.length; i++){
            Items.add(String.valueOf(ItemArray[i]));
        }
        adapter.setElementsList(Items);
    }
}