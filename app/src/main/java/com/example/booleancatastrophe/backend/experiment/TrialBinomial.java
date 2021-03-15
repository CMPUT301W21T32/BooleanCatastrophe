package com.example.booleancatastrophe.backend.experiment;

import android.location.Location;

import com.example.booleancatastrophe.User;

public class TrialBinomial extends Trial{

    private boolean result;

    protected TrialBinomial(boolean result, User submitter) {
        super(submitter);
        this.result = result;
    }

    protected TrialBinomial(boolean result, User submitter, Location loc) {
        super(submitter, loc);
        this.result = result;
    }

    public boolean getResult(){
        return this.result;
    }
}
