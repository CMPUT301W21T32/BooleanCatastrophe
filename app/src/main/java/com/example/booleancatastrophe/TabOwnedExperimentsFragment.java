package com.example.booleancatastrophe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentManager;
import com.example.booleancatastrophe.model.ForumQuestion;
import com.example.booleancatastrophe.model.User;
import com.example.booleancatastrophe.model.UserManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class TabOwnedExperimentsFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    private List<Experiment> experiments;
    private ExperimentFirestoreRecyclerAdapter adapter;
    private FirestoreRecyclerOptions<Experiment> experimentOptions;
    private ExperimentManager eManager = new ExperimentManager();
    private UserManager uManager = new UserManager();
    private User currentUser;

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
// Get the current user, if null don't read in data to avoid crash and re-check the user in
        // the onStart and onStop to set it up
        currentUser = ((ExperimentApplication) getActivity().getApplication()).getCurrentUser();

        // If current user hasn't been retrieved from the asynchronous database call yet, option
        // for experiment options should be blank and updated later in the onresume and onpause
        if(currentUser == null) {
            experimentOptions = eManager.getNoExperiments();
        } else {
            experimentOptions = eManager.getUserPublishedExperiments(currentUser);
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
                intent.putExtra("EXPERIMENT", experiment);
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
        experiments.add(e6);*/
        /* See the ExperimentManager and UserManager classes to integrate */

//        String id = ((ExperimentApplication) this.getActivity().getApplication()).getAccountID();
//        Log.d("Owned", id);
//        uManager.getUser(id, (user) -> {
//                //In the database callback, get the experiments owned by the retrieved user
//                eManager.getExperimentList(user.getOwnedExperiments(), (ArrayList<Experiment> list) -> {
//                        experiments.addAll(list);
//                        if(!(list.isEmpty())){
//                            Log.d("Owned", "Got list");
//                        }
//                });
//        });

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        if(currentUser == null) {
            currentUser = ((ExperimentApplication) getActivity().getApplication()).getCurrentUser();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        if(currentUser == null) {
            currentUser = ((ExperimentApplication) getActivity().getApplication()).getCurrentUser();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(currentUser == null) {
            currentUser = ((ExperimentApplication) getActivity().getApplication()).getCurrentUser();
            experimentOptions = eManager.getUserPublishedExperiments(currentUser);
            adapter.updateOptions(experimentOptions);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(currentUser == null) {
            currentUser = ((ExperimentApplication) getActivity().getApplication()).getCurrentUser();
            experimentOptions = eManager.getUserPublishedExperiments(currentUser);
            adapter.updateOptions(experimentOptions);
        }
    }
}