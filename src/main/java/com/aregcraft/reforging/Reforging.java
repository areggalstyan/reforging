package com.aregcraft.reforging;

import org.bukkit.plugin.java.JavaPlugin;

public class Reforging extends JavaPlugin {
    public static Reforging PLUGIN;

    @Override
    public void onEnable() {
        PLUGIN = this;
        Reforge.REFORGES.size();
        new ReforgeAnvil();
    }
}
