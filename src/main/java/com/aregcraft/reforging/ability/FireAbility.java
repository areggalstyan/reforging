package com.aregcraft.reforging.ability;

import com.aregcraft.reforging.data.Price;
import com.aregcraft.reforging.Reforging;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FireAbility implements Ability {
    private final Set<UUID> players = new HashSet<>();
    private Price price; Particle particle;
    private double particleFrequency;
    private double circleDistance;
    private double radius;
    private double fireRange;
    private int circleCount;
    private int fireDuration;
    private int circlePeriod;

    @Override
    public void activate(Player player) {
        var id = player.getUniqueId();
        if (players.contains(id)) {
            return;
        }
        player.damage(price.health);
        player.setFoodLevel(Math.max(player.getFoodLevel() - price.food, 0));
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
