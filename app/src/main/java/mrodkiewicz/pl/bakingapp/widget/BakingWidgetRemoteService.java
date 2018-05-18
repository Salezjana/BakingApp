package mrodkiewicz.pl.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class BakingWidgetRemoteService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingWidgetProvider(this, intent);
    }
}
