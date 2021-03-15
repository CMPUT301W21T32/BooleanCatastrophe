package com.example.booleancatastrophe;

public class Experiment {

    private String id;
    private String region;
    private String description;
    private String owner;
    private String type;

    public Experiment() {}

    public Experiment(String description, String region, String owner, String type){
        id = ""; //id will be determined by the database
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

    public String getType(){
        return type;
    }

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

    public void setType(String type){
        this.type = type;
    }

}
