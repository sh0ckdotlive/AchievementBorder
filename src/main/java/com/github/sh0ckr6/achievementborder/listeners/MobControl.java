package com.github.sh0ckr6.achievementborder.listeners;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.github.sh0ckr6.achievementborder.AchievementBorder;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Level;

/**
 * Listener event to control the number of hostile mobs
 *
 * @author gtaEPIC
 * @since 1.1
 */
public class MobControl implements Listener {
    /**
     * Reference to the plugin
     *
     * @since 1.1
     */
    private final AchievementBorder plugin;

    private int hostileMobs = 0; // Number of hostile mobs that are spawned

    /**
     * Registers this class as a {@link Listener} for the provided plugin
     *
     * @author gtaEPIC
     * @since 1.1
     */
    public MobControl(AchievementBorder plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) {
            onEntityAddToWorld(new EntityAddToWorldEvent(entity));
        }
    }

    /**
     * Calculates the number of Hostile Mobs should be allowed.
     *
     * @author gtaEPIC
     * @since 1.1
     * @return The max number of Hostile Mobs allowed
     */
    private int getMaxHostile() {
        WorldBorder border = Bukkit.getWorlds().get(0).getWorldBorder();
        double size = border.getSize();
        if (size > 16) {
            return (int) size;
        }else return 0;
    }

    /**
     * Checks to see if an entity is a hostile mob from the overworld
     *
     * @author gtaEPIC
     * @since 1.1
     * @param entity The entity to check
     * @return True if hostile
     */
    private boolean isHostile(Entity entity) {
        String[] hostile = {"Creeper", "Endermite", "Evoker", "Guardian", "Husk", "Silverfish", "Skeleton", "Slime",
        "Stray", "Vex", "Vindicator", "Witch", "Zombie", "Zombie Villager"};
        for (String name : hostile) {
            if (entity.getName().equals(name)) return true;
        }
        return false;
    }

    /**
     * Runs to check every new entity that is added to the game
     *
     * @author gtaEPIC
     * @since 1.1
     * @param event New Entity Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onEntityAddToWorld(EntityAddToWorldEvent event) {
        plugin.getLogger().log(Level.INFO, event.getEntity().getName() + " | " + isHostile(event.getEntity()));
        if (isHostile(event.getEntity())) {
            plugin.getLogger().log(Level.INFO, hostileMobs + " / " + getMaxHostile());
            if (getMaxHostile() < hostileMobs + 1) event.getEntity().remove(); // Cancel spawn
            hostileMobs++;
        }
    }

    /**
     * Removes the number of hostile mobs (if it is hostile) that are alive
     *
     * @author gtaEPIC
     * @since 1.1
     * @param event Entity Removed Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onEntityRemoveFromWorld(EntityRemoveFromWorldEvent event) {
        if (isHostile(event.getEntity())) {
            plugin.getLogger().log(Level.INFO, event.getEntity().getName() + " was removed");
            hostileMobs--;
        }
    }
}
