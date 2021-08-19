package com.github.sh0ckr6.achievementborder;

import com.github.sh0ckr6.achievementborder.builders.ShapedRecipeBuilder;
import com.github.sh0ckr6.achievementborder.commands.ConfigCommand;
import com.github.sh0ckr6.achievementborder.listeners.BorderControl;
import com.github.sh0ckr6.achievementborder.listeners.MobControl;
import com.github.sh0ckr6.achievementborder.listeners.WorldSetup;
import com.github.sh0ckr6.achievementborder.managers.ConfigManager;
import com.github.sh0ckr6.achievementborder.managers.Configuration;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing the current {@link JavaPlugin}
 *
 * {@link #onEnable()} is the main entry point of the plugin
 *
 * @author sh0ckR6
 * @author gtaEPIC
 * @since 1.0
 */
public final class AchievementBorder extends JavaPlugin {
  
  public List<Advancement> advancements = new ArrayList<>();
  
  /**
   * Plugin setup
   *
   * @author sh0ckR6
   * @author gtaEPIC
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
    Configuration config = ConfigManager.getConfig("config");
    config.yamlConfig.addDefault("setup-complete", false);
    config.yamlConfig.addDefault("starting-size", 1);
    config.yamlConfig.addDefault("advancements", new String[]{});
    Map<String, Boolean> borderWorlds = new HashMap<>();
    for (World world : Bukkit.getWorlds()) {
      borderWorlds.put(world.getName(), true);
    }
    config.yamlConfig.addDefault("borders", borderWorlds);
    config.yamlConfig.options().copyDefaults(true);
    ConfigManager.saveConfig(config);
    
    // Reload after setting defaults
    ConfigManager.reloadConfigs(this);
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
