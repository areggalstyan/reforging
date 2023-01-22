package com.aregcraft.reforging.ability;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Allows the player to reflect a portion of the received damage
 */
public class ThornsAbility extends Ability implements Listener {
    /**
     * The duration in ticks (1 second = 20 ticks)
     */
    private long duration;
    /**
     * How much damage to reflect on the attacker
     */
    private double multiplier;

    @Override
    protected void perform(Player player) {
        setPlayerActive(player, duration);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity entity && isPlayerActive(event.getEntity())) {
            entity.damage(multiplier * event.getDamage());
        }
    }
}
