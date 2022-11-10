package com.aregcraft.reforging.ability;

import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Damages and pushes back all entities in the specified range.
 */
public class SeismicWaveAbility extends RepeatingAbility {
    private Function function;
    /**
     * Specifies the type of the particle which is used to visualize the function.
     */
    private Particle particle;
    /**
     * Specifies the range of the seismic wave.
     */
    private double range;
    /**
     * Specifies how much to push the surrounding entities back.
     */
    private double factor;
    /**
     * Specifies how high to push the surrounding entities.
     */
    private double height;
    /**
     * Specifies the damage to deal to the surrounding entities.
     */
    private double damage;

    @Override
    protected void setup(Player player) {
        player.getNearbyEntities(range / 2, 1, range / 2).stream()
                .filter(it -> it != player).filter(it -> it instanceof LivingEntity)
                .map(it -> (LivingEntity) it).forEach(it -> {
                    it.setVelocity(it.getLocation().subtract(player.getLocation()).toVector().normalize()
                            .multiply(factor).add(new Vector(0, height, 0)));
                    it.damage(damage);
                });
    }

    @Override
    public boolean perform(Player player, int time) {
        for (var i = function.min; i < function.max; i += function.delta) {
            spawnParticle(evaluate(function, i).multiply(time), particle, player.getLocation());
        }
        return true;
    }
}
