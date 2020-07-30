package com.rfdetke.digitriadlaboratory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rfdetke.digitriadlaboratory.database.daos.ExperimentDao;

import java.util.List;

public class ExperimentListAdapter extends RecyclerView.Adapter<ExperimentListAdapter.ExperimentViewHolder> {
    public static final String EXTRA_ID = "com.rfdetke.digitriadlaboratory.ID";


    private final LayoutInflater inflater;
    private List<ExperimentDao.ExperimentDone> allExperiments;
    private Context context;

    public ExperimentListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ExperimentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ExperimentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExperimentViewHolder holder, int position) {
        if (allExperiments != null) {
            ExperimentDao.ExperimentDone current = allExperiments.get(position);
            holder.experimentItemView.setText(current.codename);
            holder.parentLayout.setOnClickListener(v -> {
//                Toast.makeText(context, String.format("Clicked: %s", current.codename), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ExperimentDetailActivity.class);
                intent.putExtra(EXTRA_ID, current.id);
                context.startActivity(intent);
                // TODO: Implementar llamada a vista de detalle de experimento con corridas.
                //      pasar como extradata en el intent el id del experimento que se tapeo.
            });
            if(current.total==0) {
                holder.statusImageView.setImageResource(R.drawable.ic_baseline_warning_24);
                holder.statusImageView.setColorFilter(ContextCompat.getColor(context, R.color.warning), android.graphics.PorterDuff.Mode.SRC_IN);
            } else if ((current.total-current.done)==0) {
                holder.statusImageView.setImageResource(R.drawable.ic_baseline_done_24);
                holder.statusImageView.setColorFilter(ContextCompat.getColor(context, R.color.done), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                holder.statusImageView.setImageResource(R.drawable.ic_baseline_timer_24);
                holder.statusImageView.setColorFilter(ContextCompat.getColor(context, R.color.scheduled), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        } else {
            holder.experimentItemView.setText("No experiments");
        }
    }

    @Override
    public int getItemCount() {
        if (allExperiments != null)
            return allExperiments.size();
        else
            return 0;
    }

    void setExperiments (List<ExperimentDao.ExperimentDone> experiments) {
        allExperiments = experiments;
        notifyDataSetChanged();
    }

    public class ExperimentViewHolder extends RecyclerView.ViewHolder{

        private final TextView experimentItemView;
        private final ImageView statusImageView;
        private final ConstraintLayout parentLayout;

        public ExperimentViewHolder(@NonNull View itemView) {
            super(itemView);
            experimentItemView = itemView.findViewById(R.id.textView);
            statusImageView = itemView.findViewById(R.id.status);
            parentLayout = itemView.findViewById(R.id.list_item);
        }
    }
}
