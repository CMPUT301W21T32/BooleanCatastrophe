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
import com.example.booleancatastrophe.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ViewForumQuestionsActivity extends AppCompatActivity implements
        NewForumQuestionFragment.OnFragmentInteractionListener {

    private ForumQuestionFirestoreRecyclerAdapter adapter;
    private FirestoreRecyclerOptions<ForumQuestion> questionOptions;
    private ForumManager forumManager;
    private RecyclerView rv;
    private Button btnAddQuestion;
    private User currentUser;
    private Experiment currentExperiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_forum_questions);

        // Get the current user
        currentUser = ((ExperimentApplication) this.getApplication()).getCurrentUser();
        if(currentUser == null){
            finish();
        }

        // Retrieve the experiment passed via the intent
        currentExperiment = (Experiment) getIntent().getSerializableExtra("EXPERIMENT");

        // Get the forum manager instance
        forumManager = ForumManager.getInstance();

        // Set up UI elements and recyclerview / adapter

        // Set up the adapter to listen for a specific data / query combination - note that this can be changed by modifying questionOptions via
        // possible ForumManager queries and then call
        // adapter.updateOptions(questionOptions);
        questionOptions = forumManager.getAllExperimentQuestions(currentExperiment);
        adapter = new ForumQuestionFirestoreRecyclerAdapter(questionOptions);

        rv = (RecyclerView) findViewById(R.id.forum_question_rv);
        rv.setLayoutManager(new LinearLayoutManager(ViewForumQuestionsActivity.this));
        rv.setAdapter(adapter);

        // Set up adapter on item click listener - if a question is clicked go to the reply view activity
        // for that particular question
        adapter.setOnItemClickListener(new ForumQuestionFirestoreRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                ForumQuestion question = documentSnapshot.toObject(ForumQuestion.class);
                Intent intent = new Intent(ViewForumQuestionsActivity.this, ViewForumRepliesActivity.class);
                intent.putExtra("QUESTION", question);
                startActivity(intent);
            }
        });

        btnAddQuestion = (Button) findViewById(R.id.btn_forum_add_question);

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
     * Function for the new question fragment interaction listener interface
     **/
    @Override
    public void onOkPressed(String askedQuestionContent){
       ForumQuestion newForumQuestion = new ForumQuestion(currentExperiment, currentUser, askedQuestionContent);
       forumManager.addForumQuestion(newForumQuestion);
    }
}