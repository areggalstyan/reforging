package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Allows the player to start a rapid movement in the looking direction
 */
@ProcessedAbility
public class DashAbility extends Ability {
    /**
     * The amount of health and hunger deducted from the player upon activation
     */
    private Price price;
    /**
     * The cooldown in ticks (1 second = 20 ticks)
     */
    private long cooldown;
    /**
     * The velocity
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
        player.setVelocity(player.getLocation().getDirection().multiply(velocity));
    }
}
