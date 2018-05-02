package mrodkiewicz.pl.bakingapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import mrodkiewicz.pl.bakingapp.helper.Config;

public class RecipeContentProvider extends ContentProvider{
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int RECIPE = 100;
    private static final int RECIPE_BY_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Config.CONTENT_AUTHORITY;

        matcher.addURI(authority, Config.TABLE_RECIPE, RECIPE);
        matcher.addURI(authority, Config.DATABASE_RECIPE + "/#", RECIPE_BY_ID);


        return matcher;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case RECIPE: {
            }
            case RECIPE_BY_ID: {
                return null;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case RECIPE: {
                return Config.CONTENT_DIR_TYPE;
            }
            case RECIPE_BY_ID: {
                return Config.CONTENT_ITEM_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case RECIPE: {
//                long _id = db.insert(Config.TABLE_MOVIE, null, values);
//                if (_id > 0) {
//                    returnUri = Config.MovieEntry.buildFlavorsUri(_id);
//                } else {
//                    throw new android.database.SQLException("Failed to insert row into: " + uri);
//                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}
