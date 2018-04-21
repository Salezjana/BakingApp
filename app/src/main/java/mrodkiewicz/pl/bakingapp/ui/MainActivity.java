package mrodkiewicz.pl.bakingapp.ui;

import android.os.Bundle;


import butterknife.BindView;
import butterknife.ButterKnife;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

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
            Call<Recipe> call;
            call = apiService.getRecipe();
            call.enqueue(new Callback<Recipe>() {
                @Override
                public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                    Timber.d("Callback<Recipe> onResponse" + response.toString());
                }

                @Override
                public void onFailure(Call<Recipe> call, Throwable t) {

                }
            });
        }
    }
}
