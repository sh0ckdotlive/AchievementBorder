package com.github.sh0ckr6.achievementborder.listeners;

import com.github.sh0ckr6.achievementborder.AchievementBorder;
import com.github.sh0ckr6.achievementborder.managers.ConfigManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listener class to handle events relating to any setup required
 *
 * @author sh0ckR6
 * @since 1.0
 */
public class WorldSetup implements Listener {
  
  /**
   * A reference to the plugin
   *
   * @since 1.0
   */
  private AchievementBorder plugin;
  
  /**
   * Holds if the setup has been completed yet
   *
   * @since 1.1
   */
  private boolean setupCompleted = false;
  
  /**
   * Register this class as a {@link Listener} for the provided plugin
   *
   * @param plugin The plugin to register this class under
   * @author sh0ckR6
   * @since 1.0
   */
  public WorldSetup(AchievementBorder plugin) {
    this.plugin = plugin;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
  
  /**
   * Perform first-time setup once a player logs on for the first time.
   *
   * @param event The {@link PlayerJoinEvent} passed to this function automatically
   * @author sh0ckR6
   * @since 1.0
   */
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    // Only continue if this is first time setup
    if (setupCompleted) return; // Check variable to see if setup has been completed.
    setupCompleted = ConfigManager.readFromConfig("config", "setup-complete"); // Pull from config if we haven't completed setup.
    if (setupCompleted) return; // Check the config's result and return if the config says we've completed setup.
    
    // Get the border location from the player's spawn and initialize the border
    Player player = event.getPlayer();
    Location playerSpawnLocation = player.getLocation();
    Location borderLocation = new Location(playerSpawnLocation.getWorld(), Math.floor(playerSpawnLocation.getX()) + 0.5d, playerSpawnLocation.getY(), Math.floor(playerSpawnLocation.getZ()) + 0.5d);
  
    player.getWorld().getWorldBorder().setCenter(borderLocation);
  
    // Move the player so that they can't get out of the border
    player.teleport(borderLocation);
  
    // Give the player three wood logs underneath them to guarantee a start
    for (int i = 1; i <= 3; i++) {
      player.getWorld().getBlockAt(new Location(playerSpawnLocation.getWorld(), playerSpawnLocation.getX(), playerSpawnLocation.getY() - i, playerSpawnLocation.getZ())).setType(Material.OAK_LOG);
    }
    
    // Mark that we've completed setup
    setupCompleted = true;
    ConfigManager.setInConfig("config", "setup-complete", true);
  }
}
