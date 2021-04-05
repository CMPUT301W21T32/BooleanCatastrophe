package com.example.booleancatastrophe.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Forum Question POJO for easy database transaction
 **/
@IgnoreExtraProperties
public class ForumQuestion {

    public static final String FIELD_ID = "id";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_EXPERIMENT = "experimentID";
    public static final String FIELD_POSTER = "posterID";
    public static final String FIELD_CONTENT = "content";

    private String id;
    private LocalDateTime date;
    private String experimentID;
    private String posterID;
    private String content;

    public ForumQuestion() {}

    public ForumQuestion(String content, Experiment experiment, User user) {
        this.id = null;    // will be set by the ForumManager
        this.date = LocalDateTime.now();
        this.experimentID = experiment.getId();
        this.posterID = user.getAccountID();
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getExperimentID() {
        return experimentID;
    }

    public String getPosterID() {
        return posterID;
    }

    public String getContent() {
        return content;
    }

    public
}
