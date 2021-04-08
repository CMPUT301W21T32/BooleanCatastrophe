package com.example.booleancatastrophe.storage;

public interface FirestoreCallback<T> {
    public void onCallback(T databaseResult);
}
