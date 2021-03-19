package com.example.booleancatastrophe.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.booleancatastrophe.storage.FirestoreCallback;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserManager {

    private static final String TAG = "User Manager";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection("users");
    public static User currentUser;


    /**
     * add a user to the data base, call the first time a user opens the app
     * @param u the user to be added
     **/
    public void addUser(User u){
        usersRef.document(u.getAccountID()).set(u)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Added new user " + u.getAccountID());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Couldn't add user " + u.getAccountID());
                    }
                });

    }

    /**
     * get a user object from the data base
     * @param accountID the id of the user
     * @param firestoreCallback defines the function called when the database operation is complete
     **/
    public void getUser(String accountID, FirestoreCallback<User> firestoreCallback) {
        usersRef.document(accountID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, "Found document");
                        } else {
                            Log.d(TAG, "No such document");
                        }
                        //should return a null object if the user does not exist
                        firestoreCallback.onCallback((User) documentSnapshot.toObject(User.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "exception", e);
                    }
                });
    }

    /**
     * set the email of a user
     * @param accountID the id of the user
     * @param email the new value of email
     **/
    public void setEmail(String accountID, String email){
        usersRef.document(accountID).update("email", email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Email has been changed");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Email update", e);
                    }
                });
    }

    /**
     * set the username of a user
     * @param accountID the id of the user
     * @param username the new value of username
     **/
    public void setUsername(String accountID, String username){
        usersRef.document(accountID).update("username", username)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Username has been changed");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Username update", e);
                    }
                });
    }


    /**
     * add an experiment to your subscriptions
     * @param accountID the id of the user
     * @param experimentID the experiment to be added
     **/
    public void subsribe(String accountID, String experimentID) {
        usersRef.document(accountID).update("subscriptions", FieldValue.arrayUnion(experimentID))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Subscribed to experiment");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Subscription failed", e);
                    }
                });
    }

    /**
     * remove an experiment from your subscriptions
     * @param accountID the id of the user
     * @param experimentID the experiment to be removed
     **/
    public void unsubscribe(String accountID, String experimentID) {
        usersRef.document(accountID).update("subscriptions", FieldValue.arrayRemove(experimentID))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "unsubscribed from experiment");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "unsubscription failed", e);
                    }
                });
    }



}
