package mrodkiewicz.pl.bakingapp.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.adapter.RecipesRecycleViewAdapter;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.helper.Config;
import mrodkiewicz.pl.bakingapp.listeners.RecyclerViewItemClickListener;
import mrodkiewicz.pl.bakingapp.ui.MainActivity;
import timber.log.Timber;

public class RecipeListFragment extends Fragment {

    @BindView(R.id.recipe_recycleviewlist)
    RecyclerView recipesRecycleView;
    Unbinder unbinder;

    private ArrayList<Recipe> recipeArrayList;
    private RecipesRecycleViewAdapter recipesRecycleViewAdapter;
    private RecipeDetailFragment recipeDetailFragment;


    public RecipeListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (recipeArrayList == null) {
            recipeArrayList = new ArrayList<Recipe>();
        }
        if (getArguments() != null) {
            recipeArrayList.addAll(getArguments().<Recipe>getParcelableArrayList(Config.BUNDLE_RECIPELIST));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (recipeArrayList == null) {
            recipeArrayList = new ArrayList<>();
        }

        setupView();
        initListeners();
    }

    private void initListeners() {
        recipesRecycleView.addOnItemTouchListener(new RecyclerViewItemClickListener(getContext(), recipesRecycleView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Timber.d("RecipeListFragment onItemClick " + position);
                recipeDetailFragment = new RecipeDetailFragment();
                MainActivity mainActivity = (MainActivity) getContext();
                Bundle bundle = new Bundle();
                bundle.putInt(Config.BUNDLE_KEY_POSITION, position);
                mainActivity.switchFragment(recipeDetailFragment, bundle);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void setupView() {
        recipesRecycleViewAdapter = new RecipesRecycleViewAdapter(getContext(), recipeArrayList);
        recipesRecycleView.setAdapter(recipesRecycleViewAdapter);
        MainActivity mainActivity = (MainActivity) getContext();
        if (mainActivity.getTablet()) {
            recipesRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        } else {
            recipesRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        }
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

}
