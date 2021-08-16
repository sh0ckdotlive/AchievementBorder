package com.github.sh0ckr6.achievementborder;

import com.github.sh0ckr6.achievementborder.listeners.BorderControl;
import com.github.sh0ckr6.achievementborder.listeners.WorldSetup;
import com.github.sh0ckr6.achievementborder.managers.ConfigManager;
import org.bukkit.advancement.Advancement;
import org.bukkit.configuration.file.YamlConfiguration;
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
    setupConfigs();
    
    new BorderControl(this);
    new WorldSetup(this);
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
    config.options().copyDefaults(true);
    ConfigManager.saveConfig(config);
  }
}
