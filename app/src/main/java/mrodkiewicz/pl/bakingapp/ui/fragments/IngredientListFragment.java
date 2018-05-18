package mrodkiewicz.pl.bakingapp.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.adapter.IngredientsRecycleViewAdapter;
import mrodkiewicz.pl.bakingapp.db.models.Ingredient;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.helper.Config;

public class IngredientListFragment extends Fragment {

    @BindView(R.id.recipe_recycleviewlist)
    RecyclerView recipeRecycleviewlist;
    Unbinder unbinder;
    private ArrayList<Ingredient> ingredientArrayList = new ArrayList<Ingredient>();
    private IngredientsRecycleViewAdapter ingredientsRecycleViewAdapter;

    public IngredientListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ingredientArrayList.addAll(getArguments().<Recipe>getParcelableArrayList(Config.BUNDLE_RECIPELIST).get(getArguments().getInt(Config.BUNDLE_KEY_POSITION)).getIngredients());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }


    private void setupView() {
        ingredientsRecycleViewAdapter = new IngredientsRecycleViewAdapter(getContext(), ingredientArrayList);
        recipeRecycleviewlist.setAdapter(ingredientsRecycleViewAdapter);
        recipeRecycleviewlist.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeRecycleviewlist.setItemAnimator(new DefaultItemAnimator());
        ingredientsRecycleViewAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
