package mrodkiewicz.pl.bakingapp.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import mrodkiewicz.pl.bakingapp.db.models.Recipe;

@Dao
public interface RecipeDoa {
    @Query("SELECT * FROM recipe")
    List<Recipe> getAll();
}
