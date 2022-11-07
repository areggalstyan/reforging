package com.aregcraft.reforging.ability;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.lang.reflect.Type;

/**
 * Specifies a mathematical parametric function with variable t.
 */
public class Function {
    /**
     * Specifies the parametric expression for the x coordinate.
     */
    Expression x;
    /**
     * Specifies the parametric expression for the y coordinate.
     */
    Expression y;
    /**
     * Specifies the parametric expression for the z coordinate.
     */
    Expression z;
    /**
     * Specifies the minimum value of the parameter.
     */
    double min;
    /**
     * Specifies the maximum value of the parameter.
     */
    double max;
    /**
     * Specifies the change of the parameter's value.
     */
    double delta;

    public static class Deserializer implements JsonDeserializer<Function> {
        @Override
        public Function deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            var obj = json.getAsJsonObject();
            var function = new Function();
            if (obj.has("x")) {
                function.x = new Expression(obj.get("x").getAsString(), new Argument("t"));
            }
            if (obj.has("y")) {
                function.y = new Expression(obj.get("y").getAsString(), new Argument("t"));
            }
            if (obj.has("z")) {
                function.z = new Expression(obj.get("z").getAsString(), new Argument("t"));
            }
            function.min = obj.get("min").getAsDouble();
            function.max = obj.get("max").getAsDouble();
            function.delta = obj.get("delta").getAsDouble();
            return function;
        }
    }
}
