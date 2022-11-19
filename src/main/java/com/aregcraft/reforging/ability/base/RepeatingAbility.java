package com.aregcraft.reforging.ability.base;

import com.aregcraft.reforging.Reforging;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class RepeatingAbility extends BaseAbility {
    protected final Set<UUID> players = new HashSet<>();
    /**
     * Specifies the duration in ticks (1 second = 20 ticks).
     */
    private int duration;

    @Override
    public void activate(Player player) {
        var id = player.getUniqueId();
        if (!players.add(id)) {
            return;
        }
        charge(player);
        setup(player);
        new BukkitRunnable() {
            private int time = 0;

            @Override
            public void run() {
                if (time++ == duration || player.isDead() || !perform(player, time)) {
                    players.remove(id);
                    shutdown(player);
                    cancel();
                }
            }
        }.runTaskTimer(Reforging.PLUGIN, 0, 1);
    }

    protected void setup(Player player) {
    }

    protected void shutdown(Player player) {
    }

    protected abstract boolean perform(Player player, int time);
}
