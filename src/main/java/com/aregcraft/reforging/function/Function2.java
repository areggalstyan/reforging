package com.aregcraft.reforging.function;

import org.bukkit.util.Vector;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.function.Consumer;

public class Function2 {
    private final Expression x;
    private final Expression z;
    private final double min;
    private final double max;
    private final double delta;

    public Function2(Expression x, Expression z, double min, double max, double delta) {
        this.x = x;
        this.z = z;
        this.min = min;
        this.max = max;
        this.delta = delta;
    }

    public void evaluate(Consumer<Vector> action) {
        for (var t = min; t < max; t += delta) {
            x.setArgumentValue("t", t);
            z.setArgumentValue("t", t);
            action.accept(new Vector(x.calculate(), 0, z.calculate()));
        }
    }
}
