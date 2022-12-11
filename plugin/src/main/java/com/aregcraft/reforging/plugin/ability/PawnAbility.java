package com.aregcraft.reforging.plugin.ability;

import com.aregcraft.reforging.plugin.Reforging;
import com.aregcraft.reforging.plugin.ability.base.CooldownAbility;
import com.aregcraft.reforging.plugin.annotation.Ability;
import com.aregcraft.reforging.plugin.annotation.Placeholder;
import com.aregcraft.reforging.plugin.annotation.Placeholders;
import com.aregcraft.reforging.plugin.format.Format;
import com.aregcraft.reforging.plugin.util.Spawner;
import com.aregcraft.reforging.plugin.util.UUIDPersistentDataType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;
import java.util.UUID;

/**
 * Allows the player to spawn pawns who will attack other players.
 */
@Ability
public class PawnAbility extends CooldownAbility implements Listener {
    private static final NamespacedKey MASTER = Reforging.key("master");
    private static final NamespacedKey INFLAMMABLE = Reforging.key("inflammable");

    /**
     * Specifies the type of the entity.
     */
    private EntityType entity;
    /**
     * Specifies the custom name of the entity.
     */
    @Placeholders(@Placeholder(name = "PLAYER", description = "the name of the player"))
    private String customName;
    /**
     * Specifies whether the entity should be an adult.
     */
    private boolean adult;
    /**
     * Specifies the number of pawns to spawn.
     */
    private int count;
    /**
     * Specifies the duration of the lives of the pawns in ticks (1 second = 20 ticks). If 0, the lives of the pawns
     * will not be limited.
     */
    private int duration;
    /**
     * Specifies the health of the pawns.
     */
    private double maxHealth;
    /**
     * Specifies the follow range of the pawns.
     */
    private double followRange;
    /**
     * Specifies the knockback resistance of the pawns.
     */
    private double knockbackResistance;
    /**
     * Specifies the movement speed of the pawns.
     */
    private double movementSpeed;
    /**
     * Specifies the attack damage of the pawns.
     */
    private double attackDamage;
    /**
     * Specifies the armor of the pawns.
     */
    private double armor;
    /**
     * Specifies the armor toughness of the pawns.
     */
    private double armorToughness;
    /**
     * Specifies the attack knockback of the pawns.
     */
    private double attackKnockback;

    public PawnAbility() {
        Bukkit.getPluginManager().registerEvents(this, Reforging.plugin());
    }

    private static UUID getEntityPlayerData(PersistentDataHolder entity) {
        return entity.getPersistentDataContainer().get(MASTER, UUIDPersistentDataType.UUID);
    }

    private static void setEntityPlayerData(PersistentDataHolder entity, Player player) {
        entity.getPersistentDataContainer().set(MASTER, UUIDPersistentDataType.UUID, player.getUniqueId());
    }

    private static void setAttributeValueIfPresent(Attributable entity, Attribute attribute, double value) {
        if (value > 0) {
            Optional.ofNullable(entity.getAttribute(attribute)).ifPresent(it -> it.setBaseValue(value));
        }
    }

    private static boolean isEntityInflammable(PersistentDataHolder entity) {
        var inflammable = entity.getPersistentDataContainer().get(INFLAMMABLE, PersistentDataType.BYTE);
        return inflammable != null && inflammable != 0;
    }

    private static void makeEntityInflammable(PersistentDataHolder entity) {
        entity.getPersistentDataContainer().set(INFLAMMABLE, PersistentDataType.BYTE, (byte) 1);
    }

    private static Format format(Player player) {
        return Format.builder()
                .entry("PLAYER", player.getDisplayName())
                .build();
    }

    @Override
    protected void perform(Player player) {
        for (int i = 0; i < count; i++) {
            var pawn = (LivingEntity) Spawner.spawnEntity(entity, player.getLocation());
            setAttributeValueIfPresent(pawn, Attribute.GENERIC_MAX_HEALTH, maxHealth);
            pawn.setHealth(maxHealth);
            setAttributeValueIfPresent(pawn, Attribute.GENERIC_FOLLOW_RANGE, followRange);
            setAttributeValueIfPresent(pawn, Attribute.GENERIC_KNOCKBACK_RESISTANCE, knockbackResistance);
            setAttributeValueIfPresent(pawn, Attribute.GENERIC_MOVEMENT_SPEED, movementSpeed);
            setAttributeValueIfPresent(pawn, Attribute.GENERIC_ATTACK_DAMAGE, attackDamage);
            setAttributeValueIfPresent(pawn, Attribute.GENERIC_ARMOR, armor);
            setAttributeValueIfPresent(pawn, Attribute.GENERIC_ARMOR_TOUGHNESS, armorToughness);
            setAttributeValueIfPresent(pawn, Attribute.GENERIC_ATTACK_KNOCKBACK, attackKnockback);
            setEntityPlayerData(pawn, player);
            makeEntityInflammable(pawn);
            if (pawn instanceof Ageable ageable) {
                if (adult) {
                    ageable.setAdult();
                } else {
                    ageable.setBaby();
                }
            }
            if (!customName.isBlank()) {
                pawn.setCustomName(format(player).format(customName));
                pawn.setCustomNameVisible(true);
            }
            if (duration > 0) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Reforging.plugin(), pawn::remove, duration);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityTargetLivingEntityEvent event) {
        var target = event.getTarget();
        event.setCancelled(target != null && target.getUniqueId().equals(getEntityPlayerData(event.getEntity())));
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        event.setCancelled(isEntityInflammable(event.getEntity()));
    }
}
