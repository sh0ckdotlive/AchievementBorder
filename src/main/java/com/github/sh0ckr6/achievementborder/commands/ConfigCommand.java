package com.github.sh0ckr6.achievementborder.commands;

import com.github.sh0ckr6.achievementborder.AchievementBorder;
import com.github.sh0ckr6.achievementborder.managers.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ConfigCommand extends BaseCommand {
  
  public ConfigCommand(AchievementBorder plugin) {
    super(plugin, "config");
  }
  
  @Override
  protected boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (args[0].equalsIgnoreCase("reload")) {
      ConfigManager.reloadConfigs(plugin);
    }
    return true;
  }
}
