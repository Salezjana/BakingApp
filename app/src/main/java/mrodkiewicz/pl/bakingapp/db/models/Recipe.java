package mrodkiewicz.pl.bakingapp.db.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class Recipe {
    @PrimaryKey
    @SerializedName("id")
    private Integer id;
    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;
    @Embedded(prefix = "ingredients_")
    @SerializedName("ingredients")
    private List<Ingredient> ingredients = null;
    @Embedded(prefix = "steps_")
    @SerializedName("steps")
    private List<Step> steps = null;
    @ColumnInfo(name = "servings")
    @SerializedName("servings")
    private Integer servings;
    @ColumnInfo(name = "image")
    @SerializedName("image")
    private String image;

    public Recipe(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
