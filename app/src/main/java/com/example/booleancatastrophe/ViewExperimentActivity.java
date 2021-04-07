package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.booleancatastrophe.interfaces.FirestoreTrialListCallback;
import com.example.booleancatastrophe.interfaces.FirestoreUserCallback;
import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentManager;
import com.example.booleancatastrophe.model.ExperimentType;
import com.example.booleancatastrophe.model.Trial;
import com.example.booleancatastrophe.model.User;
import com.example.booleancatastrophe.model.UserManager;

import java.util.ArrayList;

//Activity to view the experiment that was clicked
//TODO forum, owner tools, statistics
public class ViewExperimentActivity extends AppCompatActivity implements NewTrialFragment.OnFragmentInteractionListener {

    private Experiment currentExperiment;
    private ArrayList<Trial> currentTrials;
    private User currentUser;
    private ExperimentManager eManager = new ExperimentManager();
    private UserManager userManager = new UserManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_experiment);

        //user = ((ExperimentApplication) this.getApplication()).getCurrentUser();
        TextView usernameText = (TextView) findViewById(R.id.usernameText);
        TextView descriptionText = (TextView) findViewById(R.id.descriptionText);
        TextView regionText = (TextView) findViewById(R.id.regionText);
        TextView trialCountText = (TextView) findViewById(R.id.trialCountText);
        Button newTrialButton = (Button) findViewById(R.id.newTrialButton);

        //get experiment data (eventually) through intent
        //TODO Check that this bundle/intent loading to get experiment is working, add else case if the extra received is null
//        currentExperiment = new Experiment("Coin Flip", "AB", "Braden", 10, ExperimentType.MEASUREMENT);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            currentExperiment = (Experiment) getIntent().getSerializableExtra("experiment");
        }

        //get trial data from DB
        eManager.getTrials(currentExperiment.getId(), new FirestoreTrialListCallback() {
            @Override
            public void OnCallBack(ArrayList<Trial> trials) {
                currentTrials = trials;
                String trialCount = ((Integer)trials.size()).toString();
                String minTrials = ((Integer) currentExperiment.getMinTrials()).toString();
                String display = trialCount + " / " + minTrials + " trials completed";
                trialCountText.setText(display);
            }
        });

        userManager.getUser(((ExperimentApplication) this.getApplication()).getAccountID(), new FirestoreUserCallback() {
            @Override
            public void OnCallBack(User user) {
                currentUser = user;
                usernameText.setText( currentUser.getUsername() );
            }
        });

        descriptionText.setText( currentExperiment.getDescription() );
        regionText.setText(currentExperiment.getRegion());

        newTrialButton.setOnClickListener((v) -> {
            new NewTrialFragment().show(getSupportFragmentManager(), "ADD_TRIAL");
        });
    }

    public Experiment getCurrentExperiment(){
        return currentExperiment;
    }

    //Adds a new trial to the experiment
    public void onOkPressed(Trial newTrial){
        eManager.addTrial(currentExperiment.getId(), newTrial);

    }


}