package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.PersistentDataWrapper;
import com.aregcraft.delta.api.PlayerRegistry;
import com.aregcraft.delta.api.entity.Entities;
import com.aregcraft.delta.api.entity.ProjectileBuilder;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 * Allows the player to throw a snowball (or any other projectile), freezing the hit entity
 */
@ProcessedAbility
public class FreezeAbility extends Ability implements Listener {
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
    private transient Reforging plugin;

    @Override
    public void activate(Player player) {
        if (cooldownPlayers.contains(player)) {
            return;
        }
        cooldownPlayers.add(player, cooldown, plugin);
        price.deduct(player);
        new ProjectileBuilder()
                .source(player)
                .type(projectileType)
                .velocity(projectileVelocity)
                .direction(player)
                .persistentData("freeze", true)
                .build(plugin);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (PersistentDataWrapper.wrap(plugin, event.getEntity()).check("freeze", true)
                && event.getHitEntity() instanceof LivingEntity entity) {
            Entities.addPotionEffect(entity, PotionEffectType.SLOW, duration, 255, true);
        }
    }
}
