package com.example.booleancatastrophe.backend.experiment;

import android.location.Location;

import com.example.booleancatastrophe.User;

public class TrialMeasurement extends Trial {

    private double result;

    protected TrialMeasurement(double result, User submitter) {
        super(submitter);
        this.result = result;
    }

    protected TrialMeasurement(double result, User submitter, Location loc) {
        super(submitter, loc);
        this.result = result;
    }

    public double getResult() {
        return this.result;
    }
}
