package com.aregcraft.reforging.ability;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.json.JsonReader;
import com.aregcraft.delta.api.json.annotation.JsonAdapterFor;
import com.aregcraft.delta.api.util.Classes;
import com.aregcraft.reforging.Reforging;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import org.bukkit.event.Listener;

import java.lang.reflect.Type;

@JsonAdapterFor(Ability.class)
public class AbilityDeserializer implements JsonDeserializer<Ability> {
    private static final String CLASS_NAME_TEMPLATE = "com.aregcraft.reforging.ability.%sAbility";

    @InjectPlugin
    private Reforging plugin;

    @Override
    public Ability deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json.isJsonPrimitive()) {
            return plugin.getAbilities().findAny(json.getAsString());
        }
        var reader = new JsonReader(context, json);
        var ability = reader.deserialize(json, getClass(reader.getString("base")));
        Classes.setPluginField(Ability.class, ability, plugin);
        if (ability instanceof Listener listener) {
            plugin.registerListener(listener);
        }
        return ability;
    }

    private Class<? extends Ability> getClass(String base) {
        return Classes.getClass(CLASS_NAME_TEMPLATE.formatted(base));
    }
}
