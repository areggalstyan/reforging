package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.Entities;
import com.aregcraft.reforging.function.Function3;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Allows the player to create a spiral (or any other shape) around them, protecting them from all entities
 */
public class ShieldAbility extends Ability implements Listener {
    /**
     * The duration in ticks (1 second = 20 ticks)
     */
    private long duration;
    /**
     * The function describing the shape
     */
    private Function3 function;
    /**
     * The particle used to create the shape
     */
    private Particle particle;
    /**
     * Whether the player should not be able to attack other entities
     */
    private boolean disableAttack;

    @Override
    public void perform(Player player) {
        setPlayerActive(player, duration);
        getPlugin().getSynchronousScheduler()
                .scheduleRepeatingTask(() -> update(player), 0, 1, duration);
    }

    private void update(Player player) {
        function.evaluate(it -> Entities.spawnParticle(particle, player.getLocation().add(it)));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        event.setCancelled(isPlayerActive(event.getEntity()) || disableAttack && isPlayerActive(event.getDamager()));
    }
}
