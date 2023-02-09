package com.aregcraft.reforging;

import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.reforging.target.Target;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LanguageLoader implements Listener {
    private static final String LANG_URL = "https://assets.mcasset.cloud/1.19.2/assets/minecraft/lang/%s.json";
    private static final TypeToken<Map<String, String>> LANG_TYPE = new TypeToken<>() {};

    private final Reforging plugin;
    private final LoadingCache<String, Map<String, String>> cache;

    public LanguageLoader(Reforging plugin) {
        this.plugin = plugin;
        cache = CacheBuilder.newBuilder().build(new LanguageCacheLoader());
        plugin.registerListener(this);
    }

    public String get(Player player, ItemWrapper item) {
        return get(player, getLanguageKey(item));
    }

    public String get(Player player, String name) {
        return cache.getUnchecked(player.getLocale()).get(name);
    }

    private String getLanguageKey(ItemWrapper item) {
        return "item.minecraft." + item.getMaterial().name().toLowerCase();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        CompletableFuture.runAsync(() -> cache.getUnchecked(event.getPlayer().getLocale()));
    }

    private class LanguageCacheLoader extends CacheLoader<String, Map<String, String>> {
        @Override
        public Map<String, String> load(String key) {
            try (var reader = new InputStreamReader(new URL(LANG_URL.formatted(key)).openStream())) {
                return getTargets(plugin.getGson().fromJson(reader, LANG_TYPE.getType()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private Map<String, String> getTargets(Map<String, String> items) {
            return items.entrySet().stream()
                    .filter(it -> isSlot(it.getKey()) || Target.isTarget(it.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        private boolean isSlot(String name) {
            return name.startsWith("item.modifiers");
        }
    }
}
