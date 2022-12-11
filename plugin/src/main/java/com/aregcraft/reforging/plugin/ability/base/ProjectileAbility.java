package com.aregcraft.reforging.plugin.ability.base;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

public class ProjectileAbility<T extends Projectile> extends CooldownAbility {
    /**
     * Specifies the speed of the projectile.
     */
    private double speed;
    private final transient Class<T> projectile;

    protected ProjectileAbility(Class<T> projectile) {
        this.projectile = projectile;
    }

    @Override
    protected void perform(Player player) {
        var launchedProjectile = player.launchProjectile(projectile);
        configure(launchedProjectile);
        launchedProjectile.setVelocity(player.getLocation().getDirection().multiply(speed));
    }

    protected void configure(T projectile) {
    }
}
