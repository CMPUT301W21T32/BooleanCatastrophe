package com.example.booleancatastrophe;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentType;
import com.example.booleancatastrophe.model.User;


public class PublishExperimentFragment extends DialogFragment {

    /* Required empty public constructor */
    public PublishExperimentFragment() {

    }

    /* Set up fragment UI elements and listener interface that needs to be implemented
    * by the activity */
    private EditText etMinimumTrials;
    private TextView tvOwner;  // Can't be changed; set to the current user
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
        etDescription = view.findViewById(R.id.et_pub_exp_description);
        tvOwner = view.findViewById(R.id.tv_pub_exp_owner);
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
                        String expDescription = etDescription.getText().toString();
                        String expRegion = "";  // TODO set up location properly
                        String expOwner = user.getUsername();
                        int expMinTrials = 0;  // TODO set up minimum number of trials properly
                        ExperimentType expType = ExperimentType.BINOMIAL; // TODO set up dropdown or something for type of experiment
                        listener.onOkPressed(new Experiment(
                                expDescription, expRegion, expOwner, expMinTrials, expType));
                    }
                }).create();
    }
}