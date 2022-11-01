package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.Reforging;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Allows players to teleport at the direction that they are looking.
 */
public class TeleportAbility extends Ability {
    private final Set<UUID> players = new HashSet<>();
    /**
     * Specifies the maximum distance that player can teleport.
     */
    private double maxDistance;
    /**
     * Specifies the cooldown in ticks (1 second = 20 ticks).
     */
    private int cooldown;

    @Override
    public void activate(Player player) {
        var id = player.getUniqueId();
        if (players.contains(id)) {
            return;
        }
        players.add(id);
        charge(player);
        var location = player.getLocation();
        var direction = location.getDirection();
        for (int i = 0; i < Math.floor(maxDistance); i++) {
            var target = location.add(direction.multiply(maxDistance - i));
            if (!target.getBlock().getType().isSolid()
                    && !target.add(0, 1, 0).getBlock().getType().isSolid()) {
                while (target.subtract(0, 1, 0).getBlock().getType().isAir());
                player.teleport(target.add(0, 1, 0));
                break;
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                players.remove(id);
            }
        }.runTaskLaterAsynchronously(Reforging.PLUGIN, cooldown);
    }
}
