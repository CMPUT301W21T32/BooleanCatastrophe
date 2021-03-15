package com.example.booleancatastrophe.backend.experiment;

import android.location.Location;

import com.example.booleancatastrophe.User;

/**
 * TODO: Inconsistent API.  Trials subclasses each have a different type for result, and each
 *       has to reimplement it from scratch.  Collections of Trials are rendered useless.
 *       Perhaps we could make this class Generic and provide a method numericValue()
 *       that each subtype implements for the experiments to use in calculating statistics.
 */
public abstract class Trial {

    private User submitter;
    //TODO: Figure out if this class is appropriate for this field, or if we need to make our own
    //TODO: How can we ensure that geolocation-enabled experiments have no trials with null locations?
    private Location location;

    protected Trial(User submitter){
        this.submitter = submitter;
        this.location = null;
    }

    protected Trial(User submitter, Location loc){
        this.submitter = submitter;
        this.location = loc;
    }
}