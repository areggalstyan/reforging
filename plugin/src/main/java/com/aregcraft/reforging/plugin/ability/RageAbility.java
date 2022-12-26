package com.aregcraft.reforging.plugin.ability;

import com.aregcraft.reforging.plugin.Reforging;
import com.aregcraft.reforging.plugin.ability.base.CooldownAbility;
import com.aregcraft.reforging.plugin.annotation.Ability;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Increases the player damage, but makes enemies reflect damage.
 */
@Ability
public class RageAbility extends CooldownAbility implements Listener {
    /**
     * Specifies the duration of the effect in ticks (1 second = 20 ticks).
     */
    private int duration;
    /**
     * Specifies the amplifier of the effect.
     */
    private int amplifier;
    /**
     * Specifies how much damage should enemies reflect.
     */
    private double multiplier;

    public RageAbility() {
        Bukkit.getPluginManager().registerEvents(this, Reforging.plugin());
    }

    @Override
    protected void perform(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, amplifier,
                false, false));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && hasPlayer(player)) {
            player.damage(multiplier * event.getDamage());
        }
    }
}
