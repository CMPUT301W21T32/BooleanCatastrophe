package com.example.booleancatastrophe.model;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Date;

public class ForumReply {

    @Exclude
    private User poster;
    @Exclude
    private ForumQuestion question;

    private String id;
    private Date date;
    private String questionID;
    private String posterID;
    private String content;

    /**
     * Empty ForumReply constructor for Firestore easy object serialization / deserialization
     **/
    public ForumReply() {}

    /**
     * ForumReply constructor
     * @param question
     * The ForumQuestion that this reply responds to
     * @param poster
     * User who posted this reply
     * @param content
     * The visible reply contents
     **/
    public ForumReply(ForumQuestion question, User poster, String content) {
        this.poster = poster;
        this.question = question;

        this.id = null;    // will be set by the ForumManager
        this.date = new Date();
        this.questionID = question.getId();
        this.posterID = poster.getAccountID();
        this.content = content;
    }

    @Exclude
    public User getPoster() {
        return poster;
    }

    @Exclude
    public void setPoster(User poster) {
        this.poster = poster;
    }

    @Exclude
    public ForumQuestion getQuestion() {
        return question;
    }

    @Exclude
    public void setQuestion(ForumQuestion question) {
        this.question = question;
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

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
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
}
