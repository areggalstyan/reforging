package com.aregcraft.reforging.ability;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Allows player to throw a potion with the specified effect, duration and amplifier.
 */
public class PotionAbility extends CooldownAbility {
    /**
     * Specifies the potion effect.
     */
    private PotionEffectType effect;
    /**
     * Specifies the duration of the effect.
     */
    private int duration;
    /**
     * Specifies the amplifier of the effect.
     */
    private int amplifier;

    @Override
    protected void perform(Player player) {
        var item = new ItemStack(Material.SPLASH_POTION);
        var itemMeta = (PotionMeta) item.getItemMeta();
        itemMeta.addCustomEffect(new PotionEffect(effect, duration, amplifier), true);
        item.setItemMeta(itemMeta);
        player.launchProjectile(ThrownPotion.class).setItem(item);
    }
}
