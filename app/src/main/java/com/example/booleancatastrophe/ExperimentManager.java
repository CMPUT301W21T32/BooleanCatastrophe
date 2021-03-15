package com.example.booleancatastrophe;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExperimentManager {

    private static final String TAG = "Experiment Manager";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference experiments = db.collection("experiments");


    /**
     * function to add an experiment to the database
     * the experiment id will be determined by firestore
     * @param exp the experiment object to be added
     **/
    void addExperiment(Experiment exp){
        //make new document
        DocumentReference newRef = db.collection("experiments").document();
        //set the experiment id for later reference
        exp.setId(newRef.getId());
        //add object to the database
        newRef.set(exp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully added experiment");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Couldn't add experiment");
                    }
                });
    }


    /**
     * function to add a trial to any given experiment
     * @param eId the id of the experiment
     * @param trial the trial object that will be added
     **/
    void addTrial(String eId, Trial trial){
       experiments.document(eId).collection("trials")
               .add(trial).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
           @Override
           public void onSuccess(DocumentReference documentReference) {
               Log.d(TAG, "Successfully added trial");
           }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Couldn't add trial");
                }
            });
    }
}
