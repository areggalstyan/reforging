package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.PersistentDataWrapper;
import com.aregcraft.delta.api.entity.EquipmentWrapper;
import com.aregcraft.reforging.Reforging;
import org.bukkit.entity.Player;

public class CooldownManager {
    private static final long TICKS_TO_MILLIS = 50;

    public boolean isOnCooldown(Player player, long cooldown, Reforging plugin) {
        return System.currentTimeMillis() - getMainHandItemTimestamp(player, plugin) < cooldown * TICKS_TO_MILLIS;
    }

    private long getMainHandItemTimestamp(Player player, Reforging plugin) {
        return getMainHandItemPersistentData(player, plugin).getOrElse("cooldown", 0L);
    }

    public void putOnCooldown(Player player, Reforging plugin) {
        getMainHandItemPersistentData(player, plugin).set("cooldown", System.currentTimeMillis());
    }

    private PersistentDataWrapper getMainHandItemPersistentData(Player player, Reforging plugin) {
        return EquipmentWrapper.wrap(player).getItemInMainHand().getPersistentData(plugin);
    }
}
