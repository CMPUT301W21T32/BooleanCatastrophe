package com.example.booleancatastrophe.interfaces;

import com.example.booleancatastrophe.model.Experiment;

import java.util.ArrayList;

public interface FirestoreExperimentListCallback {
    void OnCallBack(ArrayList<Experiment> list);
}
