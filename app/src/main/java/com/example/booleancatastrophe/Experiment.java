package com.example.booleancatastrophe;

public class Experiment {

    String id;
    String region;
    String description;
    String owner;
    String type;

    public Experiment() {}

    public Experiment(String region, String description, String owner, String type){
        id = ""; //id will be determined by the database
        this.region = region;
        this.description = description;
        this.owner = owner;
        this.type = type;

    }
    String getId(){
        return id;
    }

    String getRegion(){
        return region;
    }

    String getDescription(){
        return description;
    }

    String getOwner(){
        return owner;
    }

    String getType(){
        return type;
    }

    void setId(String id){
        this.id = id;
    }

}
