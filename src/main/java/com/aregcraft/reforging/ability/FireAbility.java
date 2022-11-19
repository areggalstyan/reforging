package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.ability.base.RepeatingAbility;
import com.aregcraft.reforging.ability.external.Function;
import com.aregcraft.reforging.annotation.Ability;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/**
 * Sets entities on fire in the player's facing direction according to the specified function.
 */
@Ability
public class FireAbility extends RepeatingAbility {
    private Function function;
    /**
     * Specifies the type of the particle which is used to visualize the function.
     */
    private Particle particle;
    /**
     * Specifies how long will hit entities be on fire in ticks (1 second = 20 ticks).
     */
    private int fireDuration;

    @Override
    protected boolean perform(Player player, int time) {
        var location = player.getLocation().add(0, 1, 0);
        var matrix = changeOfBasisDirection(player);
        for (var i = function.min; i < function.max; i += function.delta) {
            var target = evaluate(function, i).multiply(matrix);
            spawnParticle(target, particle, location);
            forEachEntity(target.at(location), player, it -> it.setFireTicks(fireDuration));
        }
        return true;
    }
}
