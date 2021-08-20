package com.github.sh0ckr6.achievementborder.managers;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Represents a {@link YamlConfiguration}, it's corresponding {@link File}, and the name of the configuration
 *
 * @author sh0ckR6
 * @since latest
 */
public class Configuration {
  
  /**
   * The current {@link YamlConfiguration}
   *
   * @since latest
   */
  public YamlConfiguration yamlConfig;
  
  /**
   * The current {@link File}
   *
   * @since latest
   */
  public File file;
  
  /**
   * This configuration's name
   *
   * @since latest
   */
  public String name;
  
  /**
   * Constructor to create a new Configuration
   *
   * @param yamlConfig The {@link YamlConfiguration} this will represent
   * @param file The {@link File} this will represent
   * @param name The name of this configuration
   */
  public Configuration(YamlConfiguration yamlConfig, File file, String name) {
    this.yamlConfig = yamlConfig;
    this.file = file;
    this.name = name;
  }
}
