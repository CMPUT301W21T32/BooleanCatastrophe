package com.example.booleancatastrophe;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Objects;

/**
 * This class provides serves as the single global storage for all Experiments, Users, and Codes. And for that reason it follows a Singleton pattern
 */
public class FirebaseDatabase {
    private static final FirebaseDatabase instance = new FirebaseDatabase();

    public final HashMap<String, Experiment> experiments = new HashMap<>();
    public final HashMap<String, User> users = new HashMap<>();
    public final HashMap<String, Code> codes = new HashMap<>();

    /**
     * Constructor gets all the data from the firestore database and stores it in the three hashmaps.
     */
    private FirebaseDatabase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            users.put(document.getId(), (User) document.toObject(User.class));
                        }
                    } else {
                        Log.w("ERROR", "Error getting User documents.", task.getException());
                    }
                });

        db.collection("experiments")
                .get()
                .addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            experiments.put(document.getId(), (Experiment) document.toObject(Experiment.class));
                        }
                    } else {
                        Log.w("ERROR", "Error getting Experiment documents.", task.getException());
                    }
                });

        db.collection("codes")
                .get()
                .addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            codes.put(document.getId(), (Code) document.toObject(Code.class));
                        }
                    } else {
                        Log.w("ERROR", "Error getting Code documents.", task.getException());
                    }
                });
    }

    public static FirebaseDatabase getInstance() {
        return instance;
    }


}
