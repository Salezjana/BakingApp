package mrodkiewicz.pl.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.helper.Config;
import timber.log.Timber;

public class BakingWidgetProvider implements RemoteViewsService.RemoteViewsFactory {

    private static ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    Context context = null;
    private static int positionRecipe;
    private SharedPreferences preferences;

    public BakingWidgetProvider(Context context, Intent intent) {
        this.context = context;
        preferences = context.getSharedPreferences(Config.PREFERENCES_KEY, Context.MODE_PRIVATE);
        positionRecipe = preferences.getInt(Config.PREFERENCES_KEY_POSITION, 0);
    }

    @Override
    public void onCreate() {
        recipeArrayList = new ArrayList<>();
    }

    @Override
    public void onDataSetChanged() {
        Timber.d("onDataSetChanged " + preferences.getInt(Config.PREFERENCES_KEY_POSITION, 0));
        positionRecipe = preferences.getInt(Config.PREFERENCES_KEY_POSITION, 0);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return recipeArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(context.getPackageName(),
                android.R.layout.simple_list_item_1);
        view.setTextViewText(android.R.id.text1,
                recipeArrayList.get(positionRecipe).getIngredients().get(position).getIngredient()
                        + " " + recipeArrayList.get(positionRecipe).getIngredients().get(position).getQuantity()
                        + " " + recipeArrayList.get(positionRecipe).getIngredients().get(position).getMeasure());
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public static void setRecipeArrayList(ArrayList<Recipe> recipeArrayList) {
        BakingWidgetProvider.recipeArrayList = recipeArrayList;
    }


}
