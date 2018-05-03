package mrodkiewicz.pl.bakingapp.ui.fragments;


import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.adapter.RecipesRecycleViewAdapter;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.listeners.RecyclerViewItemClickListener;

public class RecipeListFragment extends Fragment {

    @BindView(R.id.recipe_recycleviewlist)
    RecyclerView recipesRecycleView;
    Unbinder unbinder;

    private ArrayList<Recipe> recipeArrayList;
    private RecipesRecycleViewAdapter recipesRecycleViewAdapter;

    public RecipeListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipeArrayList = new ArrayList<>();

        setupView();
        initListeners();
    }

    private void initListeners() {
        recipesRecycleView.addOnItemTouchListener(new RecyclerViewItemClickListener(getContext(), recipesRecycleView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(view.getContext(),"click" + position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void setupView() {
        recipesRecycleViewAdapter = new RecipesRecycleViewAdapter(getContext(), recipeArrayList);
        recipesRecycleView.setAdapter(recipesRecycleViewAdapter);
        recipesRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipesRecycleView.setItemAnimator(new DefaultItemAnimator());
        recipesRecycleViewAdapter.notifyDataSetChanged();
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

    public void setRecipeList(ArrayList<Recipe> recipeList){
        if (recipeArrayList == null){
            recipeArrayList = new ArrayList<>();
        }
        recipeArrayList.addAll(recipeList);
        if (recipesRecycleViewAdapter != null){
            recipesRecycleViewAdapter.notifyDataSetChanged();
        }
    }
}
