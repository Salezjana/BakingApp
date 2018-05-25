package mrodkiewicz.pl.bakingapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.adapter.StepsRecycleViewAdapter;
import mrodkiewicz.pl.bakingapp.db.models.Ingredient;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.db.models.Step;
import mrodkiewicz.pl.bakingapp.helper.Config;
import mrodkiewicz.pl.bakingapp.listeners.RecyclerViewItemClickListener;
import mrodkiewicz.pl.bakingapp.ui.MainActivity;
import timber.log.Timber;

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.step_recycleviewlist)
    RecyclerView stepRecycleviewlist;
    Unbinder unbinder;
    @BindView(R.id.textView_row_recipe)
    TextView textViewRowRecipe;
    @BindView(R.id.card_view)
    CardView cardView;
    private ArrayList<Step> stepArrayList;
    private ArrayList<Recipe> recipeArrayList;
    private StepsRecycleViewAdapter stepsRecycleViewAdapter;
    private int positonR;

    public RecipeDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (recipeArrayList == null) {
            recipeArrayList = new ArrayList<Recipe>();
            stepArrayList = new ArrayList<Step>();
        }
        if (getArguments() != null) {
            recipeArrayList.addAll(getArguments().<Recipe>getParcelableArrayList(Config.BUNDLE_RECIPELIST));
            positonR = getArguments().getInt(Config.BUNDLE_KEY_POSITION);
            stepArrayList.addAll(getArguments().<Recipe>getParcelableArrayList(Config.BUNDLE_RECIPELIST).get(positonR).getSteps());
        }
        setHasOptionsMenu(true);
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

        setupView();
        initListeners();

    }


    private void initListeners() {
        stepRecycleviewlist.addOnItemTouchListener(new RecyclerViewItemClickListener(getContext(), stepRecycleviewlist, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Timber.d("RecipeDetailFragment onItemClick " + position);
                MainActivity mainActivity = (MainActivity) getContext();
                Bundle bundle = new Bundle();
                bundle.putInt(Config.BUNDLE_KEY_POSITION_STEP, position);
                bundle.putInt(Config.BUNDLE_KEY_POSITION, positonR);
                mainActivity.switchFragment(new StepDetailFragment(), bundle);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void setupView() {
        stepsRecycleViewAdapter = new StepsRecycleViewAdapter(getContext(), stepArrayList);
        stepRecycleviewlist.setAdapter(stepsRecycleViewAdapter);
        stepRecycleviewlist.setLayoutManager(new GridLayoutManager(getContext(), 1));
        stepRecycleviewlist.setItemAnimator(new DefaultItemAnimator());
        stepsRecycleViewAdapter.notifyDataSetChanged();

        String string = "";
        StringBuilder stringBuilder = new StringBuilder(string);
        for (Ingredient ingredient : recipeArrayList.get(positonR).getIngredients()) {
            stringBuilder.append(ingredient.getIngredient());
            stringBuilder.append(" ");
            stringBuilder.append(ingredient.getQuantity());
            stringBuilder.append(" ");
            stringBuilder.append(ingredient.getMeasure());
            stringBuilder.append(" \n ");

        }
        string = stringBuilder.toString();

        textViewRowRecipe.setText(string);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
