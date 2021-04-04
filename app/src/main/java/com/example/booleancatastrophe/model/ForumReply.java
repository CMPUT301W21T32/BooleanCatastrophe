package com.example.booleancatastrophe.model;


/**
 * This represents replies that will be nested under questions in the forum
 **/
public class ForumReply extends ForumPost {

    /**
     * Constructor for replies that calls its parent constructor ForumPost
     * @param poster
     * The user who posted this reply
     * @param content
     * The initial content of this reply
     * @param experiment
     * The experiment tied to this reply
     **/

    public ForumReply(User poster, String content, Experiment experiment) {
        super(poster, content, experiment);
    }
}
