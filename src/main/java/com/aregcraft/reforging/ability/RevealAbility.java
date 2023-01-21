package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.EntityFinder;
import com.aregcraft.delta.api.entity.selector.ExcludingSelector;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * Allows the player to reveal all invisible entities within range
 */
public class RevealAbility extends Ability {
    /**
     * The range
     */
    private double range;

    @Override
    public void perform(Player player) {
        EntityFinder.createAtLocation(player.getLocation(), range / 2)
                .find(LivingEntity.class, new ExcludingSelector(player))
                .forEach(it -> it.removePotionEffect(PotionEffectType.INVISIBILITY));
    }
}
