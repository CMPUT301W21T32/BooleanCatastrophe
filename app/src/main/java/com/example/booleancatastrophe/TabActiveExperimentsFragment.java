package com.example.booleancatastrophe;

import android.content.Intent;
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
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class TabActiveExperimentsFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    private ExperimentFirestoreRecyclerAdapter adapter;
    private FirestoreRecyclerOptions<Experiment> experimentOptions;
    private ExperimentManager eManager = new ExperimentManager();
    private User currentUser;

    public TabActiveExperimentsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_active_experiments, container,
                false);
        recyclerView = (RecyclerView) view.findViewById(R.id.tab_active_experiments_rv);

        // Get the current user, if null don't read in data to avoid crash and re-check the user in
        // the onStart and onStop to set it up
        currentUser = ((ExperimentApplication) getActivity().getApplication()).getCurrentUser();

        // If current user hasn't been retrieved from the asynchronous database call yet, option
        // for experiment options should be blank and updated later in the onresume and onpause
        if(currentUser == null) {
            experimentOptions = eManager.getNoExperiments();
        } else {
            experimentOptions = eManager.getExperimentsActive();
        }

        adapter = new ExperimentFirestoreRecyclerAdapter(experimentOptions);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Set up adapter on item click listener - if a question is clicked go to the individual
        // experiment view activity
        adapter.setOnItemClickListener(new ExperimentFirestoreRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Experiment experiment = documentSnapshot.toObject(Experiment.class);
                Intent intent = new Intent(getContext(), ViewExperimentActivity.class);
                intent.putExtra("experiment", experiment);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        experiments = new ArrayList<>();

        /* This block is temporary and must be replaced with the code to actively get the required
         * experiments subcategory (often based on the current user) and query ExperimentManager to
         * get the updated list from the database */
/*        Experiment e1 = new Experiment();
        e1.setDescription("A1 Description");
        e1.setOwner("A1 Owner");
        experiments.add(e1);
        Experiment e2 = new Experiment();
        e2.setDescription("A2 Description");
        e2.setOwner("A2 Owner");
        experiments.add(e2);
        Experiment e3 = new Experiment();
        e3.setDescription("A3 Description");
        e3.setOwner("A3 Owner");
        experiments.add(e3);
        Experiment e4 = new Experiment();
        e4.setDescription("A4 Description");
        e4.setOwner("A4 Owner");
        experiments.add(e4);
        Experiment e5 = new Experiment();
        e5.setDescription("A5 Description");
        e5.setOwner("A5 Owner");
        experiments.add(e5);
        Experiment e6 = new Experiment();
        e6.setDescription("A6 Description");
        e6.setOwner("A6 Owner");
        experiments.add(e6);
        Experiment e7 = new Experiment();
        e7.setDescription("A7 Description");
        e7.setOwner("A7 Owner");
        experiments.add(e7);
        Experiment e8 = new Experiment();
        e8.setDescription("A8 Description");
        e8.setOwner("A8 Owner");
        experiments.add(e8);
        Experiment e9 = new Experiment();
        e9.setDescription("A9 Description");
        e9.setOwner("A9 Owner");
        experiments.add(e9);
        Experiment e10 = new Experiment();
        e10.setDescription("A10 Description");
        e10.setOwner("A10 Owner");
        experiments.add(e10);
        Experiment e11 = new Experiment();
        e11.setDescription("A11 Description");
        e11.setOwner("A11 Owner");
        experiments.add(e11);
        Experiment e12 = new Experiment();
        e12.setDescription("A12 Description");
        e12.setOwner("A12 Owner");
        experiments.add(e12);
        Experiment e13 = new Experiment();
        e13.setDescription("A13 Description");
        e13.setOwner("A13 Owner");
        experiments.add(e13);
        Experiment e14 = new Experiment();
        e14.setDescription("A14 Description");
        e14.setOwner("A14 Owner");
        experiments.add(e14);
        Experiment e15 = new Experiment();
        e15.setDescription("A15 Description");
        e15.setOwner("A15 Owner");
        experiments.add(e15);
        Experiment e16 = new Experiment();
        e16.setDescription("A16 Description");
        e16.setOwner("A16 Owner");
        experiments.add(e16);
        Experiment e17 = new Experiment();
        e17.setDescription("A17 Description");
        e17.setOwner("A17 Owner");
        experiments.add(e17);
        Experiment e18 = new Experiment();
        e18.setDescription("A18 Description");
        e18.setOwner("A18 Owner");
        experiments.add(e18);
        Experiment e19 = new Experiment();
        e19.setDescription("A19 Description");
        e12.setOwner("A19 Owner");
        experiments.add(e19);*/
        /* See the ExperimentManager and UserManager classes to integrate */

//        eManager.getPublishedExperiments((ArrayList<Experiment> list) -> { //this function is currently broken
//            experiments.addAll(list);
//            if(!(list.isEmpty())){
//                Log.d("Published", "Got list");
//            }
//            else{
//                Log.d("Published", "No list :(");
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(currentUser == null) {
            currentUser = ((ExperimentApplication) getActivity().getApplication()).getCurrentUser();
            if(currentUser != null) {
                experimentOptions = eManager.getExperimentsActive();
                adapter.updateOptions(experimentOptions);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(currentUser == null) {
            currentUser = ((ExperimentApplication) getActivity().getApplication()).getCurrentUser();
            if(currentUser != null) {
                experimentOptions = eManager.getExperimentsActive();
                adapter.updateOptions(experimentOptions);
            }
        }
    }
}