package mrodkiewicz.pl.bakingapp.helper;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Config {
    public static String BAKING_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    public static String EXO_STATE_RESUME_WINDOW = "resumeWindow";
    public static String EXO_STATE_RESUME_POSITION = "resumePosition";
    public static String EXO_STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    public static String STATE_KEY_DATABASE_STATE = "bakingapp.PREFERENCES_KEY.database";
    public static String STATE_KEY_POSITION = "bakingapp.POSITION";

    public static String PREFERENCES_KEY = "bakingapp.PREFERENCES_KEY";
    public static String PREFERENCES_KEY_DATABASE_STATE = "bakingapp.PREFERENCES_KEY.database";
    public static String PREFERENCES_KEY_POSITION = "bakingapp.POSITION";
    public static String BUNDLE_KEY_POSITION = "bakingapp.POSITION";

    public static String BUNDLE_KEY_POSITION_STEP = "bakingapp.POSITION.STEP";


    public static String BUNDLE_RECIPELIST = "bakingapp.BUNDLE_RECIPELIST";

    public static final String CONTENT_AUTHORITY = "mrodkiewicz.pl.bakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String DATABASE_RECIPE = "recipesDB";
    public static final int DATABASE_VERSION_RECIPE = 1;
    public static final String TABLE_RECIPE = "recipes";
    public static final String TABLE_STEP = "steps";
    public static final String TABLE_INGREDIENT = "ingredients";


    public static final class RecipeEntry implements BaseColumns {
        public static String KEY_ID = "_ID";
        public static String KEY_NAME = "name";
        public static String KEY_IMAGE = "image";
        public static String KEY_SERVINGS = "servings";
        public static String KEY_STEP = "steps";
        public static String KEY_INGREDIENT = "ingredients";
    }

    public static final class StepEntry implements BaseColumns {
        public static String KEY_ID = "_ID";
        public static String KEY_FROM_RECIPE_WITH_ID = "fromRecipeWithID";
        public static String KEY_SHORT_DESCRIPTION = "shortDescription";
        public static String KEY_DESCRIPTION = "description";
        public static String KEY_VIDEO_URL = "videoURL";
        public static String KEY_THUMBNAUL_URL = "thumbnailURL";
    }

    public static final class IngredientEntry implements BaseColumns {
        public static String KEY_FROM_RECIPE_WITH_ID = "fromRecipeWithID";
        public static String KEY_QUANTITY = "quantity";
        public static String KEY_MEASURE = "measure";
        public static String KEY_INGREDIENT = "ingredient";

    }

    public static String DATABASE_CREATE_RECIPE = "CREATE TABLE " + Config.TABLE_RECIPE + "("
            + RecipeEntry.KEY_ID + " INTEGER NOT NULL,"
            + RecipeEntry.KEY_NAME + " TEXT NOT NULL,"
            + RecipeEntry.KEY_SERVINGS + " INTEGER NOT NULL,"
            + RecipeEntry.KEY_IMAGE + " TEXT NOT NULL"
            + ")";
    public static String DATABASE_CREATE_STEP = "CREATE TABLE " + Config.TABLE_STEP + "("
            + StepEntry.KEY_ID + " INTEGER NOT NULL,"
            + StepEntry.KEY_FROM_RECIPE_WITH_ID + " INTEGER NOT NULL,"
            + StepEntry.KEY_SHORT_DESCRIPTION + " TEXT NOT NULL,"
            + StepEntry.KEY_DESCRIPTION + " TEXT NOT NULL,"
            + StepEntry.KEY_VIDEO_URL + " TEXT NOT NULL,"
            + StepEntry.KEY_THUMBNAUL_URL + " TEXT NOT NULL"
            + ")";
    public static String DATABASE_CREATE_INGREDIENT = "CREATE TABLE " + Config.TABLE_INGREDIENT + "("
            + IngredientEntry.KEY_FROM_RECIPE_WITH_ID + " INTEGER NOT NULL,"
            + IngredientEntry.KEY_QUANTITY + " TEXT NOT NULL,"
            + IngredientEntry.KEY_MEASURE + " TEXT NOT NULL,"
            + IngredientEntry.KEY_INGREDIENT + " TEXT NOT NULL"
            + ")";


    public static final String CONTENT_DIR_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + DATABASE_RECIPE;

    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + DATABASE_RECIPE;

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(TABLE_RECIPE).build();

    public static Uri buildFlavorsUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
