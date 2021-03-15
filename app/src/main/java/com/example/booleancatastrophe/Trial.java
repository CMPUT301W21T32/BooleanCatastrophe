package com.example.booleancatastrophe;

import com.google.firebase.firestore.GeoPoint;

public class Trial {

    public String experimenter;
    public Number result;
    public GeoPoint location;

    public Trial() {}

    public Trial(String experimenter, Number result, GeoPoint location){
        this.experimenter = experimenter;
        this.result = result;
        this.location = location;
    }

    String getExperimenter(){
        return experimenter;
    }

    GeoPoint getLocation(){
        return location;
    }

    Number getResult(){
        return result;
    }
}
