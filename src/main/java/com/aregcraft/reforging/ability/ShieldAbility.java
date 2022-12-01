package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.ability.base.RepeatingAbility;
import com.aregcraft.reforging.annotation.Ability;
import com.aregcraft.reforging.config.model.Function3Model;
import com.aregcraft.reforging.util.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Makes the player immune to attacks from aggressive mobs.
 */
@Ability
public class ShieldAbility extends RepeatingAbility implements Listener {
    private Function3Model function;
    /**
     * Specifies the particle used to create visual effects.
     */
    private Particle particle;
    /**
     * Specifies whether the player should be prevented from attacking other entities.
     */
    private boolean disableAttack;

    public ShieldAbility() {
        Bukkit.getPluginManager().registerEvents(this, Reforging.plugin());
    }

    @Override
    protected boolean update(Player player, int time) {
        function.evaluate(it -> Spawner.spawnParticle(particle, it.at(player.getLocation())));
        return true;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (hasPlayer(event.getEntity()) || disableAttack && hasPlayer(event.getDamager())) {
            event.setDamage(0);
        }
    }
}
