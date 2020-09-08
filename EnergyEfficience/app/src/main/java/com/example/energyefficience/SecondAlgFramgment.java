package com.example.energyefficience;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.nio.file.Path;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondAlgFramgment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondAlgFramgment extends Fragment {

    public static final String ARG_OBJECT = "secondAlg" ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SecondAlgFramgment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondAlgFramgment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondAlgFramgment newInstance(String param1, String param2) {
        SecondAlgFramgment fragment = new SecondAlgFramgment();
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
    ImageView PathCanvasImageView;
    Button PathComputingBtn;
    View rootview;
    CustomDrawable picasso;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_second_alg_framgment, container, false);
        PathCanvasImageView = (ImageView) rootview.findViewById(R.id.PathFindingCanvas);
        PathComputingBtn = (Button) rootview.findViewById(R.id.PathComputeBtn);

        picasso = new CustomDrawable(6);
        PathCanvasImageView.setImageDrawable(picasso);
        return rootview;
    }
}