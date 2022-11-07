package com.aregcraft.reforging.ability;

import org.bukkit.entity.Player;

public abstract class SimpleAbility extends Ability {
    @Override
    public void activate(Player player) {
        charge(player);
        perform(player);
    }

    protected abstract void perform(Player player);
}
