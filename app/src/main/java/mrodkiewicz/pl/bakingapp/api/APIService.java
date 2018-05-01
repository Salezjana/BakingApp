package mrodkiewicz.pl.bakingapp.api;

import java.util.List;

import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("baking.json")
    Call<List<Recipe>> getRecipes();

}
