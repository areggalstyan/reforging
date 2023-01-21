package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.ProjectileBuilder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Allows the player to throw a projectile
 */
public class ProjectileAbility extends Ability {
    /**
     * The projectile type
     */
    private EntityType type;
    /**
     * The projectile velocity
     */
    private Vector velocity;

    @Override
    public void perform(Player player) {
        new ProjectileBuilder()
                .source(player)
                .type(type)
                .velocity(velocity)
                .direction(player)
                .build();
    }
}
