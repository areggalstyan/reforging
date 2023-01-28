package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.Identifiable;
import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.PersistentDataWrapper;
import com.aregcraft.delta.meta.AbilitySuperclass;
import com.aregcraft.reforging.Reforging;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@AbilitySuperclass
public abstract class Ability implements Identifiable<String> {
    private final CooldownManager cooldownManager = new CooldownManager();
    private String id;
    private String name;
    private Price price;
    private long cooldown;
    @InjectPlugin
    private transient Reforging plugin;

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name == null ? id : name;
    }

    public void activate(Player player) {
        if (cooldownManager.isOnCooldown(player, cooldown, plugin)) {
            return;
        }
        cooldownManager.putOnCooldown(player, plugin);
        price.deduct(player);
        perform(player);
    }

    protected Reforging getPlugin() {
        return plugin;
    }

    protected boolean isPlayerActive(Entity player) {
        return PersistentDataWrapper.wrap(plugin, player).check(id, true);
    }

    protected void setPlayerActive(Entity player, long duration) {
        setPlayerActive(player, duration, () -> {});
    }

    protected void setPlayerActive(Entity player, long duration, Runnable task) {
        PersistentDataWrapper.wrap(plugin, player).set(id, true);
        plugin.getSynchronousScheduler().scheduleDelayedTask(() -> {
            task.run();
            unsetPlayerActive(player);
        }, duration);
    }

    protected void unsetPlayerActive(Entity player) {
        PersistentDataWrapper.wrap(plugin, player).remove(id);
    }

    protected abstract void perform(Player player);
}
