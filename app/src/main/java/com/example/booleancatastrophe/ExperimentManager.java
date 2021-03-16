package com.example.booleancatastrophe;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

// Class to manage all database operations related to experiments
public class ExperimentManager {

    private static final String TAG = "Experiment Manager";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference experimentRef = db.collection("experiments");


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
    void publish(String eId){
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
     * function to get a list of experiment objects from the database, with an option of returning
     * only published experiments
     * Due to the Asynchronous behaviour it it nessecary to use a callback function, which is called
     * after a successful read
     *
     * @param experimentIDs a list of experiments to get
     * @param onlyPublished if true only published experiments will be returned
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
    void getExperimentList(ArrayList<String> experimentIDs, Boolean onlyPublished, FirestoreExperimentListCallback firestoreCallback) {
        ArrayList<Experiment> experimentList = new ArrayList<Experiment>();
        if (onlyPublished) {
            experimentRef.whereEqualTo("published", true)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    experimentList.add((Experiment) document.toObject(Experiment.class));
                                }
                                firestoreCallback.OnCallBack(experimentList);

                            } else {
                                Log.d(TAG, "Failure getting experiments");
                            }
                        }
                    });
        } else {
            experimentRef
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    experimentList.add((Experiment) document.toObject(Experiment.class));
                                }
                                firestoreCallback.OnCallBack(experimentList);
                            } else {
                                Log.d(TAG, "Failure getting experiments");
                            }
                        }
                    });
        }
    }



    /**
     * function to get a list of experiment objects from the database, with an option of returning
     * only published experiments
     * Due to the Asynchronous behaviour it it nessecary to use a callback function
     *
     * @param eId the id of the experiment
     * @return the experiment, accessible through the callback function -> list.get(0)
     *
     * EXAMPLE USAGE
     *         eManager.getExperiment(experiment.getId(), new FirestoreCallback() {
     *             @Override
     *             public void OnCallBack(ArrayList<Experiment> list) {
     *                    Log.d(TAG, list.get(0).getDescription() );
     *             }
     *         });
     **/
    void getExperiment(String eId, FirestoreExperimentCallback firestoreCallback){
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
                            firestoreCallback.OnCallBack((Experiment) document.toObject(Experiment.class));
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }

                    }
                });
    }

    void getTrials(String eId, FirestoreTrialListCallback firestoreCallback){
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
                            firestoreCallback.OnCallBack(trials);
                        } else {
                            Log.d(TAG, "Failure getting experiments");
                        }
                    }
                });
    }




}
