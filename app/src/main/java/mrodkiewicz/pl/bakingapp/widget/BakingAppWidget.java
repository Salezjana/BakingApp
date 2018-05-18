package mrodkiewicz.pl.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.helper.Config;
import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {
    private static SharedPreferences preferences;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        preferences = context.getSharedPreferences(Config.PREFERENCES_KEY,context.MODE_PRIVATE);
        Timber.d("updateAppWidget" + preferences.getInt(Config.PREFERENCES_KEY_POSITION,-1));
        CharSequence widgetText = context.getString(R.string.appwidget_text) + preferences.getInt(Config.PREFERENCES_KEY_POSITION,-1);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Timber.d("onUpdate" + context.getSharedPreferences(Config.PREFERENCES_KEY,Context.MODE_PRIVATE).getInt(Config.PREFERENCES_KEY_POSITION,-1));
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }
}

