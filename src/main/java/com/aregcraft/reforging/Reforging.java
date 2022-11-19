package com.aregcraft.reforging;

import com.aregcraft.reforging.ability.external.Function;
import com.aregcraft.reforging.command.ReforgeCommand;
import com.aregcraft.reforging.command.ReloadReforgingCommand;
import com.aregcraft.reforging.config.Config;
import com.aregcraft.reforging.config.adapter.PotionEffectTypeDeserializer;
import com.aregcraft.reforging.config.adapter.ReforgeDeserializer;
import com.aregcraft.reforging.data.Abilities;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class Reforging extends JavaPlugin {
    public static Reforging PLUGIN;
    public static Config CONFIG;

    @Override
    public void onEnable() {
        PLUGIN = this;
        registerAdapters();
        CONFIG = new Config();
        CONFIG.load();
        registerCommands();
        new ReforgingAnvil();
        new Metrics(this, 16827);
    }

    private void registerAdapters() {
        Config.ADAPTERS.put(Reforge.class, new ReforgeDeserializer());
        Config.ADAPTERS.put(PotionEffectType.class, new PotionEffectTypeDeserializer());
        Config.ADAPTERS.put(Abilities.class, new Abilities.Deserializer());
        Config.ADAPTERS.put(Function.class, new Function.Deserializer());
    }

    private void registerCommands() {
        registerCommand("reforge", new ReforgeCommand());
        registerCommand("reloadreforging", new ReloadReforgingCommand());
    }

    private void registerCommand(String name, TabExecutor tabExecutor) {
        var command = Bukkit.getPluginCommand(name);
        command.setExecutor(tabExecutor);
        command.setTabCompleter(tabExecutor);
    }

    private void registerCommand(String name, CommandExecutor executor) {
        Bukkit.getPluginCommand(name).setExecutor(executor);
    }
}
