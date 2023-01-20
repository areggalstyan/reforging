package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.ProjectileBuilder;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 * Allows the player to throw a potion with a custom effect
 */
@ProcessedAbility
public class PotionAbility extends Ability {
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
    /**
     * The potion velocity
     */
    private Vector velocity;
    private transient Reforging plugin;

    @Override
    public void activate(Player player) {
        if (cooldownManager.isOnCooldown(player, cooldown, plugin)) {
            return;
        }
        cooldownManager.putOnCooldown(player, plugin);
        price.deduct(player);
        launchProjectile(player).setItem(ItemWrapper.builder()
                .material(Material.SPLASH_POTION)
                .<PotionMeta>editMeta(it -> it.addCustomEffect(getPotionEffect(), true))
                .build().unwrap());
    }

    private ThrownPotion launchProjectile(Player player) {
        return (ThrownPotion) new ProjectileBuilder()
                .source(player)
                .type(EntityType.SPLASH_POTION)
                .velocity(velocity)
                .direction(player)
                .build();
    }

    private PotionEffect getPotionEffect() {
        return new PotionEffect(type, duration, amplifier, !hideParticles, !hideParticles);
    }
}
