package com.aregcraft.reforging.config.adapter;

import com.aregcraft.reforging.Reforge;
import com.aregcraft.reforging.Reforging;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class ReforgeDeserializer implements JsonDeserializer<Reforge> {
    @Override
    public Reforge deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        var obj = json.getAsJsonObject();
        return new Reforge(obj.get("name").getAsString(),
                Reforging.CONFIG.abilities.get(obj.get("ability").getAsString()), get(obj, "maxHealth"),
                get(obj, "knockbackResistance"), get(obj, "movementSpeed"), get(obj, "attackDamage"),
                get(obj, "armor"), get(obj, "armorToughness"), get(obj, "attackSpeed"),
                get(obj, "attackKnockback"), (int) get(obj, "weight"));
    }

    private float get(JsonObject obj, String name) {
        return obj.has(name) ? obj.get(name).getAsFloat() : 0;
    }
}
