package com.github.sh0ckr6.achievementborder;

import com.github.sh0ckr6.achievementborder.builders.ShapedRecipeBuilder;
import com.github.sh0ckr6.achievementborder.commands.ConfigCommand;
import com.github.sh0ckr6.achievementborder.listeners.BorderControl;
import com.github.sh0ckr6.achievementborder.listeners.MobControl;
import com.github.sh0ckr6.achievementborder.listeners.WorldSetup;
import com.github.sh0ckr6.achievementborder.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    setupConfigs();
    
    new BorderControl(this);
    new WorldSetup(this);
    new MobControl(this);
    
    registerCommands();
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
    ConfigManager.setInConfig("config", "advancements", advancements.stream().map(advancement -> advancement.getKey().toString()).toArray());
  }
  
  /**
   * Helper function for setting up configuration files
   *
   * @author sh0ckR6
   * @since 1.1
   */
  private void setupConfigs() {
    // Load and cache all configs
    ConfigManager.loadAllConfigs(this);
    
    // Generate configs if not present
    ConfigManager.createIfNotPresent("config", this);
    
    // Setup config.yml defaults
    YamlConfiguration config = ConfigManager.getConfig("config");
    config.addDefault("setup-complete", false);
    config.addDefault("starting-size", 1);
    config.addDefault("advancements", new String[]{});
    Map<String, Boolean> borderWorlds = new HashMap<>();
    for (World world : Bukkit.getWorlds()) {
      borderWorlds.put(world.getName(), true);
    }
    config.addDefault("borders", borderWorlds);
    config.options().copyDefaults(true);
    ConfigManager.saveConfig(config);
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
  
  /**
   * Helper function to register commands
   *
   * @author sh0ckR6
   * @since 1.1
   */
  private void registerCommands() {
    new ConfigCommand(this);
  }
}
