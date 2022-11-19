package com.aregcraft.reforging.ability.base;

import com.aregcraft.reforging.Reforging;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class CooldownAbility extends BaseAbility {
    protected final Set<UUID> players = new HashSet<>();
    /**
     * Specifies the cooldown in ticks (1 second = 20 ticks).
     */
    private int cooldown;

    @Override
    public void activate(Player player) {
        var id = player.getUniqueId();
        if (!players.add(id)) {
            return;
        }
        charge(player);
        perform(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                players.remove(id);
            }
        }.runTaskLater(Reforging.PLUGIN, cooldown);
    }

    protected abstract void perform(Player player);
}
