package com.aregcraft.reforging;

import com.aregcraft.delta.api.DeltaPlugin;
import com.aregcraft.delta.api.item.ItemDisplay;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.delta.api.json.JsonConfigurationLoader;
import com.aregcraft.delta.api.registry.RegistrableRegistry;
import com.aregcraft.delta.api.registry.Registry;
import com.aregcraft.delta.api.update.UpdateChecker;
import com.aregcraft.delta.api.update.Updater;
import com.aregcraft.reforging.ability.Ability;
import com.google.gson.reflect.TypeToken;
import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Reforging extends DeltaPlugin {
    private static final TypeToken<Map<String, Double>> REFORGE_WEIGHTS_TYPE = new TypeToken<>() {};

    private final JsonConfigurationLoader configurationLoader = JsonConfigurationLoader.builder()
            .name(REFORGE_WEIGHTS_TYPE, "reforge_weights")
            .plugin(this)
            .build();
    private final Registry<String, Ability> abilities =
            new Registry<>("abilities", Ability.class, configurationLoader);
    private final Registry<String, Reforge> reforges =
            new Registry<>("reforges", Reforge.class, configurationLoader);
    private final Registry<String, Stone> stones =
            new RegistrableRegistry<>("stones", Stone.class, configurationLoader);
    private final LanguageLoader languageLoader = new LanguageLoader(this);
    private final Random random = new Random();
    private final Updater updater = new Updater(this);

    @Override
    public void onEnable() {
        super.onEnable();
        getReforgingAnvil().register(this);
        configurationLoader.get(UpdateChecker.class).scheduleChecks(this);
        new Metrics(this, 16827);
    }

    public List<String> getAvailableLocales() {
        return configurationLoader.getAvailableLocales();
    }

    public boolean setLocale(String locale) {
        var result = configurationLoader.setLocale(locale);
        reload();
        return result;
    }

    public Updater getUpdater() {
        return updater;
    }

    public ReforgingAnvil getReforgingAnvil() {
        return configurationLoader.get(ReforgingAnvil.class);
    }

    public String[] getReforgingInfoUsage() {
        return configurationLoader.get("reforginginfo_usage", String[].class);
    }

    public ItemDisplay getWeaponDisplay() {
        return configurationLoader.get("weapon", ItemDisplay.class);
    }

    public ItemDisplay getArmorDisplay() {
        return configurationLoader.get("armor", ItemDisplay.class);
    }

    public boolean isStone(ItemWrapper item) {
        return item.getPersistentData(this).check("id", "reforge_stone");
    }

    public Reforge getRandomStandardReforge(ItemWrapper item) {
        return configurationLoader.get(REFORGE_WEIGHTS_TYPE).entrySet().stream()
                .map(it -> Map.entry(reforges.findAny(it.getKey()), it.getValue()))
                .filter(it -> it.getKey().isApplicable(item))
                .map(it -> Map.entry(it.getKey(), -Math.log(random.nextDouble()) / it.getValue()))
                .min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
    }

    public Reforge getUltimateReforge(ItemWrapper stone) {
        return reforges.findAny(stone.getPersistentData(this).get("reforge_id", String.class));
    }

    public Registry<String, Ability> getAbilities() {
        return abilities;
    }

    public Registry<String, Reforge> getReforges() {
        return reforges;
    }

    public Registry<String, Stone> getStones() {
        return stones;
    }

    public String getDefaultName(Player player, ItemWrapper item) {
        return languageLoader.get(player, item);
    }

    public String getSlotName(Player player, EquipmentSlot slot) {
        return languageLoader.get(player, "item.modifiers." + switch (slot) {
            case HAND -> "mainhand";
            case OFF_HAND -> "offhand";
            default -> slot.name().toLowerCase();
        });
    }

    public void reload() {
        configurationLoader.invalidateAll();
        abilities.invalidateAll();
        reforges.invalidateAll();
        stones.invalidateAll();
        getReforgingAnvil().register(this);
    }

    public double getReforgeChance(String id) {
        var weights = configurationLoader.get(REFORGE_WEIGHTS_TYPE);
        return weights.get(id) / weights.entrySet().stream()
                .filter(it -> haveCommonTarget(it.getKey(), id))
                .mapToDouble(Map.Entry::getValue)
                .sum() * 100;
    }

    private boolean haveCommonTarget(String id, String other) {
        return !Collections.disjoint(reforges.findAny(id).getTargets(), reforges.findAny(other).getTargets());
    }
}
