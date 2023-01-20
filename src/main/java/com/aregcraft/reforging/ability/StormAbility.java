package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.PersistentDataWrapper;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.function.Function2;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Allows the player to strike a circle (or any other shape) of lightning bolts around them
 */
@ProcessedAbility
public class StormAbility extends Ability implements Listener {
    private static final long INVULNERABILITY_DURATION = 20;

    /**
     * The amount of health and hunger deducted from the player upon activation
     */
    private Price price;
    /**
     * The cooldown in ticks (1 second = 20 ticks)
     */
    private long cooldown;
    /**
     * The function describing the shape
     */
    private Function2 function;
    private transient Reforging plugin;

    @Override
    public void activate(Player player) {
        if (cooldownManager.isOnCooldown(player, cooldown, plugin)) {
            return;
        }
        cooldownManager.putOnCooldown(player, plugin);
        var persistentData = PersistentDataWrapper.wrap(plugin, player);
        persistentData.set("storm", true);
        plugin.getSynchronousScheduler()
                .scheduleDelayedTask(() -> persistentData.remove("storm"), INVULNERABILITY_DURATION);
        price.deduct(player);
        function.evaluate(it -> player.getWorld().strikeLightning(player.getLocation().add(it)));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING
                && PersistentDataWrapper.wrap(plugin, event.getEntity()).check("storm", true)) {
            event.setDamage(0);
        }
    }
}
