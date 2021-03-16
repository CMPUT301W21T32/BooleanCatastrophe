package com.example.booleancatastrophe;

import com.google.firebase.firestore.GeoPoint;

// Class to hold all information about a trial
public class Trial {

    private String experimenter;
    private double result;   //to make the class serializable and deserializable we use a double for all results
    private GeoPoint location;
    private ExperimentType type;

    public Trial() {}

    public Trial(String experimenter, double result, GeoPoint location, ExperimentType type){
        this.experimenter = experimenter;
        this.result = result;
        this.location = location;
        this.type = type;
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
                temp = (int) result;
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

}
