package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.ability.base.ProjectileAbility;
import com.aregcraft.reforging.annotation.Ability;
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
