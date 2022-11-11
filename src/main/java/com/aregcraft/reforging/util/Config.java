package com.aregcraft.reforging.util;

import com.aregcraft.reforging.Reforge;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.ability.Function;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Config {
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Reforge.class, new ReforgeDeserializer())
            .registerTypeAdapter(PotionEffectType.class, new PotionEffectTypeDeserializer())
            .registerTypeAdapter(Function.class, new Function.Deserializer())
            .setPrettyPrinting().create();

    public static <T> T readFile(String name, Class<T> type) {
        name += ".json";
        try {
            var path = resolve(name);
            if (Files.notExists(path)) {
                Files.copy(Objects.requireNonNull(Reforging.PLUGIN.getResource(name)), path);
            }
            try (var reader = Files.newBufferedReader(path)) {
                return GSON.fromJson(reader, type);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path resolve(String name) {
        try {
            return Files.createDirectories(Reforging.PLUGIN.getDataFolder().toPath()).resolve(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
