package com.example.booleancatastrophe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booleancatastrophe.model.ForumQuestion;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ForumQuestionFirestoreRecyclerAdapter extends FirestoreRecyclerAdapter<ForumQuestion, ForumQuestionFirestoreRecyclerAdapter.ForumQuestionHolder> {

    private OnItemClickListener listener;

    public ForumQuestionFirestoreRecyclerAdapter(
            @NonNull FirestoreRecyclerOptions<ForumQuestion> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull ForumQuestionHolder holder, int position, @NonNull ForumQuestion model) {
        holder.tvQuestion.setText(model.getContent());
        holder.tvNumberOfReplies.setText(String.valueOf(model.getReplyNumber()));
        // Holder image and reply label have values in the XML but could change here...
    }

    @NonNull
    @Override
    public ForumQuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.forum_question_as_list_item, parent, false);
        return new ForumQuestionHolder(view);
    }

    public class ForumQuestionHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvQuestion;
        TextView tvNumberOfReplies;
        TextView tvReplyLabel;

        public ForumQuestionHolder(View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img_forum_question);
            tvQuestion = (TextView) itemView.findViewById(R.id.tv_forum_question);
            tvNumberOfReplies = (TextView) itemView.findViewById(R.id.tv_forum_question_num_replies);
            tvReplyLabel = itemView.findViewById(R.id.lbl_forum_question_num_replies);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    // Avoid case where item is clicked on when recycler view is in the removal animation
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
