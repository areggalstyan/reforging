package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.Entities;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Player;

/**
 * Allows the player to summon evoker fangs
 */
@ProcessedAbility
public class EvokerAbility extends Ability {
    /**
     * The amount of health and hunger deducted from the player upon activation
     */
    private Price price;
    /**
     * The cooldown in ticks (1 second = 20 ticks)
     */
    private long cooldown;
    /**
     * The number of fangs
     */
    private int number;
    private transient Reforging plugin;

    @Override
    public void activate(Player player) {
        if (cooldownManager.isOnCooldown(player, cooldown, plugin)) {
            return;
        }
        cooldownManager.putOnCooldown(player, plugin);
        price.deduct(player);
        var location = player.getLocation();
        var direction = location.getDirection().setY(0);
        for (var i = 1; i <= number; i++) {
            Entities.<EvokerFangs>spawnEntity(EntityType.EVOKER_FANGS,
                    location.clone().add(direction.clone().multiply(i))).setOwner(player);
        }
    }
}
