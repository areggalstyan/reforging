package com.aregcraft.reforging.plugin;

import com.aregcraft.reforging.core.Context;
import com.aregcraft.reforging.core.data.Namespace;
import com.aregcraft.reforging.plugin.ability.base.BaseAbility;
import com.aregcraft.reforging.plugin.command.ReforgeCommand;
import com.aregcraft.reforging.plugin.command.ReloadReforgingCommand;
import com.aregcraft.reforging.plugin.config.PluginConfig;
import com.aregcraft.reforging.plugin.config.adapter.BaseAbilityDeserializer;
import com.aregcraft.reforging.plugin.config.adapter.ExpressionDeserializer;
import com.aregcraft.reforging.plugin.config.adapter.PotionEffectTypeDeserializer;
import com.aregcraft.reforging.plugin.config.adapter.RecordTypeAdapterFactory;
import com.aregcraft.reforging.core.Spawner;
import org.bstats.bukkit.Metrics;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.mariuszgromada.math.mxparser.Expression;

public class Reforging extends JavaPlugin implements Namespace {
    private static Reforging plugin;
    private static PluginConfig config;

    public static Reforging plugin() {
        return plugin;
    }

    public static PluginConfig config() {
        return config;
    }

    public static void loadConfig() {
        config = PluginConfig.load();
    }

    private static void registerAdapters() {
        PluginConfig.addAdapter(PotionEffectType.class, new PotionEffectTypeDeserializer());
        PluginConfig.addAdapter(BaseAbility.class, new BaseAbilityDeserializer());
        PluginConfig.addAdapter(Expression.class, new ExpressionDeserializer());
        PluginConfig.addAdapterFactory(new RecordTypeAdapterFactory());
    }

    private static void registerCommands() {
        new ReforgeCommand();
        new ReloadReforgingCommand();
    }

    @Override
    public void onEnable() {
        plugin = this;
        Context.plugin(this);
        registerCommands();
        registerAdapters();
        loadConfig();
        new Spawner();
        new ReforgingAnvil();
        new Metrics(this, 16827);
    }

    @Override
    public NamespacedKey key(String key) {
        return new NamespacedKey(plugin, key);
    }
}
