package com.aregcraft.reforging;

import com.aregcraft.delta.api.Recipe;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.delta.api.registry.Identifiable;
import com.aregcraft.delta.api.registry.Registrable;

public class Stone implements Identifiable<String>, Registrable<Reforging> {
    private final String id;
    private final ItemWrapper item;
    private final Recipe recipe;

    public Stone(String id, ItemWrapper item, Recipe recipe) {
        this.id = id;
        this.item = item;
        this.recipe = recipe;
    }

    @Override
    public String getId() {
        return id;
    }

    public ItemWrapper getItem() {
        return item;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public void register(Reforging plugin) {
        item.createBuilder()
                .persistentData(plugin, "id", "reforge_stone")
                .persistentData(plugin, "reforge_id", id);
        recipe.add(plugin, id, item);
    }

    @Override
    public void unregister(Reforging plugin) {
        recipe.remove(plugin, id);
    }
}
