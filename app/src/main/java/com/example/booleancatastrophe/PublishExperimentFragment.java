package com.example.booleancatastrophe;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentType;
import com.example.booleancatastrophe.model.User;


// TODO set up the new experiment location functionality
public class PublishExperimentFragment extends DialogFragment {

    /* Required empty public constructor */
    public PublishExperimentFragment() {

    }

    /* Set up fragment UI elements and listener interface that needs to be implemented
    * by the activity */
    private EditText etMinimumTrials;
    private TextView tvOwner;  // Can't be changed; set to the current user
    private RadioGroup radGroup;
    private EditText etDescription;

    User user;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Experiment experiment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PublishExperimentFragment.OnFragmentInteractionListener");
        }
    }

    /* Create dialog and link UI elements to it */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_publish_experiment, null);

        etMinimumTrials = view.findViewById(R.id.et_pub_exp_min);
        etDescription = view.findViewById(R.id.et_pub_exp_description);
        tvOwner = view.findViewById(R.id.tv_pub_exp_owner);
        radGroup = view.findViewById(R.id.rad_group_pub_exp);

        user = ((ExperimentApplication) this.getActivity().getApplication()).getCurrentUser();
        if(user.getUsername().equals("")) {
            tvOwner.setText("Owner: YOU (username not set)");
        } else {
            tvOwner.setText("Owner: " + user.getUsername());
        }

        /* Build and return the dialog with listener on the OK button - will return a new
        * experiment on press */
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setTitle("Publish New Experiment")
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Publish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        /* As soon as OK is clicked, pass the new experiment with this dialog
                        * fragment's UI value selections */
                        String expDescription = etDescription.getText().toString();
                        String expRegion = "";  // TODO set up location properly
                        String expOwner = user.getAccountID();
                        int expMinTrials = Integer.parseInt(etMinimumTrials.getText().toString());

                        ExperimentType expType;
                        int selectedID = radGroup.getCheckedRadioButtonId();
                        if(selectedID == R.id.rad_binomial) {
                            expType = ExperimentType.BINOMIAL;
                        } else if(selectedID == R.id.rad_count) {
                            expType = ExperimentType.COUNT;
                        } else if(selectedID == R.id.rad_measurement) {
                            expType = ExperimentType.MEASUREMENT;
                        } else if(selectedID == R.id.rad_nonnegcount) {
                            expType = ExperimentType.NONNEGCOUNT;
                        } else {
                            /* No radio button was detected to be selected; shouldn't ever happen
                            * as one of the radio buttons is checked by default in the XML...
                            * set the experiment type default as binomial and think of how to
                            * handle this error better (note that selectedID == -1 would mean
                            * no box was selected) */
                            expType = ExperimentType.BINOMIAL;
                        }

                        listener.onOkPressed(new Experiment(
                                expDescription, expRegion, expOwner, expMinTrials, expType));
                    }
                }).create();
    }
}