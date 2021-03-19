package com.example.booleancatastrophe;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentType;
import com.example.booleancatastrophe.model.Trial;
import com.google.firebase.firestore.GeoPoint;

//Fragment to capture a new trial on the given experiment
//TODO implement geolocation features
//TODO clean up UI
//TODO add "date" as a field for trials (for graphing) and implement it here
public class NewTrialFragment extends DialogFragment {

    private EditText trialResult;
    private TextView trialType;

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
            throw new RuntimeException(context.toString() + "must implement OnFragmentInterationListner");
        }

    }

    // Setup editText for new trial, send info back to activity on OK click
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.new_trial_fragment_layout, null);
        trialResult = view.findViewById(R.id.et_trial_result);
        trialType = view.findViewById(R.id.trial_type);
        ExperimentType type = (((ViewExperimentActivity) getActivity()).getCurrentExperiment().getType());
        String typeText = type.name();
        trialType.setText(typeText + " Trial");

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


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Experiment")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Double result = Double.parseDouble(trialResult.getText().toString());
                        Trial newTrial = new Trial(((ExperimentApplication) getActivity().getApplication()).getAccountID()
                                , result
                                , new GeoPoint(45, 45)
                                , type);
                        listener.onOkPressed(newTrial);
                    }
                }).create();
    }
}
