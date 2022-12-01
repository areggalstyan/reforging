package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.ability.base.CooldownAbility;
import com.aregcraft.reforging.annotation.Ability;
import com.aregcraft.reforging.config.model.Function2Model;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Allows the player to strike lighting around them.
 */
@Ability
public class StormAbility extends CooldownAbility implements Listener {
    private Function2Model function;

    public StormAbility() {
        Bukkit.getPluginManager().registerEvents(this, Reforging.plugin());
    }

    @Override
    protected void perform(Player player) {
        function.evaluate(it -> player.getWorld().strikeLightning(it.at(player.getLocation())));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING
                && hasPlayer(event.getEntity()));
    }
}
