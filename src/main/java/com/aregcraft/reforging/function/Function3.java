package com.aregcraft.reforging.function;

import org.bukkit.util.Vector;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.function.Consumer;

public class Function3 {
    private final Expression x;
    private final Expression y;
    private final Expression z;
    private final double min;
    private final double max;
    private final double delta;

    public Function3(Expression x, Expression y, Expression z, double min, double max, double delta) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.min = min;
        this.max = max;
        this.delta = delta;
    }

    public void evaluate(Consumer<Vector> action) {
        for (var t = min; t < max; t += delta) {
            x.setArgumentValue("t", t);
            y.setArgumentValue("t", t);
            z.setArgumentValue("t", t);
            action.accept(new Vector(x.calculate(), y.calculate(), z.calculate()));
        }
    }
}
