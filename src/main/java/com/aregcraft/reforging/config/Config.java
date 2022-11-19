package com.aregcraft.reforging.config;

import com.aregcraft.reforging.Reforge;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.ability.base.BaseAbility;
import com.aregcraft.reforging.data.Abilities;
import com.aregcraft.reforging.data.Anvil;
import com.aregcraft.reforging.data.Item;
import com.google.common.collect.BiMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.rng.sampling.DiscreteProbabilityCollectionSampler;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Config {
    public static final Map<Class<?>, Object> ADAPTERS = new HashMap<>();
    public static final Random RANDOM = new Random();
    public Item item;
    public Anvil anvil;
    public Map<String, Reforge> reforges;
    public BiMap<String, BaseAbility> abilities;
    public DiscreteProbabilityCollectionSampler<Reforge> reforgeSampler;
    private final Gson gson;

    public Config() {
        var builder = new GsonBuilder();
        ADAPTERS.forEach(builder::registerTypeAdapter);
        gson = builder.create();
    }

    public void load() {
        item = read("item", Item.class);
        anvil = read("anvil", Anvil.class);
        abilities = read("abilities", Abilities.class).abilities;
        reforges = List.of(read("reforges", Reforge[].class)).stream()
                .collect(Collectors.toMap(Reforge::name, Function.identity()));
        reforgeSampler = new DiscreteProbabilityCollectionSampler<>(RANDOM::nextLong, Reforging.CONFIG.reforges
                .values().stream().collect(Collectors.toMap(Function.identity(), Reforge::weight)));
    }

    private <T> T read(String name, Class<T> type) {
        name += ".json";
        try {
            var path = Files.createDirectories(Reforging.PLUGIN.getDataFolder().toPath()).resolve(name);
            if (Files.notExists(path)) {
                Files.copy(Objects.requireNonNull(Reforging.PLUGIN.getResource(name)), path);
            }
            try (var reader = Files.newBufferedReader(path)) {
                return gson.fromJson(reader, type);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
