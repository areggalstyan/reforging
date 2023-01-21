package com.aregcraft.reforging.ability;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Allows the player to start a rapid movement in the looking direction
 */
public class DashAbility extends Ability {
    /**
     * The velocity
     */
    private Vector velocity;

    @Override
    public void perform(Player player) {
        player.setVelocity(player.getLocation().getDirection().multiply(velocity));
    }
}
