package com.example.booleancatastrophe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.booleancatastrophe.model.Experiment;

import java.io.Serializable;
import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<Experiment> experiments;

    public CustomRecyclerViewAdapter(Context context, List<Experiment> experiments) {
        this.context = context;
        this.experiments = experiments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.experiment_as_list_item, parent,
                false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        /* Onclick listener that should send the selected experiment to be viewed in another activity*/
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = viewHolder.getAdapterPosition();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("experiment", experiments.get(pos));
                Intent intent = new Intent(context, ViewExperimentActivity.class);
                intent.putExtra("experiment", experiments.get(pos));
                context.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tvExperimentItemDescription.setText(experiments.get(position).getDescription());
        holder.tvExperimentItemOwner.setText(experiments.get(position).getOwnerID());
        // TODO use the icon as some kind of data indicator or allow users to set the picture, etc.
        // do it in this kind of way somehow:
//        holder.imgExperimentImage.setImageResource(experiments.get(position).getPhoto());


    }

    @Override
    public int getItemCount() {
        return experiments.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvExperimentItemDescription;
        TextView tvExperimentItemOwner;
        ImageView imgExperimentImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvExperimentItemDescription = (TextView) itemView.findViewById(
                    R.id.tv_experiment_item_description);
//            tvExperimentItemOwner = (TextView) itemView.findViewById(
//                    R.id.tv_experiment_item_owner);
            imgExperimentImage = (ImageView) itemView.findViewById(
                    R.id.img_experiment_item);
        }
    }
}
