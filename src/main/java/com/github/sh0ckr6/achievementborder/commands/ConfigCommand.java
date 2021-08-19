package com.github.sh0ckr6.achievementborder.commands;

import com.github.sh0ckr6.achievementborder.AchievementBorder;
import com.github.sh0ckr6.achievementborder.managers.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
      case "reload":
        ConfigManager.reloadConfigs(plugin);
        break;
      case "edit":
        handleEdit(sender, Arrays.copyOfRange(args, 0, args.length));
    }
    return true;
  }
  
  private void handleEdit(CommandSender sender, String[] args) {
    
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    List<String> tabList = new ArrayList<>();
    if (args.length == 1) {
      tabList.addAll(List.of("reload", "edit"));
      return tabList;
    } else if (args.length == 2) {
      if (args[0].equalsIgnoreCase("edit")) {
        for (File file : ConfigManager.getAllConfigFiles()) {
          tabList.add(file.getName().substring(0, file.getName().length() - 4));
        }
        return tabList;
      }
    } else if (args.length == 3) {
      tabList.addAll(ConfigManager.getAllKeysFromConfig(args[1], true));
    }
    return tabList;
  }
}
