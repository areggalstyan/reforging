package com.aregcraft.reforging;

import com.aregcraft.delta.api.DeltaPlugin;
import com.aregcraft.delta.api.item.ItemDisplay;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.delta.api.json.JsonConfigurationLoader;
import com.aregcraft.reforging.ability.Ability;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Reforging extends DeltaPlugin {
    private static final TypeToken<List<Reforge>> REFORGES_TYPE = new TypeToken<>() {};
    private static final TypeToken<List<ReforgeStone>> REFORGE_STONES_TYPE = new TypeToken<>() {};
    private static final TypeToken<Map<String, Double>> REFORGE_WEIGHTS_TYPE = new TypeToken<>() {};
    private static final TypeToken<List<Ability>> ABILITIES_TYPE = new TypeToken<>() {};

    private final JsonConfigurationLoader configurationLoader = JsonConfigurationLoader.builder()
            .name(REFORGES_TYPE, "reforges")
            .name(REFORGE_STONES_TYPE, "reforge_stones")
            .name(REFORGE_WEIGHTS_TYPE, "reforge_weights")
            .name(ABILITIES_TYPE, "abilities")
            .plugin(this)
            .build();
    private final LanguageLoader languageLoader = new LanguageLoader(this);
    private final Random random = new Random();

    @Override
    public void onEnable() {
        super.onEnable();
        load();
        configurationLoader.get(UpdateChecker.class).scheduleChecks(this);
    }

    public ReforgingAnvil getReforgingAnvil() {
        return configurationLoader.get(ReforgingAnvil.class);
    }

    public ItemDisplay getItemDisplay() {
        return configurationLoader.get("item", ItemDisplay.class);
    }

    public Reforge getRandomStandardReforge() {
        return getReforge(configurationLoader.get(REFORGE_WEIGHTS_TYPE).entrySet().stream()
                .map(it -> Map.entry(it.getKey(), -Math.log(random.nextDouble()) / it.getValue()))
                .min(Map.Entry.comparingByValue()).orElseThrow().getKey());
    }

    public Reforge getUltimateReforge(ItemWrapper stone) {
        return getReforge(stone.getPersistentData(this).get("reforge_id", String.class));
    }

    public Reforge getReforge(String id) {
        return Identifiable.find(configurationLoader.get(REFORGES_TYPE), id);
    }

    public Ability getAbility(String id) {
        return Identifiable.find(configurationLoader.get(ABILITIES_TYPE), id);
    }

    public List<String> getReforgeIds() {
        return configurationLoader.get(REFORGES_TYPE).stream().map(Identifiable::getId).toList();
    }

    public String getDefaultName(Player player, ItemWrapper item) {
        return languageLoader.get(player, item);
    }

    public void reload() {
        configurationLoader.get(ABILITIES_TYPE).stream()
                .filter(Listener.class::isInstance)
                .map(Listener.class::cast)
                .forEach(this::unregisterListener);
        configurationLoader.invalidateAll();
        load();
    }

    private void load() {
        getReforgingAnvil().register(this);
        configurationLoader.get(REFORGE_STONES_TYPE).forEach(it -> it.register(this));
        configurationLoader.get(ABILITIES_TYPE);
    }
}
