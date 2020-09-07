package com.example.energyefficience;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Base64Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Base64Fragment extends Fragment {

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
    Button decode_btn;
    List<Base64BlindTextEntity> li;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_base64, container, false);
        //set recyclerView and adapter(manages the layout per item)
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview);
        adapter = new Base64RecyclerViewAdapter(this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //View Model
        base64ViewModel = new ViewModelProvider(this).get(Base64ViewModel.class);
        base64ViewModel.getAllEntries().observe(requireActivity(), new Observer<List<Base64BlindTextEntity>>(){
            @Override
            public void onChanged(List<Base64BlindTextEntity> base64BlindTextEntities) {
                adapter.setElementsList(base64BlindTextEntities);
                li = base64BlindTextEntities;
            }
        });
        //decode button
        decode_btn = (Button) rootView.findViewById(R.id.encodebtn);
        decode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decode_btn.setBackgroundColor(Color.GRAY);

                for (Base64BlindTextEntity x : li)
                {
                    x.setEncodedText(Base64Implementation.encodeOnUIThread(x.getBlindText()));
                }
                decode_btn.setBackgroundColor(Color.RED);
                for (Base64BlindTextEntity x : li)
                {
                    base64ViewModel.insert(x);
                }
               decode_btn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        });
        return rootView;

    }
}