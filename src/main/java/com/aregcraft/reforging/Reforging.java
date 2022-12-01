package com.aregcraft.reforging;

import com.aregcraft.reforging.ability.base.BaseAbility;
import com.aregcraft.reforging.command.ReforgeCommand;
import com.aregcraft.reforging.command.ReloadReforgingCommand;
import com.aregcraft.reforging.config.PluginConfig;
import com.aregcraft.reforging.config.adapter.BaseAbilityDeserializer;
import com.aregcraft.reforging.config.adapter.ExpressionDeserializer;
import com.aregcraft.reforging.config.adapter.PotionEffectTypeDeserializer;
import com.aregcraft.reforging.config.adapter.RecordTypeAdapterFactory;
import com.aregcraft.reforging.util.Spawner;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.mariuszgromada.math.mxparser.Expression;

public class Reforging extends JavaPlugin {
    private static Reforging plugin;
    private static PluginConfig config;

    public static Reforging plugin() {
        return plugin;
    }

    public static PluginConfig config() {
        return config;
    }

    public static NamespacedKey key(String key) {
        return new NamespacedKey(plugin, key);
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
        registerCommands();
        registerAdapters();
        loadConfig();
        new Spawner();
        new ReforgingAnvil();
        new Metrics(this, 16827);
    }
}
