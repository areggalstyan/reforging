package com.aregcraft.reforging.data;

import com.aregcraft.reforging.ability.base.BaseAbility;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Abilities {
    public BiMap<String, BaseAbility> abilities;

    public static class Deserializer implements JsonDeserializer<Abilities> {
        @Override
        public Abilities deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            var obj = json.getAsJsonObject();
            var map = new HashMap<String, BaseAbility>();
            obj.keySet().forEach(key -> {
                try {
                    var type = Class.forName("com.aregcraft.reforging.ability."
                            + (Character.toUpperCase(key.charAt(0)) + key.substring(1))
                            .substring(0, key.length() - 3) + "y");
                    map.putAll(obj.get(key).getAsJsonObject().entrySet().stream().map(it -> Map.entry(it.getKey(),
                            context.deserialize(it.getValue(), type))).collect(Collectors.toMap(Map.Entry::getKey,
                            it -> (BaseAbility) it.getValue())));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            var abilities = new Abilities();
            abilities.abilities = HashBiMap.create(map);
            return abilities;
        }
    }
}
