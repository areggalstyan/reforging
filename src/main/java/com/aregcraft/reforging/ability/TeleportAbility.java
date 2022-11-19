package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.ability.base.CooldownAbility;
import com.aregcraft.reforging.annotation.Ability;
import com.aregcraft.reforging.math.Vector;
import org.bukkit.entity.Player;

/**
 * Teleports the player in their facing direction.
 */
@Ability
public class TeleportAbility extends CooldownAbility {
    /**
     * Specifies the maximum distance that player can teleport.
     */
    private double distance;

    @Override
    protected void perform(Player player) {
        var location = player.getLocation();
        var direction = new Vector(location.getDirection());
        for (var i = 0; i < Math.floor(distance); i++) {
            var target = direction.multiply(distance - i).at(location.clone());
            if (isUnfilled(location) && isUnfilled(target.add(0, 1, 0))) {
                while (isUnfilled(target.subtract(0, 1, 0)));
                target.setY(Math.floor(target.getY()));
                player.teleport(target.add(0, 1, 0));
                break;
            }
        }
    }
}
