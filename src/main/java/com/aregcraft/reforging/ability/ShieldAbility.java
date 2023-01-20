package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.PersistentDataWrapper;
import com.aregcraft.delta.api.entity.Entities;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.function.Function3;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Allows the player to create a spiral (or any other shape) around them, protecting them from all entities
 */
@ProcessedAbility
public class ShieldAbility extends Ability implements Listener {
    /**
     * The amount of health and hunger deducted from the player upon activation
     */
    private Price price;
    /**
     * The cooldown in ticks (1 second = 20 ticks)
     */
    private long cooldown;
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
    private transient Reforging plugin;

    @Override
    public void activate(Player player) {
        if (cooldownManager.isOnCooldown(player, cooldown, plugin)) {
            return;
        }
        cooldownManager.putOnCooldown(player, plugin);
        var persistentData = PersistentDataWrapper.wrap(plugin, player);
        persistentData.set("shield", true);
        plugin.getSynchronousScheduler().scheduleDelayedTask(() -> persistentData.remove("shield"), duration);
        price.deduct(player);
        plugin.getSynchronousScheduler().scheduleRepeatingTask(() -> update(player), 0, 1, duration);
    }

    private void update(Player player) {
        function.evaluate(it -> Entities.spawnParticle(particle, player.getLocation().add(it)));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        event.setCancelled(PersistentDataWrapper.wrap(plugin, event.getEntity()).check("shield", true)
                || disableAttack && PersistentDataWrapper.wrap(plugin, event.getDamager()).check("shield", true));
    }
}
