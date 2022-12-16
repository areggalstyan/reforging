package com.aregcraft.reforging.plugin.config;

import com.aregcraft.reforging.plugin.Reforging;
import com.aregcraft.reforging.plugin.ability.base.BaseAbility;
import com.aregcraft.reforging.plugin.config.model.AnvilModel;
import com.aregcraft.reforging.plugin.config.model.ItemModel;
import com.aregcraft.reforging.plugin.reforge.Reforge;
import com.aregcraft.reforging.plugin.reforge.StandardReforge;
import com.aregcraft.reforging.plugin.reforge.UltimateReforge;
import com.aregcraft.reforging.core.Named;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.TypeAdapterFactory;
import org.apache.commons.rng.sampling.DiscreteProbabilityCollectionSampler;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public record PluginConfig(ItemModel item, AnvilModel anvil, Map<String, String> reforgeStones,
                           Map<String, Reforge> reforges,
                           DiscreteProbabilityCollectionSampler<Reforge> reforgeSampler) {
    private static final Map<Class<?>, Object> ADAPTERS = new HashMap<>();
    private static final List<TypeAdapterFactory> ADAPTER_FACTORIES = new ArrayList<>();
    private static Map<String, BaseAbility> abilities;

    public static Map<String, BaseAbility> abilities() {
        return abilities;
    }

    public static boolean hasAdapter(Class<?> type) {
        return ADAPTERS.containsKey(type);
    }

    public static <T> void addAdapter(Class<T> type, JsonDeserializer<T> adapter) {
        ADAPTERS.put(type, adapter);
    }

    public static void addAdapterFactory(TypeAdapterFactory adapterFactory) {
        ADAPTER_FACTORIES.add(adapterFactory);
    }

    public static PluginConfig load() {
        var gson = createGson();
        abilities = arrayToMap(read("abilities", BaseAbility[].class, gson));
        abilities.put("NONE", BaseAbility.NONE);
        var standardReforges = arrayToMap(read("standard_reforges",
                StandardReforge[].class, gson));
        var ultimateReforges = arrayToMap(read("ultimate_reforges",
                UltimateReforge[].class, gson));
        return new PluginConfig(read("item", ItemModel.class, gson),
                read("anvil", AnvilModel.class, gson), reforgeStones(ultimateReforges),
                reforges(standardReforges, ultimateReforges),
                new DiscreteProbabilityCollectionSampler<>(new Random()::nextLong, reforgeSampler(standardReforges)));
    }

    private static Map<String, String> reforgeStones(Map<String, UltimateReforge> ultimateReforges) {
        return ultimateReforges.entrySet().stream().collect(Collectors.toMap(it -> it.getValue().stone().name(),
                Map.Entry::getKey));
    }

    private static Map<String, Reforge> reforges(Map<String, StandardReforge> standardReforges,
                                                 Map<String, UltimateReforge> ultimateReforges) {
        var reforges = new HashMap<String, Reforge>(standardReforges);
        reforges.putAll(ultimateReforges);
        return reforges;
    }

    private static Map<Reforge, Double> reforgeSampler(Map<String, StandardReforge> standardReforges) {
        return standardReforges.values().stream().collect(Collectors.toMap(Function.identity(),
                StandardReforge::weight));
    }

    private static Gson createGson() {
        var builder = new GsonBuilder();
        ADAPTERS.forEach(builder::registerTypeAdapter);
        ADAPTER_FACTORIES.forEach(builder::registerTypeAdapterFactory);
        return builder.create();
    }

    private static <T> T read(String name, Class<T> type, Gson gson) {
        name += ".json";
        try {
            var path = Files.createDirectories(Reforging.plugin().getDataFolder().toPath()).resolve(name);
            if (Files.notExists(path)) {
                Files.copy(Objects.requireNonNull(Reforging.plugin().getResource(name)), path);
            }
            try (var reader = Files.newBufferedReader(path)) {
                return gson.fromJson(reader, type);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends Named> Map<String, T> arrayToMap(T[] array) {
        return Arrays.stream(array).collect(Collectors.toMap(Named::name, Function.identity()));
    }
}
