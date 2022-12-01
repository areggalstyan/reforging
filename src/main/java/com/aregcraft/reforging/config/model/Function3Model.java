package com.aregcraft.reforging.config.model;

import com.aregcraft.reforging.annotation.External;
import com.aregcraft.reforging.math.Vector;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.function.Consumer;

/**
 * Specifies the three-dimensional mathematical function used to create visual effects.
 * @param x Specifies how much <i>x</i> changes based on <i>t</i>.
 * @param y Specifies how much <i>y</i> changes based on <i>t</i>.
 * @param z Specifies how much <i>z</i> changes based on <i>t</i>.
 * @param min Specifies the starting value of <i>t</i>.
 * @param max Specifies the ending value of <i>t</i>.
 * @param delta Specifies the step of <i>t</i>.
 */
@External
public record Function3Model(Expression x, Expression y, Expression z, double min, double max, double delta) {
    public void evaluate(Consumer<Vector> action) {
        for (var t = min; t < max; t += delta) {
            x.setArgumentValue("t", t);
            y.setArgumentValue("t", t);
            z.setArgumentValue("t", t);
            action.accept(new Vector(x.calculate(), y.calculate(), z.calculate()));
        }
    }
}
