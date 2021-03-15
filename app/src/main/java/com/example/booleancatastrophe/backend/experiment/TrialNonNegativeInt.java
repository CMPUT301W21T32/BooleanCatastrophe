package com.example.booleancatastrophe.backend.experiment;

import android.location.Location;

import com.example.booleancatastrophe.User;

public class TrialNonNegativeInt extends Trial {

    private int result;

    protected TrialNonNegativeInt(int result, User submitter) {
        super(submitter);
        this.result = result;
    }

    protected TrialNonNegativeInt(int result, User submitter, Location loc) {
        super(submitter, loc);
        this.result = result;
    }

    public int getResult() {
        return this.result;
    }
}
