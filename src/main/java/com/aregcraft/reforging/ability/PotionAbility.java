package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.ability.base.ProjectileAbility;
import com.aregcraft.reforging.annotation.Ability;
import com.aregcraft.reforging.item.ItemStackWrapper;
import org.bukkit.Material;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.potion.PotionEffectType;

/**
 * Allows the player to throw a potion.
 */
@Ability
public class PotionAbility extends ProjectileAbility<ThrownPotion> {
    /**
     * Specifies the effect of the thrown potion.
     */
    private PotionEffectType effect;
    /**
     * Specifies the duration of the effect in ticks (1 second = 20 ticks).
     */
    private int duration;
    /**
     * Specifies the amplifier of the effect.
     */
    private int amplifier;

    protected PotionAbility() {
        super(ThrownPotion.class);
    }

    @Override
    protected void configure(ThrownPotion projectile) {
        var item = ItemStackWrapper.create(Material.SPLASH_POTION);
        item.addEffect(effect, duration, amplifier);
        projectile.setItem(item.unwrap());
    }
}
