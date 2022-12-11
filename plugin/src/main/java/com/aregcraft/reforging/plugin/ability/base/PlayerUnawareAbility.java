package com.aregcraft.reforging.plugin.ability.base;

import org.bukkit.entity.Player;

public abstract class PlayerUnawareAbility extends BaseAbility {
    @Override
    public boolean activate(Player player) {
        player.damage(price.health());
        player.setFoodLevel(Math.max(player.getFoodLevel() - price.food(), 0));
        return super.activate(player);
    }
}
