package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ForumManager;
import com.example.booleancatastrophe.model.ForumQuestion;
import com.example.booleancatastrophe.model.Trial;
import com.example.booleancatastrophe.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ViewForumQuestionsActivity extends AppCompatActivity
        implements NewForumQuestionFragment.OnFragmentInteractionListener{

    private ForumQuestionFirestoreRecyclerAdapter adapter;
    private ForumManager forumManager;
    private RecyclerView rv;
    private Button btnAddQuestion;
    private User currentUser;
    private Experiment currentExperiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_experiment_questions);

        // Get the current user
        currentUser = ((ExperimentApplication) this.getApplication()).getCurrentUser();
        if(currentUser == null){
            finish();
        }

        // Retrieve the experiment passed via the intent
        Intent intent = getIntent();
        currentExperiment = (Experiment) intent.getSerializableExtra("EXPERIMENT");

        // Set up UI elements and recyclerview / adapter
        btnAddQuestion = (Button) findViewById(R.id.btn_add_question);

        forumManager = ForumManager.getInstance();

        adapter = new ForumQuestionFirestoreRecyclerAdapter(forumManager.getAllExperimentQuestions(currentExperiment));

        rv = (RecyclerView) findViewById(R.id.experiment_forum_rv);
        rv.setLayoutManager(new LinearLayoutManager(ViewForumQuestionsActivity.this));
        rv.setAdapter(adapter);

        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewForumQuestionFragment frag = new NewForumQuestionFragment();
                frag.show(getSupportFragmentManager(), "NEW_FORUM_QUESTION");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    /**
     * On fragment dialog's OK, create the new ForumQuestion and add it to the database
     **/
    @Override
    public void onOkPressed(String askedQuestionContent){
       ForumQuestion newForumQuestion = new ForumQuestion(currentExperiment, currentUser, askedQuestionContent);
       forumManager.addForumQuestion(newForumQuestion);
//       adapter.updateOptions(forumManager.getAllExperimentQuestions(currentExperiment));
    }
}