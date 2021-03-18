package com.example.booleancatastrophe.model;

import java.util.ArrayList;

// TODO better error checking in the username and email setters - possibly the owned experiments too
// TODO make sure integration works with UserManager and also figure out how setting email/username will work with the manager
// TODO set up the myCodes functionality (figure out how that should work, if its just simple addition/removal or if more functionality is needed)

/**
 * This is a class that represents an application user
 * @ see UserManager as this class works closely with it to keep one user per device, persistent
 * data, no duplicates, etc.
 **/

public class User {
    private String accountID; //changed from device ID
    private String username;
    private String email;

    private ArrayList<String> subscriptions;
    private ArrayList<String> ownedExperiments;
    private ArrayList<String> myCodes;

    private static final int MAX_USERNAME_LENGTH = 40;
    private static final int MAX_EMAIL_LENGTH = 25;

    public User() {}

    /**
     * Constructor setting up a new user which will be tied to a firestore installation ID - this constructor will
     * be called by UserManager and the ID will be generated and passed from there
     * Username and email fields will initially be set up as blank strings until the user edits
     * their profile later
     * @param id
     * The device/installation id tied to this user account
     **/
    public User(String id) {
        this.accountID = id;
        this.email = "";
        this.username = "";
        subscriptions = new ArrayList<String>();
        ownedExperiments = new ArrayList<String>();
        myCodes = new ArrayList<String>();
    }

    /**
     * This is the getter for the User's id
     * @return
     * Returns the User's id
     **/
    public String getAccountID() {
        return accountID;
    }

    /**
     * This is the getter for the User's username
     * @return
     * Returns the User's username
     **/
    public String getUsername() {
        return username;
    }

    /**
     * This is the getter for the User's email
     * @return
     * Returns the User's email
     **/
    public String getEmail() {
        return email;
    }


    public ArrayList<String> getSubscriptions(){
        return subscriptions;
    }

    public ArrayList<String> getOwnedExperiments(){
        return ownedExperiments;
    }

    public ArrayList<String> getMyCodes() {
        return myCodes;
    }

    /**
     * This is the setter for the User's email; error checking is done
     * for length and other improper data entry
     * @param email
     * The username to set for this User account
     **/
    public void setEmail(String email) {
        if(email.length() > MAX_EMAIL_LENGTH) {
            throw new IllegalArgumentException("Email exceeds the maximum allowed length");
        } else {
            this.email = email;
        }
    }

    /**
     * This is the setter for the User's username; error checking should be done
     * for length and other improper data entry
     * @param username
     * The username to set for this User account
     **/
    public void setUsername(String username) {
        if(username.length() > MAX_USERNAME_LENGTH) {
           throw new IllegalArgumentException("Username exceeds the maximum allowed length");
        } else {
            this.username = username;
        }
    }

    /**
     * This adds the experiment to the user's subscription list if they aren't subscribed to it yet
     * @param experimentID
     * The experiment to add to this user's subscription list
     **/
    public void addSubscription(String experimentID) {
        if(subscriptions.contains(experimentID)) {
            throw new IllegalArgumentException("Cannot add as the user is already " +
                    "subscribed to that experiment");
        } else {
            subscriptions.add(experimentID);
        }
    }

    /**
     * This removes the experiment from the user's subscription list if they were subscribed to it
     * @param experimentID
     * The experiment to remove from this user's subscription list
     **/
    public void removeSubscription(String experimentID) {
        if(!subscriptions.contains(experimentID)) {
            throw new IllegalArgumentException("Cannot remove as the user wasn't subscribed" +
                    "to that experiment");
        } else {
            subscriptions.remove(experimentID);
        }
    }

    /**
     * This adds the experiment to the user's owned list - note that User's won't be able to un-own
     * experiments once they have been started although they may be unpublished
     * @param experimentID
     * The experiment to add to this user's owned list
     **/
    public void addOwnedExperiment(String experimentID) {
        ownedExperiments.add(experimentID);
    }

    /**
     * This adds the code to the user's account
     * @param code
     * The code to add to this user's saved codes collection
     **/
    public void addCode(String code) {
        if(myCodes.contains(code)) {
            throw new IllegalArgumentException("Cannot add as the user already has that code");
        } else {
            myCodes.add(code);
        }
    }

    /**
     * This removes the code from the user's account
     * @param code
     * The code to remove from this user's saved codes collection
     **/
    public void removeCode(String code) {
        if(!myCodes.contains(code)) {
            throw new IllegalArgumentException("Cannot remove as the user didn't have that code");
        } else {
            myCodes.remove(code);
        }
    }
}