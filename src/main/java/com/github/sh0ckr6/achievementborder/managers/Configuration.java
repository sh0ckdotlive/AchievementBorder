package com.github.sh0ckr6.achievementborder.managers;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Configuration {
  public YamlConfiguration yamlConfig;
  public File file;
  public String name;

  public Configuration(YamlConfiguration yamlConfig, File file, String name) {
    this.yamlConfig = yamlConfig;
    this.file = file;
    this.name = name;
  }
}
