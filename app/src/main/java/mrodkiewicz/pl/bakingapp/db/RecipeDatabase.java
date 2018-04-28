package mrodkiewicz.pl.bakingapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import mrodkiewicz.pl.bakingapp.models.Recipe;

@Database(entities = {Recipe.class}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase {
    private static  RecipeDatabase INSTANCE;
    public abstract RecipeDoa recipeDoa();

}
