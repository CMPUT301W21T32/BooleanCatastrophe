package com.example.booleancatastrophe;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentType;
import com.example.booleancatastrophe.model.Trial;
import com.google.firebase.firestore.GeoPoint;
import com.schibstedspain.leku.LocationPickerActivity;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

//Fragment to capture a new trial on the given experiment
//TODO implement geolocation features
//TODO clean up UI
//TODO add "date" as a field for trials (for graphing) and implement it here
public class NewTrialFragment extends DialogFragment {

    private String TAG = "NewTrialFragment";

    private EditText trialResult;
    private TextView trialType;
    private FrameLayout trialLocationFrame;
    private boolean isLocationRequired;
    private GeoPoint location;

    private NewTrialFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener{
        void onOkPressed(Trial newTrial);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof NewTrialFragment.OnFragmentInteractionListener){
            listener = (NewTrialFragment.OnFragmentInteractionListener) context;
        }
        else{
            throw new RuntimeException(context.toString() + "must implement OnFragmentInterationListener");
        }

    }

    // Setup editText for new trial, send info back to activity on OK click
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.new_trial_fragment_layout, null);
        trialResult = view.findViewById(R.id.et_trial_result);
        trialType = view.findViewById(R.id.trial_type);
        trialLocationFrame = view.findViewById(R.id.get_location_frame);
        ExperimentType type = ExperimentType.valueOf(((ViewExperimentActivity) getActivity()).getCurrentExperiment().getStrType());
        String typeText = type.name();
        trialType.setText(typeText + " Trial");
        isLocationRequired = ((ViewExperimentActivity) getActivity()).getCurrentExperiment().isLocationRequired();

        //SET INPUT PARAMS FROM THE TYPE
        switch (type) {
            case BINOMIAL:
                //Only allow for binomial input
                trialResult.setKeyListener(DigitsKeyListener.getInstance("01"));
                trialResult.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
                trialResult.setHint("0 or 1");

                break;
            case COUNT:
                //No input needed, set result to 1 and dont change
                trialResult.setText("1");
                trialResult.setFocusable(false);
                break;
            case NONNEGCOUNT:
                trialResult.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                trialResult.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                trialResult.setHint("Positive integer");
                break;
            case MEASUREMENT:
                trialResult.setKeyListener(DigitsKeyListener.getInstance("0123456789.-"));
                trialResult.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                trialResult.setHint("Decimal number");
                break;
            default:
                break;
            // Error, abort the fragment
        }

        if (isLocationRequired) {
            Button btnAddLocation = new Button(getContext());
            btnAddLocation.setText("LOCATION");
            trialLocationFrame.addView(btnAddLocation);
            btnAddLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent locationPickerIntent = new LocationPickerActivity.Builder()
                            .withLegacyLayout()
                            .withGeolocApiKey("AIzaSyA4xH3jPbxGnZMYjLCkoNqR6z8hadtzhpA")
                            .withSatelliteViewHidden()
                            .withGoogleTimeZoneEnabled()
                            .withVoiceSearchHidden()
                            .build(getContext());

                    startActivityForResult(locationPickerIntent, 1);
                }
            });
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog d = builder
                .setView(view)
                .setTitle("Add Experiment")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialog, which) -> {  }).create();

        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String strNum = trialResult.getText().toString();
                        if (strNum.length() == 0) {
                            Toast.makeText(getContext(), "Invalid result!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Double result = Double.parseDouble(trialResult.getText().toString());

                        //todo change to if location required
                        if (isLocationRequired && location == null) {
                            Toast.makeText(getContext(), "Location required!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Trial newTrial = new Trial(((ExperimentApplication) getActivity().getApplication()).getAccountID()
                                , result
                                , location
                                , type);
                        listener.onOkPressed(newTrial);
                        d.dismiss();
                    }
                });
            }
        });

        return d;

//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.d(TAG, "LOCATION OK");
            if (requestCode == 1) {
                double latitude = data.getDoubleExtra(LATITUDE, 0.0);
                Log.d(TAG, "lat - " + Double.toString(latitude));
                double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                Log.d(TAG, "lon - " + Double.toString(longitude));
                location = new GeoPoint(latitude, longitude);
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d(TAG, "LOCATION CANCELLED");
        }
    }
}
