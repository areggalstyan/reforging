package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.Entities;
import com.aregcraft.delta.api.entity.EntityFinder;
import com.aregcraft.delta.api.entity.selector.ExcludingSelector;
import com.aregcraft.reforging.function.Function3;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Allows the player to create a spiral in the looking direction (or any other shape) of fire, igniting all touching
 * entities
 */
public class FireAbility extends Ability {
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

    @Override
    public void perform(Player player) {
        function.evaluate(it -> {
            var location = player.getEyeLocation().add(changeBasis(it, player));
            Entities.spawnParticle(particle, location);
            igniteEntities(location, player);
        });
    }

    private void igniteEntities(Location location, Player player) {
        EntityFinder.createAtLocation(location, 0.5)
                .find(LivingEntity.class, new ExcludingSelector(player))
                .forEach(it -> it.setFireTicks(fireDuration));
    }

    private Vector changeBasis(Vector vector, Player player) {
        var z = player.getLocation().getDirection();
        var x = z.getCrossProduct(new Vector(0, 1, 0)).normalize();
        return x.clone().multiply(vector.getX())
                .add(x.getCrossProduct(z).normalize().multiply(vector.getY()))
                .add(z.multiply(vector.getZ()));
    }
}
