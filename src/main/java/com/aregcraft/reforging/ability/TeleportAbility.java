package com.aregcraft.reforging.ability;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Allows the player to teleport in the looking direction
 */
public class TeleportAbility extends Ability {
    /**
     * The maximum distance
     */
    private int distance;

    @Override
    public void perform(Player player) {
        var location = player.getLocation();
        var direction = location.getDirection();
        direction.setY(Math.max(direction.getY(), 0));
        location.add(direction.multiply(distance));
        while (isBlockSolid(location) || isBlockAboveSolid(location)) {
            if (isPlayerCurrentLocation(player, location, direction)) {
                return;
            }
            location.subtract(direction);
        }
        teleportToCenter(player, location);
    }

    private boolean isPlayerCurrentLocation(Player player, Location location, Vector direction) {
        return player.getLocation().add(direction).equals(location);
    }

    private boolean isBlockAboveSolid(Location location) {
        return isBlockSolid(location.clone().add(0, 1, 0));
    }

    private boolean isBlockSolid(Location location) {
        return location.getBlock().getType().isSolid();
    }

    private void teleportToCenter(Player player, Location location) {
        location.setX(location.getBlockX());
        location.setY(location.getBlockY());
        location.setZ(location.getBlockZ());
        player.teleport(location.add(0.5, 0, 0.5));
    }
}
