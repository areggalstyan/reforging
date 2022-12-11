package com.aregcraft.reforging.plugin.ability;

import com.aregcraft.reforging.plugin.ability.base.ProjectileAbility;
import com.aregcraft.reforging.plugin.annotation.Ability;
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
