package com.aregcraft.reforging.particle;

import com.aregcraft.reforging.util.Config;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record ParticleEngine(Particle particle, Vector size, double space) {
    public List<Triangle> triangulate(Vector[] vertices) {
        var triangles = new ArrayList<Triangle>();
        for (var i = 0; i < vertices.length; i += 3) {
            triangles.add(new Triangle(vertices[i], vertices[i + 1], vertices[i + 2]));
        }
        return triangles;
    }

    public ParticleObject create(Vector[] vertices) {
        var triangles = triangulate(vertices);
        var particles = new ArrayList<Vector>();
        for (var i = -size.getX() / 2; i < size.getX() / 2; i += space) {
            for (var j = -size.getY() / 2; j < size.getY() / 2; j += space) {
                for (var k = -size.getZ() / 2; k < size.getZ() / 2; k += space) {
                    var point = new Vector(i, j, k);
                    if (triangles.stream().anyMatch(it -> it.contains(point))) {
                        particles.add(point);
                    }
                }
            }
        }
        return new ParticleObject(this, particles);
    }

    public ParticleObject create(String name) {
        var vertices = new ArrayList<Vector>();
        var file = Config.resolve(name + ".obj");
        try(var reader = Files.newBufferedReader(file)) {
            var buffer = new ArrayList<Vector>();
            var line = reader.readLine();
            while (line != null && !line.startsWith("v ")) {
                line = reader.readLine();
            }
            while (line != null && line.startsWith("v ")) {
                var coordinates = Arrays.stream(line.split(" ")).skip(1)
                        .mapToDouble(Double::parseDouble).toArray();
                buffer.add(new Vector(coordinates[0], coordinates[1], coordinates[2]));
                line = reader.readLine();
            }
            while (line != null && !line.startsWith("f ")) {
                line = reader.readLine();
            }
            while (line != null && line.startsWith("f ")) {
                var indices = Arrays.stream(line.split(" ")).skip(1).map(it -> it.split("/")[0])
                        .mapToInt(Integer::parseInt).map(it -> it - 1).toArray();
                vertices.add(buffer.get(indices[0]));
                vertices.add(buffer.get(indices[1]));
                vertices.add(buffer.get(indices[2]));
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return create(vertices.toArray(Vector[]::new));
    }
}
