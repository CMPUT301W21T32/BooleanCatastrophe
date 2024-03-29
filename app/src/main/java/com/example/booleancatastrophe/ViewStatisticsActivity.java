package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentManager;
import com.example.booleancatastrophe.model.ExperimentType;
import com.example.booleancatastrophe.model.Trial;
import com.example.booleancatastrophe.storage.FirestoreCallback;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.DecimalFormat;
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

    private int numIntervals = 5;
    private ExperimentType type;

    private LineGraphSeries<DataPoint> timeSeries = new LineGraphSeries<DataPoint>();
    private GraphView timePlot;

    private BarGraphSeries<DataPoint> histogramSeries = new BarGraphSeries<DataPoint>();
    private ArrayList<BarGraphSeries<DataPoint>> intervalSeries = new ArrayList<>();
    private BarGraphSeries<DataPoint> passSeries = new BarGraphSeries<DataPoint>();
    private BarGraphSeries<DataPoint> failSeries = new BarGraphSeries<DataPoint>();

    private GraphView histogram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);

        // Retrieve the experiment passed via the intent
        currentExperiment = (Experiment) getIntent().getSerializableExtra("EXPERIMENT");
        Log.d("STATS", "Got experiment " + currentExperiment.getDescription());
        type =  ExperimentType.valueOf(currentExperiment.getStrType());

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

        switch (type){
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
        ArrayList<DataPoint> data = new ArrayList<DataPoint>();
        for(int i = 0; i < trials.size(); i++){
            results.add(trials.get(i).getResult());
        }
        switch (type){
            case BINOMIAL:
                int pass = Collections.frequency(results, 1d);
                int fail = Collections.frequency(results, 0d);
                //Log.d("Stats", "Pass: " + pass + "  Fail: " + fail);
                failSeries.appendData(new DataPoint(0, fail), true, 2);
                passSeries.appendData(new DataPoint(1, pass), true, 2);
                failSeries.setTitle("Failures");
                passSeries.setTitle("Successes");
                failSeries.setColor(Color.RED);
                passSeries.setColor(Color.BLUE);
                histogram.getViewport().setYAxisBoundsManual(true);
                histogram.getViewport().setMinY(0);
                histogram.getViewport().setMaxY(Math.max(pass, fail));
                histogram.addSeries(passSeries);
                histogram.addSeries(failSeries);
                histogram.getViewport().setXAxisBoundsManual(true);
                histogram.getViewport().setMinX(0);
                histogram.getLegendRenderer().setVisible(true);
                histogram.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                histogram.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                //histogram.getViewport().setYAxisBoundsManual(true);
                break;
            case NONNEGCOUNT:
                max = Collections.max(results);
                min = Collections.min(results);
                for(int i = (int)Math.round(min); i <= max; i++){
                    int resultCount = Collections.frequency(results, (double) i);
                    //Log.d("STATS", "Found " + resultCount + " trials with result " + Integer.toString(i));
                    histogramSeries.appendData(new DataPoint(i, resultCount), true, (int)Math.round(max - min));
                }
                histogramSeries.setDrawValuesOnTop(true);
                histogramSeries.setSpacing(0);
                histogram.addSeries(histogramSeries);
                histogram.getViewport().setXAxisBoundsManual(true);
                histogram.getViewport().setMinX(Math.round(min));
                histogram.getViewport().setMaxX(Math.round(max));
                break;
            case MEASUREMENT:
                max = Collections.max(results);
                min = Collections.min(results);
                double interval = (max - min) / numIntervals;
                // for each interval loop the results and count all in range
                for(int i = 0; i < numIntervals; i++){
                    int resultCount = 0;
                    double intervalMin = min + i*interval;
                    double intervalMax = min + (i+1)*interval + 0.1;
                    intervalSeries.add(i, new BarGraphSeries<DataPoint>() );
                    for(int j = 0; j < trials.size(); j++){
                        if(trials.get(j).getResult() >= intervalMin && trials.get(j).getResult() < intervalMax){
                            resultCount++;
                        }
                    }
                    //After result is found the series corresponding to i should get the data appended
                    //and the title should be set
                    String low = new DecimalFormat("###.#").format(intervalMin);
                    String high = new DecimalFormat("###.#").format(intervalMax);
                    Log.d("STATS", "Section " + i + " has " + resultCount + " results");
                    intervalSeries.get(i).appendData(new DataPoint(i, resultCount), true, 1);
                    intervalSeries.get(i).setTitle(low + " - " + high);
                    intervalSeries.get(i).setDrawValuesOnTop(true);
                    //histogram.addSeries(intervalSeries.get(i));
                    //histogramSeries.appendData(new DataPoint(i, resultCount), true, 5);
                }
                intervalSeries.get(0).setColor(Color.BLUE);
                intervalSeries.get(1).setColor(Color.RED);
                intervalSeries.get(2).setColor(Color.GREEN);
                intervalSeries.get(3).setColor(Color.YELLOW);
                intervalSeries.get(4).setColor(Color.MAGENTA);
                for(int i = 0; i < numIntervals; i++){
                    histogram.addSeries(intervalSeries.get(i));
                }
                histogram.getViewport().setXAxisBoundsManual(true);
                histogram.getViewport().setMinX(-1);
                histogram.getViewport().setMaxX(numIntervals + 2);
                histogram.getViewport().setYAxisBoundsManual(true);
                histogram.getViewport().setMinY(0);
                //histogram.getViewport().setMaxY();
                histogram.getLegendRenderer().setVisible(true);
                histogram.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                histogram.getGridLabelRenderer().setHorizontalLabelsVisible(false);


                break;
            case COUNT:
                break;

        }

        histogram.getGridLabelRenderer().setHorizontalAxisTitle("Result");
        histogram.getGridLabelRenderer().setVerticalAxisTitle("Occurences");


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
                switch (type){
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