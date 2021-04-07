package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentManager;
import com.example.booleancatastrophe.model.Trial;
import com.example.booleancatastrophe.model.User;
import com.example.booleancatastrophe.model.UserManager;
import com.example.booleancatastrophe.storage.FirestoreCallback;

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
        Button btnViewExperimentForum = (Button) findViewById(R.id.btn_experiment_forum);

        // Get the current experiment data through the intent
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            currentExperiment = (Experiment) getIntent().getSerializableExtra("experiment");
        }

        //get trial data from DB
        eManager.getTrials(currentExperiment.getId(), new FirestoreCallback<ArrayList<Trial>>() {
            @Override
            public void onCallback(ArrayList<Trial> trials) {
                currentTrials = trials;
                String trialCount = ((Integer)trials.size()).toString();
                String minTrials = ((Integer) currentExperiment.getMinTrials()).toString();
                String display = trialCount + " / " + minTrials + " trials completed";
                trialCountText.setText(display);
            }
        });

        userManager.getUser(((ExperimentApplication) this.getApplication()).getAccountID(),
                (User user) -> {
                    currentUser = user;
                    usernameText.setText( currentUser.getUsername() );
        });

        descriptionText.setText( currentExperiment.getDescription() );
        regionText.setText(currentExperiment.getRegion());

        newTrialButton.setOnClickListener((v) -> {
            new NewTrialFragment().show(getSupportFragmentManager(), "ADD_TRIAL");
        });

        /* Go to the experiment question forum activity if this button is clicked */
        btnViewExperimentForum.setOnClickListener((v) -> {
            Intent newIntent = new Intent(this, ViewForumQuestionsActivity.class);
            newIntent.putExtra("EXPERIMENT", currentExperiment);
            startActivity(newIntent);
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