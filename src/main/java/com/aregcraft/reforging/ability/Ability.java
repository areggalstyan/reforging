package com.aregcraft.reforging.ability;

import org.bukkit.entity.Player;

public interface Ability {
    Ability DUMMY = __ -> {};
    void activate(Player player);
}
