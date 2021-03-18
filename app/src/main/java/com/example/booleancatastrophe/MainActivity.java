package com.example.booleancatastrophe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentManager;
import com.example.booleancatastrophe.model.ExperimentType;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "Main Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        //ExperimentManager e = new ExperimentManager();
        //e.addExperiment(new Experiment("Coin", "AB", ((ExperimentApplication) this.getApplication()).getAccountID(), 5, ExperimentType.BINOMIAL));

        


    }



}