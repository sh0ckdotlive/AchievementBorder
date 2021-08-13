package com.github.sh0ckr6.achievementborder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

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
  
    for (Player player : Bukkit.getOnlinePlayers()) {
      for (Advancement completedAdvancement : getCompletedAdvancements(player)) {
        if (!plugin.advancements.contains(completedAdvancement)) {
          plugin.advancements.add(completedAdvancement);
        }
      }
    }
    
    updateBorder(Bukkit.getWorlds().get(0).getWorldBorder());
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
    if (!isValidAdvancement(event.getAdvancement())) return;
    
    // Update the master list if needed
    if (!plugin.advancements.contains(event.getAdvancement())) {
      plugin.advancements.add(event.getAdvancement());
    }
  
    for (Player player : Bukkit.getOnlinePlayers()) {
      AdvancementProgress progress = player.getAdvancementProgress(event.getAdvancement());
      for (String criterion : progress.getRemainingCriteria()) {
        progress.awardCriteria(criterion);
      }
    }
  
    // Update every border's size
    for (World world : Bukkit.getWorlds()) {
      updateBorder(world.getWorldBorder());
    }
  }
  
  /**
   * Helper function to update a world's border.
   * @param border The border to update
   * @author sh0ckR6
   * @since 1.0
   */
  private void updateBorder(WorldBorder border) {
    border.setSize(plugin.advancements.size() * 5 + 1, 1);
  }
  
  /**
   * Updates the border's size to include the new {@link Player}'s {@link Advancement}s.<br><br>
   *
   * Example:<br>
   * {@code PlayerA} has the Advancements "Stone Age" and "Acquire Hardware." The border size should be 11 blocks.
   * {@code PlayerB} joins, and has the Advancements "Ice Bucket Challenge" and "Isn't It Iron Pick." {@code PlayerB}'s
   * advancements get added to the master list and the border's size grows to 21 blocks. {@code PlayerA} receives
   * the "Ice Bucket Challenge" and "Isn't it Iron Pick" advancements, while {@code PlayerB} receives the "Stone Age"
   * and "Acquire Hardware" advancements.
   *
   * @param event The {@link PlayerJoinEvent} passed to this function automatically
   * @author sh0ckR6
   * @since 1.0
   */
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    WorldBorder border = event.getPlayer().getWorld().getWorldBorder();
    
    // Add any achievements this player has to the master list if needed
    for (Advancement advancement : getCompletedAdvancements(event.getPlayer())) {
      if (!plugin.advancements.contains(advancement)) {
        plugin.advancements.add(advancement);
      }
    }
    
    // Update every border's size
    for (World world : Bukkit.getWorlds()) {
      updateBorder(world.getWorldBorder());
    }
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
    return getCompletedAdvancements(player).size();
  }
  
  /**
   * Helper function for retrieving a list of valid advancements that a {@link Player} has completed.<br>
   *
   * Example: Loop through all a player's completed advancements and print them to Bukkit's logger<br>
   * <code>
   * for ({@link Advancement} completedAdvancement : getCompletedAdvancements(player) {<br>
   *   Bukkit.getLogger().info(completedAdvancement.getKey().toString());<br>
   * }
   * </code>
   *
   * @param player The player to check completed advancements for
   * @return A list of the player's completed advancements (if any)
   */
  private List<Advancement> getCompletedAdvancements(Player player) {
    // Loop through all advancements and return a list of valid advancements that the player has completed.
    List<Advancement> completedAdvancements = new ArrayList<>();
    Iterator<Advancement> it = Bukkit.advancementIterator();
    while (it.hasNext()) {
      Advancement advancement = it.next();
      if (!isValidAdvancement(advancement)) continue;
      if (player.getAdvancementProgress(advancement).isDone()) {
        completedAdvancements.add(advancement);
      }
    }
    return completedAdvancements;
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
