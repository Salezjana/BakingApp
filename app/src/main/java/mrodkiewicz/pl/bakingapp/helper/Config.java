package mrodkiewicz.pl.bakingapp.helper;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Config {
    public static String BAKING_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    public static final String CONTENT_AUTHORITY = "mrodkiewicz.pl.bakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String DATABASE_RECIPE = "recipesDB";
    public static final String TABLE_RECIPE = "recipes";

    public static final String CONTENT_DIR_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + DATABASE_RECIPE;

    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + DATABASE_RECIPE;

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(DATABASE_RECIPE).build();

    public static Uri buildFlavorsUri(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
