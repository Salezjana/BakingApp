package mrodkiewicz.pl.bakingapp.ui;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import mrodkiewicz.pl.bakingapp.BakingApp;
import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.api.APIService;
import mrodkiewicz.pl.bakingapp.models.Recipe;
import mrodkiewicz.pl.bakingapp.ui.base.BaseAppCompatActivity;
import mrodkiewicz.pl.bakingapp.ui.fragments.RecipeListFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends BaseAppCompatActivity {
    private ArrayList<Recipe> recipeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        recipeArrayList = new ArrayList<>();

        setupView(savedInstanceState);
        loadRecipes();
    }

    private void setupView(Bundle savedInstanceState) {
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            RecipeListFragment firstFragment = new RecipeListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

    }

    private void loadRecipes() {
        Timber.d("loadRecipes isOnline " + isInternetEnable());
        if (isInternetEnable()){
            APIService apiService = BakingApp.getClient().create(APIService.class);
            Call<List<Recipe>> call;
            call = apiService.getRecipes();
            Timber.d("apiService getRecipes ");
            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    Timber.d("onResponse");
                    recipeArrayList.addAll(response.body());
                    Timber.d("beka z typa " + recipeArrayList.size());
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    Timber.d("onFailure");
                    Timber.d("onFailure Throwable" + t);
                }
            });
        }
    }
}
