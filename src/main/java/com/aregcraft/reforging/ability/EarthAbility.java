package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.ability.base.SimpleAbility;
import com.aregcraft.reforging.ability.external.Function;
import com.aregcraft.reforging.annotation.Ability;
import org.bukkit.entity.Player;

/**
 * Forms one block tall protective barrier around the player according to the specified function.
 */
@Ability
public class EarthAbility extends SimpleAbility {
    private Function function;

    @Override
    protected void perform(Player player) {
        for (var i = function.min; i < function.max; i += function.delta) {
            var location = evaluate(function, i).at(player.getLocation());
            if (isUnfilled(location)) {
                var source = location.subtract(0, 1, 0).getBlock();
                location.add(0, 1, 0).getBlock().setType(source.getType());
                source.breakNaturally();
            }
        }
    }
}
