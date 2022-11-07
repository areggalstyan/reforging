package com.aregcraft.reforging.particle;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public record ParticleObject(ParticleEngine engine, List<Vector> particles) {
    public void draw(Location location) {
        var world = location.getWorld();
        if (world == null) {
            return;
        }
        particles.forEach(it -> world.spawnParticle(engine.particle(), location.clone().add(it), 0));
    }
}
