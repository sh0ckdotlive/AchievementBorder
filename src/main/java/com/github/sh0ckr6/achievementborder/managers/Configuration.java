package com.github.sh0ckr6.achievementborder.managers;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
   * This configuration's default settings
   *
   * @since latest
   */
  public final Map<String, Object> defaults = new HashMap<>();
  
  /**
   * Constructor to create a new {@link Configuration}
   *
   * @param yamlConfig The {@link YamlConfiguration} this will represent
   * @param file The {@link File} this will represent
   * @param name The name of this configuration
   *
   * @author sh0ckR6
   * @since latest
   */
  public Configuration(YamlConfiguration yamlConfig, File file, String name) {
    this.yamlConfig = yamlConfig;
    this.file = file;
    this.name = name;
  }

  /**
   * Add a new default to the configuration
   *
   * @param key The path to the configuration default
   * @param value The value of the configuration default
   *
   * @author sh0ckR6
   * @since latest
   */
  public void addDefault(String key, Object value) {
    defaults.put(key, value);
    yamlConfig.addDefault(key, value);
  }
}
