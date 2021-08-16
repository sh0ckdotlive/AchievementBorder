package com.github.sh0ckr6.achievementborder.commands;

import com.github.sh0ckr6.achievementborder.AchievementBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class BaseCommand implements CommandExecutor {
  
  protected AchievementBorder plugin;
  
  public BaseCommand(AchievementBorder plugin, String name) {
    this.plugin = plugin;
    plugin.getCommand(name).setExecutor(this);
  }
  
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    return execute(sender, command, label, args);
  }
  
  protected abstract boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
}
