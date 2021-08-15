package com.github.sh0ckr6.achievementborder;

import com.github.sh0ckr6.achievementborder.builders.ShapedRecipeBuilder;
import com.github.sh0ckr6.achievementborder.listeners.BorderControl;
import com.github.sh0ckr6.achievementborder.listeners.WorldSetup;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class AchievementBorder extends JavaPlugin {
  
  public List<Advancement> advancements = new ArrayList<>();
  
  /**
   * Plugin setup
   *
   * @author sh0ckR6
   * @since 1.0
   */
  @Override
  public void onEnable() {
    // Plugin startup logic
    new BorderControl(this);
    new WorldSetup(this);
    
    registerRecipes();
  }
  
  /**
   * Plugin tear-down
   *
   * @author sh0ckR6
   * @since 1.0
   */
  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
  
  /**
   * Helper function to register recipes
   *
   * @author sh0ckR6
   * @since 1.0
   */
  private void registerRecipes() {
    new ShapedRecipeBuilder()
            .setRecipeKey("end_stone")
            .setResult(Material.END_STONE)
            .setShape("EEE", "ECE", "EEE")
            .setIngredient('E', Material.ENDER_PEARL)
            .setIngredient('C', Material.COBBLESTONE)
            .register();
    
    new ShapedRecipeBuilder()
            .setRecipeKey("end_portal_frame")
            .setResult(Material.END_PORTAL_FRAME)
            .setShape("E E", "EOE", "EEE")
            .setIngredient('E', Material.END_STONE)
            .setIngredient('O', Material.OBSIDIAN)
            .register();
  }
}
