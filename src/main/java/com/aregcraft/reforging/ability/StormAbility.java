package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.data.Price;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StormAbility implements Ability, Listener {
    private final Set<UUID> players = new HashSet<>();
    private Price price;
    private double radius;
    private double frequency;

    public StormAbility() {
        Bukkit.getPluginManager().registerEvents(this, Reforging.PLUGIN);
    }

    @Override
    public void activate(Player player) {
        var id = player.getUniqueId();
        if (players.contains(id)) {
            return;
        }
        player.damage(price.health);
        player.setFoodLevel(Math.max(player.getFoodLevel() - price.food, 0));
        players.add(id);
        for (double i = 0; i < 2 * Math.PI; i += Math.PI / frequency) {
            player.getWorld().strikeLightning(player.getLocation().add(radius * Math.cos(i), 0,
                    radius * Math.sin(i)));
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                players.remove(id);
            }
        }.runTaskLaterAsynchronously(Reforging.PLUGIN, 22);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(event.getEntity() instanceof Player player
                && event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING
                && players.contains(player.getUniqueId()));
    }
}
