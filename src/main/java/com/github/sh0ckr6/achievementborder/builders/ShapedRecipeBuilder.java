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

/**
 * Builder class for {@link ShapedRecipe}s.<br><br>
 *
 * <b>A recipe key, result, shape, and at least one pair of ingredients</b> are required for a recipe to be registered properly<br>
 * Example: Registering a recipe for a bucket of lava<br>
 * <pre>
 *   new ShapedRecipeBuilder()
 *        .setRecipeKey("lava_bucket")
 *        .setResult(Material.LAVA_BUCKET)
 *        .setShape(" F ", " B ", " F ")
 *        .setIngredient('F', Material.FLINT_AND_STEEL)
 *        .setIngredient('B', Material.BUCKET)
 *        .register();
 * </pre>
 *
 * @author sh0ckR6
 * @since 1.0
 */
public class ShapedRecipeBuilder {
  
  /**
   * The recipe that will be registered
   *
   * @since 1.0
   */
  ShapedRecipe recipe;
  /**
   * The unique key for this recipe
   *
   * @since 1.0
   */
  String recipeKey;
  /**
   * The result of this recipe
   *
   * @since 1.0
   */
  Material result;
  /**
   * A map of ingredients for this recipe, where each <code>char key</code> corresponds to a key in the
   * recipe's {@link #shape}.
   *
   * @since 1.0
   * @see #shape
   */
  Map<Character, Material> ingredients;
  /**
   * The shape of the recipe, where each letter in the recipe corresponds to a {@link Material} set by
   * the {@link #ingredients} map.
   *
   * @since 1.0
   * @see #ingredients
   */
  List<String> shape;
  
  /**
   * Initializes required variables to being building
   *
   * @author sh0ckR6
   * @since 1.0
   */
  public ShapedRecipeBuilder() {
    this.ingredients = new HashMap<>();
    this.shape = new ArrayList<>();
  }
  
  /**
   * Sets the recipe's unique key
   *
   * @param key The unique key of this recipe
   * @return The current builder to continue building
   * @author sh0ckR6
   * @since 1.0
   * @see #recipeKey
   */
  public ShapedRecipeBuilder setRecipeKey(String key) {
    this.recipeKey = key;
    return this;
  }
  
  /**
   * Sets the recipe's result
   *
   * @param result The result of the recipe
   * @return The current builder to continue building
   * @author sh0ckR6
   * @since 1.0
   * @see #result
   */
  public ShapedRecipeBuilder setResult(Material result) {
    this.result = result;
    return this;
  }
  
  /**
   * Sets a shape's key to an ingredient
   *
   * @param key The unique key of this ingredient
   * @param ingredient The {@link Material} the provided key represents
   * @return The current builder to continue building
   * @author sh0ckR6
   * @since 1.0
   * @see #ingredients
   * @see #shape
   */
  public ShapedRecipeBuilder setIngredient(char key, Material ingredient) {
    this.ingredients.put(key, ingredient);
    return this;
  }
  
  /**
   * Sets the recipe's shape.<br>
   * Each {@link String} passed to the function represents a row in the crafting grid, with each letter representing
   * an ingredient used in crafting.
   *
   * @param shape The shape of the recipe
   * @return The current builder to continue building
   * @author sh0ckR6
   * @since 1.0
   * @see #shape
   */
  public ShapedRecipeBuilder setShape(String... shape) {
    this.shape.addAll(List.of(shape));
    return this;
  }
  
  /**
   * Generates a recipe and registers it to Bukkit.
   *
   * @return The created recipe
   * @author sh0ckR6
   * @since 1.0
   */
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
