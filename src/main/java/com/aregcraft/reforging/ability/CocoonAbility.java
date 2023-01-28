package com.aregcraft.reforging.ability;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Allows the player to put themselves in a block cocoon
 */
public class CocoonAbility extends Ability {
    private static final List<Vector> POSITIONS = List.of(
            new Vector(0, -1, 0),
            new Vector(-1, 0, 0),
            new Vector(1, 0, 0),
            new Vector(0, 0, -1),
            new Vector(0, 0, 1),
            new Vector(0, 2, 0),
            new Vector(-1, 1, 0),
            new Vector(1, 1, 0),
            new Vector(0, 1, -1),
            new Vector(0, 1, 1)
    );
    /**
     * The block
     */
    private Material block;

    @Override
    protected void perform(Player player) {
        POSITIONS.forEach(it -> setBlockIfAir(player, it));
    }

    private void setBlockIfAir(Player player, Vector position) {
        var block = player.getLocation().add(position).getBlock();
        if (!block.getType().isSolid()) {
            block.setType(this.block);
        }
    }
}
