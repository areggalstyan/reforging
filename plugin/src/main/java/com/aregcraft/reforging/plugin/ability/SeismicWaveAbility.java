package com.aregcraft.reforging.plugin.ability;

import com.aregcraft.reforging.plugin.ability.base.RepeatingAbility;
import com.aregcraft.reforging.plugin.annotation.Ability;
import com.aregcraft.reforging.plugin.config.model.Function2Model;
import com.aregcraft.reforging.plugin.util.Spawner;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Allows the player to create a seismic wave knocking back and damaging all entities within range.
 */
@Ability
public class SeismicWaveAbility extends RepeatingAbility {
    private Function2Model function;
    /**
     * Specifies the particle used to create visual effects.
     */
    private Particle particle;
    /**
     * Specifies the range of the ability.
     */
    private double range;
    /**
     * Specifies the speed at which to knock back the entities.
     */
    private double speed;
    /**
     * Specifies the height to which to knock back the entities.
     */
    private double height;
    /**
     * Specifies the damage that is dealt to the entities.
     */
    private double damage;

    @Override
    protected void setup(Player player) {
        Spawner.nearbyEntities(player, player.getLocation(), range, 1, range).forEach(it -> {
            it.setVelocity(it.getLocation().subtract(player.getLocation()).toVector().normalize().multiply(speed)
                    .add(new Vector(0, height, 0)));
            it.damage(damage);
        });
    }

    @Override
    protected boolean update(Player player, int time) {
        function.evaluate(it -> Spawner.spawnParticle(particle, it.multiply(time).at(player.getLocation())));
        return true;
    }
}
