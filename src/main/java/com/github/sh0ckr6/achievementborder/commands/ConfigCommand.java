package com.github.sh0ckr6.achievementborder.commands;

import com.github.sh0ckr6.achievementborder.AchievementBorder;
import com.github.sh0ckr6.achievementborder.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;

/**
 * {@link BaseCommand} class representing the <code>/config</code> command
 *
 * @author sh0ckR6
 * @since 1.1
 */
public class ConfigCommand extends BaseCommand implements TabCompleter {
  
  /**
   * Registers this class as a {@link CommandExecutor} for the given command
   *
   * @param plugin The plugin to register this to
   * @author sh0ckR6
   * @since 1.1
   */
  public ConfigCommand(AchievementBorder plugin) {
    super(plugin, "config");
    plugin.getCommand(name).setTabCompleter(this);
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
      handleReload(sender, Arrays.copyOfRange(args, 1, args.length));
    }
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    List<String> tabList = new ArrayList<>();
    if (args.length == 1) {
      tabList.addAll(List.of("reload"));
    } else if (args.length == 2) {
      if (args[0].equals("reload")) {
        tabList.addAll(ConfigManager.getConfigurations().stream().map(config -> config.name).toList());
        tabList.add("*");
      }
    }
    return tabList;
  }
  
  /**
   * Handle <code>reload</code> subcommand
   *
   * @param sender The {@link CommandSender} that sent the command
   * @param args The arguments of this subcommand
   */
  private void handleReload(CommandSender sender, String[] args) {
    String configName = args[0];
    
    if (configName.equals("*")) {
      ConfigManager.reloadConfigs(plugin);
      sender.sendMessage(ChatColor.GREEN + "Reloaded all configuration files!");
      return;
    }
    
    try {
      ConfigManager.reloadConfig(configName, plugin);
      sender.sendMessage(ChatColor.GREEN + "Reloaded the configuration '" + ChatColor.GOLD + name + ChatColor.GREEN + '!');
    } catch (MissingResourceException e) {
      sender.sendMessage(ChatColor.RED + "The requested configuration file could not be found. Check you spelling and try again!");
      sender.sendMessage(ChatColor.RED + "If this issue is repeating and you know it shouldn't, make a " + ChatColor.GOLD + "bug report" + ChatColor.RED + " at https://github.com/sh0ckR6/AchievementBorder/issues/new/choose.");
    }
  }
}
