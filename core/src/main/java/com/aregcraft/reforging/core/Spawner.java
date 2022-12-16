package com.aregcraft.reforging.core;

import com.aregcraft.reforging.core.data.Data;
import com.aregcraft.reforging.core.data.PersistentDataTypeExtension;
import com.aregcraft.reforging.core.item.ItemWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Spawner implements Listener {
    public Spawner() {
        Bukkit.getPluginManager().registerEvents(this, Context.plugin());
    }

    public static <T extends Entity> T spawnEntity(Class<T> type, Location location) {
        return nullSafeWorld(location).spawn(location, type);
    }

    public static Entity spawnEntity(EntityType type, Location location) {
        return nullSafeWorld(location).spawnEntity(location, type);
    }

    public static void spawnParticle(Particle particle, Location location) {
        nullSafeWorld(location).spawnParticle(particle, location, 0);
    }

    public static ArmorStand spawnArmorStand(Location location) {
        var armorStand = spawnEntity(ArmorStand.class, location);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setArms(true);
        armorStand.setGravity(false);
        armorStand.setCanPickupItems(false);
        Data.of(armorStand).set("disable_interaction", PersistentDataType.BYTE, (byte) 1);
        return armorStand;
    }

    public static ArmorStand spawnArmorStand(Location location, ItemWrapper item) {
        var armorStand = spawnArmorStand(location);
        Objects.requireNonNull(armorStand.getEquipment()).setItemInMainHand(item.unwrap());
        return armorStand;
    }

    public static List<LivingEntity> nearbyEntities(Entity exclude, Location location, double x, double y, double z) {
        return nullSafeWorld(location).getNearbyEntities(location, x, y, z).stream()
                .filter(Predicate.not(exclude::equals))
                .filter(LivingEntity.class::isInstance)
                .map(LivingEntity.class::cast).toList();
    }

    public static List<LivingEntity> nearbyEntities(Block block) {
        return block.getWorld().getNearbyEntities(BoundingBox.of(block)).stream()
                .filter(LivingEntity.class::isInstance)
                .map(LivingEntity.class::cast).toList();
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        event.setCancelled(Data.of(event.getRightClicked()).get("id", PersistentDataTypeExtension.BOOLEAN));
    }

    private static World nullSafeWorld(Location location) {
        return Objects.requireNonNull(location.getWorld());
    }
}
