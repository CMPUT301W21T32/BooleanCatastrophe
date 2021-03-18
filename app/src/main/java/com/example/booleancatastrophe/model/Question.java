package com.example.booleancatastrophe.model;


import java.util.Collection;


public class Question extends ForumPost {
    Collection<Reply> replies;

    /**
     * Constructor for questions that calls its parent constructor ForumPost
     * @param poster
     * The user who posted this question
     * @param content
     * The initial content of this question
     **/
    public Question(User poster, String content) {
        super(poster, content);
    }

    /**
     * Aggregate replies to this question
     * @param reply
     * The reply to add to the collection of replies
     **/
    public void addReply(Reply reply) {
        replies.add(reply);
    }
}
