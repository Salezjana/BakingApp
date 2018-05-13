package mrodkiewicz.pl.bakingapp.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.C;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.adapter.StepsRecycleViewAdapter;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.db.models.Step;
import mrodkiewicz.pl.bakingapp.helper.Config;
import mrodkiewicz.pl.bakingapp.listeners.RecyclerViewItemClickListener;
import mrodkiewicz.pl.bakingapp.ui.MainActivity;

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.step_recycleviewlist)
    RecyclerView stepRecycleviewlist;
    Unbinder unbinder;
    private ArrayList<Step> stepArrayList;
    private ArrayList<Recipe> recipeArrayList;
    private StepsRecycleViewAdapter stepsRecycleViewAdapter;

    public RecipeDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (stepArrayList == null) {
            stepArrayList = new ArrayList<Step>();
        }

        setupView();
        initListeners();

    }


    private void initListeners() {
        stepRecycleviewlist.addOnItemTouchListener(new RecyclerViewItemClickListener(getContext(), stepRecycleviewlist, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MainActivity mainActivity = (MainActivity) getContext();
                mainActivity.switchFragment(new StepDetailFragment(), 1);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void setupView() {
        stepsRecycleViewAdapter = new StepsRecycleViewAdapter(getContext(), stepArrayList);
        stepRecycleviewlist.setAdapter(stepsRecycleViewAdapter);
        stepRecycleviewlist.setLayoutManager(new LinearLayoutManager(getContext()));
        stepRecycleviewlist.setItemAnimator(new DefaultItemAnimator());
        stepsRecycleViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setStepArrayList(ArrayList<Step> stepArrayList) {
        this.stepArrayList = stepArrayList;
    }

    public void setRecipeArrayList(ArrayList<Recipe> recipeArrayList, int position) {
        this.recipeArrayList = recipeArrayList;
        setStepArrayList((ArrayList<Step>) recipeArrayList.get(position).getSteps());
    }

}
