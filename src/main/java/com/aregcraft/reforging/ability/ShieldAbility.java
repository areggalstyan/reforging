package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.Reforging;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Gives player damage resistance for the specified period of time.
 */
public class ShieldAbility extends RepeatingAbility implements Listener {
    private Function function;
    /**
     * Specifies the type of the particle which is used to visualize the function.
     */
    private Particle particle;
    /**
     * Specifies whether the player should be prevented from attacking other entities while the shield is active.
     */
    private boolean disableAttack;

    public ShieldAbility() {
        Bukkit.getPluginManager().registerEvents(this, Reforging.PLUGIN);
    }

    @Override
    protected boolean perform(Player player) {
        for (var i = function.min; i < function.max; i += function.delta) {
            spawnParticle(evaluate(function, i), particle, player.getLocation());
        }
        return true;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (players.contains(event.getEntity().getUniqueId()) || disableAttack
                && players.contains(event.getDamager().getUniqueId())) {
            event.setDamage(0);
        }
    }
}
