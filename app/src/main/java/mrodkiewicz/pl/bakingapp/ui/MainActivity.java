package mrodkiewicz.pl.bakingapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.C;

import java.util.ArrayList;
import java.util.List;

import mrodkiewicz.pl.bakingapp.BakingApp;
import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.api.APIService;
import mrodkiewicz.pl.bakingapp.db.RecipeDatabaseHelper;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.helper.ArrayListConverter;
import mrodkiewicz.pl.bakingapp.helper.Config;
import mrodkiewicz.pl.bakingapp.ui.base.BaseAppCompatActivity;
import mrodkiewicz.pl.bakingapp.ui.fragments.RecipeListFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends BaseAppCompatActivity implements
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final int TASK_LOADER_ID = 0;
    private ArrayList<Recipe> recipeArrayList;
    private RecipeDatabaseHelper recipeDatabaseHelper;
    private SharedPreferences preferences;
    private boolean isDatabaseWithData;
    private RecipeListFragment firstFragment;
    private Loader loader;
    private ArrayListConverter arrayListConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        showProgressDialog(null, "loading");

        firstFragment = new RecipeListFragment();
        recipeArrayList = new ArrayList<Recipe>();
        preferences = this.getSharedPreferences(Config.PREFERENCES_KEY, Context.MODE_PRIVATE);
        recipeDatabaseHelper = new RecipeDatabaseHelper(this);
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        arrayListConverter = new ArrayListConverter();

        isDatabaseWithData = preferences.getBoolean(Config.PREFERENCES_KEY_DATABASE_STATE, false);

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
//                ArrayList<Recipe> recipesTMP = (ArrayList<Recipe>) recipeDatabaseHelper.getAllRecipe();
//                if (recipesTMP != null){
//                    Timber.d("recipesTMP != null");
//                    recipeArrayList.addAll(recipeDatabaseHelper.getAllRecipe());
//                }
//                hideProgressDialog();
//                firstFragment.setRecipeList(recipeArrayList);
            loader = getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
        } else {
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
        for (Recipe recipe : list) {
            recipeDatabaseHelper.addRecipe(recipe);
        }
        preferences.edit().putBoolean(Config.PREFERENCES_KEY_DATABASE_STATE, true).apply();
        firstFragment.setRecipeList(recipeArrayList);
        hideProgressDialog();
    }

    private void showErrorSnackabr(String text) {
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(Config.CONTENT_URI,
                            null,
                            null,
                            null,
                            Config.RecipeEntry.KEY_ID);

                } catch (Exception e) {
                    Timber.d("no sie popsulo");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };

    }


    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Timber.d("onLoadFinished");
        if (data == null) {
            Timber.d(" Cursor data = null");
        }else{
            cursorToList(data);
        }
        Timber.d("CONTENT_URI " + Config.CONTENT_URI);
        hideProgressDialog();
    }


    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Timber.d("onLoaderReset");
        hideProgressDialog();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Toast.makeText(getApplicationContext(),"REFRESH ZIOM",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void cursorToList(Cursor cursor) {
        Timber.d("cursorToList");
        ArrayList<Recipe> recipesTMP = new ArrayList<Recipe>();
        if (cursor != null) {
            recipesTMP.clear();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Recipe recipeTMP = new Recipe();
                recipeTMP.setId(cursor.getInt(cursor.getColumnIndex(Config.RecipeEntry.KEY_ID)));
                recipeTMP.setName(cursor.getString(cursor.getColumnIndex(Config.RecipeEntry.KEY_NAME)));
                recipeTMP.setImage(cursor.getString(cursor.getColumnIndex(Config.RecipeEntry.KEY_IMAGE)));
                recipeTMP.setServings(cursor.getInt(cursor.getColumnIndex(Config.RecipeEntry.KEY_SERVINGS)));
                recipeTMP.setSteps(arrayListConverter.getArrayStepsFromString(cursor.getString(cursor.getColumnIndex(Config.RecipeEntry.KEY_STEP))));
                recipeTMP.setIngredients(arrayListConverter.getArrayIngredientFromString(cursor.getString(cursor.getColumnIndex(Config.RecipeEntry.KEY_INGREDIENT))));
                recipesTMP.add(recipeTMP);
                cursor.moveToNext();
            }
        }
        firstFragment.setRecipeList(recipesTMP);
    }

}
