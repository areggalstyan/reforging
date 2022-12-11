package com.aregcraft.reforging.plugin.config.model;

import org.bukkit.Material;

import java.util.Map;

public record RecipeModel(String[] shape, Map<Character, Material> keys) {
}
