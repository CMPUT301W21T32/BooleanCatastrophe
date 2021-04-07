package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentManager;
import com.example.booleancatastrophe.model.ExperimentType;
import com.example.booleancatastrophe.model.Trial;
import com.example.booleancatastrophe.storage.FirestoreCallback;

import java.util.ArrayList;
import java.util.Collections;

public class ViewStatisticsActivity extends AppCompatActivity {

    private ExperimentManager eManager = new ExperimentManager();
    private Experiment currentExperiment;
    private ArrayList<Trial> trials;

    private TextView title;
    private TextView meanText;
    private TextView stdDevText;
    private TextView medianText;
    private TextView quartileUpperText;
    private TextView quartileLowerText;

    private Double mean;
    private Double stdDev;
    private Double median;
    private Double quartileUpper;
    private Double quartileLower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);

        // Retrieve the experiment passed via the intent
        currentExperiment = (Experiment) getIntent().getSerializableExtra("EXPERIMENT");

        title = (TextView) findViewById(R.id.statisticsTitle);
        meanText = (TextView) findViewById(R.id.mean_value_text);
        medianText = (TextView) findViewById(R.id.median_value_text);
        quartileUpperText = (TextView) findViewById(R.id.upperQ_value_text);
        quartileLowerText = (TextView) findViewById(R.id.lowerQ_value_text);
        stdDevText = (TextView) findViewById(R.id.stddev_value_text);
        title.setText("Statistics for " + currentExperiment.getDescription());

        eManager.getTrials(currentExperiment.getId(), new FirestoreCallback<ArrayList<Trial>>() {
            @Override
            public void onCallback(ArrayList<Trial> databaseResult) {
                //All statistic values should be set on call back and graphs should be filled in
                trials = databaseResult;
                setExperimentStatistics();
            }
        });
    }

    //calculates and sets the textViews of required statistics
    private void setExperimentStatistics(){
        ArrayList<Double> trialResults = new ArrayList<>();
        int numResults = trials.size();
        // check for empty trials
        if(numResults == 0){
            meanText.setText(" ~ ");
            medianText.setText(" ~ ");
            quartileLowerText.setText(" ~ ");
            quartileUpperText.setText(" ~ ");
            stdDevText.setText(" ~ ");
            return;
        }
        //Get list of the results
        for(int i = 0; i < numResults; i++){
            trialResults.add(trials.get(i).getResult());
        }
        //check for type COUNT which doesnt have significant results
        ExperimentType type = currentExperiment.getType();
        if(type == ExperimentType.COUNT){
            // Most stats are not significant, use fillers and return
            meanText.setText(" ~ ");
            medianText.setText(" ~ ");
            quartileLowerText.setText(" ~ ");
            quartileUpperText.setText(" ~ ");
            stdDevText.setText(" ~ ");
            return;
        }
        //Get all statistics
        mean = getMean(trialResults);
        median = getMedian(trialResults);
        quartileLower = getLowerQuartile(trialResults);
        quartileUpper = getUpperQuartile(trialResults);
        stdDev = getStdDev(trialResults, mean);

        //Set TextViews
        meanText.setText(String.valueOf(mean));
        medianText.setText(String.valueOf(median));
        quartileLowerText.setText(String.valueOf(quartileLower));
        quartileUpperText.setText(String.valueOf(quartileUpper));
        stdDevText.setText(String.valueOf(stdDev));


    }


    private Double getMedian(ArrayList<Double> list){
        Collections.sort(list);
        int numResults = list.size();
        if(numResults % 2 == 0){
            return (list.get(numResults / 2) + list.get((numResults / 2) - 1)) / 2;
        }
        else{
            return list.get((numResults - 1) / 2 );
        }
    }

    private Double getMean(ArrayList<Double> list){
        Double sum = 0d;
        for(int i = 0; i < list.size(); i++){
            sum += (Double) list.get(i);
        }
        return (sum / list.size());
    }

    private Double getStdDev(ArrayList<Double> list, Double mean){
            Double sum = 0d;
            for(int i = 0; i < list.size(); i++){
                sum += Math.pow(list.get(i) - mean, 2);
            }
            return Math.sqrt(sum / list.size());
    }

    private Double getUpperQuartile(ArrayList<Double> list){
        Collections.sort(list);
        if(list.size() % 2 == 0){
            return getMedian( new ArrayList<Double>(list.subList(list.size()/2, list.size())));
        }
        else {
            return getMedian(  new ArrayList<Double>(list.subList((list.size() + 1) / 2, list.size())));
        }
    }

    private Double getLowerQuartile(ArrayList<Double> list){
        Collections.sort(list);
        if(list.size() % 2 == 0){
            return getMedian(new ArrayList<Double>(list.subList(0, list.size()/2)));
        }
        else {
            return getMedian( new ArrayList<Double>(list.subList(0, (list.size() - 1) / 2)));
        }
    }

}