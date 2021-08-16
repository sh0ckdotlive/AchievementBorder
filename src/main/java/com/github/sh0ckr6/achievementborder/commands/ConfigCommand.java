package com.github.sh0ckr6.achievementborder.commands;

import com.github.sh0ckr6.achievementborder.AchievementBorder;
import com.github.sh0ckr6.achievementborder.managers.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * {@link BaseCommand} class representing the <code>/config</code> command
 *
 * @author sh0ckR6
 * @since 1.1
 */
public class ConfigCommand extends BaseCommand {
  
  /**
   * Registers this class as a {@link CommandExecutor} for the given command
   *
   * @param plugin The plugin to register this to
   * @author sh0ckR6
   * @since 1.1
   */
  public ConfigCommand(AchievementBorder plugin) {
    super(plugin, "config");
  }
  
  /**
   * Extended from this class's parent, after passing all checks in {@link #onCommand}, this function will be run
   *
   * @param sender The sender of the command
   * @param command The {@link Command} that was run
   * @param label The name of the command
   * @param args The arguments passed to the command
   * @return If the command was successfully executed
   * @author sh0ckR6
   * @since 1.1
   */
  @Override
  protected boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (args[0].equalsIgnoreCase("reload")) {
      ConfigManager.reloadConfigs(plugin);
    }
    return true;
  }
}
