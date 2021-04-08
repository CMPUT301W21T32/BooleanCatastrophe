package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentManager;
import com.example.booleancatastrophe.model.ExperimentType;
import com.example.booleancatastrophe.model.Trial;
import com.example.booleancatastrophe.storage.FirestoreCallback;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

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

    private LineGraphSeries<DataPoint> timeSeries = new LineGraphSeries<DataPoint>();
    private GraphView timePlot;

    private BarGraphSeries<DataPoint> histogramSeries = new BarGraphSeries<DataPoint>();
    private GraphView histogram;

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

        timePlot = (GraphView) findViewById(R.id.time_plot);
        histogram = (GraphView) findViewById(R.id.histogram);

        eManager.getTrials(currentExperiment.getId(), new FirestoreCallback<ArrayList<Trial>>() {
            @Override
            public void onCallback(ArrayList<Trial> databaseResult) {
                //All statistic values should be set on call back and graphs should be filled in
                trials = databaseResult;
                setExperimentStatistics();
                setExperimentPlot();
                setExperimentHistogram();
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

    //calculates and sets the dataset for the timeplot
    private void setExperimentPlot(){
        if(trials.isEmpty()){
            timePlot.getGridLabelRenderer().setHorizontalAxisTitle("No data");
            timePlot.addSeries(timeSeries);
            return;
        }
        Calendar c = Calendar.getInstance();
        //Create a data set showing the average or success rate or count over time
        ArrayList<Integer> days = new ArrayList<Integer>();
        for(int i = 0; i < trials.size(); i++){
            c.setTime(trials.get(i).getDate());
            days.add(c.get(Calendar.DAY_OF_YEAR));
        }
        int firstDay = Collections.min(days);
        int lastDay = Collections.max(days);

        int x;
        double y;

        for(int i = firstDay; i <= lastDay; i++){
            //Find a running statistic for each day
            x = i;
            y = runningResult(i);
            timeSeries.appendData(new DataPoint(x,y), true, lastDay - firstDay + 1);
        }
        c.set(Calendar.DAY_OF_YEAR, firstDay);
        Date firstDate = new Date(c.getTimeInMillis());
        c.set(Calendar.DAY_OF_YEAR, lastDay);
        Date lastDate = new Date(c.getTimeInMillis());
        DateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");

        timePlot.setTitle("Showing results from " + df.format(firstDate) + " to " + df.format(lastDate));
        timePlot.setTitleTextSize(40);
        timePlot.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        timePlot.getGridLabelRenderer().setVerticalAxisTitleTextSize(35);

        switch (currentExperiment.getType()){
            case BINOMIAL:
                timePlot.getGridLabelRenderer().setVerticalAxisTitle("Success Rate");
                break;
            case NONNEGCOUNT:
                timePlot.getGridLabelRenderer().setVerticalAxisTitle("Running Average");
                break;
            case MEASUREMENT:
                timePlot.getGridLabelRenderer().setVerticalAxisTitle("Running Average");
                break;
            case COUNT:
                timePlot.getGridLabelRenderer().setVerticalAxisTitle("Total Count");
                break;
        }
        timePlot.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        timePlot.addSeries(timeSeries);
    }


    //calculates and assigns data to the histogram GraphView
    private void setExperimentHistogram(){
        if(trials.isEmpty()){
            histogram.getGridLabelRenderer().setHorizontalAxisTitle("No data");
            histogram.addSeries(histogramSeries);
            return;
        }
        Double max, min;
        ArrayList<Double> results = new ArrayList<Double>();
        for(int i = 0; i < trials.size(); i++){
            results.add(trials.get(i).getResult());
        }
        switch (currentExperiment.getType()){
            case BINOMIAL:
                int pass = Collections.frequency(results, 1d);
                int fail = Collections.frequency(results, 0d);
                histogramSeries.appendData(new DataPoint(0, fail), true, 2);
                histogramSeries.appendData(new DataPoint(1, pass), true, 2);
                histogram.getViewport().setMinY(0);
                histogram.getGridLabelRenderer().setNumHorizontalLabels(2);
                histogram.getViewport().setYAxisBoundsManual(true);
                break;
            case NONNEGCOUNT:
                max = Collections.max(results);
                min = Collections.min(results);
                for(int i = (int)Math.round(min); i <= max; i++){
                    int resultCount = Collections.frequency(trials, (double) i);
                    if(resultCount > 0){
                        histogramSeries.appendData(new DataPoint(i, resultCount), true, (int)Math.round(max - min));
                    }
                }
                break;
            case MEASUREMENT:
                int numIntervals = 5;
                max = Collections.max(results);
                min = Collections.min(results);
                double interval = (max - min) / numIntervals;
                // for each interval loop the results and count all in range
                for(int i = 0; i < 5; i++){
                    int resultCount = 0;
                    double intervalMin = min + i*interval;
                    double intervalMax = min + (i+1)*interval;
                    for(int j = 0; j < trials.size(); j++){
                        if(trials.get(j).getResult() >= intervalMin && trials.get(j).getResult() <= intervalMax){
                            resultCount++;
                        }
                    }
                    histogramSeries.appendData(new DataPoint(i, resultCount), true, 5);
                }
                break;
            case COUNT:
                break;

        }
        histogramSeries.setSpacing(50);
        histogramSeries.setDrawValuesOnTop(true);
        histogramSeries.setValuesOnTopColor(Color.GREEN);

        histogram.getGridLabelRenderer().setHorizontalAxisTitle("Result");
        histogram.getGridLabelRenderer().setVerticalAxisTitle("# of Occurences");
        histogram.addSeries(histogramSeries);

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

    //Returns a running average or count for the given day
    private Double runningResult(int dayofyear){
        Calendar c = Calendar.getInstance();
        Double sum = 0d;
        int resultCount = 0;
        for(int i = 0; i < trials.size(); i++){
            c.setTime(trials.get(i).getDate());
            int trialDay = c.get(Calendar.DAY_OF_YEAR);
            // IF the trial was before or on this day, include it in the stats
            if( trialDay <= dayofyear){
                resultCount++;
                switch (currentExperiment.getType()){
                    case BINOMIAL:
                        sum += trials.get(i).getResult();
                        break;
                    case NONNEGCOUNT:
                        sum += trials.get(i).getResult();
                        break;
                    case MEASUREMENT:
                        sum += trials.get(i).getResult();
                        break;
                    case COUNT:
                        sum++;
                        break;
                }
            }
        }
        if(currentExperiment.getType() == ExperimentType.COUNT){
            return(sum);
        }
        else{
            return(sum / resultCount);
        }

    }

}