package com.aregcraft.reforging.target;

import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;

public enum Chestplate implements TargetItem {
    GOLDEN_CHESTPLATE(Material.GOLD_INGOT, 5, 0),
    IRON_CHESTPLATE(Material.IRON_INGOT, 6, 0),
    DIAMOND_CHESTPLATE(Material.DIAMOND, 8, 2),
    NETHERITE_CHESTPLATE(Material.NETHERITE_SCRAP, 8, 3);

    private final Material ingredient;
    private final double armor;
    private final double armorToughness;

    Chestplate(Material ingredient, double armor, double armorToughness) {
        this.ingredient = ingredient;
        this.armor = armor;
        this.armorToughness = armorToughness;
    }

    public static Chestplate of(ItemWrapper item) {
        return valueOf(item.getMaterial().name());
    }

    @Override
    public boolean isIngredient(ItemWrapper item) {
        return item.getMaterial() == ingredient;
    }

    @Override
    public void addAttributeModifiers(ItemWrapper item) {
        item.createAttributeModifierBuilder()
                .name("BASE_ARMOR")
                .attribute(Attribute.GENERIC_ARMOR)
                .amount(armor)
                .slot(EquipmentSlot.CHEST)
                .add();
        item.createAttributeModifierBuilder()
                .name("BASE_ARMOR_TOUGHNESS")
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS)
                .amount(armorToughness)
                .slot(EquipmentSlot.CHEST)
                .add();
    }

    @Override
    public EquipmentSlot getSlot() {
        return EquipmentSlot.CHEST;
    }

    @Override
    public double getArmor() {
        return armor;
    }

    @Override
    public double getArmorToughness() {
        return armorToughness;
    }
}
