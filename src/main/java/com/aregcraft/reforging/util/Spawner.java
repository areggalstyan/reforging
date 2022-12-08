package com.aregcraft.reforging.util;

import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.item.ItemStackWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
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
import java.util.function.Predicate;

public class Spawner implements Listener {
    public Spawner() {
        Bukkit.getPluginManager().registerEvents(this, Reforging.plugin());
    }

    public static <T extends Entity> T spawnEntity(Class<T> type, Location location) {
        return location.getWorld().spawn(location, type);
    }

    public static Entity spawnEntity(EntityType type, Location location) {
        return location.getWorld().spawnEntity(location, type);
    }

    public static void spawnParticle(Particle particle, Location location) {
        location.getWorld().spawnParticle(particle, location, 0);
    }

    public static ArmorStand spawnArmorStand(Location location) {
        var armorStand = spawnEntity(ArmorStand.class, location);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setArms(true);
        armorStand.setGravity(false);
        armorStand.setCanPickupItems(false);
        armorStand.getPersistentDataContainer().set(Reforging.key("disable_interaction"), PersistentDataType.BYTE,
                (byte) 1);
        return armorStand;
    }

    public static ArmorStand spawnArmorStand(Location location, ItemStackWrapper item) {
        var armorStand = spawnArmorStand(location);
        armorStand.getEquipment().setItemInMainHand(item.unwrap());
        return armorStand;
    }

    public static List<LivingEntity> nearbyEntities(Entity exclude, Location location, double x, double y, double z) {
        return location.getWorld().getNearbyEntities(location, x, y, z).stream()
                .filter(Predicate.not(Predicate.isEqual(exclude)))
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
        var disableInteraction = event.getRightClicked().getPersistentDataContainer()
                .get(Reforging.key("disable_interaction"), PersistentDataType.BYTE);
        event.setCancelled(disableInteraction != null && disableInteraction == (byte) 1);
    }
}
