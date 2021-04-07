package com.example.booleancatastrophe;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ForumQuestion;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ExperimentFirestoreRecyclerAdapter extends FirestoreRecyclerAdapter<Experiment, ExperimentFirestoreRecyclerAdapter.ExperimentHolder> {

    private OnItemClickListener listener;

    public ExperimentFirestoreRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Experiment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull ExperimentFirestoreRecyclerAdapter.ExperimentHolder holder, int position, @NonNull Experiment model) {
        holder.tvExperimentItemDescription.setText(model.getDescription());
        holder.tvExperimentItemOwner.setText(model.getOwner());
        // Holder image could be set here...
    }

    @NonNull
    @Override
    public ExperimentFirestoreRecyclerAdapter.ExperimentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.experiment_as_list_item, parent, false);
        return new ExperimentFirestoreRecyclerAdapter.ExperimentHolder(view);
    }

    public class ExperimentHolder extends RecyclerView.ViewHolder {

        TextView tvExperimentItemDescription;
        TextView tvExperimentItemOwner;
        ImageView imgExperimentImage;

        public ExperimentHolder(View itemView) {
            super(itemView);

            tvExperimentItemDescription = (TextView) itemView.findViewById(
                    R.id.tv_experiment_item_description);
            tvExperimentItemOwner = (TextView) itemView.findViewById(
                    R.id.tv_experiment_item_owner);
            imgExperimentImage = (ImageView) itemView.findViewById(
                    R.id.img_experiment_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    // Avoid case where item is clicked on when recycler view is in the removal animation
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

        public interface OnItemClickListener {
            void onItemClick(DocumentSnapshot documentSnapshot, int position);
        }

        public void setOnItemClickListener(ExperimentFirestoreRecyclerAdapter.OnItemClickListener listener) {
            this.listener = listener;
        }
}
