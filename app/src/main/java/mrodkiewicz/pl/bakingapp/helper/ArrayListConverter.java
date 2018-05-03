package mrodkiewicz.pl.bakingapp.helper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mrodkiewicz.pl.bakingapp.db.models.Ingredient;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.db.models.Step;

public class ArrayListConverter {
    public String getStringFromArrayListIngredient(List<Ingredient> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
    public String getStringFromArrayListStep(List<Step> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
    public ArrayList<Step> getArrayStepsFromString(String string){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Step>>() {}.getType();
        return gson.fromJson(string, type);
    }
    public ArrayList<Ingredient> getArrayIngredientFromString(String string){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Ingredient>>() {}.getType();
        return gson.fromJson(string, type);
    }
}
