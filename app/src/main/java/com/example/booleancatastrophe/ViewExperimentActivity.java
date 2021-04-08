package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.booleancatastrophe.interfaces.FirestoreCodeCallback;
import com.example.booleancatastrophe.interfaces.FirestoreTrialListCallback;
import com.example.booleancatastrophe.interfaces.FirestoreUserCallback;
import com.example.booleancatastrophe.model.Code;
import com.example.booleancatastrophe.model.CodeManager;
import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentManager;
import com.example.booleancatastrophe.model.Trial;
import com.example.booleancatastrophe.model.User;
import com.example.booleancatastrophe.model.UserManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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

    private static final String TAG = "View Experiment Activity";

    // a hacky solution to pass the trial received from NewTrialFragment to onActivityResult()
    private Trial tempNewTrial;

    // keeps track of which of the three buttons the user pressed
    private String trialFragmentMode;
    // it can be any of the three following values
    private static final String ADD_TRIAL = "ADD_TRIAL";
    private static final String GENERATE_QR_CODE = "GENERATE_QR_CODE";
    private static final String REGISTER_BARCODE = "REGISTER_BARCODE";

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
        Button newQrCodeButton = findViewById(R.id.newQrCodeButton);
        Button newBarcodeButton = findViewById(R.id.newBarcodeButton);
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
            trialFragmentMode = ADD_TRIAL;
            new NewTrialFragment().show(getSupportFragmentManager(), ADD_TRIAL);
        });

        newQrCodeButton.setOnClickListener(view -> {
            trialFragmentMode = GENERATE_QR_CODE;
            new NewTrialFragment().show(getSupportFragmentManager(), GENERATE_QR_CODE);
        });

        newBarcodeButton.setOnClickListener(view -> {
            trialFragmentMode = REGISTER_BARCODE;
            new NewTrialFragment().show(getSupportFragmentManager(), REGISTER_BARCODE);
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
        switch (trialFragmentMode) {
            case ADD_TRIAL:
                eManager.addTrial(currentExperiment.getId(), newTrial);
                break;
            case GENERATE_QR_CODE:
                Intent intent = new Intent(this, GenerateQRCodeActivity.class);
                intent.putExtra("EXPERIMENT", currentExperiment);
                intent.putExtra("TRIAL", newTrial);
                startActivity(intent);
                break;
            case REGISTER_BARCODE:
                tempNewTrial = newTrial;
                new IntentIntegrator(this).setDesiredBarcodeFormats(IntentIntegrator.EAN_13).initiateScan();
                break;
        }

    }

    // launch qr code scanner and add trial if successfully scanned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult() started!");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                ExperimentManager expManager = new ExperimentManager();

                // BARCODE REGISTER LOGIC -- this code block should be moved to onActivityResult() in the ViewExperimentActivity.  Here for testing purposes only.

                if (result.getFormatName().equals(IntentIntegrator.EAN_13)) {
                    CodeManager.addBarcode(((ExperimentApplication) this.getApplication()).getAccountID(), result.getContents(), currentExperiment.getId(), tempNewTrial);
//                    Log.d(TAG, "ADDED BARCODE");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}