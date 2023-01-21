package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.LazyValue;
import com.aregcraft.delta.api.entity.ProjectileBuilder;
import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 * Allows the player to throw a potion with a custom effect
 */
public class PotionAbility extends Ability {
    private final transient LazyValue<ItemStack> item = LazyValue.of(this::getItem);
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

    @Override
    public void perform(Player player) {
        new ProjectileBuilder()
                .source(player)
                .type(EntityType.SPLASH_POTION)
                .velocity(velocity)
                .direction(player)
                .<ThrownPotion>build()
                .setItem(item.get());
    }

    private ItemStack getItem() {
        return ItemWrapper.builder()
                .material(Material.SPLASH_POTION)
                .<PotionMeta>editMeta(it -> it.addCustomEffect(getPotionEffect(), true))
                .build().unwrap();
    }

    private PotionEffect getPotionEffect() {
        return new PotionEffect(type, duration, amplifier, !hideParticles, !hideParticles);
    }
}
