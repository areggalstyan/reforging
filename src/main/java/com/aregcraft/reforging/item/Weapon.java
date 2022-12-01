package com.aregcraft.reforging.item;

import org.bukkit.Material;

import java.util.Arrays;

public enum Weapon {
    GOLDEN_SWORD("Golden Sword", 4, 1.6f, Material.GOLD_INGOT),
    STONE_SWORD("Stone Sword", 5, 1.6f, Material.STONE),
    IRON_SWORD("Iron Sword", 6, 1.6f, Material.IRON_INGOT),
    DIAMOND_SWORD("Diamond Sword", 7, 1.6f, Material.DIAMOND),
    NETHERITE_SWORD("Netherite Sword", 8, 1.6f, Material.NETHERITE_SCRAP),
    GOLDEN_AXE("Golden Axe", 7, 1, Material.GOLD_INGOT),
    STONE_AXE("Stone Axe", 9, 0.8f, Material.STONE),
    IRON_AXE("Iron Axe", 9, 0.9f, Material.IRON_INGOT),
    DIAMOND_AXE("Diamond Axe", 9, 1, Material.DIAMOND),
    NETHERITE_AXE("Netherite Axe", 10, 1, Material.NETHERITE_SCRAP);

    private final String displayName;
    private final int damage;
    private final float speed;
    private final Material material;

    Weapon(String displayName, int damage, float speed, Material material) {
        this.displayName = displayName;
        this.damage = damage;
        this.speed = speed;
        this.material = material;
    }

    public static boolean has(ItemStackWrapper item) {
        return item != null && Arrays.stream(values()).map(Enum::name).anyMatch(item.material().name()::equals);
    }

    public static Weapon of(ItemStackWrapper item) {
        return Weapon.valueOf(item.material().name());
    }

    public String displayName() {
        return displayName;
    }

    public int damage() {
        return damage;
    }

    public float speed() {
        return speed;
    }

    public Material material() {
        return material;
    }
}
