package com.aregcraft.reforging.ability.base;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class PlayerAwareAbility extends PlayerUnawareAbility {
    private transient final Set<UUID> players = new HashSet<>();

    @Override
    public boolean activate(Player player) {
        if (players.add(player.getUniqueId())) {
            return super.activate(player);
        }
        return false;
    }

    protected void removePlayer(Entity entity) {
        players.remove(entity.getUniqueId());
    }

    protected boolean hasPlayer(Entity entity) {
        return players.contains(entity.getUniqueId());
    }
}
