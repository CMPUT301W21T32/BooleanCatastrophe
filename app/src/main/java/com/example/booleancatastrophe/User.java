package com.example.booleancatastrophe;

import java.util.Collection;

// TODO better error checking in the username and email setters - possibly the owned experiments too
// TODO set the constructor function and get id based on firebase app installation id - figure out how that integration will work
// TODO set up the myCodes functionality (figure out how that should work, if its just simple addition/removal or if more functionality is needed)

/**
 * This is a class that represents an application user
 * @see UserManager as this class works closely with it to keep one user per device, persistent
 * data, no duplicates, etc.
 **/

public class User {
    private String deviceID;
    private String username;
    private String email;

    Collection<Integer> subscriptions;
    Collection<Integer> ownedExperiments;
    Collection<String> myCodes;

    private static final int MAX_USERNAME_LENGTH = 40;
    private static final int MAX_EMAIL_LENGTH = 25;

    /**
     * Constructor setting up a new user which will be tied to a deviceID (need to check with the
     * User Manager if this user already exists)
     **/
    public User() {
        // String id = FirebaseInstallations.getInstance().getID
        // NEED TO WAIT TO SEE HOW THE FIREBASE IS SETUP AND POSSIBLY INCLUDE LOGIC TO AVOID
        // DUPLICATES EITHER HERE OR IN THE MANAGER CLASS
        this.deviceID = id;
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
    public void addSubscription(int experimentID) {
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
    public void removeSubscription(int experimentID) {
        if(!subscriptions.contains(experimentID)) {
            throw new IllegalArgumentException("Cannot remove as the user wasn't subscribed" +
                    "to that experiment");
        } else {
            subscriptions.remove(experimentID);
        }
    }

    /**
     * This adds the experiment to the user's owned list
     * @param experimentID
     * The experiment to add to this user's owned list
     **/
    public void addOwnedExperiment(int experimentID) {
        ownedExperiments.add(experimentID);
    }
}
