package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.PersistentDataWrapper;
import com.aregcraft.delta.api.entity.Entities;
import com.aregcraft.delta.api.entity.ProjectileBuilder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 * Allows the player to throw a snowball (or any other projectile), freezing the hit entity
 */
public class FreezeAbility extends Ability implements Listener {
    /**
     * The projectile type
     */
    private EntityType projectileType;
    /**
     * The projectile velocity
     */
    private Vector projectileVelocity;
    /**
     * How long to freeze hit entity in ticks (1 second = 20 ticks)
     */
    private int duration;

    @Override
    public void perform(Player player) {
        new ProjectileBuilder()
                .source(player)
                .type(projectileType)
                .velocity(projectileVelocity)
                .direction(player)
                .persistentData("freeze", true)
                .build(getPlugin());
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getHitEntity() instanceof LivingEntity entity && isFreezingProjectile(event.getEntity())) {
            Entities.addPotionEffect(entity, PotionEffectType.SLOW, duration, 255, true);
        }
    }

    private boolean isFreezingProjectile(Projectile projectile) {
        return PersistentDataWrapper.wrap(getPlugin(), projectile).check("freeze", true);
    }
}
