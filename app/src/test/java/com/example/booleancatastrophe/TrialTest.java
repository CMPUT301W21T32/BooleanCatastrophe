package com.example.booleancatastrophe;

import com.example.booleancatastrophe.model.ExperimentType;
import com.example.booleancatastrophe.model.Trial;

import com.google.firebase.firestore.GeoPoint;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TrialTest {
    //TODO: What should the default constructor even do? What does it mean?

    //TODO: How are trials associated with Experiments in our model? As is, there is no relationship.


    @Test
    public void testMiscellaneous(){
        Trial t1 = new Trial("new", 1, new GeoPoint(0, 0), ExperimentType.COUNT, new Date());

        //Test trials without a Geolocation
        Trial t3 = new Trial("new", 1, null, ExperimentType.COUNT, new Date());
        assertNull(t3.getLocation());
        assertNotEquals(t1, t3);
    }

    @Test
    public void testType(){
        //TODO: There is no checking if the input in a valid range when the trial is created.
        boolean flag = false;
        try {
            Trial t1 = new Trial("a phony", -10, new GeoPoint(1, 5), ExperimentType.NONNEGCOUNT, new Date());

            if(!(t1.getResult().intValue() < 0)){ flag = true; }
        } catch (IllegalArgumentException ex){
            flag = true;
        }

        assertTrue(flag, "A Non-Negative Integer Trial instantiated with a negative number should either cause an error or correct the input value.");
    }

    @Test
    public void testResult(){
        Trial t1 = new Trial("new", 1, new GeoPoint(0, 0), ExperimentType.COUNT, new Date());
        Trial t2 = new Trial("someone", -1, new GeoPoint(0, 0), ExperimentType.COUNT, new Date());

        assertEquals(t1.getResult(), t2.getResult());
    }
}
