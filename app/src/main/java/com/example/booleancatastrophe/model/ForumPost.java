package com.example.booleancatastrophe.model;


import java.util.Date;


/**
 * This represents objects posted in a forum (can either be questions or replies but have nearly
 * identical functionality - contain User, Date, and content information
 **/
public abstract class ForumPost {

    private String id;
    private Date datePosted;
    private Experiment experiment;
    private String content;
    private User poster;

    public static final int MAX_FORUM_POST_LENGTH = 500;

    /**
     * The constructor that is used by both questions and replies in the forum - date is
     * automatically set
     * @param poster
     * The user that posted the forum post
     * @param content
     * The content of the forum post
     * @param experiment
     * The experiment tied to this forum post
     **/
    public ForumPost(Experiment experiment, User poster, String content) {
        this.experiment = experiment;
        this.poster = poster;
        this.content = content;

        this.datePosted = new Date();  // Set the date to the moment the object is created
        this.id = null;  // ID will be set by the manager class
    }

    /**
     * This function gets the id of the forum post
     * @return id
     * Get the id of the forum post
     **/
    public String getId() {
        return id;
    }

    /**
     * This function sets the id of the forum post
     * @param id
     * Set the id of the forum post
     **/
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This function returns the content of the forum post
     * @return content
     * The content line of the forum post
     **/
    public String getContent() {
        return content;
    }

    /**
     * This function returns the posted date of this forum post
     * @return datePosted
     * The date this forum post was posted
     **/
    public Date getDatePosted() {
        return datePosted;
    }

    /**
     * This function sets the posted date of this forum post
     * @param datePosted
     * The date to set for this forum post
     **/
    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    /**
     * This function gets the username of the User who posted this forum post
     * @return posterUsername
     * The username of the poster of this forum post
     **/
    public String getPosterName() {
        return poster.getUsername();
    }

    /**
     * This function gets the user id of the User who posted this forum post
     * @return posterID
     * The ID of the poster of this forum post
     **/
    public String getPosterID() {
        return poster.getAccountID();
    }

    /**
     * This function gets the id of the experiment tied to this forum post
     * @return experimentID
     * The ID of the experiment this forum post is tied to
     * **/
    public String getExperimentID() {
        return experiment.getId();
    }

    /**
     * This function replaces the content of the forum post with a message indicating that it was
     * deleted by the user
     **/
    public void delete() {
        this.content = "< Content deleted by the user >";
    }
}
