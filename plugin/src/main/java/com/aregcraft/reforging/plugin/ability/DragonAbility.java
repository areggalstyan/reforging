package com.aregcraft.reforging.plugin.ability;

import com.aregcraft.reforging.plugin.ability.base.ProjectileAbility;
import com.aregcraft.reforging.plugin.annotation.Ability;
import org.bukkit.entity.DragonFireball;

/**
 * Allows the player to throw a dragon fireball.
 */
@Ability
public class DragonAbility extends ProjectileAbility<DragonFireball> {
    protected DragonAbility() {
        super(DragonFireball.class);
    }
}
