package com.aregcraft.reforging.plugin.config.adapter;

import com.aregcraft.reforging.plugin.ability.base.BaseAbility;
import com.aregcraft.reforging.plugin.config.PluginConfig;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public class BaseAbilityDeserializer implements JsonDeserializer<BaseAbility> {
    @Override
    public BaseAbility deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json.isJsonPrimitive()) {
            return PluginConfig.abilities().get(json.getAsString());
        }
        try {
            return context.deserialize(json, Class.forName("com.aregcraft.reforging.plugin.ability."
                    + json.getAsJsonObject().get("type").getAsString() + "Ability"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
