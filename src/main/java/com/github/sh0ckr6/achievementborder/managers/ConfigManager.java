package com.github.sh0ckr6.achievementborder.managers;

import com.github.sh0ckr6.achievementborder.AchievementBorder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.Set;

/** Utilities for configuration files
 *
 * @author Kalcoder (sh0ckR6)
 * @since 1.1
 */
public class ConfigManager {
  
  /** List of cached configurations
   *
   * @since 1.1
   */
  private static HashMap<YamlConfiguration, File> configurations = new HashMap<>();
  
  /** Loads all configurations and caches them
   * @param plugin The current plugin
   *
   * @author Kalcoder (sh0ckR6)
   * @since 1.1
   */
  public static void loadAllConfigs(JavaPlugin plugin) {
    if (!plugin.getDataFolder().exists()) {
      System.out.println("Making data folder!");
      plugin.getDataFolder().mkdir();
    }
    if (plugin.getDataFolder().listFiles() == null) return;
    System.out.println("Files were found!");
    
    for (File file : plugin.getDataFolder().listFiles()) {
      System.out.println(file.getAbsolutePath());
      YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
      configurations.put(configuration, file);
    }
  }
  
  /** Creates a new configuration
   * @param configName The name of the configuration
   * @param plugin The current plugin
   *
   * @author Kalcoder (sh0ckR6)
   * @since 1.1
   */
  public static void createNewConfig(String configName, JavaPlugin plugin) {
    File configFile = new File(plugin.getDataFolder(), configName + ".yml");
    
    if (configFile.exists()) {
      plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error: Could not create new config called " + configName + " because it already exists!");
      return;
    }
    
    try {
      configFile.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    configurations.put(YamlConfiguration.loadConfiguration(configFile), configFile);
  }
  
  /** Checks if a configuration exists
   * @param configName The name of the configuration
   * @param plugin The current plugin
   *
   * @return true if the given configuration was found
   * @author Kalcoder (sh0ckR6)
   * @since 1.1
   */
  public static boolean configurationExists(String configName, JavaPlugin plugin) {
    File configFile = new File(plugin.getDataFolder(), configName + ".yml");
    return configFile.exists();
  }
  
  /**
   * Generate a new configuration file if the file is not present
   *
   * @param configName The name of the configuration to generate
   * @param plugin The current plugin
   * @return If a new file was created
   * @author sh0ckR6
   * @since 1.1
   */
  public static boolean createIfNotPresent(String configName, JavaPlugin plugin) {
    boolean exists = configurationExists(configName, plugin);
    
    if (!exists) createNewConfig(configName, plugin);
    return !exists;
  }
  
  /** Retrieve all keys from configuration
   * @param name The name of the configuration to retrieve keys from
   * @param deep If false will read only top-level keys
   *
   * @return The keys
   * @throws MissingResourceException if configuration was not found
   * @author Kalcoder (sh0ckR6)
   * @since 1.1
   */
  public static Set<String> getAllKeysFromConfig(String name, Boolean deep) throws MissingResourceException {
    for (File file : configurations.values()) {
      System.out.println("file.getName() = " + file.getName().substring(0, file.getName().length() - 4));
      if (file.getName().substring(0, file.getName().length() - 4).equalsIgnoreCase(name)) return YamlConfiguration.loadConfiguration(file).getKeys(deep);
    }
    throw new MissingResourceException("The requested configuration file could not be found!", name + ".yml", name);
  }
  
  /** Read a value from a configuration
   * @param name The name of the configuration to read from
   * @param path The path to read from
   *
   * @return The value that was read
   * @throws MissingResourceException if configuration was not found
   * @author Kalcoder (sh0ckR6)
   * @since 1.1
   */
  public static <T> T readFromConfig(String name, String path) throws MissingResourceException {
    for (File file : configurations.values()) {
      if (file.getName().equals(name + ".yml")) return (T) YamlConfiguration.loadConfiguration(file).get(path);
    }
    throw new MissingResourceException("The requested configuration file could not be found!", name + ".yml", name);
  }
  
  /** Read a value from a configuration
   * @param config The configuration to read from
   * @param path The path to read from
   *
   * @return The value that was read
   * @author Kalcoder (sh0ckR6)
   * @since 1.1
   */
  public static Object readFromConfig(YamlConfiguration config, String path) {
    return config.get(path);
  }
  
  /** Read a value from a configuration
   * @param configFile The configuration's file
   * @param path The path to read from
   *
   * @return The value that was read
   * @author Kalcoder (sh0ckR6)
   * @since 1.1
   */
  public static Object readFromConfig(File configFile, String path) {
    return YamlConfiguration.loadConfiguration(configFile).get(path);
  }
  
  /** Set a value in a configuration
   * @param name The name of the configuration
   * @param path The path to set the value to
   * @param value The value to set
   *
   * @throws MissingResourceException if configuration was not found
   * @author Kalcoder (sh0ckR6)
   * @since 1.1
   */
  public static <T> void setInConfig(String name, String path, T value) throws MissingResourceException {
    for (File file : configurations.values()) {
      if (file.getName().equalsIgnoreCase(name + ".yml")) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
          config.save(file);
          return;
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    throw new MissingResourceException("The requested configuration file could not be found!", name + ".yml", name);
  }
  
  /**
   * Get a {@link YamlConfiguration} by name
   *
   * @param name The name of the configuration
   * @return The found configuration, if any
   * @throws MissingResourceException If the requested configuration could not be found
   * @author sh0ckR6
   * @since 1.1
   */
  public static YamlConfiguration getConfig(String name) throws MissingResourceException {
    for (YamlConfiguration config : configurations.keySet()) {
      if (configurations.get(config).getName().equals(name + ".yml")) return config;
    }
    throw new MissingResourceException("The requested configuration file could not be found!", name + ".yml", name);
  }
  
  /**
   * Save a {@link YamlConfiguration} to the corresponding {@link File}
   *
   * @param config The {@link YamlConfiguration} to save
   * @throws MissingResourceException If the requested configuration could not be found
   * @author sh0ckR6
   * @since 1.1
   */
  public static void saveConfig(YamlConfiguration config) throws MissingResourceException {
    File configFile = configurations.get(config);
    if (configFile == null) throw new MissingResourceException("The requested configuration file could not be found!", config.getName() + ".yml", config.getName());
    try {
      config.save(configFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Reload all {@link YamlConfiguration}s from disk
   *
   * @param plugin The current plugin
   * @author sh0ckR6
   * @since 1.1
   */
  public static void reloadConfigs(AchievementBorder plugin) {
    configurations = new HashMap<>();
    loadAllConfigs(plugin);
  }
}