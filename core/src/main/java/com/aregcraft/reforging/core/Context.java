package com.aregcraft.reforging.core;

import com.aregcraft.reforging.core.data.Namespace;

public class Context {
    private static Namespace plugin;

    public static Namespace plugin() {
        return plugin;
    }

    public static void plugin(Namespace plugin) {
        if (plugin() == null) {
            Context.plugin = plugin;
        }
    }
}
