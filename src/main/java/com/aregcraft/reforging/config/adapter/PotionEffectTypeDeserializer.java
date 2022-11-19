package com.aregcraft.reforging.config.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;

public class PotionEffectTypeDeserializer implements JsonDeserializer<PotionEffectType> {
    @Override
    public PotionEffectType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return PotionEffectType.getByName(json.getAsString());
    }
}
