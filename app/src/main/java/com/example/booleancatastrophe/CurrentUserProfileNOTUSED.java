package com.example.booleancatastrophe;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.example.booleancatastrophe.model.UserManager;
import com.example.booleancatastrophe.storage.FirestoreCallback;

/**
 * CURRENTLY UNUSED: would have replaced ExperimentApplication as it was returning null object
 * BUT going to go with splash screen / UI wait activity route instead
 * TODO to finish this class would just need to finish the database update functionality but still suffers from some of the same issues as ExperimentApplication
 *
 * Was having issues with the old system we used of creating a class that extended application -
 * this class represents instantly retrievable, most basic information (account id, username, and
 * email) of the current user that is stored in shared preferences
 * if the full current user object is needed it should be retrieved using the User Manager
 **/

public class CurrentUserProfileNOTUSED {

    static final String PREFERENCE_KEY = "com.example.booleancatastrophe.PREFERENCE_FILE_KEY";
    static final String DEFAULT_VALUE = "";

    static final String PREF_ID = "accountID";
    static final String PREF_USERNAME = "accountUsername";
    static final String PREF_EMAIL = "accountEmail";

    private static CurrentUserProfileNOTUSED currentUserProfileNOTUSED = null;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String currentAccountID;
    private String currentAccountUsername;
    private String currentAccountEmail;

    /** Private constructor - singleton design pattern for this current user profile
    * This object mirrors the three most basic parameters of the User object and should be used to
     * set them (username, email, and account ID)... from here SharedPreferences are used to save
     * the current user for instantly accessible data, there is also functionality to get the current internally updates are sent to the
     * database to keep things in sync
    **/
    private CurrentUserProfileNOTUSED() {

    }

    /* Gets the singleton instance of this current user class - lazy construction */
    public static CurrentUserProfileNOTUSED getInstance() {
        if(currentUserProfileNOTUSED == null) {
            currentUserProfileNOTUSED = new CurrentUserProfileNOTUSED();
        }
        return currentUserProfileNOTUSED;
    }

    /**
     * Retrieve the current user's account ID from shared preferences
     * @param context
     * The context associated with the calling activity or fragment
     **/
    public String getAccountID(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        currentAccountID = sharedPreferences.getString(PREF_ID, DEFAULT_VALUE);

        if(currentAccountID == DEFAULT_VALUE) {
            currentAccountID = generateID();
            setAccountID(context, currentAccountID);
        }

        return currentAccountID;
    }

    /**
     * Set the current user's account ID in shared preferences and update database afterwards, set
     * to private as this functionality should only be needed on seeing a new user for the first time
     * @param context
     * The context associated with the calling activity or fragment
     **/
    private void setAccountID(Context context, String newAccountID) {
        sharedPreferences = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(PREF_ID, newAccountID);
        editor.apply();  // Although asynchronous it should be safe to read immediately and update db
        updateCurrentUserDatabaseEntry();
    }

    /**
     * Retrieve the current user's account username from shared preferences
     * @param context
     * The context associated with the calling activity or fragment
     **/
    public String getUsername(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        currentAccountUsername = sharedPreferences.getString(PREF_USERNAME, DEFAULT_VALUE);
        return currentAccountUsername;
    }

    /**
     * Set the current user's account username in shared preferences and update database afterwards
     * @param context
     * The context associated with the calling activity or fragment
     **/
    public void setUsername(Context context, String newAccountUsername) {
        sharedPreferences = context.getSharedPreferences(PREF_USERNAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(PREF_USERNAME, newAccountUsername);
        editor.apply();  // Although asynchronous it should be safe to read immediately and update db
        updateCurrentUserDatabaseEntry();
    }

    /**
     * Retrieve the current user's account email from shared preferences
     * @param context
     * The context associated with the calling activity or fragment
     **/
    public String getEmail(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        currentAccountEmail = sharedPreferences.getString(PREF_EMAIL, DEFAULT_VALUE);
        return currentAccountEmail;
    }

    /**
     * Set the current user's account email in shared preferences and update database afterwards
     * @param context
     * The context associated with the calling activity or fragment
     **/
    public void setEmail(Context context, String newAccountEmail) {
        sharedPreferences = context.getSharedPreferences(PREF_EMAIL, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(PREF_EMAIL, newAccountEmail);
        editor.apply();  // Although asynchronous it should be safe to read immediately and update db
        updateCurrentUserDatabaseEntry();
    }

    // UNFISHINED
    public void updateCurrentUserDatabaseEntry() {
        UserManager userManager = new UserManager();
        userManager.setEmail(currentAccountID, currentAccountEmail, new FirestoreCallback<Void>() {
            @Override
            public void onCallback(Void databaseResult) {

            }
        });

    }

    /**
     * Function to generate a Pseudo-Unique ID, taken from
     * https://www.pocketmagic.net/android-unique-device-id/
     * @return uID
     * A string that will be the identification for this new user
     **/
    private String generateID() {
        String uID = "35" +
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10 ;
        return(uID);
    }
}
