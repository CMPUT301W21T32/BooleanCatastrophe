package com.example.booleancatastrophe;

import com.google.firebase.firestore.GeoPoint;

public class Trial {

    public String experimenter;
    public Number result;
    public GeoPoint location;

    Trial(String experimenter, Number result, GeoPoint location){
        this.experimenter = experimenter;
        this.result = result;
        this.location = location;
    }
}
