package mrodkiewicz.pl.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import mrodkiewicz.pl.bakingapp.BakingApp;
import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.api.APIService;
import mrodkiewicz.pl.bakingapp.db.RecipeDatabaseHelper;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.helper.ArrayListConverter;
import mrodkiewicz.pl.bakingapp.helper.Config;
import mrodkiewicz.pl.bakingapp.ui.base.BaseAppCompatActivity;
import mrodkiewicz.pl.bakingapp.ui.fragments.FullScreenForTabletFragment;
import mrodkiewicz.pl.bakingapp.ui.fragments.RecipeDetailFragment;
import mrodkiewicz.pl.bakingapp.ui.fragments.RecipeListFragment;
import mrodkiewicz.pl.bakingapp.ui.fragments.StepDetailFragment;
import mrodkiewicz.pl.bakingapp.widget.BakingWidget;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static mrodkiewicz.pl.bakingapp.helper.Config.PREFERENCES_KEY_APPBAR;
import static mrodkiewicz.pl.bakingapp.helper.Config.PREFERENCES_KEY_TABLET;

public class MainActivity extends BaseAppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {


    private static final int TASK_LOADER_ID = 0;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.fragment_container_main)
    @Nullable
    LinearLayout fragmentContainerMain;
    @BindView(R.id.fragment_container_left)
    @Nullable
    FrameLayout fragmentContainerLeft;
    private ArrayList<Recipe> recipeArrayList;
    private RecipeDatabaseHelper recipeDatabaseHelper;
    private SharedPreferences preferences;
    private boolean isDatabaseWithData;
    private RecipeListFragment recipeListFragment;
    private RecipeDetailFragment recipeDetailFragment;
    private StepDetailFragment stepDetailFragment;
    private ArrayListConverter arrayListConverter;
    private FragmentManager fragmentManager;
    private Loader loader;
    private Menu menu;
    private Boolean isTablet;
    private int positonRecipe, postionStep;
    private String lastFragment;
    private boolean shouldReturn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        showProgressDialog(null, "loading");
        ButterKnife.bind(this);

        preferences = this.getSharedPreferences(Config.PREFERENCES_KEY, Context.MODE_PRIVATE);
        //check is table or smartphone
        if (fragmentContainerLeft != null) {
            Timber.d("isTablet = true");
            isTablet = true;
        } else {
            isTablet = false;
        }

        if (savedInstanceState == null) {
            Timber.d("bekolandia savedInstanceState == null");
            recipeDetailFragment = new RecipeDetailFragment();
            recipeListFragment = new RecipeListFragment();
            stepDetailFragment = new StepDetailFragment();

            recipeArrayList = new ArrayList<Recipe>();

            recipeDatabaseHelper = new RecipeDatabaseHelper(this);
            SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
            arrayListConverter = new ArrayListConverter();

            fragmentManager = getSupportFragmentManager();

            isDatabaseWithData = preferences.getBoolean(Config.PREFERENCES_KEY_DATABASE_STATE, false);
            invalidateOptionsMenu();
            loadRecipes();

            Timber.d("WAZNE BARDZO NULL");

            setupView(savedInstanceState);


        } else {
            Timber.d("bekolandia savedInstanceState != null");
            recipeDetailFragment = new RecipeDetailFragment();
            recipeListFragment = new RecipeListFragment();
            stepDetailFragment = new StepDetailFragment();

            recipeDatabaseHelper = new RecipeDatabaseHelper(this);
            recipeArrayList = new ArrayList<Recipe>();
            recipeArrayList.addAll(savedInstanceState.<Recipe>getParcelableArrayList(Config.BUNDLE_RECIPELIST));
            fragmentManager = getSupportFragmentManager();
            hideProgressDialog();

            if (isTablet) {
                Fragment fragmentTEST = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
                if (preferences.getBoolean(PREFERENCES_KEY_TABLET, false)) {
                    getSupportActionBar().setTitle(preferences.getString(Config.PREFERENCES_KEY_APPBAR, "BakingApp"));
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    Timber.d("DOBRZE rz");
                }
            } else {
                Fragment fragmentTEST = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (recipeListFragment.getClass() != fragmentTEST.getClass()) {
                    getSupportActionBar().setTitle(preferences.getString(Config.PREFERENCES_KEY_APPBAR, "BakingApp"));
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    Timber.d("DOBRZE tez");
                }
            }
        }


        preferences.edit().remove(Config.BUNDLE_RECIPE_POSITON).apply();
        preferences.edit().remove(Config.BUNDLE_STEP_POSITION).apply();
        preferences.edit().remove(Config.BUNDLE_FRAGMENT).apply();


    }

    private void setupView(Bundle savedInstanceState) {
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            Timber.d("setupView ");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        preferences = this.getSharedPreferences(Config.PREFERENCES_KEY, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(Config.BUNDLE_RETURN, true).apply();
        Timber.d("bekolandia ONPAUSE");
    }


    @Override
    protected void onResume() {
        super.onResume();
        preferences = this.getSharedPreferences(Config.PREFERENCES_KEY, Context.MODE_PRIVATE);
        Timber.d("bekolandia onResume" + preferences.getBoolean(Config.BUNDLE_RETURN, false));
        if (preferences.getBoolean(Config.BUNDLE_RETURN, false)) {
            recipeDetailFragment = new RecipeDetailFragment();
            Timber.d("bekolandia BUNDLE RETURN == treue");
            String savedFragment = preferences.getString(Config.BUNDLE_FRAGMENT, null);
            Timber.d("preferences.getString(Config.BUNDLE_FRAGMENT,null) == " + savedFragment);
            Timber.d("preferences.getString(Config.BUNDLE_FRAGMENT,null) == " + recipeDetailFragment.getClass());
            if (savedFragment.equals(String.valueOf(this.recipeDetailFragment.getClass()))) {
                Timber.d("bekolandia if  " + savedFragment.equals(String.valueOf(recipeDetailFragment.getClass())));
                Bundle bundle = new Bundle();
                bundle.putInt(Config.BUNDLE_KEY_POSITION, preferences.getInt(Config.BUNDLE_RECIPE_POSITON, 0));
                bundle.putInt(Config.BUNDLE_KEY_POSITION_STEP, preferences.getInt(Config.BUNDLE_STEP_POSITION, 0));
                switchFragment(recipeDetailFragment, bundle);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferences = this.getSharedPreferences(Config.PREFERENCES_KEY, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(Config.BUNDLE_RETURN, false);
        Timber.d("bekolandia onDestroy");
    }

    private void loadRecipes() {
        Timber.d("loadRecipes isOnline " + isInternetEnable());
        if (isDatabaseWithData) {
            loader = getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
        } else {
            if (isInternetEnable()) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                APIService apiService = BakingApp.getClient().create(APIService.class);
                Call<List<Recipe>> call;
                call = apiService.getRecipes();
                Timber.d("apiService getRecipes ");
                call.enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                        Timber.d("onResponse");
                        recipeArrayList.clear();
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
        startFirstFragment();
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
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        Fragment fragmentLeft = getSupportFragmentManager().findFragmentById(R.id.fragment_container_left);
        Fragment fragmentMain = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (isTablet) {
            if (fragmentLeft.getClass() == recipeDetailFragment.getClass()) {
                getSupportFragmentManager().beginTransaction().remove(fragmentLeft).commit();
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                switchFragment(recipeListFragment, null);
            } else {
                Timber.d("onBackPressed fragmentManager.popBackStackImmediate()");
                fragmentManager.popBackStackImmediate();
            }

        } else {
            if (fragment.getClass() == RecipeListFragment.class) {
                Timber.d("fragment.getClass() == RecipeListFragment.class ");
                finish();
                super.onBackPressed();
            } else {
                Timber.d("onBackPressed fragmentManager.popBackStackImmediate()");
                fragmentManager.popBackStackImmediate();
            }
        }
        if (fragment.getClass() == recipeDetailFragment.getClass()) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            setTitleAndSaveIt(getString(R.string.app_name));
        }
    }

    public void setTitleAndSaveIt(String string) {
        getSupportActionBar().setTitle(string);
        preferences.edit().putString(PREFERENCES_KEY_APPBAR, string).apply();
        Timber.d("DOBRZE " + preferences.getString(PREFERENCES_KEY_APPBAR, "asd"));
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
        } else {
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
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Config.BUNDLE_RECIPELIST, recipeArrayList);
        outState.putString(Config.BUNDLE_FRAGMENT, String.valueOf(recipeListFragment.getClass()));
        outState.putInt(Config.BUNDLE_RECIPE_POSITON, preferences.getInt(Config.BUNDLE_RECIPE_POSITON, 0));
        outState.putInt(Config.BUNDLE_STEP_POSITION, preferences.getInt(Config.BUNDLE_STEP_POSITION, 0));

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_refresh).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                isDatabaseWithData = false;
                recipeArrayList.clear();
                recipeDatabaseHelper.deleteAllData();
                showProgressDialog(null, "loading");
                loadRecipes();
                setupView(null);
                return true;
            case android.R.id.home:
                onBackPressed();
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
        recipeArrayList.clear();
        recipeArrayList.addAll(recipesTMP);
        startFirstFragment();
    }

    public void switchFragment(Fragment fragment, @Nullable Bundle bundleARGS) {
        preferences.edit().putString(Config.BUNDLE_FRAGMENT, String.valueOf(fragment.getClass())).apply();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (isTablet) {
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_in_left);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Config.BUNDLE_RECIPELIST, recipeArrayList);
            if (bundleARGS == null) {
                setTitleAndSaveIt(getString(R.string.app_name));
            } else {
                bundle.putInt(Config.BUNDLE_KEY_POSITION, bundleARGS.getInt(Config.BUNDLE_KEY_POSITION));
                bundle.putInt(Config.BUNDLE_KEY_POSITION_STEP, bundleARGS.getInt(Config.BUNDLE_KEY_POSITION_STEP));
                preferences.edit().putInt(Config.BUNDLE_RECIPE_POSITON, bundleARGS.getInt(Config.BUNDLE_KEY_POSITION)).apply();
                preferences.edit().putInt(Config.BUNDLE_STEP_POSITION, bundleARGS.getInt(Config.BUNDLE_KEY_POSITION_STEP)).apply();
                setTitleAndSaveIt(recipeArrayList.get(bundleARGS.getInt(Config.BUNDLE_KEY_POSITION)).getName());
            }

            fragment.setArguments(bundle);
            if (fragment.getClass() != recipeListFragment.getClass()) {
                getSupportFragmentManager().beginTransaction().remove(recipeListFragment).commit();
                preferences.edit().putBoolean(PREFERENCES_KEY_TABLET, true).apply();
                if (fragment.getClass() == stepDetailFragment.getClass()) {
                    getSupportFragmentManager().beginTransaction().remove(stepDetailFragment).commit();

                    stepDetailFragment.setArguments(bundle);
                    fragmentTransaction
                            .replace(R.id.fragment_container, fragment);

                } else {
                    getSupportFragmentManager().beginTransaction().remove(recipeListFragment).commit();

                    fragmentTransaction
                            .replace(R.id.fragment_container_left, fragment);

                    stepDetailFragment.setArguments(bundle);
                    fragmentTransaction
                            .replace(R.id.fragment_container, stepDetailFragment);

                }
            } else {
                for (Fragment fragment1 : getSupportFragmentManager().getFragments()) {
                    getSupportFragmentManager().beginTransaction().remove(fragment1).commit();
                }
                preferences.edit().putBoolean(PREFERENCES_KEY_TABLET, false).apply();
                fragmentTransaction
                        .replace(R.id.fragment_container_main, fragment);

            }
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_in_left);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Config.BUNDLE_RECIPELIST, recipeArrayList);
            if (bundleARGS == null) {
                setTitleAndSaveIt(getString(R.string.app_name));
            } else {
                bundle.putInt(Config.BUNDLE_KEY_POSITION_STEP, bundleARGS.getInt(Config.BUNDLE_KEY_POSITION_STEP));
                bundle.putInt(Config.BUNDLE_KEY_POSITION, bundleARGS.getInt(Config.BUNDLE_KEY_POSITION));
                preferences.edit().putInt(Config.BUNDLE_RECIPE_POSITON, bundleARGS.getInt(Config.BUNDLE_KEY_POSITION)).apply();
                preferences.edit().putInt(Config.BUNDLE_STEP_POSITION, bundleARGS.getInt(Config.BUNDLE_KEY_POSITION_STEP)).apply();
                setTitleAndSaveIt(recipeArrayList.get(bundleARGS.getInt(Config.BUNDLE_KEY_POSITION)).getName());
            }
            fragment.setArguments(bundle);
            fragmentTransaction
                    .replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
        if (fragment.getClass() == recipeListFragment.getClass()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void startFirstFragment() {
        switchFragment(recipeListFragment, null);

        Intent intent = new Intent(this, BakingWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), BakingWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intent.putParcelableArrayListExtra(Config.BUNDLE_RECIPELIST, recipeArrayList);
        sendBroadcast(intent);

    }

    public Boolean getTablet() {
        return isTablet;
    }

    public void setFullScreen(int positonStep, long positionPlayer) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Config.BUNDLE_RECIPELIST, recipeArrayList);
        bundle.putInt(Config.BUNDLE_KEY_POSITION_STEP, positonStep);
        bundle.putLong(Config.BUNDLE_KEY_POSITION, positionPlayer);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (isTablet) {
            Fragment test = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
            Timber.d("REPREZNTUJ " + test.getClass());
            if (test.getClass() == FullScreenForTabletFragment.class) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                switchFragment(recipeDetailFragment, bundle);
            } else {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                FullScreenForTabletFragment fullScreenForTabletFragment = new FullScreenForTabletFragment();
                fullScreenForTabletFragment.setArguments(bundle);
                fragmentTransaction
                        .replace(R.id.fragment_container_main, fullScreenForTabletFragment).commit();
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } else {
            if (getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


    }

}

