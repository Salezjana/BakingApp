package mrodkiewicz.pl.bakingapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import mrodkiewicz.pl.bakingapp.db.models.Recipe;

@Database(entities = Recipe.class, version = 1, exportSchema = false)
public abstract class RecipeDatabase  extends RoomDatabase{
    public RecipeDoa recipeDoa;

    public RecipeDatabase(){}

    public RecipeDoa getRecipeDoa() {
        return recipeDoa;
    }
}
