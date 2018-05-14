package mrodkiewicz.pl.bakingapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import mrodkiewicz.pl.bakingapp.db.models.Ingredient;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.db.models.Step;
import mrodkiewicz.pl.bakingapp.helper.Config;
import timber.log.Timber;

import static mrodkiewicz.pl.bakingapp.helper.Config.DATABASE_CREATE_INGREDIENT;
import static mrodkiewicz.pl.bakingapp.helper.Config.DATABASE_CREATE_RECIPE;
import static mrodkiewicz.pl.bakingapp.helper.Config.DATABASE_CREATE_STEP;
import static mrodkiewicz.pl.bakingapp.helper.Config.TABLE_INGREDIENT;
import static mrodkiewicz.pl.bakingapp.helper.Config.TABLE_RECIPE;
import static mrodkiewicz.pl.bakingapp.helper.Config.TABLE_STEP;

public class RecipeDatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    public RecipeDatabaseHelper(Context context) {
        super(context, Config.DATABASE_RECIPE, null, Config.DATABASE_VERSION_RECIPE);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Timber.d("RecipeDatabaseHelper onCreate");
        db.execSQL(DATABASE_CREATE_RECIPE);
        db.execSQL(DATABASE_CREATE_STEP);
        db.execSQL(DATABASE_CREATE_INGREDIENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Timber.d("RecipeDatabaseHelper onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENT);
    }

    public void addRecipe(Recipe recipe) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues valuesRecipe = new ContentValues();
        ContentValues valuesStep = new ContentValues();
        ContentValues valuesIngrefient = new ContentValues();
        valuesRecipe.put(Config.RecipeEntry.KEY_ID, recipe.getId());
        valuesRecipe.put(Config.RecipeEntry.KEY_NAME, recipe.getName());
        valuesRecipe.put(Config.RecipeEntry.KEY_IMAGE, recipe.getImage());
        valuesRecipe.put(Config.RecipeEntry.KEY_SERVINGS, recipe.getServings());
        sqLiteDatabase.insertOrThrow(Config.TABLE_RECIPE, null, valuesRecipe);

        for (Step step : recipe.getSteps()) {
            valuesStep.put(Config.StepEntry.KEY_ID, step.getId());
            valuesStep.put(Config.StepEntry.KEY_DESCRIPTION, step.getDescription());
            valuesStep.put(Config.StepEntry.KEY_SHORT_DESCRIPTION, step.getShortDescription());
            valuesStep.put(Config.StepEntry.KEY_THUMBNAUL_URL, step.getThumbnailURL());
            valuesStep.put(Config.StepEntry.KEY_VIDEO_URL, step.getVideoURL());
            valuesStep.put(Config.StepEntry.KEY_FROM_RECIPE_WITH_ID, recipe.getId());
            sqLiteDatabase.insertOrThrow(Config.TABLE_STEP, null, valuesStep);
        }
        for (Ingredient ingredient : recipe.getIngredients()) {
            valuesIngrefient.put(Config.IngredientEntry.KEY_INGREDIENT, ingredient.getIngredient());
            valuesIngrefient.put(Config.IngredientEntry.KEY_MEASURE, ingredient.getMeasure());
            valuesIngrefient.put(Config.IngredientEntry.KEY_QUANTITY, ingredient.getQuantity());
            valuesIngrefient.put(Config.IngredientEntry.KEY_FROM_RECIPE_WITH_ID, recipe.getId());
            sqLiteDatabase.insertOrThrow(Config.TABLE_INGREDIENT, null, valuesIngrefient);
        }

    }

    public List<Recipe> getAllRecipe() {
        ArrayList<Recipe> list = new ArrayList<Recipe>();
        String selectQuery = "SELECT  * FROM " + Config.TABLE_RECIPE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Recipe recipeTMP = new Recipe();
                recipeTMP.setId(cursor.getInt(0));
                recipeTMP.setName(cursor.getString(1));
                recipeTMP.setServings(cursor.getInt(2));
                recipeTMP.setImage(cursor.getString(3));
                String selectQueryStep = "SELECT  * FROM " + Config.TABLE_STEP + " WHERE " + Config.StepEntry.KEY_FROM_RECIPE_WITH_ID + "=" + recipeTMP.getId();
                Cursor cursorStep = db.rawQuery(selectQueryStep, null);
                ArrayList<Step> listSteps = new ArrayList<Step>();
                if (cursorStep.moveToFirst()) {
                    do {
                        Step stepTMP = new Step();
                        stepTMP.setId(cursorStep.getInt(0));
                        stepTMP.setShortDescription(cursorStep.getString(2));
                        stepTMP.setDescription(cursorStep.getString(3));
                        stepTMP.setVideoURL(cursorStep.getString(4));
                        if (cursorStep.getString(5) != null) {
                            stepTMP.setThumbnailURL(cursorStep.getString(5));
                        }
                        listSteps.add(stepTMP);
                    } while (cursorStep.moveToNext());
                }
                recipeTMP.setSteps(listSteps);
                String selectQueryIngredient = "SELECT  * FROM " + Config.TABLE_INGREDIENT + " WHERE " + Config.IngredientEntry.KEY_FROM_RECIPE_WITH_ID + "=" + recipeTMP.getId();
                Cursor cursorIngredient = db.rawQuery(selectQueryIngredient, null);
                ArrayList<Ingredient> listIngredient = new ArrayList<Ingredient>();
                if (cursorIngredient.moveToFirst()) {
                    do {
                        Ingredient ingredientTMP = new Ingredient();
                        ingredientTMP.setIngredient(cursorIngredient.getString(3));
                        ingredientTMP.setMeasure(cursorIngredient.getString(2));
                        ingredientTMP.setQuantity(cursorIngredient.getDouble(1));
                        listIngredient.add(ingredientTMP);
                    } while (cursorIngredient.moveToNext());
                }
                recipeTMP.setIngredients(listIngredient);
                list.add(recipeTMP);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public Recipe getRecipe(int id) {
        return null;
    }

    public List<Step> getStepsFromRecipeWithID(int id) {
        return null;

    }

    public List<Ingredient> getIngredientsFromRecipeWithID(int id) {
        return null;
    }

    public void deleteAllData() {
        getWritableDatabase().execSQL("delete from " + Config.TABLE_RECIPE);
        getWritableDatabase().execSQL("delete from " + Config.TABLE_INGREDIENT);
        getWritableDatabase().execSQL("delete from " + Config.TABLE_STEP);
    }
}
