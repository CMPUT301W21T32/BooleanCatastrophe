package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public double [] input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public static double findMean(double[] input){
        double sum = 0;
        double mean;
        int i;
        for (i = 0, i<input.length; i++){
            sum += input[i];
        }
        mean = sum/input.length;
        return  mean;
    }

    public static double findMedian(double[] input) {
        int mid = input.length / 2;
        if (input.length % 2 == 1) {
            return input[mid];
        } else return (input[mid - 1] + input[mid]) / 2;
    }

    public static double findStandardDeviation(double[] input) {
        double mean = findMean(input);
        double sum = 0;
        int i;
        for (i = 0; i != input.length; ++i) {
            sum += Math.pow(Math.abs(mean - input[i]), 2);
        }
        return Math.sqrt(sum / input.length);
    }

    //for the quartile ranges we can simply use the median to find the desired value of the pos
    // source: https://www.geeksforgeeks.org/interquartile-range-iqr/
    public static int median(double[] a, int l, int r)    {
        int n = r - l + 1;
        n = (n + 1) / 2 - 1;
        return n + l;
    }
    int mid = median(input, 0, input.length);
    double lowerQuartile = input[median(input, 0, mid)];
    double upperQuartile = input[mid + median(input, mid + 1, input.length)];
}