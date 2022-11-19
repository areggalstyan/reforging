package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.ability.base.PlayerAwareAbility;
import com.aregcraft.reforging.ability.external.Function;
import com.aregcraft.reforging.annotation.Ability;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Strikes lighting around the player according to the specified function.
 */
@Ability
public class StormAbility extends PlayerAwareAbility implements Listener {
    private Function function;

    public StormAbility() {
        Bukkit.getPluginManager().registerEvents(this, Reforging.PLUGIN);
    }

    @Override
    protected void perform(Player player) {
        for (var i = function.min; i < function.max; i += function.delta) {
            player.getWorld().strikeLightning(evaluate(function, i).at(player.getLocation()));
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Reforging.PLUGIN, () -> remove(player), 22);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING
                && event.getEntity() instanceof Player player
                && players.contains(player.getUniqueId()));
    }
}
