package com.aregcraft.reforging.ability.base;

import org.bukkit.entity.Player;

public abstract class SimpleAbility extends BaseAbility {
    @Override
    public void activate(Player player) {
        charge(player);
        perform(player);
    }

    protected abstract void perform(Player player);
}
