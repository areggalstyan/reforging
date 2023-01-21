package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.Entities;
import org.bukkit.Location;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Player;

/**
 * Allows the player to summon evoker fangs
 */
public class EvokerAbility extends Ability {
    /**
     * The number of fangs
     */
    private int number;

    @Override
    public void perform(Player player) {
        for (var i = 1; i <= number; i++) {
            var location = getFangLocation(player, i);
            if (!isBlockSolid(location) && !player.getLocation().equals(location)) {
                Entities.spawnEntity(EvokerFangs.class, location).setOwner(player);
            }
        }
    }

    private Location getFangLocation(Player player, int number) {
        var location = player.getLocation();
        location.add(location.getDirection().setY(0).multiply(number));
        while (!isBlockSolid(location)) {
            location.subtract(0, 1, 0);
        }
        while (isBlockSolid(location)) {
            location.add(0, 1, 0);
        }
        return location;
    }

    private boolean isBlockSolid(Location location) {
        return location.getBlock().getType().isSolid();
    }
}
