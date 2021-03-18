package com.example.booleancatastrophe.interfaces;

import com.example.booleancatastrophe.model.Trial;

import java.util.ArrayList;

public interface FirestoreTrialListCallback {
    void OnCallBack(ArrayList<Trial> trials);
}
