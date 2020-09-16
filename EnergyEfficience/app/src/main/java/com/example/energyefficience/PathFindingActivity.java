package com.example.energyefficience;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PathFindingActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar ab;
    ConstraintLayout ly;
    Button startComputing;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_finding);
        toolbar = findViewById(R.id.toolbarPathFindingActivity);
        setSupportActionBar(toolbar);

        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ly = findViewById(R.id.pathFindingConstraintLayout);
        startComputing = findViewById(R.id.computePathbtn);
        startComputing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (flag){
                    case 0:
                        flag = 1;
                        ly.setBackgroundColor(getResources().getColor(R.color.canvasBackground));
                        break;
                    case 1:
                        flag = 0;
                        ly.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    default:
                        break;
                }

            }
        });


    }
}