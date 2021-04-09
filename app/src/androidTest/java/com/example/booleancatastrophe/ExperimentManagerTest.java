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
        Task<QuerySnapshot> t = Database.synchronousDBQuery((threadUnblocker)->{
            Database.getInstance().collection("experiments").limit(50).get().addOnCompleteListener(threadUnblocker::onCallback);
        });
        if(t.getResult() != null){
            for(DocumentSnapshot d : t.getResult().getDocuments()){
                Database.synchronousDBQuery((threadUnblocker)->{
                    d.getReference().delete().addOnCompleteListener(threadUnblocker::onCallback);
                });
            }
        }

        //Instantiate
        em = new ExperimentManager();
        assertTrue(Database.synchronousDBQuery(this::getAllExperiments).isEmpty());
    }

    @Before
    public void makeExperiments(){
        e1 = new Experiment("First Experiment", "Greater Toronto Area",
                "Radomir", 5, ExperimentType.BINOMIAL.name());
        e2 = new Experiment("Second Experiment", "Victoria",
                "Radomir", 5, ExperimentType.MEASUREMENT.name());
    }

    //TODO: Why doesn't ExperimentManager use FirebaseDatabase class?

    @Test
    public void testExperiment(){
        ArrayList<Experiment> theExperiments = null;

        em.addExperiment(e1);

        theExperiments = Database.synchronousDBQuery(this::getAllExperiments);
        assertEquals(1, theExperiments.size());
        //TODO: Probably need to override equals() method from Object before this will work
        assertEquals(theExperiments.get(0), e1);

        //TODO: Identical experiments?
        //      Is there a feasible way to differentiate the same instance
        //      from one that has the same field values, across different runs of the program
        //      (because the database is the only persistent storage)
        //assertThrows(IllegalArgumentException.class, ()->{em.addExperiment(e1);});

        em.addExperiment(e2);

        theExperiments = Database.synchronousDBQuery(this::getAllExperiments);
        assertEquals(2, theExperiments.size());
        assertTrue(theExperiments.contains(e1));
        assertTrue(theExperiments.contains(e2));

        assertEquals(e2,
                Database.synchronousDBQuery((Consumer<FirestoreCallback<Experiment>>)
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

        theExperiments = Database.synchronousDBQuery(this::getAllExperiments);
        assertTrue(theExperiments.get(0).getPublished());

        assertTrue(e1.getPublished());
    }

    @Test
    public void testTrials(){
        ArrayList<Trial> theTrials;
        em.addExperiment(e1);

        //Test freshly-added experiment
        theTrials = Database.synchronousDBQuery(this.getTrialsForExperiment(e1.getId()));
        assertTrue(theTrials.isEmpty());

        //Test non-existent experiment ID
        try{
            Database.synchronousDBQuery(this.getTrialsForExperiment(e2.getId()));
            fail("Expected to fail searching for trials with an empty experiment ID");
        }
        catch (IllegalArgumentException ex){}

        e2.setId("a");
        theTrials = Database.synchronousDBQuery(this.getTrialsForExperiment(e2.getId()));
        assertTrue(theTrials.isEmpty());

        //Test adding multiple trials
        em.addTrial(e1.getId(), new Trial(e1.getOwnerID(), 1, null, ExperimentType.BINOMIAL));
        theTrials = Database.synchronousDBQuery(this.getTrialsForExperiment(e1.getId()));
        assertEquals(1, theTrials.size());

        em.addTrial(e1.getId(), new Trial("A witch", 0, new GeoPoint(0, 0), ExperimentType.BINOMIAL));
        theTrials = Database.synchronousDBQuery(this.getTrialsForExperiment(e1.getId()));
        assertEquals(2, theTrials.size());

        //Test adding bogus trials
        //TODO: Would it be better to throw an exception?
        em.addTrial(e1.getId(), new Trial("The big bad wolf", 10, null, ExperimentType.NONNEGCOUNT));
        theTrials = Database.synchronousDBQuery(this.getTrialsForExperiment(e1.getId()));
        assertEquals(2, theTrials.size());

        //Test adding trials to non-existent experiment ID
        //TODO: Would it be better to throw an exception?
        em.addTrial(e2.getId(), new Trial("A wizard", 36.6, null, ExperimentType.MEASUREMENT));
        theTrials = Database.synchronousDBQuery(this.getTrialsForExperiment(e2.getId()));
        assertTrue(theTrials.isEmpty());
    }
}
