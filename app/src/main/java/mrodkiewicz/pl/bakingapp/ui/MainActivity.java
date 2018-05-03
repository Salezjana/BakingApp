package mrodkiewicz.pl.bakingapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import android.support.design.widget.Snackbar;
import mrodkiewicz.pl.bakingapp.BakingApp;
import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.api.APIService;
import mrodkiewicz.pl.bakingapp.db.RecipeDatabaseHelper;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.helper.Config;
import mrodkiewicz.pl.bakingapp.ui.base.BaseAppCompatActivity;
import mrodkiewicz.pl.bakingapp.ui.fragments.RecipeListFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends BaseAppCompatActivity {
    private ArrayList<Recipe> recipeArrayList;
    private RecipeDatabaseHelper recipeDatabaseHelper;
    private SharedPreferences preferences;
    private boolean isDatabaseWithData;
    private RecipeListFragment firstFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        showProgressDialog(null,"loading");

        firstFragment = new RecipeListFragment();
        recipeArrayList = new ArrayList<Recipe>();
        preferences = this.getSharedPreferences(Config.PREFERENCES_KEY, Context.MODE_PRIVATE);
        recipeDatabaseHelper = new RecipeDatabaseHelper(this);
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();

        isDatabaseWithData = preferences.getBoolean(Config.PREFERENCES_KEY_DATABASE_STATE,false);

        loadRecipes();
        setupView(savedInstanceState);

    }

    private void setupView(Bundle savedInstanceState) {
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

    }

    private void loadRecipes() {
        Timber.d("loadRecipes isOnline " + isInternetEnable());
            if (isDatabaseWithData) {
                ArrayList<Recipe> recipesTMP = (ArrayList<Recipe>) recipeDatabaseHelper.getAllRecipe();
                if (recipesTMP != null){
                    Timber.d("recipesTMP != null");
                    recipeArrayList.addAll(recipeDatabaseHelper.getAllRecipe());
                }
                hideProgressDialog();
                firstFragment.setRecipeList(recipeArrayList);
            }else{
                if (isInternetEnable()) {
                    APIService apiService = BakingApp.getClient().create(APIService.class);
                    Call<List<Recipe>> call;
                    call = apiService.getRecipes();
                    Timber.d("apiService getRecipes ");
                    call.enqueue(new Callback<List<Recipe>>() {
                        @Override
                        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                            Timber.d("onResponse");
                            recipeArrayList.addAll(response.body());
                            listToDatabse(recipeArrayList);
                            Timber.d("beka z typa " + recipeArrayList.size());
                        }

                        @Override
                        public void onFailure(Call<List<Recipe>> call, Throwable t) {
                            Timber.d("onFailure");
                            Timber.d("onFailure Throwable" + t);
                            hideProgressDialog();
                            showErrorSnackabr("onFailure");

                        }
                    });
                }
            }

        }

    private void listToDatabse(List<Recipe> list) {
        recipeDatabaseHelper.deleteAllData();
        for (Recipe recipe:list){
            recipeDatabaseHelper.addRecipe(recipe);
        }
        preferences.edit().putBoolean(Config.PREFERENCES_KEY_DATABASE_STATE,true).apply();
        firstFragment.setRecipeList(recipeArrayList);
        hideProgressDialog();
    }
    private void showErrorSnackabr(String text){
        Snackbar.make(
                findViewById(R.id.fragment_container),
                text,
                Snackbar.LENGTH_INDEFINITE)
                .setAction("ok :(", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }
}
