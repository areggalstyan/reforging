package com.aregcraft.reforging.plugin.ability;

import com.aregcraft.reforging.plugin.ability.base.CooldownAbility;
import com.aregcraft.reforging.plugin.annotation.Ability;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Allows the player to become invisible.
 */
@Ability
public class HideAbility extends CooldownAbility {
    /**
     * Specifies how long to hide the player in ticks (1 second = 20 ticks).
     */
    private int duration;

    @Override
    protected void perform(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 255,
                false, false));
    }
}
