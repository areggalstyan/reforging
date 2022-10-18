package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.Reforge;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.data.Price;
import com.google.common.math.DoubleMath;
import org.bukkit.Material;
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

public class ThrowAbility implements Ability {
    public static final double EPSILON = 0.000001;

    private final Set<UUID> players = new HashSet<>();
    private Price price;
    private double maxDistance;
    private double damageAmplifier;
    private double speed;
    private double range;

    @Override
    public void activate(Player player) {
        var id = player.getUniqueId();
        if (players.contains(id)) {
            return;
        }
        player.damage(price.health);
        player.setFoodLevel(Math.max(player.getFoodLevel() - price.food, 0));
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
