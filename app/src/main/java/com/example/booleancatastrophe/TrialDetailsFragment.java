package com.example.booleancatastrophe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.Trial;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrialDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrialDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EXPERIMENT_TITLE = "experimentTitle";
    private static final String EXPERIMENT_OWNER = "experimentOwner";
    private static final String EXPERIMENT_DESCRIPTION = "experimentDescription";
    private static final String TRIAL_RESULT = "trialResult";
    private static final String TRIAL_LOCATION = "trialLocation";
    private static final String TRIAL_DATE = "trialDate";

    // TODO: Rename and change types of parameters
    private String experimentTitle;
    private String experimentOwner;
    private String experimentDescription;
    private String trialResult;
    private String trialLocation;
    private Date trialDate;

    public TrialDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment trial_details.
     */
    // TODO: Rename and change types and number of parameters
    public static TrialDetailsFragment newInstance(Experiment experiment, Trial trial) {
        TrialDetailsFragment fragment = new TrialDetailsFragment();
        Bundle args = new Bundle();
        args.putString(EXPERIMENT_TITLE, "Sample Title");
        args.putString(EXPERIMENT_OWNER, experiment.getOwnerID());
        args.putString(EXPERIMENT_DESCRIPTION, experiment.getDescription());
        args.putString(TRIAL_RESULT, trial.getResult().toString());
        args.putString(TRIAL_LOCATION, trial.getLocation().toString());
        args.putString(TRIAL_DATE, String.valueOf(trial.getDate().getTime()));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            experimentTitle = getArguments().getString(EXPERIMENT_TITLE);
            experimentOwner = getArguments().getString(EXPERIMENT_OWNER);
            experimentDescription = getArguments().getString(EXPERIMENT_DESCRIPTION);
            trialResult = getArguments().getString(TRIAL_RESULT);
            trialLocation = getArguments().getString(TRIAL_LOCATION);
            trialDate = new Date(Long.parseLong(getArguments().getString(TRIAL_DATE)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_trial_details, container, false);
        TextView title = (TextView) view.findViewById(R.id.experiment_title);
        TextView owner = (TextView) view.findViewById(R.id.experiment_owner);
        TextView description = (TextView) view.findViewById(R.id.experiment_desc);
        TextView result = (TextView) view.findViewById(R.id.trial_result);
        TextView location = (TextView) view.findViewById(R.id.trial_location);

        title.setText(experimentTitle);
        owner.setText(experimentOwner);
        description.setText(experimentDescription);
        result.setText(trialResult);
        location.setText(trialLocation);

        return view;
    }
}