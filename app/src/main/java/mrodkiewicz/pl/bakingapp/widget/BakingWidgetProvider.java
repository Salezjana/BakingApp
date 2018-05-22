package mrodkiewicz.pl.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import mrodkiewicz.pl.bakingapp.db.models.Recipe;

public class BakingWidgetProvider implements RemoteViewsService.RemoteViewsFactory {

    private static ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    Context context = null;

    public BakingWidgetProvider(Context context, Intent intent) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        recipeArrayList = new ArrayList<>();
    }

    @Override
    public void onDataSetChanged() {
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
        view.setTextViewText(android.R.id.text1, recipeArrayList.get(position).getIngredients().get(0).getIngredient());
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
