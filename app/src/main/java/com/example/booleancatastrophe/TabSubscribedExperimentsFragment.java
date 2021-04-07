package com.example.booleancatastrophe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentManager;
import com.example.booleancatastrophe.model.User;
import com.example.booleancatastrophe.model.UserManager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class TabSubscribedExperimentsFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    private List<Experiment> experiments;
    private ExperimentManager eManager = new ExperimentManager();
    private UserManager uManager = new UserManager();

    public TabSubscribedExperimentsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_subscribed_experiments, container,
                false);
        recyclerView = (RecyclerView) view.findViewById(R.id.tab_subscribed_experiments_rv);
        CustomRecyclerViewAdapter recyclerViewAdapter = new CustomRecyclerViewAdapter(getContext(),
                experiments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        experiments = new ArrayList<>();

        /* This block is temporary and must be replaced with the code to actively get the required
         * experiments subcategory (often based on the current user) and query ExperimentManager to
         * get the updated list from the database */
/*
        Experiment e1 = new Experiment();
        e1.setDescription("S1 Description");
        e1.setOwner("S1 Owner");
        experiments.add(e1);
        Experiment e2 = new Experiment();
        e2.setDescription("S2 Description");
        e2.setOwner("S2 Owner");
        experiments.add(e2);
*/

        /* See the ExperimentManager and UserManager classes to integrate */
        String id = ((ExperimentApplication) this.getActivity().getApplication()).getAccountID();
        Log.d("Subscribed", id);
        uManager.getUser(id, (user) -> {
                //In the database callback, get the experiments which the retrieved user is subscribed to
                eManager.getExperimentList(user.getSubscriptions(), (ArrayList<Experiment> list) -> {
                        experiments.addAll(list);
                        if(!(list.isEmpty())){
                            Log.d("Subscribed", "Got list");
                        }
                });
        });


    }
}