package com.example.booleancatastrophe.model;

import java.util.ArrayList;

/**
 * Forum Question object that contains a number of replies
 **/
public class ForumQuestion extends ForumPost {

    private ArrayList<ForumReply> replies = new ArrayList<>();

    public ForumQuestion() {}

    public ForumQuestion(Experiment experiment, User poster, String content) {
        super(experiment, poster, content);
    }

    /**
     * This function adds a reply to the list of replies that address this question
     * @param reply
     * The reply to add to this Question's list of replies
     **/
    public void addReply(ForumReply reply) {
        replies.add(reply);
    }

    /**
     * This function gets the replies list that this question holds
     * @return replies
     * The list of replies contained by this question
     **/
    public ArrayList<ForumReply> getReplies() {
        return replies;
    }

    /**
     * This function gets the IDs of the replies in the replies list that this question holds
     * @return ids
     * The list of reply ids contained by this question
     **/
    public ArrayList<String> getReplyIds() {
        ArrayList<String> ids = new ArrayList<>();
        for(int i = 0; i < replies.size(); ++i) {
            ids.add(replies.get(i).getId());
        }
        return ids;
    }

    /**
     * This function gets the number of the replies in the replies list that this question holds
     * @return numReplies
     * The number of replies contained by this question
     **/
    public int getReplyNumber() {
        return replies.size();
    }
}