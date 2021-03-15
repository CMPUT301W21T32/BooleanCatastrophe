package com.example.booleancatastrophe.backend.experiment;

import android.location.Geocoder;
import android.location.Location;
import android.webkit.GeolocationPermissions;

import com.example.booleancatastrophe.User;

public abstract class Trial {

    private User submitter;
    //TODO: Figure out if this class is appropriate for this field, or if we need to make our own
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