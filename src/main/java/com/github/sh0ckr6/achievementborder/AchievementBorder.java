package com.github.sh0ckr6.achievementborder;

import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.border.Border;

public final class AchievementBorder extends JavaPlugin {
  
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
}
