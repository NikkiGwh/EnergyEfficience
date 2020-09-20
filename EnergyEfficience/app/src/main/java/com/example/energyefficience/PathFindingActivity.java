package com.example.energyefficience;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PathFindingActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar ab;
    ConstraintLayout ly;
    Button startComputing;
    Knot[][] matrix = null;
    EditText dimensionEditText, obstaclesEditText;
    int Dimension = 0;
    int Obstacles = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_finding);
        toolbar = findViewById(R.id.toolbarPathFindingActivity);
        dimensionEditText = findViewById(R.id.editTextTextDimensionMatrix);
        obstaclesEditText = findViewById(R.id.editTextNumberObstacles);
        setSupportActionBar(toolbar);

        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ly = findViewById(R.id.pathFindingConstraintLayout);
        startComputing = findViewById(R.id.computePathbtn);
        startComputing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ly.setBackgroundColor(getResources().getColor(R.color.canvasBackground));
                Dimension = Integer.parseInt(dimensionEditText.getText().toString());
                Obstacles = Integer.parseInt(obstaclesEditText.getText().toString());
                initializeMatrix(Dimension, Obstacles);

                DjikstraImplementation.computeSynchronouse(matrix, matrix[0][1], matrix[10][5]);
                ly.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });


    }

    private void initializeMatrix(int dim, int obs)
    {
        boolean isObs = false;
        if(obs > dim*dim-2)
            obs = 0;
        int currentObsNum = 0;
        matrix = new Knot[dim][dim];
       // List<ArrayList<Knot>> bitte = new ArrayList<ArrayList<Knot>>();

        for(int x = 0; x < dim; x++)
        {
            //bitte.add(new ArrayList<Knot>());
            for(int y = 0; y < dim; y++)
            {
                if(currentObsNum < obs){
                    if(getRandomNumber(0,3) >= 50){
                        isObs = true;
                        currentObsNum++;
                    }else{
                        isObs = false;
                    }
                }else{
                    isObs =false;
                }
                matrix[x][y] = new Knot(x, y, isObs);
             //bitte.get(x).add(new Knot(x, y, isObs));
            }
        }
    }

    private int getRandomNumber(int min, int max){
        int g = (int)(Math.random()*((max-min)+1)) + min;
        return g;
    }
}