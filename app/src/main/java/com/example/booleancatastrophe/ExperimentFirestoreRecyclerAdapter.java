package com.example.booleancatastrophe;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.User;
import com.example.booleancatastrophe.model.UserManager;
import com.example.booleancatastrophe.utils.IFilterable;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.common.base.Strings;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Future;

// TODO: Filterable using notifyDataSetChanged() and similar methods
public class ExperimentFirestoreRecyclerAdapter extends FirestoreRecyclerAdapter<Experiment, ExperimentFirestoreRecyclerAdapter.ExperimentHolder>
        implements IFilterable {

    private OnItemClickListener listener;

    private String lastQuery = "";
    private Set<Experiment> visibleElements;
    private Set<Experiment> invisibleElements;
    private static UserManager uManager = new UserManager();

    private static LinearLayout.LayoutParams visibleElementParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    private static LinearLayout.LayoutParams invisibleElementParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0);

    public ExperimentFirestoreRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Experiment> options) {
        super(options);
        onDataChanged();
    }

    @Override
    protected void onBindViewHolder(
            @NonNull ExperimentFirestoreRecyclerAdapter.ExperimentHolder holder, int position, @NonNull Experiment model) {
        holder.tvExperimentItemDescription.setText(model.getDescription());
        holder.tvExperimentItemOwner.setText(model.getOwnerID());
        if(invisibleElements.contains(model)){
            holder.itemView.setLayoutParams(invisibleElementParams);
            //holder.itemView.setVisibility(View.GONE);
        }
        if(visibleElements.contains(model)){
            //holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(visibleElementParams);
        }
        // Holder image could be set here...
    }

    @NonNull
    @Override
    public ExperimentFirestoreRecyclerAdapter.ExperimentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.experiment_as_list_item, parent, false);
        return new ExperimentFirestoreRecyclerAdapter.ExperimentHolder(view);
    }

    @Override
    public void filter(String query) {
        Log.d("Adapter", "Trying: "+query);
        String prefix = Strings.commonPrefix(query, lastQuery);
        if(lastQuery.length() > prefix.length()){
            // new query is either shorter or it changed in the middle
            this.doFilter(prefix, EnumFilterConstraint.LOOSEN);
        }
        if(query.length() != prefix.length()){
            // new query is not shorter than the last query
            // so the change must have been in the middle or text was added
            this.doFilter(query, EnumFilterConstraint.TIGHTEN);
        }
        this.lastQuery = query;
        this.notifyDataSetChanged();
    }
    private void doFilter(String query, EnumFilterConstraint constraint){
        Log.d("Adapter", "Type: "+constraint.name());
        Iterator<Experiment> removeFrom;
        Set<Experiment> addTo;
        switch (constraint){
            case LOOSEN:
                removeFrom = this.invisibleElements.iterator();
                addTo = this.visibleElements;
                break;
            case TIGHTEN:
                removeFrom = this.visibleElements.iterator();
                addTo = this.invisibleElements;
                break;
            default:
                throw new IllegalArgumentException("Unsupported constraint: " + constraint.name());
        }
        Experiment exp;
        while(removeFrom.hasNext()){
            exp = removeFrom.next();
            //User owner = uManager.getUser(exp.getOwnerID());
            if(!exp.getDescription().contains(query) &&
            //        owner.getUsername().contains(query) ||
            //        owner.getEmail().contains(query) ||
                    !exp.getRegion().contains(query) &&
            //        exp.getType().name().contains(query)){
            true){
                Log.d("Adapter", "matched: "+exp.toString());
                removeFrom.remove();
                addTo.add(exp);
            }
        }
    }
    private enum EnumFilterConstraint{
        TIGHTEN,
        LOOSEN;
    }

    @Override
    public void onChildChanged(@NonNull ChangeEventType type, @NonNull DocumentSnapshot snapshot, int newIndex, int oldIndex) {
        Experiment changed = snapshot.toObject(Experiment.class);
        switch (type){
            case ADDED:
                visibleElements.add(changed);
                break;
            case REMOVED:
                visibleElements.remove(changed);
                invisibleElements.remove(changed);
                break;
            case MOVED:
                //visibleElements
            default:
                break;
        }
        this.filter(lastQuery);
        super.onChildChanged(type, snapshot, newIndex, oldIndex);
    }

    @Override
    public void onDataChanged() {
        this.visibleElements = new HashSet<>(this.getSnapshots());
        this.invisibleElements = new HashSet<>();
        this.filter(lastQuery);
        super.onDataChanged();
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
