package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.Reforging;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Gives player damage resistance for a specified period of time. You can have multiple abilities inheriting from this
 * with different options and names.
 */
public class ShieldAbility extends Ability implements Listener {
    private final Set<UUID> players = new HashSet<>();
    /**
     * Specifies the type of the particle which is used to form a circle around the player.
     */
    private Particle particle;
    /**
     * Specifies the number of particles.
     */
    private double particleFrequency;
    /**
     * Specifies the radius of the circle formed around the player.
     */
    private double radius;
    /**
     * Specifies the duration of the shield in ticks (1 second = 20 ticks).
     */
    private int duration;
    /**
     * Specifies whether the player should be prevented from attacking other entities while the shield is active.
     */
    private boolean disableAttack;

    public ShieldAbility() {
        Bukkit.getPluginManager().registerEvents(this, Reforging.PLUGIN);
    }

    @Override
    public void activate(Player player) {
        var id = player.getUniqueId();
        if (players.contains(id)) {
            return;
        }
        players.add(id);
        new BukkitRunnable() {
            private int time = 0;

            @Override
            public void run() {
                if (time++ == duration || player.isDead()) {
                    players.remove(id);
                    cancel();
                }
                for (double i = 0; i < 2 * Math.PI; i += Math.PI / particleFrequency) {
                    player.getWorld().spawnParticle(particle, player.getLocation().add(radius * Math.cos(i), 1,
                            radius * Math.sin(i)), 0);
                }
            }
        }.runTaskTimerAsynchronously(Reforging.PLUGIN, 0, 1);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        event.setCancelled(event.getEntity() instanceof Player entity && players.contains(entity.getUniqueId())
                || disableAttack && event.getDamager() instanceof Player damager
                && players.contains(damager.getUniqueId()));
    }
}
