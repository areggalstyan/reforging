package com.aregcraft.reforging.particle;

import com.google.common.math.DoubleMath;
import org.bukkit.util.Vector;

public record Triangle(Vector a, Vector b, Vector c) {
    public static final double EPSILON = 0.005;

    public boolean contains(Vector p) {
        var pa = p.clone().subtract(a);
        var pb = p.clone().subtract(b);
        var pc = p.clone().subtract(c);
        var alpha = Math.acos(pa.dot(pb) / pa.length() / pb.length());
        var beta = Math.acos(pb.dot(pc) / pb.length() / pc.length());
        var gamma = Math.acos(pc.dot(pa) / pc.length() / pa.length());
        return DoubleMath.fuzzyEquals(alpha + beta + gamma, 2 * Math.PI, EPSILON);
    }
}
