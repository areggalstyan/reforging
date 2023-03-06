package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.EntityBuilder;
import com.aregcraft.delta.api.entity.EntityFinder;
import com.aregcraft.delta.api.entity.EquipmentWrapper;
import com.aregcraft.delta.api.entity.selector.ExcludingSelector;
import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

/**
 * Allows the player to throw their weapon, damaging all hit entities
 */
public class ThrowAbility extends Ability {
    /**
     * How long should the weapon fly in ticks (1 second = 20 ticks)
     */
    private long duration;
    /**
     * The velocity
     */
    private Vector velocity;
    /**
     * How much of the actual weapon damage to deal
     */
    private double damageMultiplier;

    @Override
    public void perform(Player player) {
        var armorStand = createArmorStand(player.getLocation(), player);
        armorStand.setRightArmPose(getArmorStandArmPose(player));
        getPlugin().getSynchronousScheduler()
                .scheduleRepeatingTask((self, i) -> update(armorStand, player, self), 0, 1, duration);
    }

    private Vector getLocationOffset(Location location) {
        return location.getDirection().getCrossProduct(new Vector(0, 1, 0)).normalize().multiply(0.2);
    }

    private ArmorStand createArmorStand(Location location, Player player) {
        return EntityBuilder.createArmorStand()
                .itemInMainHand(EquipmentWrapper.wrap(player).getItemInMainHand())
                .build(location.add(getLocationOffset(location)), getPlugin());
    }

    private void update(ArmorStand armorStand, Player player, BukkitRunnable task) {
        var location = getNewLocation(armorStand, player);
        armorStand.teleport(location);
        damageEntities(location, armorStand, player);
        if (location.getBlock().getType().isSolid()) {
            task.cancel();
        }
        if (task.isCancelled()) {
            armorStand.remove();
        }
    }

    private Location getNewLocation(ArmorStand armorStand, Player player) {
        return armorStand.getLocation().add(player.getLocation().getDirection().multiply(velocity));
    }

    private void damageEntities(Location location, ArmorStand armorStand, Player player) {
        EntityFinder.createAtLocation(location.add(0, 1.5, 0), 0.5)
                .find(LivingEntity.class, new ExcludingSelector(player), new ExcludingSelector(armorStand))
                .forEach(it -> damageEntity(it, EquipmentWrapper.wrap(player).getItemInMainHand(), player));
    }

    private void damageEntity(LivingEntity entity, ItemWrapper item, Player player) {
        entity.damage(damageMultiplier * item.getAttributeValue(Attribute.GENERIC_ATTACK_DAMAGE), player);
    }

    private EulerAngle getArmorStandArmPose(Player player) {
        return new EulerAngle(6.1, Math.toRadians(player.getLocation().getPitch()), 1.5 * Math.PI);
    }
}
