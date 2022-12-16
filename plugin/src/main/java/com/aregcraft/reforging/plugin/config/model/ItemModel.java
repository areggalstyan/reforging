package com.aregcraft.reforging.plugin.config.model;

import com.aregcraft.reforging.core.item.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public record ItemModel(String name, List<String> lore) {
    public ItemStack create(Material material, String id) {
        var item = ItemWrapper.create(material);
        item.name(name);
        item.lore(lore);
        item.data().set("id", PersistentDataType.STRING, id);
        return item.unwrap();
    }
}
