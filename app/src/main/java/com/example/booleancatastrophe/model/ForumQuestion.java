package com.example.booleancatastrophe.model;


import java.util.Collection;


public class ForumQuestion extends ForumPost {
    Collection<ForumReply> replies;

    /**
     * Constructor for questions that calls its parent constructor ForumPost
     * @param poster
     * The user who posted this question
     * @param content
     * The initial content of this question
     * @param experiment
     * The experiment this question is tied to
     **/
    public ForumQuestion(User poster, String content, Experiment experiment) {
        super(poster, content, experiment);
    }

    /**
     * Aggregate replies to this question
     * @param forumReply
     * The ForumReply to add to the collection of replies
     **/
    public void addReply(ForumReply forumReply) {
        replies.add(forumReply);
    }

    /**
     * Get the number of replies to this question
     * @return numReplies
     * The number of replies that are tied to this question
     * **/
    public int numberOfReplies() {
        return replies.size();
    }
}
