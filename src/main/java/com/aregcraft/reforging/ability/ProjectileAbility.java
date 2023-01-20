package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.ProjectileBuilder;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Allows the player to throw a projectile
 */
@ProcessedAbility
public class ProjectileAbility extends Ability {
    /**
     * The amount of health and hunger deducted from the player upon activation
     */
    private Price price;
    /**
     * The cooldown in ticks (1 second = 20 ticks)
     */
    private long cooldown;
    /**
     * The projectile type
     */
    private EntityType type;
    /**
     * The projectile velocity
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
        new ProjectileBuilder()
                .source(player)
                .type(type)
                .velocity(velocity)
                .direction(player)
                .build();
    }
}
