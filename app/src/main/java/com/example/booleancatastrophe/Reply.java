package com.example.booleancatastrophe;


/**
 * This represents replies that will be nested under questions in the forum
 **/
public class Reply extends ForumPost {

    /**
     * Constructor for replies that calls its parent constructor ForumPost
     * @param poster
     * The user who posted this reply
     * @param content
     * The initial content of this reply
     **/
    public Reply(User poster, String content) {
        super(poster, content);
    }
}
