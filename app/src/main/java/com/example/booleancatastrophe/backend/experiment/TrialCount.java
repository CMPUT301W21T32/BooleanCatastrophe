package com.example.booleancatastrophe.backend.experiment;

import android.location.Location;

import com.example.booleancatastrophe.User;

public class TrialCount extends Trial {

    //TODO: Do not need a result field, because the instance of the trial object is the result in itself
    private int result;

    protected TrialCount(User submitter) {
        super(submitter);
    }

    protected TrialCount(User submitter, Location loc) {
        super(submitter, loc);
    }
}
