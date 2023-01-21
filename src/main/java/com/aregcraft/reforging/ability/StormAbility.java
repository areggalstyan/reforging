package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.function.Function2;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Allows the player to strike a circle (or any other shape) of lightning bolts around them
 */
public class StormAbility extends Ability implements Listener {
    private static final long INVULNERABILITY_DURATION = 20;

    /**
     * The function describing the shape
     */
    private Function2 function;

    @Override
    public void perform(Player player) {
        setPlayerActive(player, INVULNERABILITY_DURATION);
        function.evaluate(it -> player.getWorld().strikeLightning(player.getLocation().add(it)));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING && isPlayerActive(event.getEntity())) {
            event.setDamage(0);
        }
    }
}
