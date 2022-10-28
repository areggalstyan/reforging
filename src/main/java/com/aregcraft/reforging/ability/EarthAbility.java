package com.aregcraft.reforging.ability;

import org.bukkit.entity.Player;

/**
 * This is the fifth base ability. It allows the player to form a protective block circle around them.
 */
public class EarthAbility extends Ability {
    /**
     * Specifies the radius of the circle formed around the player.
     */
    private double radius;
    /**
     * Specifies the "frequency" of the blocks. The "frequency" is used to determine the step angle, which is 180
     * divided by the "frequency". The higher the number is, the further apart will the blocks be.
     */
    private double frequency;

    @Override
    public void activate(Player player) {
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
