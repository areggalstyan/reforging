package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.PlayerRegistry;
import com.aregcraft.reforging.Reforging;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Allows the player to start a rapid movement in the looking direction
 */
public class DashAbility extends Ability {
    private final PlayerRegistry cooldownPlayers = PlayerRegistry.createAsynchronous();
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
        if (cooldownPlayers.contains(player)) {
            return;
        }
        cooldownPlayers.add(player, cooldown, plugin);
        price.deduct(player);
        player.setVelocity(player.getLocation().getDirection().multiply(velocity));
    }
}
