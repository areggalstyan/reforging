package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.entity.EntityBuilder;
import com.aregcraft.delta.api.entity.EntityFinder;
import com.aregcraft.delta.api.entity.EquipmentWrapper;
import com.aregcraft.delta.api.entity.selector.ExcludingSelector;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.meta.ProcessedAbility;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

/**
 * Allows the player to throw their weapon, damaging all hiy entities
 */
@ProcessedAbility
public class ThrowAbility extends Ability {
    /**
     * The amount of health and hunger deducted from the player upon activation
     */
    private Price price;
    /**
     * The cooldown in ticks (1 second = 20 ticks)
     */
    private long cooldown;
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
    private transient Reforging plugin;

    @Override
    public void activate(Player player) {
        if (cooldownManager.isOnCooldown(player, cooldown, plugin)) {
            return;
        }
        cooldownManager.putOnCooldown(player, plugin);
        price.deduct(player);
        var location = player.getLocation();
        var direction = location.getDirection();
        location.add(direction.getCrossProduct(new Vector(0, 1, 0)).normalize().multiply(0.25));
        var armorStand = EntityBuilder.createArmorStand()
                .itemInMainHand(EquipmentWrapper.wrap(player).getItemInMainHand())
                .<ArmorStand>build(location, plugin);
        armorStand.setRightArmPose(getArmorStandArmPose(location));
        plugin.getSynchronousScheduler().scheduleRepeatingTask((self, i) ->
                update(armorStand, player, direction, self), 0, 1, duration);
    }

    private void update(ArmorStand armorStand, Player player, Vector direction, BukkitRunnable task) {
        var location = armorStand.getLocation().add(direction.clone().multiply(velocity));
        armorStand.teleport(location);
        EntityFinder.createAtLocation(location.add(0, 1.5, 0), 0.5)
                .find(LivingEntity.class, new ExcludingSelector(player), new ExcludingSelector(armorStand))
                .forEach(it -> damageEntity(it, EquipmentWrapper.wrap(player).getItemInMainHand(), player));
        if (location.getBlock().getType().isSolid()) {
            task.cancel();
        }
        if (task.isCancelled()) {
            armorStand.remove();
        }
    }

    private void damageEntity(LivingEntity entity, ItemWrapper item, Player player) {
        entity.damage(damageMultiplier * item.getAttributeValue(Attribute.GENERIC_ATTACK_DAMAGE), player);
    }

    private EulerAngle getArmorStandArmPose(Location location) {
        return new EulerAngle(6.1, Math.toRadians(location.getPitch()), 1.5 * Math.PI);
    }
}
