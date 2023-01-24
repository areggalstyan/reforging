package com.aregcraft.reforging.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.util.Vector;

/**
 * Allows the player to fly as if they are wearing elytra
 */
public class FlyAbility extends Ability implements Listener {
    /**
     * The duration in ticks (1 second = 20 ticks)
     */
    private int duration;
    /**
     * The velocity
     */
    private Vector velocity;

    @Override
    protected void perform(Player player) {
        setPlayerActive(player, duration);
        player.setGliding(true);
        player.setVelocity(player.getLocation().getDirection().multiply(velocity));
    }

    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        event.setCancelled(!event.isGliding() && isPlayerActive(event.getEntity()));
    }
}
