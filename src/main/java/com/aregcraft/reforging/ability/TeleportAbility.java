package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.PlayerRegistry;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Allows the player to teleport in the looking direction
 */
@ProcessedAbility
public class TeleportAbility extends Ability {
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
     * The maximum distance
     */
    private int distance;
    private transient Reforging plugin;

    @Override
    public void activate(Player player) {
        if (cooldownPlayers.contains(player)) {
            return;
        }
        cooldownPlayers.add(player, cooldown, plugin);
        price.deduct(player);
        var location = player.getLocation();
        var direction = location.getDirection();
        direction.setY(Math.max(direction.getY(), 0));
        location.add(direction.multiply(distance));
        while (isBlockSolid(location) || isBlockSolid(location.clone().add(0, 1, 0))) {
            if (player.getLocation().add(direction).equals(location)) {
                return;
            }
            location.subtract(direction);
        }
        location.setX(location.getBlockX());
        location.setY(location.getBlockY());
        location.setZ(location.getBlockZ());
        player.teleport(location.add(0.5, 0, 0.5));
    }

    private boolean isBlockSolid(Location location) {
        return location.getBlock().getType().isSolid();
    }
}
