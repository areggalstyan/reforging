package com.aregcraft.reforging.plugin.reforge;

import com.aregcraft.reforging.plugin.Reforging;
import com.aregcraft.reforging.plugin.config.model.ItemModel;
import com.aregcraft.reforging.plugin.config.model.RecipeModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;

public record ReforgeStone(String name, ItemModel item, Material material, RecipeModel recipe) {
    public ReforgeStone {
        var shapedRecipe = new ShapedRecipe(Reforging.plugin().key(name), item.create(material, name));
        shapedRecipe.shape(recipe.shape());
        recipe.keys().forEach(shapedRecipe::setIngredient);
        Bukkit.addRecipe(shapedRecipe);
    }
}
