package com.aregcraft.reforging.util;

import com.aregcraft.reforging.Reforge;
import com.aregcraft.reforging.Reforging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class Config {
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Reforge.class, new ReforgeDeserializer())
            .setPrettyPrinting().create();

    public static <T> T readFile(String name, Class<T> type) {
        name += ".json";
        try {
            var path = Files.createDirectories(Reforging.PLUGIN.getDataFolder().toPath()).resolve(name);
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
}
