package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.Reforging;
import com.google.common.math.DoubleMath;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * This is the third base ability. It allows the player to throw sword which will disappear after hitting a solid block
 * or travelling the maximum specified distance and damage all the entities it hits on the way.
 */
public class ThrowAbility extends Ability {
    public static final double EPSILON = 0.000001;

    private final Set<UUID> players = new HashSet<>();
    /**
     * Specifies the maximum distance that the weapon travels before disappearing.
     */
    private double maxDistance;
    /**
     * Specifies how the amplifier applied to the weapons usual damage when thrown.
     */
    private double damageAmplifier;
    /**
     * Specifies the speed of the weapon when thrown in blocks.
     */
    private double speed;
    /**
     * Specifies the maximum distance from the weapon which will damage an entity.
     */
    private double range;

    @Override
    public void activate(Player player) {
        var id = player.getUniqueId();
        if (players.contains(id)) {
            return;
        }
        players.add(id);
        var location = player.getLocation();
        var direction = location.getDirection();
        location.add(direction.getCrossProduct(new Vector(0, 1, 0)).normalize().multiply(0.25));
        var armorStand = (ArmorStand) player.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setCanPickupItems(false);
        armorStand.setArms(true);
        armorStand.setRightArmPose(new EulerAngle(0, Math.toRadians(location.getPitch()), 1.5 * Math.PI));
        var item = player.getInventory().getItemInMainHand();
        var itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            players.remove(id);
            armorStand.remove();
            return;
        }
        var modifiers = itemMeta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE);
        if (modifiers == null) {
            players.remove(id);
            armorStand.remove();
            return;
        }
        var damage = damageAmplifier * modifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
        Objects.requireNonNull(armorStand.getEquipment()).setItemInMainHand(item);
        var world = location.getWorld();
        if (world == null) {
            players.remove(id);
            armorStand.remove();
            return;
        }
        new BukkitRunnable() {
            private int time = 0;

            @Override
            public void run() {
                var location = armorStand.getLocation();
                if (location.add(0, 1, 0).getBlock().getType().isSolid()
                        || DoubleMath.fuzzyEquals(time++ * speed, maxDistance, EPSILON) || player.isDead()) {
                    players.remove(id);
                    armorStand.remove();
                    cancel();
                }
                armorStand.teleport(location.subtract(0, 1, 0).add(direction.clone().multiply(speed)));
                world.getNearbyEntities(armorStand.getLocation().add(0, 1, 0), range / 2, range / 2,
                        range / 2).stream()
                        .filter(it -> it instanceof LivingEntity)
                        .map(it -> (LivingEntity) it)
                        .filter(it -> !it.equals(player)).forEach(it -> it.damage(damage));
            }
        }.runTaskTimer(Reforging.PLUGIN, 0, 1);
    }
}
