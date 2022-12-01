package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.ability.base.ProjectileAbility;
import com.aregcraft.reforging.annotation.Ability;
import org.bukkit.entity.WitherSkull;

/**
 * Allows the player to throw a wither skull.
 */
@Ability
public class WitherAbility extends ProjectileAbility<WitherSkull> {
    protected WitherAbility() {
        super(WitherSkull.class);
    }
}
