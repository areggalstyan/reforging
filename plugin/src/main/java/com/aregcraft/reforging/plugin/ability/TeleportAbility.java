package com.aregcraft.reforging.plugin.ability;

import com.aregcraft.reforging.plugin.ability.base.CooldownAbility;
import com.aregcraft.reforging.plugin.annotation.Ability;
import com.aregcraft.reforging.core.math.Vector;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Allows the player to teleport in their looking direction.
 */
@Ability
public class TeleportAbility extends CooldownAbility {
    /**
     * Specifies the maximum distance the player can teleport.
     */
    private int distance;

    @Override
    protected void perform(Player player) {
        var location = player.getLocation();
        var direction = new Vector(location.getDirection());
        for (var i = 0; i < distance; i++) {
            var target = direction.multiply(distance - i).at(location.clone());
            for (int j = 0; j < i; j++) {
                target.clone().add(0, 1, 0);
            }
            if (isValidTarget(target)) {
                player.teleport(target);
                break;
            }
        }
    }

    private boolean isValidTarget(Location location) {
        return !location.getBlock().getType().isSolid() && !location.clone().add(0, 1, 0).getBlock()
                .getType().isSolid();
    }
}
