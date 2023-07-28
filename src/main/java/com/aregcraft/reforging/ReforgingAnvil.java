package com.aregcraft.reforging;

import com.aregcraft.delta.api.Recipe;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.delta.api.json.annotation.JsonConfiguration;
import com.aregcraft.delta.api.registry.Registrable;
import org.bukkit.block.Block;

@JsonConfiguration("reforging_anvil")
public class ReforgingAnvil implements Registrable<Reforging> {
    private ItemWrapper item;
    private Recipe recipe;
    private PlayableSound sound;
    private int price;

    @Override
    public void register(Reforging plugin) {
        item.getPersistentData(plugin).set("id", "reforging_anvil");
        recipe.add(plugin, "reforging_anvil", item);
    }

    @Override
    public void unregister(Reforging plugin) {
        recipe.remove(plugin, "reforging_anvil");
    }

    public void playSound(Block block) {
        sound.play(block);
    }

    public boolean deductPrice(ItemWrapper item) {
        if (item.getAmount() < price) {
            return false;
        }
        item.decrementAmount(price);
        return true;
    }

    public ItemWrapper getItem() {
        return item;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public int getPrice() {
        return price;
    }
}
