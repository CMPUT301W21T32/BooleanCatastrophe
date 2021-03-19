package com.example.booleancatastrophe;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booleancatastrophe.interfaces.FirestoreExperimentListCallback;
import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentManager;
import com.example.booleancatastrophe.model.User;
import com.example.booleancatastrophe.model.UserManager;

import java.util.ArrayList;


public class OwnedExperimentListFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    private static final String TAG = "OwnedExperimentList";

    private ArrayList<Experiment> experiments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.experiment_lists, container, false);

        experiments = new ArrayList<>();
        User user = UserManager.currentUser;
        // TODO DID I IMPLEMENT THIS PROPERLY TO GET ALL THE EXPERIMENTS??
        ExperimentManager experimentManager = new ExperimentManager();
        experimentManager.getExperimentList(user.getOwnedExperiments(), new FirestoreExperimentListCallback() {
            @Override
            public void OnCallBack(ArrayList<Experiment> list) {
                for(int i = 0; i < list.size(); i++) {
                    Log.d(TAG, list.get(i).getDescription());
                    experiments.add(list.get(i));
                }
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewAdapter = new CustomRecyclerViewAdapter(experiments);
        recyclerView.setAdapter(recyclerViewAdapter);

        return rootView;
    }
}