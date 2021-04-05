package com.example.booleancatastrophe.model;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/** This class handles the database operations related to experiment forums */
public class ForumManager {

    private static ForumManager forumManager = null;

    private FirebaseFirestore db;
    private CollectionReference forumpostRef;
    private static final String TAG = "Forum Manager";

    /* Private constructor - singleton design pattern */
    private ForumManager() {
        this.db = FirebaseFirestore.getInstance();
        this.forumpostRef = db.collection("forumposts");
    }

    /* Gets the singleton instance of this manager class - lazy construction */
    public static ForumManager getInstance() {
        if(forumManager == null) {
            forumManager = new ForumManager();
        }
        return forumManager;
    }

    /** Add a forum post to the database - id will be created automatically
     * @param forumPost
     * The post to add to the database
     **/
    public void addForumPost(ForumPost forumPost) {
        DocumentReference docRef = forumpostRef.document();
        // Set forum post id
        forumPost.setId(docRef.getId());
        docRef.set(forumPost)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully added forum post");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding forum post");
                    }
                });
    }

    public void






}
