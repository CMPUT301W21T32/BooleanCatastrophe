package com.example.booleancatastrophe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booleancatastrophe.model.ForumQuestion;
import com.example.booleancatastrophe.model.ForumReply;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ForumReplyFirestoreRecyclerAdapter extends FirestoreRecyclerAdapter<
        ForumReply, ForumReplyFirestoreRecyclerAdapter.ForumReplyHolder> {

    public ForumReplyFirestoreRecyclerAdapter(
            @NonNull FirestoreRecyclerOptions<ForumReply> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull ForumReplyFirestoreRecyclerAdapter.ForumReplyHolder holder, int position, @NonNull ForumReply model) {
        holder.tvReply.setText(model.getContent());
        holder.tvPosterName.setText(model.getPosterUsername());
        holder.tvDate.setText(model.getDate().toString());
        // Holder image has XML values, would be easy to add additional UI elements here
    }

    @NonNull
    @Override
    public ForumReplyFirestoreRecyclerAdapter.ForumReplyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.forum_reply_as_list_item, parent, false);
        return new ForumReplyFirestoreRecyclerAdapter.ForumReplyHolder(view);
    }

    public class ForumReplyHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvReply;
        TextView tvPosterName;
        TextView tvDate;

        public ForumReplyHolder(View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img_forum_reply);
            tvReply = (TextView) itemView.findViewById(R.id.tv_forum_reply);
            tvPosterName = (TextView) itemView.findViewById(R.id.tv_reply_poster);
            tvDate = itemView.findViewById(R.id.tv_reply_date);
        }
    }
}
