package com.github.sh0ckr6.achievementborder.commands;

import com.github.sh0ckr6.achievementborder.AchievementBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Base command class that all commands extend. Allows for easy creation of
 * new commands without having to rewrite boilerplate code.
 *
 * @author sh0ckR6
 * @since 1.1
 */
public abstract class BaseCommand implements CommandExecutor {
  
  /**
   * A reference to the current plugin
   *
   * @since 1.1
   */
  protected AchievementBorder plugin;
  
  /**
   * Registers this class as a {@link CommandExecutor} for the given command
   *
   * @param plugin The plugin to register this to
   * @param name The name of the command to register
   * @author sh0ckR6
   * @since 1.1
   */
  public BaseCommand(AchievementBorder plugin, String name) {
    this.plugin = plugin;
    plugin.getCommand(name).setExecutor(this);
  }
  
  /**
   * Run when a {@link CommandSender} executes any command
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
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    return execute(sender, command, label, args);
  }
  
  /**
   * Extended by this class's children, after passing all checks in {@link #onCommand}, this function will be run
   *
   * @param sender The sender of the command
   * @param command The {@link Command} that was run
   * @param label The name of the command
   * @param args The arguments passed to the command
   * @return If the command was successfully executed
   * @author sh0ckR6
   * @since 1.1
   */
  protected abstract boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
}
