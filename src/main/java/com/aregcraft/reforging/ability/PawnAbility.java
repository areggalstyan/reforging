package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.FormattingContext;
import com.aregcraft.delta.api.PersistentDataWrapper;
import com.aregcraft.delta.api.entity.EntityBuilder;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.util.ArrayList;

/**
 * Allows the player to spawn entities that will attack other players but not them
 */
@ProcessedAbility
public class PawnAbility extends Ability implements Listener {
    /**
     * The amount of health and hunger deducted from the player upon activation
     */
    private Price price;
    /**
     * The cooldown in ticks (1 second = 20 ticks)
     */
    private long cooldown;
    /**
     * How long the entities should exist, unlimited by default
     */
    private long duration;
    /**
     * The entity
     */
    private EntityBuilder entity;
    /**
     * The number of entities
     */
    private int number;
    private transient Reforging plugin;

    @Override
    public void activate(Player player) {
        if (cooldownManager.isOnCooldown(player, cooldown, plugin)) {
            return;
        }
        cooldownManager.putOnCooldown(player, plugin);
        price.deduct(player);
        entity.nameFormattingContext(FormattingContext.builder()
                .placeholder("player", player.getDisplayName())
                .build());
        var entities = new ArrayList<Entity>();
        for (var i = 0; i < number; i++) {
            entities.add(entity.persistentData("master", player).build(player.getLocation(), plugin));
        }
        if (duration > 0) {
            plugin.getSynchronousScheduler().scheduleDelayedTask(() -> entities.forEach(Entity::remove), duration);
        }
    }

    @EventHandler
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        var target = event.getTarget();
        if (target != null) {
            event.setCancelled(PersistentDataWrapper.wrap(plugin, event.getEntity()).check("master", target));
        }
    }
}
