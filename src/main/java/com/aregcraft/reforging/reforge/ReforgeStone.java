package com.aregcraft.reforging.reforge;

import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.config.model.ItemModel;
import com.aregcraft.reforging.config.model.RecipeModel;
import com.aregcraft.reforging.item.ItemStackWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;

public record ReforgeStone(String name, ItemModel item, Material material, RecipeModel recipe) {
    public ReforgeStone {
        var shapedRecipe = new ShapedRecipe(Reforging.key(name), ItemStackWrapper.fromModel(material,
                item, name).unwrap());
        shapedRecipe.shape(recipe.shape());
        recipe.keys().forEach(shapedRecipe::setIngredient);
        Bukkit.addRecipe(shapedRecipe);
    }
}
