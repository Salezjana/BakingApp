package mrodkiewicz.pl.bakingapp.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.db.models.Ingredient;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import timber.log.Timber;


public class IngredientsRecycleViewAdapter extends RecyclerView.Adapter<IngredientsRecycleViewAdapter.ViewHolder> {
    private ArrayList<Ingredient> ingredientArrayList;
    private Context context;

    public IngredientsRecycleViewAdapter(Context context, ArrayList<Ingredient> ingredientArrayList) {
        Timber.d("IngredientsRecycleViewAdapter");

        this.context = context;
        this.ingredientArrayList = ingredientArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_ingredient, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewMain.setText(ingredientArrayList.get(position).getIngredient());
        holder.textViewSmall.setText(String.valueOf(ingredientArrayList.get(position).getQuantity())+ " " + String.valueOf(ingredientArrayList.get(position).getMeasure()));
    }

    @Override
    public int getItemCount() {
        return ingredientArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMain,textViewSmall;


        public ViewHolder(View itemView) {
            super(itemView);
            textViewMain = (TextView) itemView.findViewById(R.id.main_tv_ingredient);
            textViewSmall = (TextView) itemView.findViewById(R.id.small_tv_ingredient);

        }

    }
}
