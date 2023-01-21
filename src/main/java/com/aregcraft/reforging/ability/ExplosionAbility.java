package com.aregcraft.reforging.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Allows the player to create an explosion around them
 */
public class ExplosionAbility extends Ability implements Listener {
    private static final long INVULNERABILITY_DURATION = 20;

    /**
     * The explosion power
     */
    private float power;

    @Override
    protected void perform(Player player) {
        setPlayerActive(player, INVULNERABILITY_DURATION);
        var block = player.getLocation().subtract(0, 1, 0).getBlock();
        var type = block.getType();
        player.getWorld().createExplosion(player.getLocation(), power);
        block.setType(type);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
                && isPlayerActive(event.getEntity()));
    }
}
