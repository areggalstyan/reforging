package com.aregcraft.reforging.ability.base;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class PlayerAwareAbility extends BaseAbility {
    protected final Set<UUID> players = new HashSet<>();

    @Override
    public void activate(Player player) {
        if (!players.add(player.getUniqueId())) {
            return;
        }
        charge(player);
        perform(player);
    }

    protected void remove(Player player) {
        players.remove(player.getUniqueId());
    }

    protected abstract void perform(Player player);
}
