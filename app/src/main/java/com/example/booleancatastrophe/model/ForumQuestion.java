package com.example.booleancatastrophe.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Forum Question object that contains a number of replies
 **/
public class ForumQuestion implements Serializable {

    @Exclude
    private Experiment experiment;
    @Exclude
    private User poster;
    @Exclude
    private ArrayList<ForumReply> replies = new ArrayList<>();

    private String id;
    private Date date;
    private String experimentID;
    private String posterID;
    private String content;
    private ArrayList<String> replyIDs = new ArrayList<>();

    /**
     * Empty ForumQuestion constructor for Firestore easy object serialization / deserialization
     **/
    public ForumQuestion() {}

    /**
     * ForumQuestion constructor
     * @param experiment
     * Experiment this question is related to
     * @param poster
     * User who posted this question
     * @param content
     * The visible question contents
     **/
    public ForumQuestion(Experiment experiment, User poster, String content) {
        this.experiment = experiment;
        this.poster = poster;

        this.id = null;    // will be set by the ForumManager
        this.date = new Date();
        this.experimentID = experiment.getId();
        this.posterID = poster.getAccountID();
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getExperimentID() {
        return experimentID;
    }

    public void setExperimentID(String experimentID) {
        this.experimentID = experimentID;
    }

    public String getPosterID() {
        return posterID;
    }

    public void setPosterID(String posterID) {
        this.posterID = posterID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getReplyIDs() {
        return replyIDs;
    }

    public void setReplyIDs(ArrayList<String> replyIDs) {
        this.replyIDs = replyIDs;
    }

    @Exclude
    public Experiment getExperiment() {
        return experiment;
    }

    @Exclude
    public User getPoster() {
        return poster;
    }

    public void addReply(ForumReply reply) {
        replies.add(reply);
        replyIDs.add(reply.getId());
    }

    public int getReplyNumber() {
        return replies.size();
    }
}