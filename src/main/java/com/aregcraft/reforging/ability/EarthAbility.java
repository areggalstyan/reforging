package com.aregcraft.reforging.ability;

import org.bukkit.entity.Player;

/**
 * Allows the player to form a protective block circle around them.
 */
public class EarthAbility extends Ability {
    /**
     * Specifies the radius of the circle formed around the player.
     */
    private double radius;
    /**
     * Specifies the number of blocks.
     */
    private double frequency;

    @Override
    public void activate(Player player) {
        charge(player);
        for (double i = 0; i < 2 * Math.PI; i += Math.PI / frequency) {
            var location = player.getLocation().add(radius * Math.cos(i), 0, radius * Math.sin(i));
            var block = location.getBlock();
            if (!block.getType().isSolid()) {
                var blockBelow = location.subtract(0, 1, 0).getBlock();
                block.setType(blockBelow.getType());
                blockBelow.breakNaturally();
            }
        }
    }
}
