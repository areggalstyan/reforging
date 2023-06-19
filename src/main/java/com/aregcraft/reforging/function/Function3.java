package com.aregcraft.reforging.function;

import net.objecthunter.exp4j.Expression;
import org.bukkit.util.Vector;

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
            x.setVariable("t", t);
            y.setVariable("t", t);
            z.setVariable("t", t);
            action.accept(new Vector(x.evaluate(), y.evaluate(), z.evaluate()));
        }
    }
}
