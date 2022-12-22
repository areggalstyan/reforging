package com.aregcraft.reforging.plugin.ability;

import com.aregcraft.reforging.core.data.Data;
import com.aregcraft.reforging.core.data.PersistentDataTypeExtension;
import com.aregcraft.reforging.plugin.Reforging;
import com.aregcraft.reforging.plugin.ability.base.ProjectileAbility;
import com.aregcraft.reforging.plugin.annotation.Ability;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Allows the player to throw a snowball, freezing the hit entity.
 */
@Ability
public class FreezeAbility extends ProjectileAbility<Snowball> implements Listener {
    /**
     * Specifies how long to freeze the hit entity in ticks (1 second = 20 ticks).
     */
    private int duration;

    public FreezeAbility() {
        super(Snowball.class);
        Bukkit.getPluginManager().registerEvents(this, Reforging.plugin());
    }

    private static boolean isEntityFreezing(PersistentDataHolder entity) {
        return Data.of(entity).getOrElse("freezing", PersistentDataTypeExtension.BOOLEAN, false);
    }

    @Override
    protected void configure(Snowball projectile) {
        Data.of(projectile).set("freezing", PersistentDataTypeExtension.BOOLEAN, true);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (isEntityFreezing(event.getEntity()) && event.getHitEntity() instanceof LivingEntity entity) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 255,
                    false, false));
        }
    }
}
