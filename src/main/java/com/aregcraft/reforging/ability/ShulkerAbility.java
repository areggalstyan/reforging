package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.ability.base.ProjectileAbility;
import com.aregcraft.reforging.annotation.Ability;
import org.bukkit.entity.ShulkerBullet;

/**
 * Allows the player to throw a shulker bullet.
 */
@Ability
public class ShulkerAbility extends ProjectileAbility<ShulkerBullet> {
    protected ShulkerAbility() {
        super(ShulkerBullet.class);
    }
}
