package com.aregcraft.reforging;

import com.aregcraft.delta.api.item.ItemWrapper;
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

    public static String getLanguageKey(String name) {
        return "item.minecraft." + name.toLowerCase();
    }

    public String get(Player player, ItemWrapper item) {
        return cache.getUnchecked(player.getLocale()).get(getLanguageKey(item.getMaterial().name()));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var locale = event.getPlayer().getLocale();
        if (!cache.asMap().containsKey(locale)) {
            CompletableFuture.runAsync(() -> cache.refresh(locale));
        }
    }

    private class LanguageCacheLoader extends CacheLoader<String, Map<String, String>> {
        @Override
        public Map<String, String> load(String key) {
            try (var reader = new InputStreamReader(new URL(LANG_URL.formatted(key)).openStream())) {
                return getWeapons(plugin.getGson().fromJson(reader, LANG_TYPE.getType()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private Map<String, String> getWeapons(Map<String, String> items) {
            return items.entrySet().stream()
                    .filter(it -> Weapon.isWeapon(it.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }
}
