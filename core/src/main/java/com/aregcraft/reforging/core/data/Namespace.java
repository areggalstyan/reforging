package com.aregcraft.reforging.core.data;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public interface Namespace extends Plugin {
    NamespacedKey key(String key);
}
