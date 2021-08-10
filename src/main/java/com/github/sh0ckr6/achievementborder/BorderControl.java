package com.github.sh0ckr6.achievementborder;

import net.md_5.bungee.api.chat.TextComponent;
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

import java.util.Iterator;
import java.util.logging.Level;

public class BorderControl implements Listener {
  
  private AchievementBorder plugin;
  
  public BorderControl(AchievementBorder plugin) {
    this.plugin = plugin;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
  
  @EventHandler
  public void onAchievementUnlock(PlayerAdvancementDoneEvent event) {
    if (isValidAdvancement(event.getAdvancement())) {
      World world = Bukkit.getWorlds().get(0);
      WorldBorder border = world.getWorldBorder();
  
      border.setSize(border.getSize() + 5, 1);
    }
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    int completedAdvancements = getCompletedAdvancementsCount(event.getPlayer());
    WorldBorder border = event.getPlayer().getWorld().getWorldBorder();
    
    if (completedAdvancements * 5 + 1 > border.getSize()) {
      border.setSize(completedAdvancements * 5 + 1, 1);
      for (Player player : Bukkit.getServer().getOnlinePlayers()) {
        player.sendMessage(ChatColor.GOLD + "A new player with more advancements than the highest player has joined, changing border...");
      }
    }
  }
  
  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    WorldBorder border = event.getPlayer().getWorld().getWorldBorder();
    if (!(getCompletedAdvancementsCount(event.getPlayer()) * 5 + 1 >= border.getSize())) return;
    
    int highestAdvancementCount = 0;
    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      if (player == event.getPlayer()) continue;
      highestAdvancementCount = Math.max(highestAdvancementCount, getCompletedAdvancementsCount(player));
      player.sendMessage(ChatColor.RED + "A player with more advancements than the highest player has left, changing border...");
    }
    
    border.setSize(highestAdvancementCount * 5 + 1, 1);
  }
  
  private int getCompletedAdvancementsCount(Player player) {
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
  
  private boolean isValidAdvancement(Advancement advancement) {
    String advancementKey = advancement.getKey().getKey();
    return advancementKey.startsWith("story") ||
           advancementKey.startsWith("nether") ||
           advancementKey.startsWith("adventure") ||
           advancementKey.startsWith("end") ||
           advancementKey.startsWith("husbandry");
  }
}
