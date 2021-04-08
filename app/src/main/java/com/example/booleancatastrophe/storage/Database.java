package com.example.booleancatastrophe.storage;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

/**
 * Class which should be used to gain database accesses in this app.
 */
public class Database {

    private static FirebaseFirestore instance;

    /**
     * Credits to Ilya Shinkarenko       (https://stackoverflow.com/users/842744/ilya-shinkarenko)
     *            jokki                  (https://stackoverflow.com/users/219455/jokki)
     *            Darek Deoniziak        (https://stackoverflow.com/users/4911442/darek-deoniziak)
     * on StackOverflow, respectively at (https://stackoverflow.com/a/21369622)
     *                                   (https://stackoverflow.com/questions/21367646#comment53449966_21369622)
     *                                   (https://stackoverflow.com/a/30299889)
     * for their tips on checking at runtime if an Android application is running in a test context
     */
    private static boolean isRunningInTests(){
        try {
            Class.forName("com.example.booleancatastrophe.InstrumentationTestIndicator");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static FirebaseFirestore getInstance(){
        if(instance != null){
            return instance;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(isRunningInTests()){
            db.useEmulator("10.0.2.2", 8080);
            db.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(false).build());
        }
        instance = db;
        return instance;
    }
}
