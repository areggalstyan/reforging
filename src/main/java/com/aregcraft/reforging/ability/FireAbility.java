package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.Entities;
import com.aregcraft.delta.api.entity.EntityFinder;
import com.aregcraft.delta.api.entity.selector.ExcludingSelector;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.function.Function3;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Allows the player to create a spiral in the looking direction (or any other shape) of fire, igniting all touching
 * entities
 */
@ProcessedAbility
public class FireAbility extends Ability {
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
    private Function3 function;
    /**
     * The particle used to create the shape
     */
    private Particle particle;
    /**
     * How long the entities should burn in ticks (1 second = 20 ticks)
     */
    private int fireDuration;
    private transient Reforging plugin;

    @Override
    public void activate(Player player) {
        if (cooldownManager.isOnCooldown(player, cooldown, plugin)) {
            return;
        }
        cooldownManager.putOnCooldown(player, plugin);
        price.deduct(player);
        function.evaluate(it -> spawnParticleAndIgniteEntities(player, it));
    }

    private void spawnParticleAndIgniteEntities(Player player, Vector vector) {
        var location = player.getEyeLocation().add(getRelativeVector(player, vector));
        Entities.spawnParticle(particle, location);
        EntityFinder.createAtLocation(location, 0.5)
                .find(LivingEntity.class, new ExcludingSelector(player))
                .forEach(it -> it.setFireTicks(fireDuration));
    }

    private Vector getRelativeVector(Player player, Vector vector) {
        var z = player.getLocation().getDirection();
        var x = z.getCrossProduct(new Vector(0, 1, 0)).normalize();
        return x.clone().multiply(vector.getX())
                .add(x.getCrossProduct(z).normalize().multiply(vector.getY()))
                .add(z.multiply(vector.getZ()));
    }
}
