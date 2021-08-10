package com.github.sh0ckr6.achievementborder;

import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.border.Border;

public final class AchievementBorder extends JavaPlugin {
  
  @Override
  public void onEnable() {
    // Plugin startup logic
    new BorderControl(this);
  }
  
  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
