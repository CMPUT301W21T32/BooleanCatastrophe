package com.example.booleancatastrophe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.booleancatastrophe.model.Experiment;

import java.util.ArrayList;


public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomViewHolder> {

    ArrayList<Experiment> experiments;

    public CustomRecyclerViewAdapter(ArrayList<Experiment> experiments) {
        this.experiments = experiments;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, null);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.experimentTitle.setText(experiments.get(position).getDescription());
        //holder.experimentImage.setImageResource(R.drawable.ic_ ... );
        // TODO ADD ICONS, BUTTONS, FUNCTIONALITY FOR EACH ITEM

        holder.itemView.setOnClickListener((View.OnClickListener) v -> {
            System.out.println(experiments.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return experiments.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView experimentTitle;
        //ImageView experimentImage;
        // TODO ADD ICONS, BUTTONS, FUNCTIONALITY FOR EACH ITEM


        public CustomViewHolder(View itemView) {
            super(itemView);
            experimentTitle = (TextView) itemView.findViewById(
                    R.id.tv_experiment_item_description);
        }
    }
}
