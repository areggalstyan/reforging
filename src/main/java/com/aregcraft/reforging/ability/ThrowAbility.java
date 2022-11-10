package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.math.Vector;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

/**
 * Allows the player to throw their weapon which will travel for the specified amount of time and damage all the
 * entities it hits on the way.
 */
public class ThrowAbility extends RepeatingAbility {
    /**
     * Specifies the factor that is applied to the weapon's normal damage.
     */
    private double damageFactor;
    /**
     * Specifies the speed of the weapon when thrown in blocks per tick (1 second = 20 ticks).
     */
    private double speed;
    private transient ArmorStand armorStand;
    private transient double damage;
    private transient Vector velocity;

    @Override
    protected void setup(Player player) {
        var location = player.getLocation();
        var direction = new Vector(location.getDirection());
        direction.cross(new Vector(0, 1, 0)).normalize().multiply(0.25).at(location);
        var item = player.getInventory().getItemInMainHand();
        armorStand = spawnArmorStand(location, item);
        armorStand.setRightArmPose(new EulerAngle(0, Math.toRadians(location.getPitch()), 1.5 * Math.PI));
        damage = damageFactor * item.getItemMeta().getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE).stream()
                .mapToDouble(AttributeModifier::getAmount).sum();
        velocity = direction.multiply(speed);
    }

    @Override
    protected void shutdown(Player player) {
        armorStand.remove();
    }

    @Override
    protected boolean perform(Player player, int time) {
        var location = armorStand.getLocation();
        armorStand.teleport(velocity.at(location));
        forEachEntity(location.add(0, 1, 0), player, it -> it.damage(damage));
        return isUnfilled(location);
    }
}
