package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.FormattingContext;
import com.aregcraft.delta.api.PersistentDataWrapper;
import com.aregcraft.delta.api.entity.EntityBuilder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Allows the player to spawn entities that will attack other players but not them
 */
public class PawnAbility extends Ability implements Listener {
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

    @Override
    public void perform(Player player) {
        var entities = IntStream.range(0, number).mapToObj(it -> spawnEntity(player)).toList();
        if (duration > 0) {
            scheduleEntityRemoval(entities);
        }
    }

    private Entity spawnEntity(Player player) {
        return entity
                .nameFormattingContext(getFormattingContext(player))
                .persistentData("master", player)
                .build(player.getLocation(), getPlugin());
    }

    private FormattingContext getFormattingContext(Player player) {
        return FormattingContext.builder()
                .plugin(getPlugin())
                .placeholder("player", player.getDisplayName())
                .build();
    }

    private void scheduleEntityRemoval(List<Entity> entities) {
        getPlugin().getSynchronousScheduler().scheduleDelayedTask(() -> entities.forEach(Entity::remove), duration);
    }

    @EventHandler
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        var target = event.getTarget();
        if (target != null && isMasterOf(event.getTarget(), event.getEntity())) {
            event.setCancelled(true);
        }
    }

    private boolean isMasterOf(Entity master, Entity entity) {
        return PersistentDataWrapper.wrap(getPlugin(), entity).check("master", master);
    }
}
