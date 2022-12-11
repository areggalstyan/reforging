package com.aregcraft.reforging.plugin.ability.base;

import com.aregcraft.reforging.plugin.Reforging;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class RepeatingAbility extends PlayerAwareAbility {
    /**
     * Specifies the duration of the ability in ticks (1 second = 20 ticks).
     */
    private int duration;

    @Override
    public boolean activate(Player player) {
        if (!super.activate(player)) {
            return false;
        }
        new BukkitRunnable() {
            private int time = 0;

            @Override
            public void run() {
                if (time++ < duration && update(player, time)) {
                    return;
                }
                removePlayer(player);
                shutdown(player);
                cancel();
            }
        }.runTaskTimer(Reforging.plugin(), 0, 1);
        return true;
    }

    @Override
    protected void perform(Player player) {
        setup(player);
    }

    protected void setup(Player player) {
    }

    protected void shutdown(Player player) {
    }

    protected abstract boolean update(Player player, int time);
}
