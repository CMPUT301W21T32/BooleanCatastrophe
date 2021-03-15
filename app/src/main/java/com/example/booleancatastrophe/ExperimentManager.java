package com.example.booleancatastrophe;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExperimentManager {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    /**
     * function to add an experiment to the database
     * the experiment id will be determined by firestore
     *
     **/
    void addExperiment(Experiment exp){
        //make new document
        DocumentReference newRef = db.collection("experiments").document();
        //set the experiment id for later refrence
        exp.setId(newRef.getId());
        //add object to the database
        newRef.set(exp);



    }

    void addTrial(int e_id, Trial trial){

    }
}
