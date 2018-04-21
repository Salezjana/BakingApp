package mrodkiewicz.pl.bakingapp.ui;

import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.ui.base.BaseAppCompatActivity;
import mrodkiewicz.pl.bakingapp.ui.fragments.RecipeListFragment;
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

        }
    }
}
