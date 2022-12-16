package com.aregcraft.reforging.plugin.ability;

import com.aregcraft.reforging.plugin.ability.base.CooldownAbility;
import com.aregcraft.reforging.plugin.annotation.Ability;
import com.aregcraft.reforging.plugin.config.model.Function3Model;
import com.aregcraft.reforging.core.math.Matrix;
import com.aregcraft.reforging.core.math.Vector;
import com.aregcraft.reforging.core.Spawner;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/**
 * Allows the player to create a shape (e.g., spiral) and ignite all entities that collide with it.
 */
@Ability
public class FireAbility extends CooldownAbility {
    private Function3Model function;
    /**
     * Specifies the particle used to create visual effects.
     */
    private Particle particle;
    /**
     * Specifies the duration of burning in ticks (1 second = 20 ticks).
     */
    private int duration;

    @Override
    protected void perform(Player player) {
        var z = new Vector(player.getLocation().getDirection());
        var x = z.cross(new Vector(0, 1, 0));
        var y = x.cross(z);
        var matrix = Matrix.changeOfBasis(x, y, z);
        var location = player.getLocation().add(0, 1, 0);
        function.evaluate(vector -> {
            var target = vector.multiply(matrix).at(location);
            Spawner.spawnParticle(particle, target);
            Spawner.nearbyEntities(player, target, 0.5, 0.5, 0.5).forEach(it -> it.setFireTicks(duration));
        });
    }
}
