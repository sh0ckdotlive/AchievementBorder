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
    switch (args[0]) {
      case "reload" -> handleReload(sender, Arrays.copyOfRange(args, 1, args.length));
      case "edit" -> handleEdit(sender, Arrays.copyOfRange(args, 1, args.length));
      default -> sender.sendMessage(ChatColor.RED + "Option " + args[0] + " not found! Please check your spelling and try again!");
    }
    return true;
  }
  
  /**
   * Generate tab completion for the arguments of this command
   *
   * @param sender The {@link CommandSender} of the command
   * @param command The {@link Command} that was run
   * @param alias The alias name of this command that was used
   * @param args The current list of arguments
   * @return A list of options for tab completion
   * @author sh0ckR6
   * @since latest
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    List<String> tabList = new ArrayList<>();
    switch (args.length) {
      // Generate tab completion for first argument
      case 1 -> tabList.addAll(List.of("reload", "edit"));
      // Generate tab completion for second argument
      case 2 -> {
        switch (args[0]) {
          // Generate tab completion for /config reload
          case "reload" -> {
            tabList.addAll(ConfigManager.getConfigurations().stream().map(config -> config.name).toList());
            tabList.add("*");
          }
          // Generate tab completion for /config edit
          case "edit" -> tabList.addAll(ConfigManager.getConfigurations().stream().map(config -> config.name).toList());
        }
      }
      // Generate tab completion for third argument
      case 3 -> {
        // Generate tab completion for /config edit <config>
        if (args[0].equalsIgnoreCase("edit")) {
          tabList.addAll(ConfigManager.getAllKeysFromConfig(args[1], true));
        }
      }
      // Generate tab completion for fourth argument
      case 4 -> {
        // Generate tab completion for /config edit <config> <boolean value path>
        if (args[0].equalsIgnoreCase("edit") && ConfigManager.readFromConfig(args[1], args[2]) instanceof Boolean) {
          tabList.addAll(List.of("true", "false"));
        }
      }
    }
    return tabList;
  }
  
  /**
   * Handle <code>reload</code> subcommand
   *
   * @param sender The {@link CommandSender} that sent the command
   * @param args The arguments of this subcommand
   * @author sh0ckR6
   * @since latest
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
  
  /**
   * Handle <code>edit</code> subcommand
   *
   * @param sender The {@link CommandSender} that sent the command
   * @param args The arguments of the subcommand
   * @author sh0ckR6
   * @since latest
   */
  private void handleEdit(CommandSender sender, String[] args) {
    // Check if the user has provided arguments
    if (args.length < 3) {
      sender.sendMessage(ChatColor.RED + "You have provided too little arguments! Check your usage and try again!");
      return;
    }
    if (args.length > 3) {
      sender.sendMessage(ChatColor.RED + "You have provided too many arguments! Check your usage and try again!");
      return;
    }
    
    String config = args[0];
    String path = args[1];
    String value = args[2];
  
    Object oldValue = ConfigManager.readFromConfig(config, path);
    
    if (oldValue == null) {
      sender.sendMessage(ChatColor.RED + "The value you are trying to edit doesn't exist! Please check your spelling and try again");
      return;
    }
    
    // Check if the value we are trying to edit can be edited This must be done because all type information is lost at
    // runtime, which makes editing the files in-game annoyingly difficult.
    if (!(oldValue instanceof Integer) && !(oldValue instanceof String) && !(oldValue instanceof Float) && !(oldValue instanceof Boolean)) {
      sender.sendMessage(ChatColor.RED + "The type of value you are trying to edit is not yet supported! If you would like to see support added for this value, "
                         + ChatColor.BOLD + "please make a feature request on the issue tracker: "
                         + ChatColor.RESET + ChatColor.GOLD + "https://github.com/sh0ckR6/AchievementBorder/issues/new/choose");
      return;
    }
    
    // Convert the provided String value to the required type and set it in the config
    // This could be made much cleaner with JDK 17's "Switch Pattern Matching" but this is JDK 16, not 17.
    try {
      if (oldValue instanceof Integer) {
        ConfigManager.setInConfig(config, path, Integer.parseInt(value));
      } else if (oldValue instanceof String) {
        ConfigManager.setInConfig(config, path, value);
      } else if (oldValue instanceof Float) {
        ConfigManager.setInConfig(config, path, Float.parseFloat(value));
      } else if (oldValue instanceof Boolean) {
        // Check that the provided values are either true or false. The official implementation of Boolean.parseBoolean()
        // only checks if the string is true, not if it's true or false. This means that any string other than true
        // is considered to be false.
        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
          sender.sendMessage(ChatColor.RED + "The value you have provided is invalid! Please check your provided value and try again!");
          return;
        }
        ConfigManager.setInConfig(config, path, Boolean.parseBoolean(value));
      }
    } catch (MissingResourceException e) {
      sender.sendMessage(ChatColor.RED + "The configuration you have requested is non-existent! Check your spelling and try again!");
      return;
    } catch (NumberFormatException e) {
      sender.sendMessage(ChatColor.RED + "The value you have provided is invalid! Please check your provided value and try again!");
      return;
    }
  
    sender.sendMessage(ChatColor.GREEN + "Successfully updated configuration "
                       + ChatColor.GOLD + config
                       + ChatColor.GREEN + " at path "
                       + ChatColor.GOLD + path
                       + ChatColor.GREEN + "! New value: "
                       + ChatColor.GOLD + value);
  }
}
