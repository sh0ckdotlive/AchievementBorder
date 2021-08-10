package com.github.sh0ckr6.achievementborder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Iterator;

/**
 * Listener class to handle events relating to controlling the world border
 *
 * @author sh0ckR6
 * @since 1.0
 */
public class BorderControl implements Listener {
  
  /**
   * Reference to the plugin
   *
   * @since 1.0
   */
  private AchievementBorder plugin;
  
  /**
   * Registers this class as a {@link Listener} for the provided plugin
   *
   * @author sh0ckR6
   * @since 1.0
   */
  public BorderControl(AchievementBorder plugin) {
    this.plugin = plugin;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
  
  /**
   * Updates the border's size when the player unlocks an advancement
   *
   * @param event The {@link PlayerAdvancementDoneEvent} passed to this function automatically
   * @author sh0ckR6
   * @since 1.0
   */
  @EventHandler
  public void onAchievementUnlock(PlayerAdvancementDoneEvent event) {
    // Check if the unlocked advancement is actually an advancement
    // For some reason recipe unlocks also trigger this event
    // and it's absolutely infuriating.
    if (isValidAdvancement(event.getAdvancement())) {
      // Update the border's size
      World world = Bukkit.getWorlds().get(0);
      WorldBorder border = world.getWorldBorder();
  
      border.setSize(border.getSize() + 5, 1);
    }
  }
  
  /**
   * Updates the border's size to the most unlocked advancements when a new player joins
   * This is to ensure the border is the correct size if the new player has more advancements
   * than the highest player on the server<br>
   * <b>NOTE</b>: This really only affects multiplayer and will not have any effect on singleplayer.<br>
   *
   * Example:<br>
   * {@code PlayerA} has <b>8 advancements</b>, and so the border size is <b>41 blocks</b>.<br>
   * The new player, {@code PlayerB}, has <b>10 advancements</b>, and so the border
   * should be updated to <b>51 blocks</b> to accurately reflect this.<br><br>
   *
   * <b>IMPORTANT: THIS MAY BE REMOVED IN A FUTURE UPDATE, IT NEEDS SOME PLAY-TESTING TO SEE IF IT SHOULD STAY.</b>
   *
   * @param event The {@link PlayerJoinEvent} passed to this function automatically
   * @author sh0ckR6
   * @since 1.0
   */
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    WorldBorder border = event.getPlayer().getWorld().getWorldBorder();
    
    // Get the amount of completed advancements of the new player and compare it to the current border's size
    int completedAdvancements = getCompletedAdvancementsCount(event.getPlayer());
    if (completedAdvancements * 5 + 1 > border.getSize()) {
      // Update the border if we need to and alert all online players
      border.setSize(completedAdvancements * 5 + 1, 1);
      for (Player player : Bukkit.getServer().getOnlinePlayers()) {
        player.sendMessage(ChatColor.GOLD + "A new player with more advancements than the highest player has joined, changing border...");
      }
    }
  }
  
  /**
   * Update the border's size to the most unlocked advancements when a player disconnects
   * This is to prevent the border from being bigger than the current unlocked achievements<br>
   * <b>NOTE</b>: This really only affects multiplayer and will not have any effect on singleplayer.<br>
   *
   * Example:<br>
   * {@code PlayerA} currently has <b>8 advancements</b>, so the border is set to <b>41 blocks</b>.<br>
   * Once {@code PlayerA} leaves, {@code PlayerC} becomes the new advancement leader with
   * <b>7 advancements</b>, so the border is set to <b>36 blocks</b> to reflect this.<br><br>
   *
   * <b>IMPORTANT: THIS MAY BE REMOVED IN A FUTURE UPDATE, IT NEEDS SOME PLAY-TESTING TO SEE IF IT SHOULD STAY.</b>
   *
   * @param event The {@link PlayerQuitEvent} passed to this function automatically
   * @author sh0ckR6
   * @since 1.0
   */
  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    WorldBorder border = event.getPlayer().getWorld().getWorldBorder();
    // Cancel this action if the player is not the advancement leader
    if (!(getCompletedAdvancementsCount(event.getPlayer()) * 5 + 1 >= border.getSize())) return;
    
    // Find the new advancement leader and update the border accordingly
    int highestAdvancementCount = 0;
    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      // Even though the player in event.getPlayer() has now disconnected, they are still
      // in getOnlinePlayers() and need to be sorted out of advancement leader finding.
      if (player == event.getPlayer()) continue;
      
      highestAdvancementCount = Math.max(highestAdvancementCount, getCompletedAdvancementsCount(player));
      player.sendMessage(ChatColor.RED + "A player with more advancements than the highest player has left, changing border...");
    }
    
    border.setSize(highestAdvancementCount * 5 + 1, 1);
  }
  
  /**
   * Helper function to get the amount of advancements a player has completed.<br>
   * For some reason this isn't a built-in function. I'm not sure why, but oh well.
   *
   * @param player The player to check for completed advancements
   * @return The number of advancements the {@code player} has completed.
   * @author sh0ckR6
   * @since 1.0
   */
  private int getCompletedAdvancementsCount(Player player) {
    // Iterate over all advancements and count + return the ones completed
    int advancementsCompleted = 0;
    Iterator<Advancement> it = Bukkit.advancementIterator();
    while (it.hasNext()) {
      Advancement advancement = it.next();
      if (player.getAdvancementProgress(advancement).isDone()) {
        advancementsCompleted++;
      }
    }
    
    return advancementsCompleted;
  }
  
  /**
   * Helper function for determining if an {@link Advancement} is actually an advancement<br>
   * For some reason recipe unlocks are considered {@link Advancement}s and  I think
   * I lost my mind trying to solve this for over a solid 2-3 hours.
   *
   * @param advancement The advancement to check
   * @return If the {@code advancement} is a valid advancement
   * @author sh0ckR6
   * @since 1.0
   */
  private boolean isValidAdvancement(Advancement advancement) {
    // Check the advancement category against a hardcoded list
    // Yes it needs to be hardcoded, I feel sorry for whoever has to manually check all the
    // different advancement categories in like 3-4 major version updates for Minecraft.
    // no this does not work with custom advancements, it's a Bukkit limitation
    // for being coded this way, not my issue.
    String advancementKey = advancement.getKey().getKey(); // First .getKey() is the location (vanilla or datapack), second .getKey() is the advancement category + name
    return advancementKey.startsWith("story") ||
           advancementKey.startsWith("nether") ||
           advancementKey.startsWith("adventure") ||
           advancementKey.startsWith("end") ||
           advancementKey.startsWith("husbandry");
  }
}
