package com.example.booleancatastrophe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booleancatastrophe.model.Experiment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class TabEndedExperimentsFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    private List<Experiment> experiments;

    public TabEndedExperimentsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_ended_experiments, container,
                false);
        recyclerView = (RecyclerView) view.findViewById(R.id.tab_ended_experiments_rv);
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

        // don't add anything to the list in this case!

        /* See the ExperimentManager and UserManager classes to integrate */
    }
}