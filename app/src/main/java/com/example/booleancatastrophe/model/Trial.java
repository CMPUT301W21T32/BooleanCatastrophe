package com.example.booleancatastrophe.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.booleancatastrophe.model.ExperimentType;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

// Class to hold all information about a trial
public class Trial implements Parcelable {

    private String experimenter;
    private double result;   //to make the class easily serializable and deserializable we use a double for all results
    private GeoPoint location;
    private ExperimentType type;

    public Trial() {}

    public Trial(String experimenter, double result, GeoPoint location, ExperimentType type){
        this.experimenter = experimenter;
        this.result = result;
        this.location = location;
        this.type = type;
    }

    protected Trial(Parcel in) {
        experimenter = in.readString();
        result = in.readDouble();
        location = new GeoPoint(in.readDouble(), in.readDouble());
        type = ExperimentType.valueOf(in.readString());
    }

    public static final Creator<Trial> CREATOR = new Creator<Trial>() {
        @Override
        public Trial createFromParcel(Parcel in) {
            return new Trial(in);
        }

        @Override
        public Trial[] newArray(int size) {
            return new Trial[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(experimenter);
        dest.writeDouble(result);
        dest.writeDouble(location.getLatitude());
        dest.writeDouble(location.getLongitude());
        dest.writeString(type.toString());
    }
}
