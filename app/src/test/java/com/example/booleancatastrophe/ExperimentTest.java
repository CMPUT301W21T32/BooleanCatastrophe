package com.example.booleancatastrophe;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExperimentTest {

    private Experiment e1;
    private Experiment e2;

    @BeforeEach
    public void makeExperiments(){
        e1 = new Experiment("First Experiment", "Greater Toronto Area",
                "Radomir", 5, ExperimentType.BINOMIAL);
        e2 = new Experiment("Second Experiment", "Victoria",
                "Radomir", 5, ExperimentType.MEASUREMENT);
    }

    //TODO: What should the default constructor even do? What does it mean?

    @Test
    public void testID(){
        //TODO: ID should be assigned on instance creation and made immutable
        //assertNotEquals(e1.getId(), e2.getId());

        e1.setId("ID ex1");

        assertNotEquals(e1.getId(), e2.getId());

        assertEquals("ID ex1", e1.getId());

        e1.setId("ID ex1.1");
        assertEquals("ID ex1.1", e1.getId());

        //TODO: No two experiments should EVER have the same ID
        //assertThrows(IllegalArgumentException.class, ()->{ e1.setId(e2.getId()); });
    }

    @Test
    public void testRegion(){
        assertEquals("Greater Toronto Area", e1.getRegion());
        assertEquals("Victoria", e2.getRegion());

        e1.setRegion("That Street, Porter's Lake, Nova Scotia");
        assertEquals("That Street, Porter's Lake, Nova Scotia", e1.getRegion());
        assertEquals("Victoria", e2.getRegion());
    }

    @Test
    public void testDescription(){
        assertEquals("First Experiment", e1.getDescription());
        assertEquals("Second Experiment", e2.getDescription());

        e1.setDescription("A better description");
        assertEquals("A better description", e1.getDescription());
        assertEquals("Second Experiment", e2.getDescription());
    }

    @Test
    public void testOwner(){
        assertEquals("Radomir", e1.getOwner());
        assertEquals("Radomir", e2.getOwner());

        e2.setOwner("E.T.");
        assertEquals("Radomir", e1.getOwner());
        assertEquals("E.T.", e2.getOwner());
    }

    @Test
    public void testType(){
        assertEquals(ExperimentType.BINOMIAL, e1.getType());
        assertEquals(ExperimentType.MEASUREMENT, e2.getType());

        //TODO: Makes no sense to be allowed.
        //      Invalidates all trials in the database currently associated with the experiment.
        e2.setType(ExperimentType.COUNT);
        assertEquals(ExperimentType.BINOMIAL, e1.getType());
        assertEquals(ExperimentType.COUNT, e2.getType());
    }

    @Test
    public void testPublishing(){
        assertFalse(e1.isPublished());
        assertFalse(e2.isPublished());

        //TODO: No way to publish an experiment
    }
}
