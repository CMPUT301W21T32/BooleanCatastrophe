package com.example.booleancatastrophe.model;

import java.io.Serializable;

// Class that holds all information about a given experiment, should follow an simple POJO structure
// for easy database usage
public class Experiment implements Serializable {

    private String id;
    private String region;
    private String description;
    private String owner;
    private ExperimentType type;
    private Boolean published;
    private Boolean ended;
    private int minTrials;
    //TODO: implement blacklist


    public Experiment() {}

    public Experiment(String description, String region, String owner, int minTrials, ExperimentType type){
        id = ""; //id will be generated by the database
        published = false;
        ended = false;
        this.minTrials = minTrials;
        this.region = region;
        this.description = description;
        this.owner = owner;
        this.type = type;
    }

    public String getId(){
        return id;
    }

    public String getRegion(){
        return region;
    }

    public String getDescription(){
        return description;
    }

    public String getOwner(){
        return owner;
    }

    public ExperimentType getType(){
        return type;
    }

    public int getMinTrials() {return minTrials;}

    public Boolean isPublished() { return published; }

    public Boolean isEnded() {return ended;}

    public void setId(String id){
        this.id = id;
    }

    public void setRegion(String region){
        this.region = region;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setOwner(String owner){
        this.owner = owner;
    }

    public void setType(ExperimentType type){
        this.type = type;
    }

}
