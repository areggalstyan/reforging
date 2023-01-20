package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.Entities;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * Allows the player to add an effect on themselves
 */
@ProcessedAbility
public class EffectAbility extends Ability {
    /**
     * The amount of health and hunger deducted from the player upon activation
     */
    private Price price;
    /**
     * The cooldown in ticks (1 second = 20 ticks)
     */
    private long cooldown;
    /**
     * The effect type
     */
    private PotionEffectType type;
    /**
     * The effect duration in ticks (1 second = 20 ticks)
     */
    private int duration;
    /**
     * The effect amplifier
     */
    private int amplifier;
    /**
     * Whether to hide the effect particles
     */
    private boolean hideParticles;
    private transient Reforging plugin;

    @Override
    public void activate(Player player) {
        if (cooldownManager.isOnCooldown(player, cooldown, plugin)) {
            return;
        }
        cooldownManager.putOnCooldown(player, plugin);
        price.deduct(player);
        Entities.addPotionEffect(player, type, duration, amplifier, hideParticles);
    }
}
