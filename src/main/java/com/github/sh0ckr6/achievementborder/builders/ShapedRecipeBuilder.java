package com.github.sh0ckr6.achievementborder.builders;

import com.github.sh0ckr6.achievementborder.AchievementBorder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapedRecipeBuilder {
  
  ShapedRecipe recipe;
  String recipeKey;
  Material result;
  Map<Character, Material> ingredients;
  List<String> shape;
  
  public ShapedRecipeBuilder() {
    this.ingredients = new HashMap<>();
    this.shape = new ArrayList<>();
  }
  
  public ShapedRecipeBuilder setRecipeKey(String key) {
    this.recipeKey = key;
    return this;
  }
  
  public ShapedRecipeBuilder setResult(Material result) {
    this.result = result;
    return this;
  }
  
  public ShapedRecipeBuilder setIngredient(char key, Material ingredient) {
    this.ingredients.put(key, ingredient);
    return this;
  }
  
  public ShapedRecipeBuilder setShape(String... shape) {
    this.shape.addAll(List.of(shape));
    return this;
  }
  
  public ShapedRecipe register() {
    this.recipe = new ShapedRecipe(NamespacedKey.minecraft(recipeKey), new ItemStack(result));
    
    String[] shapeArray = new String[3];
    this.recipe.shape(this.shape.toArray(shapeArray));
    
    for (char key : ingredients.keySet()) {
      this.recipe.setIngredient(key, ingredients.get(key));
    }
    
    Bukkit.addRecipe(this.recipe);
    return this.recipe;
  }
}
