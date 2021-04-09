package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.booleancatastrophe.interfaces.FirestoreCodeCallback;
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
    private int trialCount;
    private String minTrials;

    private TextView usernameText;
    private TextView descriptionText;
    private TextView regionText;
    private TextView trialCountText;
    private Button newTrialButton;
    private Button btnViewExperimentForum ;
    private Button btnViewExperimentStatistics;
    private Button newQrCodeButton;
    private Button newBarcodeButton;
    Button btnUnpublishExperiment;
    Button btnEndExperiment;
    Button btnSubscribe;

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

        usernameText = (TextView) findViewById(R.id.usernameText);
        descriptionText = (TextView) findViewById(R.id.descriptionText);
        regionText = (TextView) findViewById(R.id.regionText);
        trialCountText = (TextView) findViewById(R.id.trialCountText);
        newTrialButton = (Button) findViewById(R.id.newTrialButton);
        newQrCodeButton = findViewById(R.id.newQrCodeButton);
        newBarcodeButton = findViewById(R.id.newBarcodeButton);
        btnViewExperimentForum = (Button) findViewById(R.id.btn_experiment_forum);
        btnViewExperimentStatistics = (Button) findViewById(R.id.btn_experiment_statistics);
        btnUnpublishExperiment = (Button) findViewById(R.id.btn_unpublish_experiment);
        btnEndExperiment = (Button) findViewById(R.id.btn_end_experiment);
        btnSubscribe = (Button) findViewById(R.id.btn_experiment_subscribe);

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
                trialCount = ((Integer)trials.size());
                minTrials = ((Integer) currentExperiment.getMinTrials()).toString();
                String display = trialCount + " / " + minTrials + " trials completed";
                trialCountText.setText(display);
            }
        });

        userManager.getUser(((ExperimentApplication) this.getApplication()).getAccountID(),
                (User user) -> {
                    currentUser = user;
                    usernameText.setText( currentUser.getUsername());

                    // Only set up all the buttons once the current user has been pulled from
                    // the database

                    /* If the user is the account owner, make the owner tools available and set their
                     * onclick listeners - unpublish, end, ignore certain people's results, specify geo-location */
                    if(currentExperiment.getOwnerID().equals(currentUser.getAccountID())) {
                        setupOwnerTools(true);
                    } else {
                        setupOwnerTools(false);
                    }

                    /* Set up subscribe button for owners and experimenters based on state */
                    setupNewBarcodeButton();
                    setupNewQrCodeButton();
                    setupSubscribeButton();
                    setupAddTrialButton();
        });

        descriptionText.setText( currentExperiment.getDescription() );
        regionText.setText(currentExperiment.getRegion());

        /* Go to the experiment question forum activity if this button is clicked */
        btnViewExperimentForum.setOnClickListener((v) -> {
            Intent newIntent = new Intent(this, ViewForumQuestionsActivity.class);
            newIntent.putExtra("EXPERIMENT", currentExperiment);
            startActivity(newIntent);
        });

        // Go to statistics view when this is clicked
        btnViewExperimentStatistics.setOnClickListener((v) -> {
            Intent newIntent = new Intent(this, ViewStatisticsActivity.class);
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
                trialCount++;
                String display = trialCount + " / " + minTrials + " trials completed";
                trialCountText.setText(display);
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

    /**
     * This function sets up the UI owner tools based on whether the experimenter viewing this
     * experiment is the owner or not and enables/disables this functionality
     * @param doIt
     * Boolean variable dictating whether the tools will be enabled/visible or disabled/invisible */
    private void setupOwnerTools(boolean doIt) {
        setupPublishButton(doIt);
        setupEndButton(doIt);
    }

    /**
     * This function sets up specific owner tool: unpublish or re-publish button which changes
     * the current experiment's internal state and the button's action depending on that state
     * @param doIt
     * Boolean variable dictating whether the button will be visible and functioning or gone */
    private void setupPublishButton(boolean doIt) {
        if(doIt) {
            if(currentExperiment.getPublished()) {
                btnUnpublishExperiment.setText("Unpublish");
            } else {
                btnUnpublishExperiment.setText("Re-publish");
            }
            btnUnpublishExperiment.setEnabled(true);
            btnUnpublishExperiment.setVisibility(View.VISIBLE);
            btnUnpublishExperiment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentExperiment.getPublished()) {
                        eManager.unpublish(currentExperiment.getId());
                        currentExperiment.setPublished(false);
                        btnUnpublishExperiment.setText("Re-publish");
                    } else {
                        eManager.publish(currentExperiment.getId());
                        currentExperiment.setPublished(true);
                        btnUnpublishExperiment.setText("Unpublish");
                    }

                }
            });
        } else {
            btnUnpublishExperiment.setEnabled(false);
            btnUnpublishExperiment.setVisibility(View.GONE);
        }
    }

    /**
     * This function sets up specific owner tool: end experiment button which changes
     * the current experiment's internal state
     * @param doIt
     * Boolean variable dictating whether the button will be visible and functioning or gone */
    private void setupEndButton(boolean doIt) {
        if(doIt) {
            if(currentExperiment.getEnded()) {
                btnEndExperiment.setText("Already Ended");
                btnEndExperiment.setEnabled(false);
            } else {
                btnEndExperiment.setText("End");
                btnEndExperiment.setEnabled(true);
            }
            btnEndExperiment.setVisibility(View.VISIBLE);
            btnEndExperiment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!currentExperiment.getEnded() && trialCount >= currentExperiment.getMinTrials()) {
                        eManager.end(currentExperiment.getId());
                        currentExperiment.setEnded(true);
                        btnEndExperiment.setText("Already Ended");
                        btnEndExperiment.setEnabled(false);
                        newTrialButton.setText("Experiment Ended");
                        newTrialButton.setEnabled(false);
                        newBarcodeButton.setText("Experiment Ended");
                        newBarcodeButton.setEnabled(false);
                        newQrCodeButton.setText("Experiment Ended");
                        newQrCodeButton.setEnabled(false);
                    }
                }
            });
        } else {
            btnEndExperiment.setEnabled(false);
            btnEndExperiment.setVisibility(View.GONE);
        }
    }

    /**
     * This function sets up state dependent subscription button which changes the current
     * experiment's subscription list and user's subscriptions
    **/
    private void setupSubscribeButton() {

        if(currentUser.getSubscriptions().contains(currentExperiment.getId())) {
            btnSubscribe.setText("Unsubscribe");
        } else {
            btnSubscribe.setText("Subscribe");
        }

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser.getSubscriptions().contains(currentExperiment.getId())) {
                    userManager.unsubscribe(currentUser.getAccountID(), currentExperiment.getId());
                    eManager.removeFromSubscriptionList(currentExperiment.getId(), currentUser.getAccountID());
                    currentUser.removeSubscription(currentExperiment.getId());
                    btnSubscribe.setText("Subscribe");
                } else {
                    userManager.subscribe(currentUser.getAccountID(), currentExperiment.getId());
                    eManager.addToSubscriptionList(currentExperiment.getId(), currentUser.getAccountID());
                    currentUser.addSubscription(currentExperiment.getId());
                    btnSubscribe.setText("Unsubscribe");
                }
            }
        });
    }

    /**
     * This function sets up state dependent add trial button - if the experiment isn't ended,
     * the onclick will activate the 'add trial' fragment in add trial mode
     **/
    private void setupAddTrialButton() {

        if(currentExperiment.getEnded()) {
            newTrialButton.setText("Experiment Ended");
            newTrialButton.setEnabled(false);
        } else {
            newTrialButton.setText("Add Trial");
            newTrialButton.setEnabled(true);

        }

        newTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!currentExperiment.getEnded()) {
                    trialFragmentMode = ADD_TRIAL;
                    new NewTrialFragment().show(getSupportFragmentManager(), ADD_TRIAL);
                } else {
                    newTrialButton.setText("Experiment Ended");
                    newTrialButton.setEnabled(false);
                }
            }
        });
    }

    /**
     * This function sets up state dependent register new barcode button - if the experiment isn't
     * ended, the onclick will activate the 'new trial' fragment in barcode mode
     **/
    private void setupNewBarcodeButton() {

        if(currentExperiment.getEnded()) {
            newBarcodeButton.setText("Experiment Ended");
            newBarcodeButton.setEnabled(false);
        } else {
            newBarcodeButton.setText("Register Barcode");
            newBarcodeButton.setEnabled(true);
        }

        newBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!currentExperiment.getEnded()) {
                    trialFragmentMode = REGISTER_BARCODE;
                    new NewTrialFragment().show(getSupportFragmentManager(), REGISTER_BARCODE);
                } else {
                    newBarcodeButton.setText("Experiment Ended");
                    newBarcodeButton.setEnabled(false);
                }
            }
        });
    }

    /**
     * This function sets up state dependent generate qr code button - if the experiment isn't
     * ended, the onclick will activate the 'new trial' fragment in qr mode
     **/
    private void setupNewQrCodeButton() {

        if(currentExperiment.getEnded()) {
            newQrCodeButton.setText("Experiment Ended");
            newQrCodeButton.setEnabled(false);
        } else {
            newQrCodeButton.setText("Generate QR");
            newQrCodeButton.setEnabled(true);
        }

        newQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!currentExperiment.getEnded()) {
                    trialFragmentMode = GENERATE_QR_CODE;
                    new NewTrialFragment().show(getSupportFragmentManager(), GENERATE_QR_CODE);
                } else {
                    newQrCodeButton.setText("Experiment Ended");
                    newQrCodeButton.setEnabled(false);
                }
            }
        });
    }
}