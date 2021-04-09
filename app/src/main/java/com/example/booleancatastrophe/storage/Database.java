package com.example.booleancatastrophe.storage;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

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


    //Could be modified to enable batch requests in parallel, but I don't have time right now
    public static <T> T synchronousDBQuery(Consumer<FirestoreCallback<T>> dbAccessFunction) {
        //Credits to qwertzguy (https://stackoverflow.com/users/965176/qwertzguy)
        //on StackOverflow     (https://stackoverflow.com/a/49523790)
        //for the method of forcing a thread to block on a condition
        CountDownLatch barrier = new CountDownLatch(1);

        AtomicReference<T> dbResultHolder = new AtomicReference<>();

        dbAccessFunction.accept((databaseResult)->{
            dbResultHolder.set(databaseResult);
            barrier.countDown();
        });

        //Credits to jdmichal (https://stackoverflow.com/users/12275/jdmichal)
        //on StackOverflow    (https://stackoverflow.com/a/3168465)
        //for the references on gracefully handling InterruptedExceptions
        try {
            barrier.await();
        } catch (InterruptedException e) {
            //Interrupted flag is not set when this exception is thrown
            Thread.currentThread().interrupt();
        }
        return dbResultHolder.get();
    }
}
