package mrodkiewicz.pl.bakingapp.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import timber.log.Timber;


public class RecipesRecycleViewAdapter extends RecyclerView.Adapter<RecipesRecycleViewAdapter.ViewHolder> {
    private ArrayList<Recipe> recipeArrayList;
    private Context context;

    public RecipesRecycleViewAdapter(Context context, ArrayList<Recipe> recipeArrayList) {
        Timber.d("RecipesRecycleViewAdapter");

        this.context = context;
        this.recipeArrayList = recipeArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_recipe, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(recipeArrayList.get(position).getName());
        if (!recipeArrayList.get(position).getImage().isEmpty()) {
            holder.imageView.setVisibility(View.VISIBLE);
            Picasso.with(context).load(recipeArrayList.get(position).getImage()).into(holder.imageView);
        }
        holder.textViewSmall.setVisibility(View.VISIBLE);
        holder.textViewSmall.setText("Servings: " + recipeArrayList.get(position).getServings());

    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textViewSmall;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView_row_recipe);
            textViewSmall = (TextView) itemView.findViewById(R.id.textView_small_row_recipe);
            imageView = (ImageView) itemView.findViewById(R.id.imageView_row_recipe);

        }

    }
}
