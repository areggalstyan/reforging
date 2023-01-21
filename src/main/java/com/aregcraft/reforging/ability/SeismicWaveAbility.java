package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.Entities;
import com.aregcraft.delta.api.entity.EntityFinder;
import com.aregcraft.delta.api.entity.selector.ExcludingSelector;
import com.aregcraft.reforging.function.Function2;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Allows the player to create a spiral (or any other shape) around them, damaging and knocking back all entities
 * within the range
 */
public class SeismicWaveAbility extends Ability {
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

    @Override
    public void perform(Player player) {
        damageAndKnockbackEntities(player);
        function.evaluate(it -> Entities.spawnParticle(particle, player.getLocation().add(it)));
    }

    private void damageAndKnockbackEntities(Player player) {
        EntityFinder.createAtLocation(player.getLocation(), range)
                .find(LivingEntity.class, new ExcludingSelector(player))
                .forEach(it -> damageAndKnockbackEntity(it, player));
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
