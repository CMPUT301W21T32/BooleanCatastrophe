package com.example.booleancatastrophe.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// TODO clean up this class; might have overcomplicated thinking about deletion and editing and there are definitely cleaner ways to handle that functionality


/**
 * This represents objects posted in a forum (can either be questions or replies but have nearly
 * identical functionality - contain User, Date, and content information as well as whether that
 * content has been 'deleted' or 'edited'
 **/
public class ForumPost {

    private LocalDateTime datePosted;
    private User poster;
    private String content;
    private boolean edited = false;
    private boolean deleted = false;

    private static final int MAX_FORUM_POST_LENGTH = 500;
    /**
     * Static function to access the maximum number of characters that a user can enter in any forum
     * question or reply - note that edited posts may have more due to the added " - [edited]" tag
     * @return MAX_FORUM_POST_LENGTH
     * Maximum number of characters in any forum question or reply
     **/
    public static int getMaxForumPostLength() {
        return MAX_FORUM_POST_LENGTH;
    }

    /**
     * The constructor that is used by both questions and replies in the forum
     * @param poster
     * The user that posted the question/reply (not modifiable, hidden if user was deleted or chose
     * to delete this message)
     * @param content
     * The content of the question/reply, can be modified
     **/
    public ForumPost(User poster, String content) {
        //datePosted = LocalDateTime.now();
        this.poster = poster;
        this.content = content;
    }

    /**
     * This function indicates whether the question/reply has been edited
     * @return edited
     * Returns true if the post has been edited and false if not
     **/
    public Boolean wasEdited() {
        return edited;
    }

    /**
     * This function indicates whether the question/reply has been deleted
     * @return deleted
     * Returns true if the post has been deleted and false if not
     **/
    public Boolean wasDeleted() {
        return deleted;
    }

    /**
     * This function returns the content - it adds on a ' - [edited]' tag if it has been edited
     * @return content
     * The content (with a tag indicator added on if it has been edited)
     **/
    public String getContent() {
        if(deleted) {
            return "[deleted]";
        } else {
            if(edited) {
                return content + " - [edited]";
            } else {
                return content;
            }
        }
    }

    /**
     * This edits the content of questions/replies and sets the value of the edited boolean
     * @param editedContent
     * The edited content to add
     **/
    public void setContent(String editedContent) {
        if(editedContent.length() > MAX_FORUM_POST_LENGTH) {
            throw new IllegalArgumentException("Too many characters; exceeded the ForumPost limit");
        } else if(deleted) {
            throw new IllegalArgumentException("The ForumPost has been deleted and cannot be edited");
        } else {
            edited = true;
            content = editedContent;
        }
    }

    /**
     * This function returns the posted date of this question/reply in a string format
     * @return strDate
     * The date formatted into a string format
     **/
    public String getDatePosted() {
        //String strDate = datePosted.format(DateTimeFormatter.RFC_1123_DATE_TIME);
        //return strDate;
        return "temp";
    }

    /**
     * This function gets the username of the User who posted this question/reply
     * @return posterUsername
     * The username of the poster of this question/reply
     **/
    public String getPoster() {
        String posterUsername;
        if(deleted) {
            return "[deleted]";
        } else {
            posterUsername = poster.getUsername();
            return posterUsername;
        }
    }

    /**
     * This function hides (doesn't technically clear) the content of the forum post by putting it
     * in a deleted state and replaces the visible poster/content with a deletion message
     **/
    public void delete() {
        deleted = true;
    }
}
