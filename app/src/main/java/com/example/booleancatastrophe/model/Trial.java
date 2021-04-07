package com.example.booleancatastrophe.model;

import com.example.booleancatastrophe.model.ExperimentType;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

// Class to hold all information about a trial
public class Trial {

    private String experimenter;
    private double result;   //to make the class easily serializable and deserializable we use a double for all results
    private GeoPoint location;
    private ExperimentType type;
    private Date date;

    public Trial() {}

    public Trial(String experimenter, double result, GeoPoint location, ExperimentType type, Date date){
        this.experimenter = experimenter;
        this.result = result;
        this.location = location;
        this.type = type;
        this.date = date;
    }

    public String getExperimenter(){
        return experimenter;
    }

    public GeoPoint getLocation(){
        return location;
    }

    public Number getResult(){
        Number temp = 0;
        switch (type){
            case COUNT:
                temp = 1;
                break;
            case BINOMIAL:     //map 0 to a failure and anything else to a success
                if(result == 0){
                    temp = 0;
                }
                else{
                    temp = 1;
                }
                break;
            case MEASUREMENT:
                temp = result;
                break;
            case NONNEGCOUNT:
                temp = (int) result;
                break;
        }
        return temp;
    }

    public ExperimentType getType(){ return type; }

    public Date getDate(){ return date; }

}
