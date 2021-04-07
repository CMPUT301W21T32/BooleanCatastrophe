package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.booleancatastrophe.model.ForumManager;
import com.example.booleancatastrophe.model.ForumQuestion;
import com.example.booleancatastrophe.model.ForumReply;
import com.example.booleancatastrophe.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ViewForumRepliesActivity extends AppCompatActivity implements
        NewForumReplyFragment.OnFragmentInteractionListener {

    private ForumReplyFirestoreRecyclerAdapter adapter;
    private FirestoreRecyclerOptions<ForumReply> replyOptions;
    private ForumManager forumManager;
    private RecyclerView rv;
    private Button btnAddReply;
    private TextView questionAddressed;
    private User currentUser;
    private ForumQuestion currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_forum_replies);

        // Get the current user
        currentUser = ((ExperimentApplication) this.getApplication()).getCurrentUser();
        if(currentUser == null) {
            finish();
        }

        // Get the current question that was passed via the intent
        currentQuestion = (ForumQuestion) getIntent().getSerializableExtra("QUESTION");

        // Get the forum manager instance
        forumManager = ForumManager.getInstance();

        // Set up the UI, bind the recycler view, adapter, and data query
        btnAddReply = (Button) findViewById(R.id.btn_forum_add_reply);

        btnAddReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewForumReplyFragment frag = new NewForumReplyFragment();
                frag.show(getSupportFragmentManager(), "NEW_FORUM_REPLY");
            }
        });

        questionAddressed = (TextView) findViewById(R.id.tv_forum_question_reiterated);
        questionAddressed.setText(currentQuestion.getContent());

        replyOptions = forumManager.getAllQuestionReplies(currentQuestion);
        adapter = new ForumReplyFirestoreRecyclerAdapter(replyOptions);

        rv = (RecyclerView) findViewById(R.id.forum_reply_rv);
        rv.setLayoutManager(new LinearLayoutManager(ViewForumRepliesActivity.this));
        rv.setAdapter(adapter);

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
     * Function for the new reply fragment interaction listener interface
     **/
    @Override
    public void onOkPressed(String replyContent){
        ForumReply newForumReply = new ForumReply(currentQuestion, currentUser, replyContent);
        forumManager.addForumReply(newForumReply);
    }
}