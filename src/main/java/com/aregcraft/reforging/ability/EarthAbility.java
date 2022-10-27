package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.data.Price;
import org.bukkit.entity.Player;

public class EarthAbility implements Ability {
    private Price price;
    private double radius;
    private double frequency;

    @Override
    public void activate(Player player) {
        player.damage(price.health);
        player.setFoodLevel(Math.max(player.getFoodLevel() - price.food, 0));
        for (double i = 0; i < 2 * Math.PI; i += Math.PI / frequency) {
            var location = player.getLocation().add(radius * Math.cos(i), 0, radius * Math.sin(i));
            var block = location.getBlock();
            if (!block.getType().isSolid()) {
                var blockBelow = location.subtract(0, 1, 0).getBlock();
                block.setType(blockBelow.getType());
                blockBelow.breakNaturally();
            }
        }
    }
}
