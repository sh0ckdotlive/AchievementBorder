package com.github.sh0ckr6.achievementborder.managers;

import com.github.sh0ckr6.achievementborder.AchievementBorder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

/** Utilities for configuration files
 *
 * @author Kalcoder (sh0ckR6)
 * @since 1.1
 */
public class ConfigManager {
  
  /** List of cached {@link Configuration}s
   *
   * @since 1.1
   */
  private static List<Configuration> configurations = new ArrayList<>();
  
  /** Loads all {@link Configuration}s and caches them
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
      configurations.add(new Configuration(configuration, file, file.getName().substring(0, file.getName().length() - 4)));
    }
  }
  
  /** Creates a new {@link Configuration}
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
    
    configurations.add(new Configuration(YamlConfiguration.loadConfiguration(configFile), configFile, configName));
  }
  
  /** Checks if a {@link Configuration} exists
   * @param configName The name of the {@link Configuration}
   *
   * @return True if the requested {@link Configuration} was found
   * @author Kalcoder (sh0ckR6)
   * @since 1.1
   */
  public static boolean configurationExists(String configName) {
    return configurations.stream().anyMatch(configuration -> configuration.name.equals(configName));
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
    boolean exists = configurationExists(configName);
    
    if (!exists) createNewConfig(configName, plugin);
    return !exists;
  }
  
  /** Retrieve all keys from configuration
   * @param name The name of the configuration to retrieve keys from
   * @param deep If false will read only top-level keys
   *
   * @return All retrieved keys
   * @throws MissingResourceException if configuration was not found
   * @author Kalcoder (sh0ckR6)
   * @since 1.1
   */
  public static Set<String> getAllKeysFromConfig(String name, Boolean deep) throws MissingResourceException {
    for (Configuration configuration : configurations) {
      if (configuration.name.equalsIgnoreCase(name)) return configuration.yamlConfig.getKeys(deep);
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
    for (Configuration configuration : configurations) {
      if (configuration.name.equals(name)) return (T) configuration.yamlConfig.get(path);
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
    for (Configuration configuration : configurations) {
      if (configuration.name.equalsIgnoreCase(name)) {
        configuration.yamlConfig.set(path, value);
        try {
          configuration.yamlConfig.save(configuration.file);
          return;
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    throw new MissingResourceException("The requested configuration file could not be found!", name + ".yml", name);
  }
  
  /**
   * Get a {@link Configuration} by name
   *
   * @param name The name of the configuration
   * @return The found configuration, if any
   * @author sh0ckR6
   * @since 1.1
   */
  public static Configuration getConfig(String name) throws MissingResourceException {
    return configurations.stream().filter(config -> config.name.equals(name)).findFirst().get();
  }
  
  /**
   * Save a {@link YamlConfiguration} to the corresponding {@link File}
   *
   * @param config The {@link Configuration} to save
   * @throws MissingResourceException If the requested configuration could not be found
   * @author sh0ckR6
   * @since 1.1
   */
  public static void saveConfig(Configuration config) throws MissingResourceException {
    if (config.file == null) throw new MissingResourceException("The requested configuration file could not be found!", config.name + ".yml", config.name);
    try {
      config.yamlConfig.save(config.file);
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
    configurations = new ArrayList<>();
    loadAllConfigs(plugin);
  }
  
  /**
   * Reload a given {@link YamlConfiguration} from disk
   *
   * @param name The name of the configuration to reload
   * @param plugin The current plugin
   * @throws MissingResourceException if the requested configuration could not be found
   * @author sh0ckR6
   * @since latest
   */
  public static void reloadConfig(String name, AchievementBorder plugin) throws MissingResourceException {
    if (configurations.stream().filter(config -> config.name.equals(name)).findFirst().isEmpty()) {
      throw new MissingResourceException("The requested configuration file could not be found!", name + ".yml", name);
    }
    
    Configuration configuration = configurations.stream().filter(config -> config.name.equals(name)).findFirst().get();
    
    configurations.remove(configuration);
    configuration.yamlConfig = YamlConfiguration.loadConfiguration(configuration.file);
    configurations.add(configuration);
  }
  
  /**
   * Return a list of all registered {@link Configuration}s
   *
   * @return The list of registered {@link Configuration}s
   * @author sh0ckR6
   * @since latest
   */
  public static List<Configuration> getConfigurations() {
    return configurations;
  }
}