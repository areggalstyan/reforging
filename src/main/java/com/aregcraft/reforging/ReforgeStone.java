package com.aregcraft.reforging;

import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.delta.api.Recipe;

public class ReforgeStone {
    private final String id;
    private final ItemWrapper item;
    private final Recipe recipe;

    public ReforgeStone(String id, ItemWrapper item, Recipe recipe) {
        this.id = id;
        this.item = item;
        this.recipe = recipe;
    }

    public void register(Reforging plugin) {
        item.createBuilder()
                .persistentData(plugin, "id", "reforge_stone")
                .persistentData(plugin, "reforge_id", id);
        recipe.add(plugin, id, item);
    }
}
