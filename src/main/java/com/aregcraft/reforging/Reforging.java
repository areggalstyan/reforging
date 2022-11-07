package com.aregcraft.reforging;

import com.aregcraft.reforging.command.ReforgeCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Reforging extends JavaPlugin {
    public static Reforging PLUGIN;

    @Override
    public void onEnable() {
        PLUGIN = this;
        Reforge.REFORGES.size();
        new ReforgingAnvil();
        var reforgeCommand = new ReforgeCommand();
        var reforgePluginCommand = Bukkit.getPluginCommand("reforge");
        reforgePluginCommand.setExecutor(reforgeCommand);
        reforgePluginCommand.setTabCompleter(reforgeCommand);
        var metrics = new Metrics(this, 16827);
    }
}
