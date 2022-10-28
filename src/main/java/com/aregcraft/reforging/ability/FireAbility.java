package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.Reforging;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * This is the second base ability. It forms multiple circles in the direction where the player is facing, each further
 * from the player, and with bigger radius. If any entity hits the particle, then that entity will be set on fire.
 */
public class FireAbility extends Ability {
    private final Set<UUID> players = new HashSet<>();
    /**
     * Specifies the type of the particle which is used to form circles.
     */
    private Particle particle;
    /**
     * Specifies the "frequency" of the particles. The "frequency" is used to determine the step angle, which is 180
     * divided by the "frequency". The higher the number is, the further apart will the particles be.
     */
    private double particleFrequency;
    /**
     * Specifies the distance between each circle.
     */
    private double circleDistance;
    /**
     * Specifies the radius of the smallest circle.
     */
    private double radius;
    /**
     * Specifies the maximum distance from a particle which will set an entity on fire.
     */
    private double fireRange;
    /**
     * Specifies the count of the circles. Each next circle is bigger than the previous one.
     */
    private int circleCount;
    /**
     * Specifies how long will hit entities be on fire in ticks (1 second = 20 ticks).
     */
    private int fireDuration;
    /**
     * Specifies how much time after forming a circle should pass to form the next one in ticks (1 second = 20 ticks).
     */
    private int circlePeriod;

    @Override
    public void activate(Player player) {
        var id = player.getUniqueId();
        if (players.contains(id)) {
            return;
        }
        players.add(id);
        new BukkitRunnable() {
            private int circle = 0;
            private Location origin;
            private Vector x;
            private Vector y;
            private Vector z;

            @Override
            public void run() {
                if (circle++ == circleCount || player.isDead()) {
                    players.remove(id);
                    cancel();
                }
                if (origin == null) {
                    origin = player.getLocation().add(0, 1, 0);
                    z = origin.getDirection();
                    y = new Vector(0, 1, 0);
                    x = z.getCrossProduct(y).normalize();
                    y = x.getCrossProduct(z).normalize();
                }
                for (double i = 0; i < 2 * Math.PI; i += Math.PI / particleFrequency) {
                    var direction = new Vector(radius * circle * Math.cos(i),
                            radius * circle * Math.sin(i), circleDistance * circle);
                    var location = origin.clone().add(x.clone().multiply(direction.getX())
                            .add(y.clone().multiply(direction.getY()).add(z.clone().multiply(direction.getZ()))));
                    var world = player.getWorld();
                    world.spawnParticle(particle, location, 0);
                    world.getNearbyEntities(location, fireRange / 2, fireRange / 2, fireRange / 2).stream()
                            .filter(it -> !it.equals(player)).forEach(it -> it.setFireTicks(fireDuration));
                }
            }
        }.runTaskTimer(Reforging.PLUGIN, 0, circlePeriod);
    }
}
