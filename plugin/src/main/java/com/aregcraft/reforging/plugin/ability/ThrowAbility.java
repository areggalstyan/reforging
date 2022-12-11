package com.aregcraft.reforging.plugin.ability;

import com.aregcraft.reforging.plugin.ability.base.RepeatingAbility;
import com.aregcraft.reforging.plugin.annotation.Ability;
import com.aregcraft.reforging.plugin.item.ItemStackWrapper;
import com.aregcraft.reforging.plugin.math.Vector;
import com.aregcraft.reforging.plugin.util.Spawner;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

/**
 * Allows the player to throw their weapon dealing damage to all entities it hits.
 */
@Ability
public class ThrowAbility extends RepeatingAbility {
    /**
     * Specifies the factory by which to multiply the base damage of the weapon.
     */
    private double damageFactor;
    /**
     * Specifies the speed of the weapon.
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
        var item = ItemStackWrapper.wrap(player.getInventory().getItemInMainHand());
        armorStand = Spawner.spawnArmorStand(location, item);
        armorStand.setRightArmPose(new EulerAngle(0, Math.toRadians(location.getPitch()), 1.5 * Math.PI));
        damage = damageFactor * item.modifiers(Attribute.GENERIC_ATTACK_DAMAGE).stream()
                .mapToDouble(AttributeModifier::getAmount).sum();
        velocity = direction.multiply(speed);
    }

    @Override
    protected boolean update(Player player, int time) {
        var location = armorStand.getLocation();
        armorStand.teleport(velocity.at(location));
        Spawner.nearbyEntities(player, location, 0.5, 0.5, 0.5).forEach(it -> it.damage(damage));
        return !location.subtract(velocity.toBukkit()).add(0, 1, 0).getBlock().getType().isSolid();
    }

    @Override
    protected void shutdown(Player player) {
        armorStand.remove();
    }
}
