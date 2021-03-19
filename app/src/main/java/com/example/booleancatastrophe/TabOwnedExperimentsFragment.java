package com.example.booleancatastrophe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.booleancatastrophe.model.Experiment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class TabOwnedExperimentsFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    private List<Experiment> experiments;

    public TabOwnedExperimentsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_owned_experiments, container,
                false);
        recyclerView = (RecyclerView) view.findViewById(R.id.tab_owned_experiments_rv);
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
        Experiment e1 = new Experiment();
        e1.setDescription("O1 Description");
        e1.setOwner("O1 Owner");
        experiments.add(e1);
        Experiment e2 = new Experiment();
        e2.setDescription("O2 Description");
        e2.setOwner("O2 Owner");
        experiments.add(e2);
        Experiment e3 = new Experiment();
        e3.setDescription("O3 Description");
        e3.setOwner("O3 Owner");
        experiments.add(e3);
        Experiment e4 = new Experiment();
        e4.setDescription("O4 Description");
        e4.setOwner("O4 Owner");
        experiments.add(e4);
        Experiment e5 = new Experiment();
        e5.setDescription("O5 Description");
        e5.setOwner("O5 Owner");
        experiments.add(e5);
        Experiment e6 = new Experiment();
        e6.setDescription("O6 Description");
        e6.setOwner("O6 Owner");
        experiments.add(e6);
        /* See the ExperimentManager and UserManager classes to integrate */
    }
}