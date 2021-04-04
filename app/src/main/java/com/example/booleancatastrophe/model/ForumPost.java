package com.example.booleancatastrophe.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * This represents objects posted in a forum (can either be questions or replies but have nearly
 * identical functionality - contain User, Date, and content information as well as whether that
 * content has been 'deleted' or 'edited'
 **/
public abstract class ForumPost {

    final private Experiment experiment;
    final private LocalDateTime datePosted;
    final private User poster;
    private String content;

    public static final int MAX_FORUM_POST_LENGTH = 500;

    /**
     * The constructor that is used by both questions and replies in the forum - date is
     * automatically set
     * @param poster
     * The user that posted the forum post
     * @param content
     * The content of the forum post
     **/
    public ForumPost(User poster, String content, Experiment experiment) {
        this.experiment = experiment;
        this.datePosted = LocalDateTime.now();
        this.poster = poster;
        this.content = content;
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
     * This function returns the posted date of this forum post in a string format
     * @return datePosted
     * The date formatted into a string format
     **/
    public String getDatePosted() {
        return datePosted.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    /**
     * This function gets the username of the User who posted this forum post
     * @return posterUsername
     * The username of the poster of this forum post
     **/
    public String getPoster() {
        return poster.getUsername();
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
