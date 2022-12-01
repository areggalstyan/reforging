package com.aregcraft.reforging.ability.base;

import com.aregcraft.reforging.Reforging;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class CooldownAbility extends PlayerAwareAbility {
    /**
     * Specifies the cooldown of the ability.
     */
    private int cooldown;

    @Override
    public boolean activate(Player player) {
        if (!super.activate(player)) {
            return false;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                removePlayer(player);
            }
        }.runTaskLater(Reforging.plugin(), cooldown);
        return true;
    }
}
