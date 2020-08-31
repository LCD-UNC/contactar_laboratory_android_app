package com.rfdetke.digitriadlaboratory.views.listadapters;

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

import com.rfdetke.digitriadlaboratory.R;
import com.rfdetke.digitriadlaboratory.constants.RunStateEnum;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.views.ExperimentDetailActivity;
import com.rfdetke.digitriadlaboratory.views.RunDetailActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RunListAdapter extends RecyclerView.Adapter<RunListAdapter.RunViewHolder> {

    public static final String EXTRA_ID = "com.rfdetke.digitriadlaboratory.ID";
    private final LayoutInflater inflater;
    private final SimpleDateFormat simpleDateFormat;
    private List<Run> allRuns;
    private Context context;

    public RunListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
        this.context = context;
    }

    @NonNull
    @Override
    public RunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.run_list_item, parent, false);
        return new RunViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RunViewHolder holder, int position) {
        if(allRuns != null) {
            Run current = allRuns.get(position);
            holder.runLabel.setText(context.getResources().getString(R.string.run_label, current.number));
            holder.parentLayout.setOnClickListener(v -> {
                Intent intent = new Intent(context, RunDetailActivity.class);
                intent.putExtra(EXTRA_ID, current.id);
                context.startActivity(intent);
            });
            holder.runDate.setText(context.getResources().getString(R.string.run_date, simpleDateFormat.format(current.start)));
            if(current.state.equals(RunStateEnum.DONE.name())) {
                holder.runStatus.setImageResource(R.drawable.ic_baseline_done_24);
                holder.runStatus.setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
            } else if (current.state.equals(RunStateEnum.RUNNING.name())) {
                holder.runStatus.setImageResource(R.drawable.ic_baseline_directions_run_24);
                holder.runStatus.setColorFilter(ContextCompat.getColor(context, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);
            } else if(current.state.equals(RunStateEnum.CANCELED.name())) {
                holder.runStatus.setImageResource(R.drawable.ic_baseline_close_24);
                holder.runStatus.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                holder.runStatus.setImageResource(R.drawable.ic_baseline_timer_24);
                holder.runStatus.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        } else {
            holder.runLabel.setText(R.string.loading_data);
        }
    }

    @Override
    public int getItemCount() {
        if (allRuns != null)
            return allRuns.size();
        else
            return 1;
    }

    public void setRuns(List<Run> runs) {
        allRuns = runs;
        notifyDataSetChanged();
    }

    public class RunViewHolder extends RecyclerView.ViewHolder {

        private final TextView runLabel;
        private final TextView runDate;
        private final ImageView runStatus;
        private final ConstraintLayout parentLayout;

        public RunViewHolder(@NonNull View itemView) {
            super(itemView);
            runLabel = itemView.findViewById(R.id.label);
            runDate = itemView.findViewById(R.id.date);
            runStatus = itemView.findViewById(R.id.status);
            parentLayout = itemView.findViewById(R.id.run_list_item);
        }
    }
}
