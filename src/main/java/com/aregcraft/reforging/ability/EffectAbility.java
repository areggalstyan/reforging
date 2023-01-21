package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.Entities;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * Allows the player to add an effect on themselves
 */
public class EffectAbility extends Ability {
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

    @Override
    public void perform(Player player) {
        Entities.addPotionEffect(player, type, duration, amplifier, hideParticles);
    }
}
