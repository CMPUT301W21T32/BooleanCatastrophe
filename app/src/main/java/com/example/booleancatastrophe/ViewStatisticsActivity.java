package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.booleancatastrophe.model.Experiment;

public class ViewStatisticsActivity extends AppCompatActivity {

    private TextView statistics;
    private Experiment currentExperiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);

        statistics = (TextView) findViewById(R.id.statisticsText);

        // Retrieve the experiment passed via the intent
        currentExperiment = (Experiment) getIntent().getSerializableExtra("EXPERIMENT");

        statistics.setText(currentExperiment.getDescription() + " Stats");

    }
}