package com.example.booleancatastrophe.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.booleancatastrophe.model.ExperimentType;
import com.google.firebase.firestore.GeoPoint;


import java.util.Date;



// Class to hold all information about a trial
public class Trial implements Parcelable {

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

    protected Trial(Parcel in) {
        experimenter = in.readString();
        result = in.readDouble();
        location = new GeoPoint(in.readDouble(), in.readDouble());
        type = ExperimentType.valueOf(in.readString());
        date = new Date(in.readLong());
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

    public Double getResult(){
        Double temp = 0d;
        switch (type){
            case COUNT:
                temp = 1d;
                break;
            case BINOMIAL:     //map 0 to a failure and anything else to a success
                if(result == 0){
                    temp = 0d;
                }
                else{
                    temp = 1d;
                }
                break;
            case MEASUREMENT:
                temp = result;
                break;
            case NONNEGCOUNT:
                temp = result;
                break;
        }
        return temp;
    }

    public ExperimentType getType(){ return type; }

    public Date getDate(){ return date; }

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
        dest.writeLong(date.getTime());
    }
}
