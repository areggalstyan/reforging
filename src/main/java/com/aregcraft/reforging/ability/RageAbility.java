package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.Entities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

/**
 * Allows the player to deal more damage in exchange for receiving their portion
 */
public class RageAbility extends Ability implements Listener {
    /**
     * The duration in ticks (1 second = 20 ticks)
     */
    private int duration;
    /**
     * The effect amplifier
     */
    private int amplifier;
    /**
     * How much damage to reflect on the player
     */
    private double multiplier;

    @Override
    public void perform(Player player) {
        setPlayerActive(player, duration);
        Entities.addPotionEffect(player, PotionEffectType.INCREASE_DAMAGE, duration, amplifier, true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && isPlayerActive(player)) {
            player.damage(multiplier * event.getDamage());
        }
    }
}
