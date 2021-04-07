package com.example.booleancatastrophe;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.booleancatastrophe.storage.FirestoreCallback;
import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentManager;

import com.example.booleancatastrophe.model.ExperimentType;
import com.example.booleancatastrophe.model.Trial;
import com.example.booleancatastrophe.storage.Database;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

//import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@RunWith(AndroidJUnit4.class)
public class ExperimentManagerTest {

    private ExperimentManager em;
    private Experiment e1;
    private Experiment e2;

    //Could be modified to enable batch requests in parallel, but I don't have time right now
    public <T> T synchronousDBQuery(Consumer<FirestoreCallback<T>> dbAccessFunction) {
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

    private void getAllExperiments(FirestoreCallback<ArrayList<Experiment>> threadUnblocker){
        em.getExperimentList(null, threadUnblocker);
    }

    private Consumer<FirestoreCallback<ArrayList<Trial>>> getTrialsForExperiment(String exID){
        return (threadUnblocker)->{em.getTrials(exID, threadUnblocker);};
    }

    @BeforeClass
    public static void setUpDB(){
        Database.getInstance();
    }

    @Before
    public void makeExperimentManager() {
        //Clear database (really quick, incomplete implementation)
        //Maybe necessary later: https://firebase.google.com/docs/firestore/manage-data/delete-data?hl=en#collections
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> t = synchronousDBQuery((threadUnblocker)->{
            db.collection("experiments").limit(50).get().addOnCompleteListener(threadUnblocker::onCallback);
        });
        if(t.getResult() != null){
            for(DocumentSnapshot d : t.getResult().getDocuments()){
                synchronousDBQuery((threadUnblocker)->{
                    d.getReference().delete().addOnCompleteListener(threadUnblocker::onCallback);
                });
            }
        }

        //Instantiate
        em = new ExperimentManager();
        assertTrue(synchronousDBQuery(this::getAllExperiments).isEmpty());
    }

    @Before
    public void makeExperiments(){
        e1 = new Experiment("First Experiment", "Greater Toronto Area",
                "Radomir", 5, ExperimentType.BINOMIAL);
        e2 = new Experiment("Second Experiment", "Victoria",
                "Radomir", 5, ExperimentType.MEASUREMENT);
    }

    //TODO: Why doesn't ExperimentManager use FirebaseDatabase class?

    @Test
    public void testExperiment(){
        ArrayList<Experiment> theExperiments = null;

        em.addExperiment(e1);

        theExperiments = synchronousDBQuery(this::getAllExperiments);
        assertEquals(1, theExperiments.size());
        //TODO: Probably need to override equals() method from Object before this will work
        assertEquals(theExperiments.get(0), e1);

        //TODO: Identical experiments?
        //      Is there a feasible way to differentiate the same instance
        //      from one that has the same field values, across different runs of the program
        //      (because the database is the only persistent storage)
        //assertThrows(IllegalArgumentException.class, ()->{em.addExperiment(e1);});

        em.addExperiment(e2);

        theExperiments = synchronousDBQuery(this::getAllExperiments);
        assertEquals(2, theExperiments.size());
        assertTrue(theExperiments.contains(e1));
        assertTrue(theExperiments.contains(e2));

        assertEquals(e2,
                synchronousDBQuery((Consumer<FirestoreCallback<Experiment>>)
                        (threadUnblocker -> {
                            em.getExperiment(e2.getId(), threadUnblocker);
                        }))
        );
    }

    @Test
    public void testPublishing(){
        ArrayList<Experiment> theExperiments;
        em.addExperiment(e1);

        em.publish(e1.getId());

        theExperiments = synchronousDBQuery(this::getAllExperiments);
        assertTrue(theExperiments.get(0).getPublished());

        assertTrue(e1.getPublished());
    }

    @Test
    public void testTrials(){
        ArrayList<Trial> theTrials;
        em.addExperiment(e1);

        //Test freshly-added experiment
        theTrials = synchronousDBQuery(this.getTrialsForExperiment(e1.getId()));
        assertTrue(theTrials.isEmpty());

        //Test non-existent experiment ID
        try{
            synchronousDBQuery(this.getTrialsForExperiment(e2.getId()));
            fail("Expected to fail searching for trials with an empty experiment ID");
        }
        catch (IllegalArgumentException ex){}

        e2.setId("a");
        theTrials = synchronousDBQuery(this.getTrialsForExperiment(e2.getId()));
        assertTrue(theTrials.isEmpty());

        //Test adding multiple trials
        em.addTrial(e1.getId(), new Trial(e1.getOwner(), 1, null, ExperimentType.BINOMIAL));
        theTrials = synchronousDBQuery(this.getTrialsForExperiment(e1.getId()));
        assertEquals(1, theTrials.size());

        em.addTrial(e1.getId(), new Trial("A witch", 0, new GeoPoint(0, 0), ExperimentType.BINOMIAL));
        theTrials = synchronousDBQuery(this.getTrialsForExperiment(e1.getId()));
        assertEquals(2, theTrials.size());

        //Test adding bogus trials
        //TODO: Would it be better to throw an exception?
        em.addTrial(e1.getId(), new Trial("The big bad wolf", 10, null, ExperimentType.NONNEGCOUNT));
        theTrials = synchronousDBQuery(this.getTrialsForExperiment(e1.getId()));
        assertEquals(2, theTrials.size());

        //Test adding trials to non-existent experiment ID
        //TODO: Would it be better to throw an exception?
        em.addTrial(e2.getId(), new Trial("A wizard", 36.6, null, ExperimentType.MEASUREMENT));
        theTrials = synchronousDBQuery(this.getTrialsForExperiment(e2.getId()));
        assertTrue(theTrials.isEmpty());
    }
}
