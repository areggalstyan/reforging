package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.PersistentDataWrapper;
import com.aregcraft.delta.api.entity.Entities;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

/**
 * Allows the player to deal more damage in exchange for receiving its portion
 */
@ProcessedAbility
public class RageAbility extends Ability implements Listener {
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
    private int duration;
    /**
     * The effect amplifier
     */
    private int amplifier;
    /**
     * How much damage to reflect on the player
     */
    private double multiplier;
    private transient Reforging plugin;

    @Override
    public void activate(Player player) {
        if (cooldownManager.isOnCooldown(player, cooldown, plugin)) {
            return;
        }
        cooldownManager.putOnCooldown(player, plugin);
        var persistentData = PersistentDataWrapper.wrap(plugin, player);
        persistentData.set("rage", true);
        plugin.getSynchronousScheduler().scheduleDelayedTask(() -> persistentData.remove("rage"), duration);
        price.deduct(player);
        Entities.addPotionEffect(player, PotionEffectType.INCREASE_DAMAGE, duration, amplifier, true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player
                && PersistentDataWrapper.wrap(plugin, player).check("rage", true)) {
            player.damage(multiplier * event.getDamage());
        }
    }
}
