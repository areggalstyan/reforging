package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.EntityFinder;
import com.aregcraft.delta.api.entity.selector.ExcludingSelector;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * Allows the player to reveal all invisible entities within range
 */
@ProcessedAbility
public class RevealAbility extends Ability {
    /**
     * The amount of health and hunger deducted from the player upon activation
     */
    private Price price;
    /**
     * The cooldown in ticks (1 second = 20 ticks)
     */
    private long cooldown;
    /**
     * The range
     */
    private double range;
    private transient Reforging plugin;

    @Override
    public void activate(Player player) {
        if (cooldownManager.isOnCooldown(player, cooldown, plugin)) {
            return;
        }
        cooldownManager.putOnCooldown(player, plugin);
        price.deduct(player);
        EntityFinder.createAtLocation(player.getLocation(), range / 2)
                .find(LivingEntity.class, new ExcludingSelector(player))
                .forEach(it -> it.removePotionEffect(PotionEffectType.INVISIBILITY));
    }
}
