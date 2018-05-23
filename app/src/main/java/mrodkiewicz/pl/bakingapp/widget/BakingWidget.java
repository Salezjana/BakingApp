package mrodkiewicz.pl.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import java.util.ArrayList;

import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.helper.Config;
import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {
    private static final String LEFT_IMAGE_CLICK = "LEFT_IMAGE_CLICK";
    private static final String RIGHT_IMAGE_CLICK = "RIGHT_IMAGE_CLICK";
    private static ArrayList<Recipe> recipeArrayList;
    private SharedPreferences preferences;
    private Context context;
    private int recipePosition;
    private Bundle bundle;

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        this.context = context;

        preferences = context.getSharedPreferences(Config.PREFERENCES_KEY, Context.MODE_PRIVATE);
        recipePosition = preferences.getInt(Config.PREFERENCES_KEY_WIDGET_POSITION, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        if (recipeArrayList != null) {
            views.setTextViewText(R.id.widget_text, recipeArrayList.get(0).getName());
        }
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
        views.setRemoteAdapter(R.id.widget_recipe_list, new Intent(context, BakingWidgetRemoteService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        watchWidget = new ComponentName(context, BakingWidget.class);
        preferences = context.getSharedPreferences(Config.PREFERENCES_KEY, Context.MODE_PRIVATE);
        recipePosition = preferences.getInt(Config.PREFERENCES_KEY_WIDGET_POSITION, 2);

        if (intent.<Recipe>getParcelableArrayListExtra(Config.BUNDLE_RECIPELIST) != null) {
            recipeArrayList = new ArrayList<>();
            recipeArrayList.addAll(intent.<Recipe>getParcelableArrayListExtra(Config.BUNDLE_RECIPELIST));
            BakingWidgetProvider.setRecipeArrayList(recipeArrayList);
        }


        if (LEFT_IMAGE_CLICK.equals(intent.getAction())) {
            if (recipePosition != 0) {
                recipePosition -= 1;
                preferences.edit().putInt(Config.PREFERENCES_KEY_WIDGET_POSITION, recipePosition).apply();
                Timber.d("ECIEPECIE  " + recipePosition);
            }

        }
        if (RIGHT_IMAGE_CLICK.equals(intent.getAction())) {
            if (recipePosition < recipeArrayList.size() - 1) {
                recipePosition += 1;
                preferences.edit().putInt(Config.PREFERENCES_KEY_WIDGET_POSITION, recipePosition).apply();
                Timber.d("ECIEPECIE2  " + recipePosition);
            }
            preferences.edit().putInt(Config.PREFERENCES_KEY_WIDGET_POSITION, recipePosition).apply();

        }
        if (recipeArrayList != null) {
            remoteViews.setTextViewText(R.id.widget_text, recipeArrayList.get(recipePosition).getName());
            Timber.d("ECIEPECIE3  " + recipePosition);
        }

        appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, BakingWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_list);
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

}

