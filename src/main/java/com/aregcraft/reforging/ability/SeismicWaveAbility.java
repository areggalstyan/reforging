package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.PlayerRegistry;
import com.aregcraft.delta.api.entity.Entities;
import com.aregcraft.delta.api.entity.EntityFinder;
import com.aregcraft.delta.api.entity.selector.ExcludingSelector;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.function.Function2;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Allows the player to create a spiral (or any other shape) around them, damaging and knocking back all entities
 * within the range
 */
@ProcessedAbility
public class SeismicWaveAbility extends Ability {
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
     * The function describing the shape
     */
    private Function2 function;
    /**
     * The particle used to create the shape
     */
    private Particle particle;
    /**
     * How much to knock back the entities within range
     */
    private Vector knockback;
    /**
     * How high to knock back the entities within range
     */
    private double height;
    /**
     * How much to damage the entities within range
     */
    private double damage;
    /**
     * The range
     */
    private double range;
    private transient Reforging plugin;

    @Override
    public void activate(Player player) {
        if (cooldownPlayers.contains(player)) {
            return;
        }
        cooldownPlayers.add(player, cooldown, plugin);
        price.deduct(player);
        var location = player.getLocation();
        EntityFinder.createAtLocation(location, range)
                .find(LivingEntity.class, new ExcludingSelector(player))
                .forEach(it -> damageAndKnockbackEntity(it, player));
        function.evaluate(it -> Entities.spawnParticle(particle, location.clone().add(it)));
    }

    private void damageAndKnockbackEntity(LivingEntity entity, Player player) {
        entity.damage(damage, player);
        var velocity = entity.getLocation().subtract(player.getLocation()).toVector();
        if (velocity.length() != 0) {
            velocity.normalize().multiply(knockback);
        }
        entity.setVelocity(velocity.setY(height));
    }
}
