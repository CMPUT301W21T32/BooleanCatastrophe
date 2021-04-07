package com.example.booleancatastrophe.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.booleancatastrophe.storage.FirestoreCallback;
import com.example.booleancatastrophe.storage.Database;

import com.example.booleancatastrophe.utils.UniversalSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

// Class to manage all database operations related to experiments
public class ExperimentManager {

    private static final String TAG = "Experiment Manager";
    private final FirebaseFirestore db = Database.getInstance();
    private final CollectionReference experimentRef = db.collection("experiments");

    /**
     * function to add an experiment to the database
     * the experiment id will be determined by firestore
     * and the experiment will also be added to the users "ownedExperiments"
     * @param exp the experiment object to be added
     **/
    public void addExperiment(Experiment exp){
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
                        // On success add the experiment ID to the owners "ownedExperiments"
                        db.collection("users").document(exp.getOwner())
                                .update("owned",  FieldValue.arrayUnion(exp.getId()));
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
    //TODO: automatically subscribe the user after submitting a trial

    public void addTrial(String eId, Trial trial){
       experimentRef.document(eId).collection("trials")
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

    /**
     * publish the given experiement
     * activity should confirm that current_user == owner before allowing publishing
     * @param eId the id of the experiment
     **/
    public void publish(String eId){
        experimentRef.document(eId).update("published", true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Experiment has been published");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Experiemnt failed to be published");
            }
        });
    }

    /**
     * unpublish the given experiement
     * activity should confirm that current_user == owner before allowing publishing/unpublishing
     * @param eId the id of the experiment
     **/
    public void unpublish(String eId){
        experimentRef.document(eId).update("published", false).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Experiment has been unpublished");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Experiemnt failed to be unpublished");
                    }
                });
    }

    //function to end the given experiment
    public void end(String eID) {};

    /**
     * function to get a list of experiment objects from the database, with an option of returning
     * only published experiments
     * Due to the Asynchronous behaviour it it nessecary to use a callback function, which is called
     * after a successful read
     *
     * @param experimentIDs a list of experiments to get
     * @param firestoreCallback defines the function that is called when the read is completed
     * @return the list of experiments, accessible through the callback function
     *
     * EXAMPLE USAGE
     *         eManager.getExperimentList(idList, true, new FirestoreCallback() {
     *             @Override
     *             public void OnCallBack(ArrayList<Experiment> list) {
     *                 for(int i = 0; i < list.size(); i++){
     *                     Log.d(TAG, list.get(i).getDescription() );
     *                 }
     *             }
     *         });
     **/
    public void getExperimentList(ArrayList<String> experimentIDs, FirestoreCallback<ArrayList<Experiment>> firestoreCallback) {
        Set<String> idsToFind;
        if(experimentIDs != null){
            idsToFind = new HashSet<>(experimentIDs);
        }
        else{
            idsToFind = new UniversalSet();
        }
        ArrayList<Experiment> experimentList = new ArrayList<Experiment>();
        experimentRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Experiment ex = (Experiment) document.toObject(Experiment.class);
                                if(idsToFind.contains(ex.getId())){
                                    experimentList.add(ex);
                                }
                            }
                            firestoreCallback.onCallback(experimentList);

                        } else {
                            Log.d(TAG, "Failure getting experiments");
                        }
                    }
                });

    }

    /**
     * function to get a single experiment object from the database
     *
     * Due to the Asynchronous behaviour it it nessecary to use a callback function
     *
     * @param eId the id of the experiment
     * @param firestoreCallback defines the function that is called when the read is completed
     * @return the experiment, accessible through the callback function
     *
     * EXAMPLE USAGE
     *         eManager.getExperiment(experiment.getId(), new FirestoreExperimentCallback() {
     *             @Override
     *             public void OnCallBack(Experiment exp) {
     *                    Log.d(TAG, exp.getDescription() );
     *             }
     *         });
     **/
    public void getExperiment(String eId, FirestoreCallback<Experiment> firestoreCallback){
        experimentRef.document(eId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "Found document");
                            } else {
                                Log.d(TAG, "No such document");
                            }
                            firestoreCallback.onCallback((Experiment) document.toObject(Experiment.class));
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }

                    }
                });
    }

    /**
     * function to get a list of trials that belong to the given experiment
     *
     * Due to the Asynchronous behaviour it it nessecary to use a callback function
     *
     * @param eId the id of the experiment
     * @param firestoreCallback defines the function that is called when the read is completed
     * @return the experiment, accessible through the callback function
     *
     * EXAMPLE USAGE
     *         eManager.getExperiment(experiment.getId(), new FirestoreExperimentCallback() {
     *             @Override
     *             public void OnCallBack(Experiment exp) {
     *                    Log.d(TAG, exp.getDescription() );
     *             }
     *         });
     **/
    public void getTrials(String eId, FirestoreCallback<ArrayList<Trial>> firestoreCallback){
        ArrayList<Trial> trials = new ArrayList<>();
        experimentRef
                .document(eId)
                .collection("trials")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                trials.add((Trial) document.toObject(Trial.class));
                            }
                            firestoreCallback.onCallback(trials);
                        } else {
                            Log.d(TAG, "Failure getting experiments");
                        }
                    }
                });
    }

    //void search(String token){ }

}