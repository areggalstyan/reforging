package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.PlayerRegistry;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.function.Function2;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Allows the player to strike a circle (or any other shape) of lightning bolts around them
 */
@ProcessedAbility
public class StormAbility extends Ability implements Listener {
    private static final long INVULNERABILITY_DURATION = 20;

    private final PlayerRegistry cooldownPlayers = PlayerRegistry.createAsynchronous();
    private final PlayerRegistry activatorPlayers = PlayerRegistry.createAsynchronous();
    /**
     * The amount of health and hunger deducted from the player upon activation
     */
    private Price price;
    /**
     * The cooldown in ticks (1 second = 20 ticks)
     */
    private long cooldown;
    /**
     * The function describing the shape
     */
    private Function2 function;
    private transient Reforging plugin;

    @Override
    public void activate(Player player) {
        if (cooldownPlayers.contains(player)) {
            return;
        }
        cooldownPlayers.add(player, cooldown, plugin);
        activatorPlayers.add(player, INVULNERABILITY_DURATION, plugin);
        price.deduct(player);
        function.evaluate(it -> player.getWorld().strikeLightning(player.getLocation().add(it)));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING
                && activatorPlayers.contains(event.getEntity()));
    }
}
