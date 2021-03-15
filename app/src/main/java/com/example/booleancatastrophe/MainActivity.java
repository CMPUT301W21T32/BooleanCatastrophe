package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.GeoPoint;

public class MainActivity extends AppCompatActivity {

    private ExperimentManager eManager = new ExperimentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Experiment a = new Experiment("TestA", "BC", "Braden", "Count");
        Experiment b = new Experiment("TestB", "BC", "Braden", "Count");

        eManager.addExperiment(a);
        eManager.addExperiment(b);

        Trial t = new Trial("Braden", 4, new GeoPoint(66.7, 32.5));
        eManager.addTrial(a.getId(), t);
        eManager.addTrial(b.getId(), t);

    }
}