package mrodkiewicz.pl.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.db.models.Step;
import timber.log.Timber;

public class StepsRecycleViewAdapter extends RecyclerView.Adapter<StepsRecycleViewAdapter.ViewHolder> {
    private ArrayList<Step> steps;
    private Context context;

    public StepsRecycleViewAdapter(Context context, java.util.ArrayList<Step> steps) {
        Timber.d("StepsRecycleViewAdapter");

        this.context = context;
        this.steps = steps;
    }

    @NonNull
    @Override
    public StepsRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_recipe, parent, false);
        return new StepsRecycleViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsRecycleViewAdapter.ViewHolder holder, int position) {
        holder.textView.setText(steps.get(position).getShortDescription());

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView_row_recipe);

        }

    }
}
