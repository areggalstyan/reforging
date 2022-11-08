package com.aregcraft.reforging.ability;

import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;

/**
 * Allows the player to launch a shulker bullet like an arrow.
 */
public class ShulkerAbility extends CooldownAbility {
    /**
     * Specifies the speed of the projectile when thrown in blocks per tick (1 second = 20 ticks).
     */
    private double speed;

    @Override
    protected void perform(Player player) {
        player.launchProjectile(ShulkerBullet.class).setVelocity(player.getLocation().getDirection().multiply(speed));
    }
}
