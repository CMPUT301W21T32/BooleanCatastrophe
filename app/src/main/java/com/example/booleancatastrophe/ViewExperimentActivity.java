package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.booleancatastrophe.interfaces.FirestoreTrialListCallback;
import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentManager;
import com.example.booleancatastrophe.model.ExperimentType;
import com.example.booleancatastrophe.model.Trial;
import com.example.booleancatastrophe.model.User;
import com.example.booleancatastrophe.model.UserManager;

import java.util.ArrayList;

public class ViewExperimentActivity extends AppCompatActivity {

    private Experiment currentExperiment;
    private ArrayList<Trial> currentTrials;
    private User user;
    private ExperimentManager eManager = new ExperimentManager();
    private UserManager userManager = new UserManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_experiment);

        TextView usernameText = (TextView) findViewById(R.id.usernameText);
        TextView descriptionText = (TextView) findViewById(R.id.descriptionText);
        TextView regionText = (TextView) findViewById(R.id.regionText);
        TextView trialCountText = (TextView) findViewById(R.id.trialCountText);

        //get experiment data through intent
        currentExperiment = new Experiment("Coin Flip", "AB", "Braden", 10, ExperimentType.BINOMIAL);
        //get trial data from DB
        eManager.getTrials("Test", new FirestoreTrialListCallback() {
            @Override
            public void OnCallBack(ArrayList<Trial> trials) {
                currentTrials = trials;
                String trialCount = ((Integer)trials.size()).toString();
                String minTrials = ((Integer) currentExperiment.getMinTrials()).toString();
                String display = trialCount + " / " + minTrials + " trials completed";
                trialCountText.setText(display);
            }
        });

        usernameText.setText( user.getUsername() );
        descriptionText.setText( currentExperiment.getDescription() );
        regionText.setText(currentExperiment.getRegion());


    }
}