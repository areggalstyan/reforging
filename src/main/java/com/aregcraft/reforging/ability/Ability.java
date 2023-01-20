package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.Identifiable;
import org.bukkit.entity.Player;

public abstract class Ability implements Identifiable {
    protected final CooldownManager cooldownManager = new CooldownManager();
    private String id;

    @Override
    public String getId() {
        return id;
    }

    public abstract void activate(Player player);
}
