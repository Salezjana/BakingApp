package mrodkiewicz.pl.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import java.util.ArrayList;

import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.helper.Config;
import mrodkiewicz.pl.bakingapp.ui.MainActivity;
import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {
    private static final String LEFT_IMAGE_CLICK    = "LEFT_IMAGE_CLICK";
    private static final String RIGHT_IMAGE_CLICK    = "RIGHT_IMAGE_CLICK";
    private static ArrayList<Recipe> recipeArrayList;
    private SharedPreferences preferences;
    private Context context;

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        this.context = context;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setOnClickPendingIntent(R.id.imageButton, getPendingSelfIntent(context, LEFT_IMAGE_CLICK));
        views.setOnClickPendingIntent(R.id.imageButton2, getPendingSelfIntent(context, RIGHT_IMAGE_CLICK));
        setRemoteAdapter(context, views);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_recipe_list,
                new Intent(context, BakingWidgetRemoteService.class));
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);
        preferences = context.getSharedPreferences(Config.PREFERENCES_KEY,Context.MODE_PRIVATE);
        if (LEFT_IMAGE_CLICK.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
            watchWidget = new ComponentName(context, BakingWidget.class);

            Timber.d("ECIEPECIE");
            preferences.edit().putInt(Config.PREFERENCES_KEY_POSITION, 3).apply();

            if (recipeArrayList != null){
                remoteViews.setTextViewText(R.id.widget_text,recipeArrayList.get(1).getName());

            }

            appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, BakingWidget.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_list);

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        }
        if (RIGHT_IMAGE_CLICK.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
            watchWidget = new ComponentName(context, BakingWidget.class);

            Timber.d("ECIEPECIE 2");
            preferences.edit().putInt(Config.PREFERENCES_KEY_POSITION, 1).apply();


            appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, BakingWidget.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_list);

            remoteViews.setTextViewText(R.id.widget_text,"xdd 2");

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }


    public static void setRecipeArrayList(ArrayList<Recipe> recipeArrayList) {
        BakingWidget.recipeArrayList = new ArrayList<>();
        BakingWidget.recipeArrayList = recipeArrayList;
        BakingWidgetProvider.setRecipeArrayList(recipeArrayList);
    }
}

